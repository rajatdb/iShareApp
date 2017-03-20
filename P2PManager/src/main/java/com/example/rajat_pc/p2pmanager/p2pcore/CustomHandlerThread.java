package com.example.rajat_pc.p2pmanager.p2pcore;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;


public class CustomHandlerThread extends Thread
{


    Handler mHandler;
    int mPriority;
    int mTid;
    Looper mLooper;
    boolean mIsReady = false;
    Class<? extends Handler> mHandlerClass;

    public CustomHandlerThread(String threadName, Class<? extends Handler> handlerClass)
    {
        super(threadName);
        mPriority = Process.THREAD_PRIORITY_DEFAULT;
        mHandlerClass = handlerClass;
    }

    public CustomHandlerThread(String threadName, int prority,
            Class<? extends Handler> handlerClass)
    {
        super(threadName);
        mPriority = prority;
        this.mHandlerClass = handlerClass;
    }

    public Handler getLooperHandler()
    {
        return mHandler;
    }

    public void isReady()
    {
        synchronized (this)
        {
            while (mIsReady == false)
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

    public void run()
    {
        mTid = Process.myTid();
        Looper.prepare();
        synchronized (this)
        {
            mLooper = Looper.myLooper();
            notifyAll();
        }

        Process.setThreadPriority(mPriority);

        try
        {
            Constructor<? extends Handler> handler_creater = mHandlerClass
                    .getConstructor(Looper.class);
            mHandler = (Handler) handler_creater.newInstance(mLooper);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        onLooperPrepared();
        Looper.loop();
        mTid = -1;
    }

    protected void onLooperPrepared()
    {
        synchronized (this)
        {
            mIsReady = true;
            notifyAll();
        }
    }

    private Looper getLooper()
    {
        if (!isAlive())
            return null;

        synchronized (this)
        {
            while (isAlive() && mLooper == null)
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

        return mLooper;
    }

    public boolean quit()
    {
        Looper looper = getLooper();
        if (looper != null)
        {
            looper.quit();
            return true;
        }
        return false;
    }

    @TargetApi(18)
    public boolean quitSafely()
    {
        Looper looper = getLooper();
        if (looper != null)
        {
            looper.quitSafely();
            return true;
        }
        return false;
    }
}
