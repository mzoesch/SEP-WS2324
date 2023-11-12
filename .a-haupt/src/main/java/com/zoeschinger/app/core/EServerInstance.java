package com.zoeschinger.app.core;


import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

/** Accepts clients connections. */
public enum EServerInstance
{
    INSTANCE;

    private final ServerSocket serverSocket;
    private final ArrayList<Lobby> lobbies;
    private final Listener listener;

    EServerInstance()
    {
        this.serverSocket = null;
        this.lobbies = new ArrayList<Lobby>();
        this.listener = new Listener(this.serverSocket);
        listener.listen();
    }

    public static EServerInstance launch()
    {
        return INSTANCE;
    }

    public void createLobby()
    {
        return;
    }

    public void destroyLobby()
    {
        return;
    }

    public void addPlayerToLobby(PlayerController PC)
    {
        return;
    }

    public void disconnectPlayerFromLobby(PlayerController PC)
    {
        return;
    }

    public Lobby getLobbyByID(String id)
    {
        return null;
    }
}
