package com.server.Models;

import java.util.ArrayList;

import com.server.Configuration.Constants;
import com.server.Models.GameModel.Action;

public class ActionsPacket {

  private String[] actions;

  public ActionsPacket() {
  }

  public ActionsPacket(String actions) {
    this.actions = actions.split(",");
  }

  public ArrayList<Action> getActions() {
    ArrayList<Action> actions = new ArrayList<>();

      for (String s : this.actions) {
          Action action = keyStrokeToAction(s);
          if (action != null) {
              actions.add(action);
          }
      }

    return actions;
  }

  private static Action keyStrokeToAction(String keystroke)
  {
    switch (keystroke) {
      case Constants.BANK_LEFT:
        return Action.BankLeft;
      case Constants.BANK_RIGHT:
        return Action.BankRight;
      case Constants.THRUST:
        return Action.Thrust;
      case Constants.FIRE:
        return Action.Fire;
    }
    return null;
  }

  public void setName(String actions) {
    this.actions = actions.split(",");
  }
}