#!/usr/bin/env python
# coding: utf-8

# In[1]:


from bokeh.models import CategoricalColorMapper
from bokeh.plotting import figure
from bokeh.models import Slider, Button
from bokeh.layouts import column, row
from bokeh.io import curdoc
import numpy as np
import pandas as pd
import json
import sys
import pathlib


# In[13]:


class Action:
    BANK_LEFT = 'L'
    BANK_RIGHT = 'R'
    THRUST = 'T'
    FIRE = 'F'

    ACTIONS = [BANK_LEFT, BANK_RIGHT, THRUST, FIRE]


class Game:
    """A single game."""

    def __init__(self, actions, states, result):
        self.actions = actions
        self.states = states
        self.result = result

    def to_df(self):
        """Outputs a Pandas DF with one row for each time"""
        rows = []
        for i, (act, state) in enumerate(zip(self.actions, self.states)):
            rows.append({
                't': state['t'],
                't_int': i + 1,
                'x1': state['players']['0']['position'][0],
                'y1': state['players']['0']['position'][1],
                'vx1': state['players']['0']['velocity'][0],
                'vy1': state['players']['0']['velocity'][1],
                'a1': state['players']['0']['orientation'],
                'x2': state['players']['1']['position'][0],
                'y2': state['players']['1']['position'][1],
                'vx2': state['players']['1']['velocity'][0],
                'vy2': state['players']['1']['velocity'][1],
                'a2': state['players']['1']['orientation'],
                'bullets': [state['bullets'][k] for k in '01']
            })
        rows = pd.DataFrame(rows).set_index('t_int')
        rows['a1_0'] = rows['a1'] + wedge_size / 2
        rows['a1_1'] = rows['a1'] - wedge_size / 2
        rows['a2_0'] = rows['a2'] + wedge_size / 2
        rows['a2_1'] = rows['a2'] - wedge_size / 2
        return rows


# In[19]:


width = 50 * 20
height = 50 * 20
player_r = 30
wedge_size = np.pi / 4
c = ['#4287f5', '#ffa30f']


# In[20]:


def parse_player_actions(actions):
    return {act: act in actions for act in Action.ACTIONS}


def read_snoopy(filename):
    with open(filename, 'r') as stream:
        actiondata, statedata, resultdata = stream.read().split('\n\n')
    actions = []
    for line in actiondata.split('\n'):
        actions.append([parse_player_actions(acts)
                        for acts in line.split(' ')])

    states = [json.loads(state) for state in statedata.split('\n')]
    res = int(resultdata.strip())
    return Game(actions, states, res)


# In[21]:


filename = pathlib.Path().cwd().joinpath(sys.argv[1])
g = read_snoopy(filename)
df = g.to_df()


names = ['Player 1', 'Player 2']


def parse_bullets(bulletlist):
    rows = []
    for p_bullets, name in zip(bulletlist, names):
        for bullet in p_bullets:
            rows.append({
                'x': bullet['position'][0],
                'y': bullet['position'][1],
                'player': name
            })
    return pd.DataFrame(rows)


def app(doc):

    player_map = CategoricalColorMapper(factors=names, palette=c)

    scale = 1 / 1.5

    def plot(t):
        # Set up plot
        plot = figure(height=round(height * scale), width=round(width * scale), title="Game",
                      tools="crosshair,save", x_range=[0, width], y_range=[0, height])

        players = df.loc[[t], :]
        bullets = parse_bullets(df['bullets'][t])
        plot.wedge('x1', 'y1', start_angle='a1_0', end_angle='a1_1',
                   color=c[0], radius=player_r, legend_label='Player 1', source=players)
        plot.wedge('x2', 'y2', start_angle='a2_0', end_angle='a2_1',
                   color=c[1], radius=player_r, legend_label='Player 2', source=players)
        if not bullets.empty:
            plot.circle('x', 'y', color={
                        'field': 'player', 'transform': player_map}, source=bullets)
        return plot

    # Set up widgets
    time = Slider(value=min(df.index), start=min(
        df.index), end=max(df.index), step=1)

    def update_data(attrname, old, new):
        col.children[0] = plot(time.value)

    time.on_change('value', update_data)

    playing = False

    def animate_update():
        global playing
        if 'playing' not in globals():
            pass
        elif playing:
            t = time.value + 1
            if t not in df.index:
                t = 1
            time.value = t

    def animate():
        global playing
        if button.label == '► Play':
            playing = True
            button.label = '❚❚ Pause'
        else:
            button.label = '► Play'
            playing = False

    doc.add_periodic_callback(animate_update, 33)

    button = Button(label='► Play', width=40)
    button.on_click(animate)

    # Set up layouts and add to document
    col = column(plot(1), row(time, button))

    doc.add_root(col)
    doc.title = "Game"


app(curdoc())
