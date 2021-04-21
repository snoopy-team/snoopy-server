package com.server.Controller;

import com.server.Configuration.Constants;
import com.server.Models.Game;
import com.server.Models.GameModel.JSON.GameStateJSON;
import com.server.Models.GameModel.Player;
import com.server.Models.ActionsPacket;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


import java.util.ArrayList;

@Controller
public class GameController {
  static Game game = null;
  private static ArrayList<Player> players = new ArrayList<Player>();
  private static int ids = Constants.AI_INDEX + 1;

  @MessageMapping("/to-server")
  @SendTo("/game/to-client")
  public GameStateJSON greeting(ActionsPacket actions) throws Exception {
    if (game == null) {
      Player player = new Player(Constants.STARTING_POSITION, Constants.STARTING_ORIENTATION,
              ids);
      ids++;
      game = new Game(player);
    }

    //  This is hardcoded as 1 for convenience. If we're still pretending that we'll one day
    //  support multiplayer really this whole controller needs to be thoroughly refactored, but
    //  this especially.
    return game.step(1, actions.getActions());
  }

  @MessageMapping("/gameconfig-to-server")
  @SendTo("/game/gameconfig-to-client")
  public GameStateJSON gameconfig() throws Exception {
    if (game == null) {
      Player player = new Player(Constants.STARTING_POSITION, Constants.STARTING_ORIENTATION, ids);
      ids++;
      game = new Game(player);
    }

    //  This is hardcoded as 1 for convenience. If we're still pretending that we'll one day
    //  support multiplayer really this whole controller needs to be thoroughly refactored, but
    //  this especially.
    return null; //game.step(1, actions.getActions());
  }
}