package com.hayseed.mymovieapp;

import java.util.ArrayList;

/**
 * A singleton
 */
public class MovieBucket
{
    private ArrayList<MovieDB> movieList;

    private static MovieBucket bucket;

    public static MovieBucket getInstance ()
    {
        if (bucket == null)
        {
            bucket = new MovieBucket ();
        }

        return bucket;
    }

    private MovieBucket ()
    {
        movieList = new ArrayList<> ();
    }

    public void clear ()
    {
        movieList.clear ();
    }

    public MovieDB getMovie (int movieIndex)
    {
        if (movieList == null) return null;

        if (movieIndex > movieList.size ()) return null;

        return movieList.get (movieIndex);
    }

    public void setMovies (ArrayList<MovieDB> movies)
    {
        movieList = movies;
    }
}
