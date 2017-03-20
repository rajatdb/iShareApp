package com.example.rajat_pc.p2pmanager.p2pinterface;


import com.example.rajat_pc.p2pmanager.p2pentity.P2PFileInfo;
import com.example.rajat_pc.p2pmanager.p2pentity.P2PNeighbor;


public interface ReceiveFile_Callback
{
    public boolean QueryReceiving(P2PNeighbor src, P2PFileInfo files[]);

    public void BeforeReceiving(P2PNeighbor src, P2PFileInfo files[]);

    public void OnReceiving(P2PFileInfo files);

    public void AfterReceiving();

    public void AbortReceiving(int error, String alias);
}
