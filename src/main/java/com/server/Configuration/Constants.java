package com.server.Configuration;

import com.server.Models.GameModel.GamePosn;

public class Constants {

  // Constants; should probably change these to read from a file or something
  public final static double THRUST_POWER = 1;
  public final static double GRAVITY_STRENGTH = 0.25;
  public final static double DRAG_FACTOR = 0.001;

  public final static double TURN_SPEED = 0.1;
  public final static double BULLET_RADIUS = 5;
  public final static double BULLET_SPEED = 1;
  public final static double PLAYER_RADIUS = 15;
  public final static double BULLET_COOLDOWN = 20;

  public final static GamePosn STARTING_POSITION = new GamePosn(0, 0); // Potentially we wanna
  public final static double STARTING_ORIENTATION = 0;

  public final static GamePosn AI_STARTING_POSITION = new GamePosn(0, 0); // Potentially we wanna
  public final static double AI_STARTING_ORIENTATION = 0;
  public final static int AI_INDEX = 0;

  public final static int WIDTH = 500;
  public final static int HEIGHT = 500;

  // Constants mapping keys to actions
  public final static String BANK_LEFT = "arrowleft";
  public final static String BANK_RIGHT = "arrowright";
  public final static String THRUST = "arrowup";
  public final static String FIRE = " ";

}
