#!/usr/bin/env python3

import numpy as np
import pandas as pd
import json


class Action:
    BANK_LEFT = "L"
    BANK_RIGHT = "R"
    THRUST = "T"
    FIRE = "F"

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
            rows.append(
                {
                    "t": state["t"],
                    "t_int": i + 1,
                    "x1": state["players"]["0"]["position"][0],
                    "y1": state["players"]["0"]["position"][1],
                    "vx1": state["players"]["0"]["velocity"][0],
                    "vy1": state["players"]["0"]["velocity"][1],
                    "a1": state["players"]["0"]["orientation"],
                    "x2": state["players"]["1"]["position"][0],
                    "y2": state["players"]["1"]["position"][1],
                    "vx2": state["players"]["1"]["velocity"][0],
                    "vy2": state["players"]["1"]["velocity"][1],
                    "a2": state["players"]["1"]["orientation"],
                    "bullets": [state["bullets"][k] for k in "01"],
                }
            )
        rows = pd.DataFrame(rows).set_index("t_int")
        rows["a1_0"] = rows["a1"] + wedge_size / 2
        rows["a1_1"] = rows["a1"] - wedge_size / 2
        rows["a2_0"] = rows["a2"] + wedge_size / 2
        rows["a2_1"] = rows["a2"] - wedge_size / 2
        return rows


# In[19]:


width = 50 * 20
height = 50 * 20
player_r = 30
bullet_r = 5
wedge_size = np.pi / 4


def parse_player_actions(actions):
    return {act: act in actions for act in Action.ACTIONS}


def read_snoopy(filename):
    with open(filename, "r") as stream:
        actiondata, statedata, resultdata = stream.read().split("\n\n")
    actions = []
    for line in actiondata.split("\n"):
        actions.append([parse_player_actions(acts)
                        for acts in line.split(" ")])

    states = [json.loads(state) for state in statedata.split("\n")]
    res = int(resultdata.strip())
    return Game(actions, states, res)


names = ['Player 1', 'Player 2']


def parse_bullets(bulletlist):
    rows = []
    for p_bullets, name in zip(bulletlist, names):
        for bullet in p_bullets:
            rows.append(
                {"x": bullet["position"][0],
                    "y": bullet["position"][1], "player": name}
            )
    return pd.DataFrame(rows)
