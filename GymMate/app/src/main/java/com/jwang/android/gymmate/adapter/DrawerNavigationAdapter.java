package com.jwang.android.gymmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwang.android.gymmate.R;
import com.jwang.android.gymmate.activity.UserDetailActivity;
import com.jwang.android.gymmate.util.InstagramOauth;

import net.londatiga.android.instagram.InstagramUser;

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
        switch (position)
        {
            case 0:
                InstagramUser user = InstagramOauth.getsInstance(mContext).getSession().getUser();
                UserDetailActivity.startActivity(mContext, user.id);
                break;
            case 1:
                break;
            case 2:
                break;
            case 4:
                break;
            case 5:
                break;
        }
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
        menuItems.add(new GlobalMenuItem(0, mContext.getString(R.string.about)));
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
