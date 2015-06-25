package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.model.ModelMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Jiajun Wang on 6/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class MediaAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<ModelMedia> mModelMedias = new ArrayList<>();

    public static class ViewHolder
    {
        public final RoundedImageView mOwnerProfileImage;
        public final TextView mOwnerUserName;
        public final ImageView mMediaImage;

        public ViewHolder(View view)
        {
            mOwnerProfileImage = (RoundedImageView) view.findViewById(R.id.owner_profile_image);
            mOwnerUserName = (TextView) view.findViewById(R.id.owner_username);
            mMediaImage = (ImageView) view.findViewById(R.id.media_image);
        }
    }

    public MediaAdapter(Context context)
    {
        mContext = context;
    }

    public void setModelMedias(ArrayList<ModelMedia> arrayList)
    {
        mModelMedias = arrayList;
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
        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_item_media, null);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) v.getTag();
        }

        Picasso.with(mContext).load(mModelMedias.get(position).getImageHighRes()).into(viewHolder.mMediaImage);
        Picasso.with(mContext).load(mModelMedias.get(position).getOwner().getProfilePicture()).into(viewHolder.mOwnerProfileImage);
        viewHolder.mOwnerUserName.setText(mModelMedias.get(position).getOwner().getUserName());
        return v;
    }
}
