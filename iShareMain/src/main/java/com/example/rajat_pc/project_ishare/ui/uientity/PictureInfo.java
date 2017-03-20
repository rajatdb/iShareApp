package com.example.rajat_pc.project_ishare.ui.uientity;


import android.graphics.drawable.Drawable;

import com.example.rajat_pc.p2pmanager.p2pconstant.P2PConstant;



public class PictureInfo implements IInfo
{
    public int type = P2PConstant.TYPE.PIC;
    public String picPath;
    public String picSize;
    public String picName;

    @Override
    public String getFilePath()
    {
        return picPath;
    }

    @Override
    public String getFileSize()
    {
        return picSize;
    }

    @Override
    public int getFileType()
    {
        return type;
    }

    @Override
    public Drawable getFileIcon()
    {
        return null;
    }

    @Override
    public String getFileName()
    {
        return picName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (getFilePath() != null && ((PictureInfo) o).getFilePath() != null)
            return getFilePath().equals(((PictureInfo) o).getFilePath());
        else
            return false;
    }
}
