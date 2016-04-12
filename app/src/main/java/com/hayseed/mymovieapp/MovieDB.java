package com.hayseed.mymovieapp;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class MovieDB implements Serializable
{
    private String id;
    private String posterPath;
    private String originalTitle;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private transient JSONArray reviewArray;
    private transient JSONArray trailerArray;

    public MovieDB (String movieId, String posterPath)
    {
        id = movieId;
        this.posterPath = posterPath;
    }

    public String getId ()
    {
        return id;
    }

    public String getOriginalTitle ()
    {
        return originalTitle;
    }

    public String getOverview ()
    {
        return overview;
    }

    public String getPosterPath ()
    {
        return posterPath;
    }

    public String getReleaseDate ()
    {
        return releaseDate;
    }

    public JSONArray getReviews ()
    {
        return reviewArray;
    }

    public JSONArray getTrailers ()
    {
        return trailerArray;
    }

    public String getVoteAverage ()
    {
        return voteAverage;
    }

    public int reviewCount ()
    {
        if (reviewArray == null) return 0;

        return reviewArray.length ();
    }

    public void setReviews (JSONArray reviews)
    {
        reviewArray = reviews;
    }

    public void setTrailers (JSONArray trailers)
    {
        trailerArray = trailers;
    }

    public void setReleaseDate (String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage (String voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    public void setOriginalTitle (String originalTitle)
    {
        this.originalTitle = originalTitle;
    }

    public void setOverview (String overview)
    {
        this.overview = overview;
    }

    public int trailerCount ()
    {
        if (trailerArray == null) return 0;

        return trailerArray.length ();
    }
}
