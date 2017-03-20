package com.example.rajat_pc.p2pmanager.p2pcore.send;


import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.example.rajat_pc.p2pmanager.p2pconstant.P2PConstant;
import com.example.rajat_pc.p2pmanager.p2pinterface.Handler;



public class SendServerHandler implements Handler
{
    private static final String tag = SendServerHandler.class.getSimpleName();

    static final int CORE_POOL_SIZE = P2PConstant.MAXIMUM_POOL_SIZE;
    static final int KEEP_ALIVE_TIME = 1;
    static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    SendManager sendManager;
    BlockingQueue<Runnable> sendBlockingQueue;
    ThreadPoolExecutor sendThreadPool;

    public SendServerHandler(SendManager sendManager)
    {
        this.sendManager = sendManager;
        sendBlockingQueue = new LinkedBlockingDeque<>();
        sendThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
            P2PConstant.MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
            sendBlockingQueue);
        sendThreadPool.allowCoreThreadTimeOut(true);
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException
    {
        Log.d(tag, "handle accept");

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        String peerIp = socketChannel.socket().getInetAddress().getHostAddress();
        Sender sender = sendManager.getSender(peerIp);
        if (sender == null)
        {
            socketChannel.close();
            return;
        }
        socketChannel.configureBlocking(false);

        SendTask sendTask = new SendTask(sender, socketChannel);
        if (sendTask.prepare() == SendTask.TRANS_START)
        {
            socketChannel.register(key.selector(), SelectionKey.OP_WRITE, sendTask);
        }
        else
        {
            socketChannel.close();
            return;
        }
        sender.mSendTasks.add(sendTask);
        sendTask.notifySender(P2PConstant.CommandNum.SEND_TCP_ESTABLISHED, null);
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException
    {

    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException
    {
        Log.d(tag, "handle write");

        SendTask sendTask = (SendTask) key.attachment();
        key.cancel();

        sendTask.waitRun();
        sendThreadPool.execute(sendTask);
    }
}
