package com.server.Configuration;

import com.server.Models.GameModel.GamePosn;

public class Constants {

  //  public final static double THRUST_POWER = 1;
  //  public final static double GRAVITY_STRENGTH = 0.25;
  //  public final static double DRAG_FACTOR = 0.001;

  // Constants; should probably change these to read from a file or something
  // "tight" controls
  private final static double TERMINAL_VELOCITY = 5.0;
  private final static double THRUST_MAX_SPEED = 20.0;
  public final static double DRAG_FACTOR = 0.4;
  // "loose" controls
  // private final static double TERMINAL_VELOCITY = 4.0;
  // private final static double THRUST_MAX_SPEED = 12.0;
  // public final static double DRAG_FACTOR = 0.2;
  public final static double GRAVITY_STRENGTH = DRAG_FACTOR * TERMINAL_VELOCITY * TERMINAL_VELOCITY;
  public final static double THRUST_POWER = DRAG_FACTOR * THRUST_MAX_SPEED * THRUST_MAX_SPEED;

  public final static double TURN_SPEED = 4.0;
  public final static double BULLET_RADIUS = 5;
  public final static double BULLET_SPEED = 1000;
  public final static double PLAYER_RADIUS = 15;
  public final static double BULLET_COOLDOWN = 1;

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
