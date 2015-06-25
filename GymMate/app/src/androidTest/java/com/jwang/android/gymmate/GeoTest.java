package com.jwang.android.gymmate;

import android.test.AndroidTestCase;

import com.jwang.android.gymmate.util.GeoLocation;

/**
 * @author Jiajun Wang on 6/25/15
 *         Copyright (c) 2015 StumbleUpon, Inc. All rights reserved.
 */
public class GeoTest extends AndroidTestCase
{
    public void testGeo()
    {
        GeoLocation here = GeoLocation.fromDegrees(37.549696, -122.314780);
        GeoLocation gym = GeoLocation.fromDegrees(37.7814460, -122.3921540);
        double dis = here.distanceTo(gym, 0);

        for (int i = 0; i < here.boundingCoordinates(3, 0).length; i++)
        {
            GeoLocation tem = here.boundingCoordinates(3, 0)[i];
            assertTrue("location is " + tem.toString(), false);
        }
    }
}
