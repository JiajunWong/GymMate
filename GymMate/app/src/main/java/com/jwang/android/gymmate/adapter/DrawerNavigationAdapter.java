package com.jwang.android.gymmate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.LoginActivity;
import com.jwang.android.gymmate.activity.MediaListActivity;
import com.jwang.android.gymmate.util.AppConfig;
import com.jwang.android.gymmate.util.InstagramOauth;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class DrawerNavigationAdapter extends
        ArrayAdapter<DrawerNavigationAdapter.GlobalMenuItem>
{
    private static final int TYPE_MENU_ITEM = 0;
    private static final int TYPE_DIVIDER = 1;

    private Context mContext;
    private final LayoutInflater inflater;
    private final List<GlobalMenuItem> menuItems = new ArrayList<>();

    public DrawerNavigationAdapter(Context context)
    {
        super(context, 0);
        mContext = context;
        this.inflater = LayoutInflater.from(context);
        setupMenuItems();
    }

    @Override
    public int getCount()
    {
        return menuItems.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public GlobalMenuItem getItem(int position)
    {
        return menuItems.get(position);
    }

    @Override
    public int getItemViewType(int position)
    {
        return menuItems.get(position).mIsDivider ? TYPE_DIVIDER : TYPE_MENU_ITEM;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (getItemViewType(position) == TYPE_MENU_ITEM)
        {
            MenuItemViewHolder holder;
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.list_item_drawer, parent, false);
                holder = new MenuItemViewHolder(convertView);
                convertView.setTag(holder);
            }
            else
            {
                holder = (MenuItemViewHolder) convertView.getTag();
            }

            GlobalMenuItem item = getItem(position);
            holder.mLabelTextView.setText(item.mLabel);
            holder.mIconImageView.setImageResource(item.mIconId);
            holder.mIconImageView.setVisibility(item.mIconId == 0 ? View.GONE : View.VISIBLE);
            holder.mRootView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onItemClicked(position, v);
                }
            });

            return convertView;
        }
        else
        {
            return inflater.inflate(R.layout.list_item_menu_divider, parent, false);
        }
    }

    private void onItemClicked(int position, View v)
    {
        String accessToken = InstagramOauth.getsInstance(mContext).getSession().getAccessToken();
        switch (position)
        {
            case 0:
                String userMediaUrl = AppConfig.INSTAGRAM_USER_MEDIA_ENDPOINT + accessToken;
                MediaListActivity.startActivity(mContext, userMediaUrl);
                break;
            case 1:
                String popularUrl = AppConfig.INSTAGRAM_POPULAR_ENDPOINT + accessToken;
                MediaListActivity.startActivity(mContext, popularUrl);
                break;
            case 2:
                String likedUrl = AppConfig.INSTAGRAM_LIKED_ENDPOINT + accessToken;
                MediaListActivity.startActivity(mContext, likedUrl);
                break;
            case 4:
                break;
            case 5:
                //log out
                showLogOutDialog();
                break;
        }
    }

    private void showLogOutDialog()
    {
        new AlertDialogWrapper.Builder(mContext).setTitle(R.string.log_out).setMessage(R.string.log_out_content).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                InstagramOauth.getsInstance(mContext).resetOauth(mContext);
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }
        }).show();
    }

    public static class MenuItemViewHolder
    {
        ImageView mIconImageView;
        TextView mLabelTextView;
        View mRootView;

        public MenuItemViewHolder(View view)
        {
            mIconImageView = (ImageView) view.findViewById(R.id.iv_icon);
            mLabelTextView = (TextView) view.findViewById(R.id.tv_label);
            mRootView = view.findViewById(R.id.root_view);
        }
    }

    private void setupMenuItems()
    {
        menuItems.add(new GlobalMenuItem(R.drawable.ic_global_menu_feed, mContext.getString(R.string.my_recent)));
        menuItems.add(new GlobalMenuItem(R.drawable.ic_global_menu_popular, mContext.getString(R.string.popular)));
        menuItems.add(new GlobalMenuItem(R.drawable.ic_global_menu_likes, mContext.getString(R.string.likes)));
        menuItems.add(GlobalMenuItem.dividerMenuItem());
        menuItems.add(new GlobalMenuItem(0, mContext.getString(R.string.action_settings)));
        menuItems.add(new GlobalMenuItem(0, mContext.getString(R.string.log_out)));
        notifyDataSetChanged();
    }

    public static class GlobalMenuItem
    {
        public int mIconId;
        public String mLabel;
        public boolean mIsDivider;

        private GlobalMenuItem()
        {
        }

        public GlobalMenuItem(int mIconId, String label)
        {
            this.mIconId = mIconId;
            this.mLabel = label;
            this.mIsDivider = false;
        }

        public static GlobalMenuItem dividerMenuItem()
        {
            GlobalMenuItem globalMenuItem = new GlobalMenuItem();
            globalMenuItem.mIsDivider = true;
            return globalMenuItem;
        }
    }
}
