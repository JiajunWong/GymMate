package com.jwang.android.gymmate.model;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class ModelMedia
{
    public static final String MEDIA_TYPE_IMAGE = "image";
    public static final String MEDIA_TYPE_VIDEO = "video";

    private String mInstagramId;
    private ArrayList<String> mTags;
    private String mType;
    private String mLocationLong;
    private String mLocationLat;
    private long mCreateTime;
    private String mLink;

    private String mImageLowRes;
    private String mImageThumbnail;
    private String mImageHighRes;

    private String mVideoLowBandwidth;
    private String mVideoStandardRes;
    private String mVideoLowRes;

    private String mCaptionText;

    private ModelUser mOwner;

    public ModelMedia()
    {
        mTags = new ArrayList<>();
    }

    public String getTagString()
    {
        if (mTags == null || mTags.size() == 0)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : mTags)
        {
            sb.append(s).append(", ");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public String getCaptionText()
    {
        return mCaptionText;
    }

    public void setCaptionText(String captionText)
    {
        this.mCaptionText = captionText;
    }

    public String getInstagramId()
    {
        return mInstagramId;
    }

    public void setInstagramId(String instagramId)
    {
        this.mInstagramId = instagramId;
    }

    public String getVideoLowBandwidth()
    {
        return mVideoLowBandwidth;
    }

    public void setVideoLowBandwidth(String videoLowBandwidth)
    {
        this.mVideoLowBandwidth = videoLowBandwidth;
    }

    public String getVideoLowRes()
    {
        return mVideoLowRes;
    }

    public void setVideoLowRes(String videoLowRes)
    {
        this.mVideoLowRes = videoLowRes;
    }

    public String getVideoStandardRes()
    {
        return mVideoStandardRes;
    }

    public void setVideoStandardRes(String videoStandardRes)
    {
        this.mVideoStandardRes = videoStandardRes;
    }

    public long getCreateTime()
    {
        return mCreateTime;
    }

    public void setCreateTime(long createTime)
    {
        this.mCreateTime = createTime;
    }

    public String getLink()
    {
        return mLink;
    }

    public void setLink(String link)
    {
        this.mLink = link;
    }

    public String getLocationLat()
    {
        return mLocationLat;
    }

    public void setLocationLat(String locationLat)
    {
        this.mLocationLat = locationLat;
    }

    public String getLocationLong()
    {
        return mLocationLong;
    }

    public void setLocationLong(String locationLong)
    {
        this.mLocationLong = locationLong;
    }

    public String getImageHighRes()
    {
        return mImageHighRes;
    }

    public void setImageHighRes(String mediaHighRes)
    {
        this.mImageHighRes = mediaHighRes;
    }

    public String getImageLowRes()
    {
        return mImageLowRes;
    }

    public void setImageLowRes(String mediaLowRes)
    {
        this.mImageLowRes = mediaLowRes;
    }

    public String getImageThumbnail()
    {
        return mImageThumbnail;
    }

    public void setImageThumbnail(String mediaThumbnail)
    {
        this.mImageThumbnail = mediaThumbnail;
    }

    public ArrayList<String> getTags()
    {
        return mTags;
    }

    public void setTags(ArrayList<String> tags)
    {
        this.mTags = tags;
    }

    public String getType()
    {
        return mType;
    }

    public void setType(String type)
    {
        this.mType = type;
    }

    public ModelUser getOwner()
    {
        return mOwner;
    }

    public void setOwner(ModelUser modelUser)
    {
        this.mOwner = modelUser;
    }
}
