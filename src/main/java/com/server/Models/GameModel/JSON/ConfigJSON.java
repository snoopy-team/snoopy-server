package com.server.Models.GameModel.JSON;

public class ConfigJSON {
  private int width;
  private int height;
  private double[] snoopySize;
  private double[] baronSize;

  public ConfigJSON(int width, int height, double[] snoopySize, double[] baronSize) {
    this.width = width;
    this.height = height;
    this.snoopySize = snoopySize;
    this.baronSize = baronSize;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public double[] getSnoopySize() {
    return snoopySize;
  }

  public double[] getBaronSize() {
    return baronSize;
  }
}
