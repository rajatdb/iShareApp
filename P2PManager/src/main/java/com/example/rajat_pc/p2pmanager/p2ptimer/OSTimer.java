package com.example.rajat_pc.p2pmanager.p2ptimer;


import android.os.Handler;



public class OSTimer implements Runnable
{
    private Handler mHandler;
    private int mInterval;
    private Timeout mTimeout;

    private boolean isCycle = false;
    private boolean isCancel = false;

    public OSTimer(Handler handler, Timeout timeout, int interval)
    {
        mTimeout = timeout;
        mInterval = interval;
        isCycle = false;
        if (handler != null)
            mHandler = handler;
        else
            mHandler = new Handler();
    }

    public void start()
    {
        if (mHandler != null)
            mHandler.postDelayed(this, mInterval);
    }

    public void cancel()
    {
        isCancel = true;
    }

    @Override
    public void run()
    {
        if (!isCancel)
        {
            if (mTimeout != null)
                mTimeout.onTimeOut();
            if (isCycle)
                start();
        }
    }
}
