package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.model.ModelMedia;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jiajunwang on 7/10/15.
 */
public class UserMediaAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<ModelMedia> mModelMedias = new ArrayList<>();

    public UserMediaAdapter(Context context, ArrayList<ModelMedia> arrayList)
    {
        mContext = context;
        mModelMedias = arrayList;
    }

    @Override
    public int getCount()
    {
        return mModelMedias.size();
    }

    @Override
    public Object getItem(int i)
    {
        return mModelMedias.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder viewHolder;
        if (view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_media_grid, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        Picasso.with(mContext).load(mModelMedias.get(i).getImageHighRes()).into(viewHolder.mImageView);
        return view;
    }

    public class ViewHolder
    {
        public ImageView mImageView;

        public ViewHolder(View v)
        {
            mImageView = (ImageView) v.findViewById(R.id.user_media);
        }
    }
}
