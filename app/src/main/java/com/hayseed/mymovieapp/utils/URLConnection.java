package com.hayseed.mymovieapp.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 */
public class URLConnection
{
    private static HttpURLConnection urlConnection;

    public static void sendRequest (String theURL)
    {
        try
        {
            urlConnection = null;

            URL url = new URL (theURL);

            urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.setRequestMethod ("GET");
            urlConnection.connect ();
            return;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace ();
            throw new RuntimeException ();
        }
        catch (ProtocolException e)
        {
            e.printStackTrace ();
            throw new RuntimeException ();
        }
        catch (IOException e)
        {
            e.printStackTrace ();
            throw new RuntimeException ();
        }
    }

    public static String getResponse ()
    {
        if (urlConnection == null) return null;

        InputStream inputStream = null;
        try
        {
            inputStream = urlConnection.getInputStream ();
            if (inputStream == null)
            {
                // Nothing to do.
                return null;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace ();
            return null;
        }

        StringBuffer buffer = new StringBuffer ();

        BufferedReader reader = new BufferedReader (new InputStreamReader (inputStream));

        String line;
        try
        {
            while ((line = reader.readLine ()) != null)
            {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append (line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }

        if (buffer.length () == 0)
        {
            // Stream was empty.  No point in parsing.
            return null;
        }

        return buffer.toString ();
    }

    public static void closeConnection ()
    {
        if (urlConnection != null)
        {
            urlConnection.disconnect ();
            urlConnection = null;
        }
    }
}
