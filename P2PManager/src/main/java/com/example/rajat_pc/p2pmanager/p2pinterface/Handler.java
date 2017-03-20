package com.example.rajat_pc.p2pmanager.p2pinterface;


import java.io.IOException;
import java.nio.channels.SelectionKey;



public interface Handler
{
    /**
     * Handle the client request connection
     * 
     * @param key
     * @throws IOException
     */
    void handleAccept(SelectionKey key) throws IOException;

    void handleRead(SelectionKey key) throws IOException;

    /**
     * Handle the sender to write the file
     * @param key
     * @throws IOException
     */
    void handleWrite(SelectionKey key) throws IOException;
}
