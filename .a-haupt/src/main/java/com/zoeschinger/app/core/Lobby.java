package com.zoeschinger.app.core;

import java.util.ArrayList;
import org.json.JSONObject;

public class Lobby
{
    private final String id;

    private ArrayList<PlayerController> playerControllers;
    private GameState gameState;

    public Lobby()
    {
        this.id = this.generateID();

        this.playerControllers = new ArrayList<PlayerController>();
        this.gameState = new GameState();
    }

    private String generateID()
    {
        return "";
    }

    public String getID()
    {
        return this.id;
    }

    public void join(PlayerController playerController)
    {

    }

    public void leave(PlayerController playerController)
    {    }

    public GameState getGameState()
    {
        return this.gameState;
    }

    public void startGame()
    {
        this.gameState.startGame(this.playerControllers.toArray(new PlayerController[0]));
    }

    public void broadcastGameState()
    {
    }

    public void broadcast(JSONObject message)
    {
    }

}
