package com.example.rajat_pc.p2pmanager.p2pinterface;


import com.example.rajat_pc.p2pmanager.p2pentity.P2PNeighbor;


public interface Melon_Callback
{
    /**
     * LAN found friends
     * @param melon
     */
    public void Melon_Found(P2PNeighbor melon);

    /**
     * 局域网好友离开
     * @param melon
     */
    public void Melon_Removed(P2PNeighbor melon);
}
