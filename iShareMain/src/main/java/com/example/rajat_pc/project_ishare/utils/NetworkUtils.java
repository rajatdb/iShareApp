package com.example.rajat_pc.project_ishare.utils;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;



public class NetworkUtils
{

    public static String getCurrentSSID(Context context)
    {
        WifiManager wifiMan = (WifiManager) (context
                .getSystemService(Context.WIFI_SERVICE));
        WifiInfo wifiInfo = wifiMan.getConnectionInfo();

        if (wifiInfo != null)
            return wifiInfo.getSSID();
        else
            return null;
    }

    public synchronized static Inet4Address getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        if (inetAddress instanceof Inet4Address)
                        {
                            return ((Inet4Address) inetAddress);
                        }
                    }
                }
            }
        }
        catch (SocketException ex)
        {
        }
        return null;
    }

    public synchronized static String[] getMACAddress(InetAddress ia) throws Exception
    {

        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();


        String[] str_array = new String[2];
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();

        for (int i = 0; i < mac.length; i++)
        {
            if (i != 0)
            {
                sb1.append(":");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb1.append(s.length() == 1 ? 0 + s : s);
            sb2.append(s.length() == 1 ? 0 + s : s);
        }
        str_array[0] = sb1.toString();
        str_array[1] = sb2.toString();
        return str_array;
        //return sb1.toString().toUpperCase();
    }

    public static String getLocalIp(Context context)
    {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);

        return ip;
    }

    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        if (wifi == NetworkInfo.State.CONNECTED)
            return true;
        else
            return false;
    }

    private static String intToIp(int i)
    {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
            + (i >> 24 & 0xFF);
    }

    /**
     * @param context
     * @return
     * @throws UnknownHostException
     */
    public static InetAddress getBroadcastAddress(Context context)
            throws UnknownHostException
    {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null)
        {
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}
