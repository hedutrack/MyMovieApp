package com.hayseed.mymovieapp.utils;

import android.os.Environment;

import java.io.File;

/**
 *
 */
public class FileOps
{
    public static File getExternalStorageDirectory ()
    {
        return Environment.getExternalStorageDirectory ();
    }

    public static File getExternalStorageDirectory (String fileName)
    {
        File f = Environment.getExternalStorageDirectory ();
        f = new File (f + fileName);

        return f;
    }

    public static File getExternalStorageDirectory (String dirName, String fileName)
    {
        File f = Environment.getExternalStorageDirectory ();
        f = new File (f + dirName + "/" + fileName);

        return f;
    }

    public static File getFileReference (String dirName, String fileName)
    {
        File f = new File (dirName + "/" + fileName);
        return f;
    }
}
