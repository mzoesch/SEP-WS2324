package com.zoeschinger.app.core;

import org.json.JSONObject;

/** Relevant information to sync to all clients. */
public class GameState
{
    private GameMode gameMode;
    private boolean bGameStarted;

    public GameState()
    {
        this.bGameStarted = false;
    }

    public void startGame(PlayerController[] playerControllers)
    {
        this.bGameStarted = true;
        this.gameMode = new GameMode("course1", playerControllers);

        while (true)
        {
            this.gameMode.prepareNextRound();
            this.gameMode.startRound();

            // Check for player win
        }
    }

    public GameMode getAuthGameMode()
    {
        return this.gameMode;
    }

    /**
     * Should return a JSON object that represents the state of the game to sync to all clients.
     * They will have to figure out how to interpret it and update the view accordingly.
     */
    public JSONObject getUpdatedState()
    {
        // Get player positions, checkpoint positions, etc.
        // Hand cards of one player are not relevant to other players, we may not include this information here.
        return new JSONObject();
    }

}
