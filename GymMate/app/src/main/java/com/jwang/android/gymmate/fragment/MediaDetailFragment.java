package com.jwang.android.gymmate.fragment;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.data.MediaContract;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by jiajunwang on 7/18/15.
 */
public class MediaDetailFragment extends BaseFragment
{
    private RoundedImageView mOwnerProfileImage;
    private ImageView mMediaImageView;
    private VideoView mMediaVideoView;
    private TextView mOwnerUserName;
    private TextView mCaptionText;
    private TextView mLocationText;
    private View mUserInfoRootView;
    private View mLocationRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_media_detail, container, false);
        mMediaImageView = (ImageView) view.findViewById(R.id.media_image);
        mMediaVideoView = (VideoView) view.findViewById(R.id.media_video);
        mOwnerUserName = (TextView) view.findViewById(R.id.owner_username);
        mCaptionText = (TextView) view.findViewById(R.id.caption_text);
        mLocationText = (TextView) view.findViewById(R.id.gym_location_tv);
        mUserInfoRootView = view.findViewById(R.id.root_user_info);
        mLocationRootView = view.findViewById(R.id.root_view_location);
        mOwnerProfileImage = (RoundedImageView) view.findViewById(R.id.owner_profile_image);
        return view;
    }

    public void setModelMedia(Cursor cursor)
    {
        if (cursor != null)
        {
            int profile_image_index = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE);
            Picasso.with(getActivity()).load(cursor.getString(profile_image_index)).into(mOwnerProfileImage);

            int username_index = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_USERNAME);
            if (!TextUtils.isEmpty(cursor.getString(username_index)))
            {
                mOwnerUserName.setText(cursor.getString(username_index));
            }

            int caption_text_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_CAPTION_TEXT);
            if (!TextUtils.isEmpty(cursor.getString(caption_text_index)))
            {
                mCaptionText.setVisibility(View.VISIBLE);
                mCaptionText.setText(cursor.getString(caption_text_index));
            }
            else
            {
                mCaptionText.setVisibility(View.GONE);
            }

            int location_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_LOCATION_NAME);
            if (!TextUtils.isEmpty(cursor.getString(location_index)))
            {
                mLocationRootView.setVisibility(View.VISIBLE);
                mLocationText.setText(cursor.getString(location_index));
            }
            else
            {
                mLocationRootView.setVisibility(View.GONE);
            }

            int indexImageHigh = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
            int indexImageLow = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
            int indexVideoHigh = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_VIDEO_STANDARD_RES);
            int indexMediaType = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_TYPE);

            String mediaType = cursor.getString(indexMediaType);
            String videoUrl = cursor.getString(indexVideoHigh);
            String imageHighUrl = cursor.getString(indexImageHigh);
            String imageLowUrl = cursor.getString(indexImageLow);

            if (mediaType.equals("image"))
            {
                mMediaImageView.setVisibility(View.VISIBLE);
                mMediaVideoView.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(imageHighUrl))
                {
                    Picasso.with(getActivity()).load(imageHighUrl).into(mMediaImageView);
                }
                else
                {
                    Picasso.with(getActivity()).load(imageLowUrl).into(mMediaImageView);
                }
            }
            else if (mediaType.equals("video"))
            {
                mMediaImageView.setVisibility(View.GONE);
                mMediaVideoView.setVisibility(View.VISIBLE);

                Uri video = Uri.parse(videoUrl);
                mMediaVideoView.setVideoURI(video);
                mMediaVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        if (mMediaVideoView != null && mMediaVideoView.getVisibility() == View.VISIBLE)
                        {
                            mMediaVideoView.start();
                        }
                    }
                });
                mMediaVideoView.start();
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mMediaVideoView.getVisibility() == View.VISIBLE)
        {
            mMediaVideoView.pause();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
