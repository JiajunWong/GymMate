package com.jwang.android.gymmate.model;

/**
 * Created by jiajunwang on 6/25/15.
 */
public class ModelFaceBookLocation extends ModelBase
{
    private String mName;
    private String mId;

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        this.mName = name;
    }

    public String getId()
    {
        return mId;
    }

    public void setId(String id)
    {
        this.mId = id;
    }
}
