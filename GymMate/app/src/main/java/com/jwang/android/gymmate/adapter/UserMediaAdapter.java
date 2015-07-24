package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.MediaDetailActivity;
import com.jwang.android.gymmate.data.MediaContract;
import com.squareup.picasso.Picasso;

/**
 * Created by jiajunwang on 7/12/15.
 */
public class UserMediaAdapter extends
        RecyclerView.Adapter<UserMediaAdapter.UserMediaViewHolder>
{
    private @NonNull Context mContext;
    private Cursor mCursor;

    public UserMediaAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    public UserMediaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewGroup instanceof RecyclerView)
        {
            int layoutId = R.layout.list_item_user_media_grid;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new UserMediaViewHolder(view);
        }
        else
        {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(UserMediaViewHolder holder, int position)
    {
        mCursor.moveToPosition(position);
        int image_index = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_STANDARD);
        if (TextUtils.isEmpty(mCursor.getString(image_index)))
        {
            Picasso.with(mContext).load(mCursor.getString(image_index)).into(holder.mImageView);
        }
        else
        {
            image_index = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_IMAGE_LOW);
            Picasso.with(mContext).load(mCursor.getString(image_index)).into(holder.mImageView);
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

    public void swapCursor(Cursor newCursor)
    {
        mCursor = newCursor;
        notifyDataSetChanged();
        //        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor()
    {
        return mCursor;
    }

    public class UserMediaViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        public ImageView mImageView;

        public UserMediaViewHolder(View v)
        {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.user_media);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.user_media:
                    int adapterPosition = getAdapterPosition();
                    mCursor.moveToPosition(adapterPosition);
                    int InstagtamIdColumnIndex = mCursor.getColumnIndex(MediaContract.MediaEntry.COLUMN_MEDIA_INSTAGRAM_ID);
                    MediaDetailActivity.startActivity(mContext, mImageView, mCursor.getString(InstagtamIdColumnIndex));
                    break;
            }
        }
    }
}
