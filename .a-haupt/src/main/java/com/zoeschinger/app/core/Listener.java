package com.zoeschinger.app.core;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

/** We should not block the main thread. */
public class Listener
{
    private final ServerSocket serverSocket;

    public Listener(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    public void listen()
    {
        // Of course on separate thread.
        try
        {
            Socket client = this.serverSocket.accept();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        ClientInstance instance = new ClientInstance(null);
    }
}
