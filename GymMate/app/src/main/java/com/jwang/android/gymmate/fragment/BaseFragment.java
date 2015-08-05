package com.jwang.android.gymmate.fragment;

import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jwang.android.gymmate.R;

/**
 * @author Jiajun Wang on 6/30/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public abstract class BaseFragment extends Fragment
{
    private MaterialDialog mMaterialDialog;

    public void showLoadingDialog()
    {
        if (getActivity() != null)
        {
            mMaterialDialog = new MaterialDialog.Builder(getActivity()).content(R.string.please_wait).progress(true, 0).cancelable(false).show();
        }
    }

    public void dismissLoadingDialog()
    {
        if (getActivity() != null && mMaterialDialog != null)
        {
            mMaterialDialog.dismiss();
        }
    }
}
