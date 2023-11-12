package com.zoeschinger.app.core;

/** IO is handled here. */
public class PlayerController
{
    private final String name;

    private Pawn possessedPawn;

    private final String lobbyID;

    public PlayerController(final String name,final String lobbyID1)
    {
        this.name = name;
        this.lobbyID = lobbyID1;
    }

    public String getName()
    {
        return this.name;
    }

    public void setPossessedPawn(Pawn pawn)
    {
        this.possessedPawn = pawn;
    }

    public Pawn getPossessedPawn()
    {
        return this.possessedPawn;
    }

    public String getLobbyID()
    {
        return this.lobbyID;
    }

}
