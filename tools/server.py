#!/usr/bin/env python
# coding: utf-8

# In[1]:


from bokeh.models import CategoricalColorMapper
from bokeh.plotting import figure
from bokeh.models import Slider, Button
from bokeh.layouts import column, row
from bokeh.io import curdoc
import sys
import pathlib
import math

from replay_reader import (read_snoopy, width, height, player_r,
                           bullet_r, parse_bullets, names)

filename = pathlib.Path().cwd().joinpath(sys.argv[1])
g = read_snoopy(filename)
df = g.to_df()

c = ["#4287f5", "#ffa30f"]


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
                        'field': 'player', 'transform': player_map}, radius=bullet_r, source=bullets)
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
