package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.MediaDetailActivity;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.data.MediaContract;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * @author Jiajun Wang on 6/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaAdapter extends
        RecyclerView.Adapter<MediaAdapter.MediaListViewHolder>
{
    public static final String TAG = MediaAdapter.class.getSimpleName();
    private Context mContext;
    private Cursor mCursor;

    public MediaAdapter(Context context)
    {
        mContext = context;
    }

    public void swapCursor(Cursor newCursor)
    {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor()
    {
        return mCursor;
    }

    @Override
    public MediaListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewGroup instanceof RecyclerView)
        {
            int layoutId = R.layout.list_item_media;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new MediaListViewHolder(view);
        }
        else
        {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(MediaListViewHolder viewHolder, int position)
    {
        mCursor.moveToPosition(position);
        int profile_image_index = mCursor.getColumnIndex(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE);
        Picasso.with(mContext).load(mCursor.getString(profile_image_index)).into(viewHolder.mOwnerProfileImage);

        int username_index = mCursor.getColumnIndex(MediaContract.UserEntry.COLUMN_USERNAME);
        viewHolder.mOwnerUserName.setText(mCursor.getString(username_index));

        int media_high_res_index = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
        int media_low_res_index = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
        if (!TextUtils.isEmpty(mCursor.getString(media_high_res_index)))
        {
            Picasso.with(mContext).load(mCursor.getString(media_high_res_index)).into(viewHolder.mMediaImage);
        }
        else
        {
            Picasso.with(mContext).load(mCursor.getString(media_low_res_index)).into(viewHolder.mMediaImage);
        }

        int caption_text_index = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_CAPTION_TEXT);
        if (!TextUtils.isEmpty(mCursor.getString(caption_text_index)))
        {
            viewHolder.mCaptionText.setVisibility(View.VISIBLE);
            viewHolder.mCaptionText.setText(mCursor.getString(caption_text_index));
        }
        else
        {
            viewHolder.mCaptionText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mCursor == null)
        {
            return 0;
        }
        return mCursor.getCount();
    }

    public class MediaListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        public final RoundedImageView mOwnerProfileImage;
        public final TextView mOwnerUserName;
        public final TextView mCaptionText;
        public final ImageView mMediaImage;
        public final View mUserInfoRootView;

        public MediaListViewHolder(View view)
        {
            super(view);
            mOwnerProfileImage = (RoundedImageView) view.findViewById(R.id.owner_profile_image);
            mOwnerUserName = (TextView) view.findViewById(R.id.owner_username);
            mMediaImage = (ImageView) view.findViewById(R.id.media_image);
            mCaptionText = (TextView) view.findViewById(R.id.caption_text);
            mUserInfoRootView = view.findViewById(R.id.root_user_info);

            mUserInfoRootView.setOnClickListener(this);
            mMediaImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int indexOwnerId = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID);
            String ownerId = mCursor.getString(indexOwnerId);
            int indexMediaId = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID);
            String mediaId = mCursor.getString(indexMediaId);

            switch (view.getId())
            {
                case R.id.root_user_info:
                    if (!TextUtils.isEmpty(ownerId))
                    {
                        UserDetailActivity.startActivity(mContext, mOwnerProfileImage, ownerId);
                    }
                    else
                    {
                        Log.e(TAG, "Owner Id is Null!!");
                    }
                    break;
                case R.id.media_image:
                    if (!TextUtils.isEmpty(mediaId))
                    {
                        MediaDetailActivity.startActivity(mContext, mMediaImage, mediaId);
                    }
                    break;
            }
        }
    }
}
