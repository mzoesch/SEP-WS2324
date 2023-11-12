package com.zoeschinger.app.core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import org.json.JSONObject;

public class ClientInstance implements Runnable
{
    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;

    private PlayerController playerController;

    public ClientInstance(Socket clientSocket)
    {
        this.socket = clientSocket;
        try
        {
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run()
    {
        // Read from the socket.
        // Create a PC and add to a lobby, maybe ask the client for player name or something like that.
        this.playerController = null;
        EServerInstance.INSTANCE.createLobby(); // Optional new lobby or lobby with already connected players.
        EServerInstance.INSTANCE.addPlayerToLobby(null);
    }

    public void handleDisconnect()
    {
    }

    public void send(JSONObject jsonObject)
    {
    }

}
