package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.data.MediaContract;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * @author Jiajun Wang on 6/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaAdapter extends CursorAdapter
{
    public MediaAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        int layoutId = R.layout.list_item_media;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
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
    }

    public static class ViewHolder
    {
        public final RoundedImageView mOwnerProfileImage;
        public final TextView mOwnerUserName;
        public final TextView mCaptionText;
        public final ImageView mMediaImage;

        public ViewHolder(View view)
        {
            mOwnerProfileImage = (RoundedImageView) view.findViewById(R.id.owner_profile_image);
            mOwnerUserName = (TextView) view.findViewById(R.id.owner_username);
            mMediaImage = (ImageView) view.findViewById(R.id.media_image);
            mCaptionText = (TextView) view.findViewById(R.id.caption_text);
        }
    }

}
