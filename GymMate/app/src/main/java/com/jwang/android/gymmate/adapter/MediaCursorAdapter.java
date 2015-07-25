package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.MediaDetailActivity;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.data.MediaContract;
import com.jwang.android.gymmate.viewholder.MediaListViewHolder;
import com.squareup.picasso.Picasso;

/**
 * @author Jiajun Wang on 7/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaCursorAdapter extends CursorAdapter implements
        View.OnClickListener
{
    public static final String TAG = MediaAdapter.class.getSimpleName();
    private Context mContext;

    public MediaCursorAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        int layoutId = R.layout.list_item_media;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        MediaListViewHolder viewHolder = new MediaListViewHolder(view);
        view.setTag(viewHolder);
        viewHolder.mUserInfoRootView.setOnClickListener(this);
        viewHolder.mUserInfoRootView.setTag(viewHolder);
        viewHolder.mMediaImage.setOnClickListener(this);
        viewHolder.mMediaImage.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        MediaListViewHolder viewHolder = (MediaListViewHolder) view.getTag();
        int profile_image_index = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_PROFILE_PICTURE);
        Picasso.with(context).load(cursor.getString(profile_image_index)).into(viewHolder.mOwnerProfileImage);

        int username_index = cursor.getColumnIndex(MediaContract.UserEntry.COLUMN_USERNAME);
        viewHolder.mOwnerUserName.setText(cursor.getString(username_index));

        int media_high_res_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
        int media_low_res_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
        if (!TextUtils.isEmpty(cursor.getString(media_high_res_index)))
        {
            Picasso.with(context).load(cursor.getString(media_high_res_index)).into(viewHolder.mMediaImage);
        }
        else
        {
            Picasso.with(context).load(cursor.getString(media_low_res_index)).into(viewHolder.mMediaImage);
        }

        int caption_text_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_CAPTION_TEXT);
        if (!TextUtils.isEmpty(cursor.getString(caption_text_index)))
        {
            viewHolder.mCaptionText.setVisibility(View.VISIBLE);
            viewHolder.mCaptionText.setText(cursor.getString(caption_text_index));
        }
        else
        {
            viewHolder.mCaptionText.setVisibility(View.GONE);
        }

        //set media id and owner id;
        int media_id_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID);
        viewHolder.mMediaId = cursor.getString(media_id_index);
        int owner_id_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_OWNER_ID);
        viewHolder.mOwnerId = cursor.getString(owner_id_index);
    }

    @Override
    public void onClick(View view)
    {
        MediaListViewHolder viewHolder = (MediaListViewHolder) view.getTag();
        switch (view.getId())
        {
            case R.id.root_user_info:
                if (!TextUtils.isEmpty(viewHolder.mOwnerId))
                {
                    UserDetailActivity.startActivity(mContext, viewHolder.mOwnerProfileImage, viewHolder.mOwnerId);
                }
                else
                {
                    Log.e(TAG, "Owner Id is Null!!");
                }
                break;
            case R.id.media_image:
                if (!TextUtils.isEmpty(viewHolder.mMediaId))
                {
                    MediaDetailActivity.startActivity(mContext, viewHolder.mMediaImage, viewHolder.mMediaId);
                }
                break;
        }
    }
}
