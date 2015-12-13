package com.hayseed.mymovieapp;

/**
 *
 */
public class MovieDB
{
    private String id;
    private String posterPath;
    private String originalTitle;
    private String overview;
    private String voteAverage;
    private String releaseDate;

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

    public String getVoteAverage ()
    {
        return voteAverage;
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
}
