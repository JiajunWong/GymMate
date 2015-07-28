package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.MediaDetailActivity;
import com.jwang.android.gymmate.data.MediaContract;
import com.squareup.picasso.Picasso;

/**
 * @author Jiajun Wang on 7/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class UserMediaCursorAdapter extends CursorAdapter implements
        View.OnClickListener
{

    private Context mContext;

    public UserMediaCursorAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        int layoutId = R.layout.list_item_user_media_grid;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        viewHolder.mImageView.setOnClickListener(this);
        viewHolder.mImageView.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int image_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
        if (TextUtils.isEmpty(cursor.getString(image_index)))
        {
            Picasso.with(mContext).load(cursor.getString(image_index)).into(viewHolder.mImageView);
        }
        else
        {
            image_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
            Picasso.with(mContext).load(cursor.getString(image_index)).into(viewHolder.mImageView);
        }

        int type_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_TYPE);
        if (!TextUtils.isEmpty(cursor.getString(type_index)))
        {
            if (cursor.getString(type_index).equals("video"))
            {
                viewHolder.mVideoImageIcon.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.mVideoImageIcon.setVisibility(View.GONE);
            }
        }

        //set media id and owner id;
        int media_id_index = cursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID);
        viewHolder.mMediaId = cursor.getString(media_id_index);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_media:
                ViewHolder viewHolder = (ViewHolder) v.getTag();
                if (!TextUtils.isEmpty(viewHolder.mMediaId))
                {
                    MediaDetailActivity.startActivity(mContext, viewHolder.mImageView, viewHolder.mMediaId);
                }
                break;
        }
    }

    public class ViewHolder
    {
        public ImageView mImageView;
        public ImageView mVideoImageIcon;
        public String mMediaId;

        public ViewHolder(View v)
        {
            mImageView = (ImageView) v.findViewById(R.id.user_media);
            mVideoImageIcon = (ImageView) v.findViewById(R.id.media_video_icon);
        }
    }
}
