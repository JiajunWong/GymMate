package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.MediaDetailActivity;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.model.ModelMedia;
import com.jwang.android.gymmate.viewholder.MediaListViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaListAdapter extends BaseAdapter implements
        View.OnClickListener
{
    private Context mContext;
    private ArrayList<ModelMedia> mModelMedias = new ArrayList<>();

    public MediaListAdapter(Context context)
    {
        mContext = context;
    }

    public void setModelMedias(ArrayList<ModelMedia> arrayList)
    {
        mModelMedias = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return mModelMedias.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mModelMedias.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MediaListViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_media, parent, false);
            viewHolder = new MediaListViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (MediaListViewHolder) convertView.getTag();
        }

        viewHolder.mUserInfoRootView.setOnClickListener(this);
        viewHolder.mUserInfoRootView.setTag(viewHolder);
        viewHolder.mMediaImage.setOnClickListener(this);
        viewHolder.mMediaImage.setTag(viewHolder);

        ModelMedia modelMedia = mModelMedias.get(position);
        String profileUrl = modelMedia.getOwner().getProfilePicture();
        if (!TextUtils.isEmpty(profileUrl))
        {
            Picasso.with(mContext).load(profileUrl).into(viewHolder.mOwnerProfileImage);
        }

        String username = modelMedia.getOwner().getUserName();
        if (!TextUtils.isEmpty(username))
        {
            viewHolder.mOwnerUserName.setText(username);
        }

        String highResImage = modelMedia.getImageHighRes();
        String lowResImage = modelMedia.getImageLowRes();
        if (!TextUtils.isEmpty(highResImage))
        {
            Picasso.with(mContext).load(highResImage).into(viewHolder.mMediaImage);
        }
        else
        {
            Picasso.with(mContext).load(lowResImage).into(viewHolder.mMediaImage);
        }

        String caption = modelMedia.getCaptionText();
        if (!TextUtils.isEmpty(caption))
        {
            viewHolder.mCaptionText.setVisibility(View.VISIBLE);
            viewHolder.mCaptionText.setText(caption);
        }
        else
        {
            viewHolder.mCaptionText.setVisibility(View.GONE);
        }

        viewHolder.mMediaId = modelMedia.getInstagramId();
        viewHolder.mOwnerId = Long.toString(modelMedia.getOwner().getInstagramId());

        return convertView;
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
