package com.example.rajat_pc.p2pmanager.p2pentity.param;


import com.example.rajat_pc.p2pmanager.p2pentity.SigMessage;

import java.net.InetAddress;



public class ParamIPMsg
{
    public SigMessage peerMSG;
    public InetAddress peerIAddr;
    public int peerPort;

    public ParamIPMsg(String msg, InetAddress addr, int port)
    {
        peerMSG = new SigMessage(msg);
        peerIAddr = addr;
        peerPort = port;
    }
}
