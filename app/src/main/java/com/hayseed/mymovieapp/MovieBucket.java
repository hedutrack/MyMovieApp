package com.hayseed.mymovieapp;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A singleton
 */
public class MovieBucket
{
    private ArrayList<MovieDB> movieList;
    private ArrayList<MovieDB> favs;
    private File dirs;
    private Context c;

    private static MovieBucket bucket;

    public static MovieBucket getInstance (Context c)
    {
        if (bucket == null)
        {
            bucket = new MovieBucket (c);
        }

        return bucket;
    }

    private MovieBucket (Context c)
    {
        this.c = c;

        movieList = new ArrayList<> ();

        // deserialize if present
        dirs = c.getFilesDir ();
        File f = new File (dirs, Defines.FavFileName);
        if (f.exists ())
        {
            // load the ser
            ObjectInputStream in = null;
            try
            {
                in = new ObjectInputStream (new FileInputStream (f));
                favs = (ArrayList<MovieDB>) in.readObject ();
            }
            catch (Exception e)
            {
                e.printStackTrace ();
            }
        }
    }

    public void addToFavs (MovieDB movie)
    {
        if (favs == null) favs = new ArrayList<> ();

        favs.add (movie);
    }

    public void clear ()
    {
        movieList.clear ();
    }

    public ArrayList<MovieDB> getFavs ()
    {
        return favs;
    }

    public MovieDB getMovie (int movieIndex)
    {
        if (movieList == null) return null;

        if (movieIndex > movieList.size ()) return null;

        return movieList.get (movieIndex);
    }

    public boolean isFav (String movieId)
    {
        if (favs == null)
        {
            return false;
        }

        for (Iterator<MovieDB> it = favs.iterator (); it.hasNext();)
        {
            MovieDB theMovie = it.next ();
            if (theMovie.getId ().equals (movieId)) return true;
        }

        return false;
    }

    public void saveFavs ()
    {
        if (favs == null) return;

        File f = new File (dirs, Defines.FavFileName);
        try
        {
            f.createNewFile ();
            ObjectOutputStream out = new ObjectOutputStream (c.openFileOutput (Defines.FavFileName, Context.MODE_PRIVATE));
            out.writeObject (favs);
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    public void setMovies (ArrayList<MovieDB> movies)
    {
        movieList = movies;
    }

    // TODO Crappy implementation
    public void toggleFavs (MovieDB theMovie)
    {
        if (favs == null)
        {
            addToFavs (theMovie);
            return;
        }

        for (int i = 0; i < favs.size (); i++)
        {
            if (favs.get (i).getId ().equals (theMovie.getId ()))
            {
                favs.remove (i);
                return;
            }
        }

        addToFavs (theMovie);
    }
}
