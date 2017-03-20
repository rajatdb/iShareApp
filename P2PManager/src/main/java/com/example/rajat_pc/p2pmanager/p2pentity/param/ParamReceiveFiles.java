package com.example.rajat_pc.p2pmanager.p2pentity.param;


import com.example.rajat_pc.p2pmanager.p2pentity.P2PFileInfo;
import com.example.rajat_pc.p2pmanager.p2pentity.P2PNeighbor;


public class ParamReceiveFiles
{
    public P2PNeighbor Neighbor;
    public P2PFileInfo[] Files;

    public ParamReceiveFiles(P2PNeighbor dest, P2PFileInfo[] files)
    {
        Neighbor = dest;
        Files = files;
    }
}
