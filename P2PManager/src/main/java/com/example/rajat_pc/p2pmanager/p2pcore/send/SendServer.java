package com.example.rajat_pc.p2pmanager.p2pcore.send;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import android.util.Log;

import com.example.rajat_pc.p2pmanager.p2pinterface.Handler;



public class SendServer extends Thread
{
    private static final String tag = SendServer.class.getSimpleName();

    Handler handler;
    int port;
    Selector selector;
    ServerSocketChannel serverSocketChannel;
    boolean ready = false;

    public SendServer(Handler handler, int port)
    {
        this.handler = handler;
        this.port = port;
    }

    @Override
    public void run()
    {
        try
        {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            Log.i(tag, "socket server started.");

            onReady();

            while (true)
            {
                int keys = selector.select();
                if (isInterrupted())
                    return;
                if (keys > 0)
                {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext())
                    {
                        SelectionKey key = it.next();
                        if (key.isAcceptable())
                        {
                            handler.handleAccept(key);
                        }
                        if (key.isReadable())
                        {
                            handler.handleRead(key);
                        }
                        if (key.isWritable())
                        {
                            handler.handleWrite(key);
                        }
                        it.remove();
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void quit()
    {
        interrupt();
        release();
    }

    private void release()
    {
        Log.d(tag, "send server release");
        if (serverSocketChannel != null)
        {
            try
            {
                serverSocketChannel.socket().close();
                serverSocketChannel.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (selector != null)
        {
            try
            {
                selector.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void isReady()
    {
        synchronized (this)
        {
            while (ready == false)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onReady()
    {
        synchronized (this)
        {
            ready = true;
            notifyAll();
        }
    }
}
