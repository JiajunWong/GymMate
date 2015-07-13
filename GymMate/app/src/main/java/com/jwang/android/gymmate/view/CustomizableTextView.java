package com.jwang.android.gymmate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jwang.android.gymmate.R;


/**
 * @author Vinh Huynh on 12/01/14
 *         Copyright (c) 2014 StumbleUpon, Inc. All rights reserved.
 */
public class CustomizableTextView extends TextView
{

    public CustomizableTextView(final Context context)
    {
        this(context, null, 0);
    }

    public CustomizableTextView(final Context context, final AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomizableTextView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);

        if (attrs != null)
        {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomizableTextView);
            final String customFont = a.getString(R.styleable.CustomizableTextView_customFont);
            a.recycle();
            if (!isInEditMode())
            {
                if (customFont != null)
                {
                    final Typeface typeFace = TypefaceFactory.getTypeFace(context, customFont);
                    setTypeface(typeFace);
                }
            }
        }
    }

}
