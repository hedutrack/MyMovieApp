package com.hayseed.mymovieapp.utils;

import android.test.AndroidTestCase;

import java.io.File;
/**
 */
public class TestFileOps extends AndroidTestCase
{
    public void testWithDir () throws Throwable
    {
        File f = FileOps.getExternalStorageDirectory ();

        f = FileOps.getExternalStorageDirectory ("/Playful");
        boolean b = f.exists ();

        assertTrue ("Not found", f.exists ());
    }
}
