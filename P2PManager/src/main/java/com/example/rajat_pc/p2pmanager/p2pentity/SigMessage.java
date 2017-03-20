package com.example.rajat_pc.p2pmanager.p2pentity;


import com.example.rajat_pc.p2pmanager.p2pconstant.P2PConstant;

import java.util.Date;



public class SigMessage
{
    /**
     * The numbering time of the sending packet is the number
     */
    public String packetNum;
    /**
     * The nickname of the sender
     */
    public String senderAlias;
    /**
     * The sender's ip address
     */
    public String senderIp;

    public int commandNum;

    public int recipient;
    /**
     * content
     */
    public String addition;

    public SigMessage()
    {
        this.packetNum = getTime();
    }

    public SigMessage(String protocolString)
    {
        protocolString = protocolString.trim();
        String[] args = protocolString.split(":");

        packetNum = args[0];
        senderAlias = args[1];
        senderIp = args[2];
        commandNum = Integer.parseInt(args[3]);
        recipient = Integer.parseInt(args[4]);
        if (args.length > 5)
            addition = args[5];
        else
            addition = null;

        for (int i = 6; i < args.length; i++)
        {
            addition += (":" + args[i]);
        }
    }

    public String toProtocolString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(packetNum);
        sb.append(":");
        sb.append(senderAlias);
        sb.append(":");
        sb.append(senderIp);
        sb.append(":");
        sb.append(commandNum);
        sb.append(":");
        sb.append(recipient);
        sb.append(":");
        sb.append(addition);
        sb.append(P2PConstant.MSG_SEPARATOR);

        return sb.toString();
    }

    private String getTime()
    {
        Date nowDate = new Date();
        return Long.toString(nowDate.getTime());
    }
}
