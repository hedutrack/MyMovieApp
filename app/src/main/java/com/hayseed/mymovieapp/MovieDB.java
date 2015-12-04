package com.hayseed.mymovieapp;

/**
 *
 */
public class MovieDB
{
    private String id;
    private String posterPath;

    public MovieDB (String movieId, String posterPath)
    {
        id = movieId;
        this.posterPath = posterPath;
    }

    public String getId ()
    {
        return id;
    }

    public String getPosterPath ()
    {
        return posterPath;
    }
}
