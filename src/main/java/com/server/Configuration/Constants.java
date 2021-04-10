package com.server.Configuration;

import com.server.Models.GameModel.GamePosn;

public class Constants {

  // Constants; should probably change these to read from a file or something
  public final static double THRUST_POWER = 1.5;
  public final static double GRAVITY_STRENGTH = 1.0;
  public final static double DRAG_FACTOR = 0.1;

  public final static double TURN_SPEED = 2;
  public final static double BULLET_RADIUS = 5;
  public final static double BULLET_SPEED = 10;
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
  public final static String BANK_LEFT = "ArrowLeft";
  public final static String BANK_RIGHT = "ArrowRight";
  public final static String THRUST = "ArrowUp";
  public final static String FIRE = " ";

}
