package com.jwang.android.gymmate.view;

import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.jwang.android.gymmate.GymMateApp;

public class TypefaceFactory
{
    private static final String TAG = TypefaceFactory.class.getSimpleName();

    private ConcurrentHashMap<String, Typeface> mCache = new ConcurrentHashMap<String, Typeface>();

    public enum TypefaceType
    {
        ROBOTO("Roboto-Regular.ttf"),
        ROBOTO_LIGHT("Roboto-Light.ttf"),
        ROBOTO_BOLD("Roboto-Bold.ttf"),
        BILLABONG("billabong.ttf");

        private String mFontName;

        TypefaceType(final String name)
        {
            mFontName = name;
        }

        public String getFontName()
        {
            return mFontName;
        }
    }

    private static class LazyHolder
    {
        public static final TypefaceFactory INSTANCE = new TypefaceFactory();
    }

    public static Typeface getTypeface(final TypefaceType type)
    {
        return LazyHolder.INSTANCE.getInternal(GymMateApp.getInstance().getContext(), type.getFontName());
    }

    public static Typeface getTypeFace(final Context context, final String fontName)
    {
        return LazyHolder.INSTANCE.getInternal(context, fontName);
    }

    private Typeface getInternal(final Context context, final String fontName)
    {
        Log.w(TAG, "getInternal: " + fontName);

        Typeface typeface = null;
        if (mCache != null)
        {
            typeface = mCache.get(fontName);

            if (typeface == null)
            {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
                if (typeface != null)
                {
                    mCache.put(fontName, typeface);
                }
                else
                {
                    Log.w(TAG, "*** typeface is NULL!");
                }
            }
        }
        else
        {
            Log.w(TAG, "*** mCache is NULL!");
        }

        return typeface;
    }

}
