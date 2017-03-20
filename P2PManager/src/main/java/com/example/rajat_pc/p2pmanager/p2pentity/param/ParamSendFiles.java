package com.example.rajat_pc.p2pmanager.p2pentity.param;


import com.example.rajat_pc.p2pmanager.p2pentity.P2PFileInfo;
import com.example.rajat_pc.p2pmanager.p2pentity.P2PNeighbor;


public class ParamSendFiles
{
    public P2PNeighbor[] neighbors;
    public P2PFileInfo[] files;

    public ParamSendFiles(P2PNeighbor[] neighbors, P2PFileInfo[] files)
    {
        this.neighbors = neighbors;
        this.files = files;
    }
}
