package com.jwang.android.gymmate.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaListViewHolder extends BaseViewHolder
{
    public final RoundedImageView mOwnerProfileImage;
    public final TextView mOwnerUserName;
    public final TextView mCaptionText;
    public final TextView mLocationText;
    public final ImageView mMediaImage;
    public final View mUserInfoRootView;
    public final View mLocationRootView;
    public String mMediaId;
    public String mOwnerId;
    public String mLocationId;

    public MediaListViewHolder(View view)
    {
        mOwnerProfileImage = (RoundedImageView) view.findViewById(R.id.owner_profile_image);
        mOwnerUserName = (TextView) view.findViewById(R.id.owner_username);
        mMediaImage = (ImageView) view.findViewById(R.id.media_image);
        mCaptionText = (TextView) view.findViewById(R.id.caption_text);
        mLocationText = (TextView) view.findViewById(R.id.gym_location_tv);
        mUserInfoRootView = view.findViewById(R.id.root_user_info);
        mLocationRootView = view.findViewById(R.id.root_view_location);
    }
}
