package com.example.rajat_pc.project_ishare.ui.setting.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rajat_pc.p2pmanager.p2pconstant.P2PConstant;
import com.example.rajat_pc.p2pmanager.p2pcore.P2PManager;
import com.example.rajat_pc.project_ishare.R;
import com.example.rajat_pc.project_ishare.ui.setting.view.ReceivedAppAdapter;
import com.example.rajat_pc.project_ishare.ui.uientity.IInfo;
import com.example.rajat_pc.project_ishare.ui.uientity.PictureInfo;
import com.example.rajat_pc.project_ishare.utils.DeviceUtils;

import java.io.File;
import java.util.ArrayList;


/**
 * show the received images, click item to browse the big one
 */
public class ReceivedPicture extends Fragment
{
    private static final String tag = ReceivedPicture.class.getSimpleName();
    private View mView;
    private ArrayList<IInfo> mPicList;
    private ReceivedAppAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mNoContentTextView;

    public static ReceivedPicture newInstance()
    {
        ReceivedPicture fragment = new ReceivedPicture();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        {
            if (mView == null)
            {
                mView = inflater.inflate(R.layout.fragment_received_picture, container, false);
                mRecyclerView = (RecyclerView) mView.findViewById(R.id.receivedpic_recyclerview);
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                mRecyclerView.setVisibility(View.GONE);
                mNoContentTextView = (TextView) mView.findViewById(R.id.receivedpic_textview);
                initData();
            }
            return mView;
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    private void initData()
    {
        String picDir = P2PManager.getSavePath(P2PConstant.TYPE.PIC);
        Log.d(tag, "picture dir = " + picDir);

        if (!TextUtils.isEmpty(picDir))
        {
            File picFile = new File(picDir);
            if (picFile.exists() && picFile.isDirectory())
            {
                File[] picFileArray = picFile.listFiles();
                if (picFileArray != null && picFileArray.length > 0)
                {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoContentTextView.setVisibility(View.GONE);
                    mPicList = new ArrayList<>();
                    for (File pic : picFileArray)
                    {
                        PictureInfo picInfo = new PictureInfo();
                        if (pic.isFile() && pic.getAbsolutePath().endsWith(".jpg"))
                        {
                            picInfo.picName = pic.getName();
                            picInfo.picSize = DeviceUtils.convertByte(pic.length());
                            picInfo.picPath = pic.getAbsolutePath();

                            if (!mPicList.contains(picInfo))
                                mPicList.add(picInfo);
                        }
                    }

                    mAdapter = new ReceivedAppAdapter(getActivity(), mPicList);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        }
    }

}
