package com.jwang.android.gymmate.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * @author Jiajun Wang on 7/14/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class AnimationUtil
{
    private static final long ANIM_DURATION = 200;

    public static void activityRevealTransition(Activity context, View targetView, View bgView)
    {
        setupEnterAnimations(context, targetView, bgView);
        setupExitAnimations(context, targetView, bgView);
    }

    private static void setupEnterAnimations(Activity context, final View targetView, final View bgView)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Transition enterTransition = context.getWindow().getSharedElementEnterTransition();
            enterTransition.addListener(new Transition.TransitionListener()
            {
                @Override
                public void onTransitionStart(Transition transition)
                {
                }

                @Override
                public void onTransitionEnd(Transition transition)
                {
                    animateRevealShow(targetView, bgView);
                }

                @Override
                public void onTransitionCancel(Transition transition)
                {
                }

                @Override
                public void onTransitionPause(Transition transition)
                {
                }

                @Override
                public void onTransitionResume(Transition transition)
                {
                }
            });
        }
    }

    private static void setupExitAnimations(Activity context, final View targetView, final View bgView)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Transition sharedElementReturnTransition = context.getWindow().getSharedElementReturnTransition();
            sharedElementReturnTransition.setStartDelay(ANIM_DURATION);

            Transition returnTransition = context.getWindow().getReturnTransition();
            returnTransition.setDuration(ANIM_DURATION);
            returnTransition.addListener(new Transition.TransitionListener()
            {
                @Override
                public void onTransitionStart(Transition transition)
                {
                    animateRevealHide(targetView, bgView);
                }

                @Override
                public void onTransitionEnd(Transition transition)
                {
                }

                @Override
                public void onTransitionCancel(Transition transition)
                {
                }

                @Override
                public void onTransitionPause(Transition transition)
                {
                }

                @Override
                public void onTransitionResume(Transition transition)
                {
                }
            });
        }
    }

    private static void animateRevealShow(View targetView, View bgView)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int cx = (targetView.getLeft() + targetView.getRight()) / 2;
            int cy = (targetView.getTop() + targetView.getBottom()) / 2;
            int finalRadius = Math.max(bgView.getWidth(), bgView.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(bgView, cx, cy, 0, finalRadius);
            bgView.setVisibility(View.VISIBLE);
            anim.setDuration(ANIM_DURATION);
            anim.start();
        }
    }

    private static void animateRevealHide(View targetView, final View bgView)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int cx = (targetView.getLeft() + targetView.getRight()) / 2;
            int cy = (targetView.getTop() + targetView.getBottom()) / 2;
            int initialRadius = bgView.getWidth();

            Animator anim = ViewAnimationUtils.createCircularReveal(bgView, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    bgView.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(ANIM_DURATION);
            anim.start();
        }
    }
}
