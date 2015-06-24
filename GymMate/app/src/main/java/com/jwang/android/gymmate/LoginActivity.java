package com.jwang.android.gymmate;

import android.content.Intent;
import android.os.Bundle;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramUser;

/**
 * Created by jiajunwang on 6/23/15.
 */
public class LoginActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //        if (!InstagramOauth.getsInstance().getSession().isActive())
        if (true)
        {
            //log in
            setContentView(R.layout.activity_login);
        }
        else
        {
            // Already logged in
        }
    }

    //    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener()
    //    {
    //        @Override
    //        public void onSuccess(InstagramUser user)
    //        {
    //            finish();
    //
    //            startActivity(new Intent(MainActivity.this, MainActivity.class));
    //        }
    //
    //        @Override
    //        public void onError(String error)
    //        {
    //            showToast(error);
    //        }
    //
    //        @Override
    //        public void onCancel()
    //        {
    //            showToast("OK. Maybe later?");
    //
    //        }
    //    };
}
