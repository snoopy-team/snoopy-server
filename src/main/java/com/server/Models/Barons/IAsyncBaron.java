package com.server.Models.Barons;

import com.server.Models.GameModel.Action;

import java.util.List;

public interface IAsyncBaron extends IBaron {


  public List<Action> forceAsyncActions();
}
