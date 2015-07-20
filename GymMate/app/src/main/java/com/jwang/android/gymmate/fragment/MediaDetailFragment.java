package com.jwang.android.gymmate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.model.ModelMedia;
import com.squareup.picasso.Picasso;

/**
 * Created by jiajunwang on 7/18/15.
 */
public class MediaDetailFragment extends BaseFragment
{
    private ImageView mMediaImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_media_detail, container, false);
        mMediaImageView = (ImageView) view.findViewById(R.id.media_image);
        return view;
    }

    public void setModelMedia(ModelMedia modelMedia)
    {
        if (!TextUtils.isEmpty(modelMedia.getImageHighRes()))
        {
            Picasso.with(getActivity()).load(modelMedia.getImageHighRes()).into(mMediaImageView);
        }
        else if (!TextUtils.isEmpty(modelMedia.getImageLowRes()))
        {
            Picasso.with(getActivity()).load(modelMedia.getImageLowRes()).into(mMediaImageView);
        }
    }
}
