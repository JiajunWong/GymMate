package com.jwang.android.gymmate.model;

import android.database.Cursor;
import android.text.TextUtils;

import com.jwang.android.gymmate.data.MediaContract;

import java.util.ArrayList;
import java.util.Comparator;

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
    private String mLocationId;
    private String mLocationName;
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

    public static Comparator<ModelMedia> sComparator = new Comparator<ModelMedia>()
    {
        @Override
        public int compare(ModelMedia m1, ModelMedia m2)
        {
            if (m1.getCreateTime() < m2.getCreateTime())
            {
                return 1;
            }
            else if (m1.getCreateTime() > m2.getCreateTime())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    };

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

    public void setLocationId(String id)
    {
        mLocationId = id;
    }

    public String getLocationId()
    {
        return mLocationId;
    }

    public void setLocationName(String name)
    {
        mLocationName = name;
    }

    public String getLocationName()
    {
        return mLocationName;
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

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ModelMedia)
        {
            ModelMedia modelMedia = (ModelMedia) o;
            if (TextUtils.isEmpty(modelMedia.getInstagramId()) || TextUtils.isEmpty(getInstagramId()))
            {
                return false;
            }
            if (modelMedia.getInstagramId().equals(getInstagramId()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    public static ArrayList<ModelMedia> getMedias(Cursor cursor)
    {
        ArrayList<ModelMedia> medias = new ArrayList<>();
        ModelMedia modelMedia;
        while (cursor.moveToNext())
        {
            modelMedia = new ModelMedia();
            int indexInstagramId = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID);
            modelMedia.setInstagramId(cursor.getString(indexInstagramId));

            //TODO: missing tags
            int indexType = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_TYPE);
            modelMedia.setType(cursor.getString(indexType));

            int indexLocationLng = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_LOCATION_LONGITUDE);
            modelMedia.setLocationLong(cursor.getString(indexLocationLng));

            int indexLocationLat = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_LOCATION_LATITUDE);
            modelMedia.setLocationLat(cursor.getString(indexLocationLat));

            int indexLocationId = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_LOCATION_INSTAGRAM_ID);
            modelMedia.setLocationId(cursor.getString(indexLocationId));

            int indexLocationName = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_LOCATION_NAME);
            modelMedia.setLocationName(cursor.getString(indexLocationName));

            int indexCreateTime = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_CREATE_TIME);
            modelMedia.setCreateTime(cursor.getShort(indexCreateTime));

            int indexLink = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_LINK);
            modelMedia.setLink(cursor.getString(indexLink));

            int indexImageLow = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
            modelMedia.setImageLowRes(cursor.getString(indexImageLow));

            int indexImageThumbnail = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_THUMBNAIL);
            modelMedia.setImageThumbnail(cursor.getString(indexImageThumbnail));

            int indexImageHigh = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
            modelMedia.setImageHighRes(cursor.getString(indexImageHigh));

            int indexVideoLow = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_RES);
            modelMedia.setVideoLowRes(cursor.getString(indexVideoLow));

            int indexVideoLowBrand = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_LOW_BANDWIDTH);
            modelMedia.setVideoLowBandwidth(cursor.getString(indexVideoLowBrand));

            int indexVideoHigh = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_STANDARD_RES);
            modelMedia.setVideoStandardRes(cursor.getString(indexVideoHigh));

            int indexCaptionText = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_CAPTION_TEXT);
            modelMedia.setCaptionText(cursor.getString(indexCaptionText));

            ModelUser modelUser = new ModelUser();
            int indexUserInstagramId = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_INSTAGRAM_ID);
            if (indexUserInstagramId != -1)
            {
                modelUser.setInstagramId(cursor.getLong(indexUserInstagramId));

                int indexUserName = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_USERNAME);
                modelUser.setUserName(cursor.getString(indexUserName));

                int indexProfilePic = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE);
                modelUser.setProfilePicture(cursor.getString(indexProfilePic));

                int indexFullName = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_FULL_NAME);
                modelUser.setFullName(cursor.getString(indexFullName));

                int indexMediaCount = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_MEDIA_COUNT);
                modelUser.setMediaCount(cursor.getInt(indexMediaCount));

                int indexFollowedByCount = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_FOLLOWED_BY_COUNT);
                modelUser.setFollowedByCount(cursor.getInt(indexFollowedByCount));

                int indexFollowsCount = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_FOLLOW_COUNT);
                modelUser.setFollowsCount(cursor.getInt(indexFollowsCount));

                modelMedia.setOwner(modelUser);
            }
            medias.add(modelMedia);
        }
        return medias;
    }
}
