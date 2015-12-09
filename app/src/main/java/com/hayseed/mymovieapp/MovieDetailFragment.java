package com.hayseed.mymovieapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 *
 */
public class MovieDetailFragment extends Fragment
{
    private Integer selectedItem;
    private MovieDB theMovieDetails;

    public interface OnMovieDetailBackListener
    {
        public void OnDetailBack ();
    }

    public MovieDetailFragment ()
    {
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        //setHasOptionsMenu (true);

       // ((AppCompatActivity) getActivity ()).getSupportActionBar ().setDisplayOptions (android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
       // ((AppCompatActivity) getActivity ()).getSupportActionBar ().setTitle ("Movie Details");
       // ((AppCompatActivity) getActivity ()).getSupportActionBar ().setDisplayShowTitleEnabled (true);

    }

    @Nullable
    @Override
    /**
     *
     */
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate (R.layout.fragment_detail, container, false);

        Integer pos = getArguments ().getInt ("Position");

        TextView tvTextTitle       = (TextView) rootView.findViewById (R.id.textTitle);
        TextView tvTextRating      = (TextView) rootView.findViewById (R.id.textRating);
        TextView tvTextReleaseDate = (TextView) rootView.findViewById (R.id.textReleaseDate);
        TextView tvTextPlot        = (TextView) rootView.findViewById (R.id.textPlot);

        tvTextTitle.setText (theMovieDetails.getOriginalTitle ());
        tvTextRating.setText (theMovieDetails.getVoteAverage ());
        tvTextReleaseDate.setText (theMovieDetails.getReleaseDate ());
        tvTextPlot.setText (theMovieDetails.getOverview ());

        String posterPath = theMovieDetails.getPosterPath ();
        Uri.Builder uri = new Uri.Builder ();
        uri.scheme ("http").authority ("image.tmdb.org")
                .appendPath ("t")
                .appendPath ("p")
                .appendPath ("w185")
                .appendEncodedPath (posterPath);

        ImageView imageView = (ImageView) rootView.findViewById (R.id.imageView);
        Context   c = rootView.getContext ();
        Picasso.with (c).load (uri.toString ()).into (imageView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        inflater.inflate (R.menu.movie_detail_menu, menu);
    }

    /**
     * Handles back button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId () == android.R.id.home)
        {
            OnMovieDetailBackListener listener = (OnMovieDetailBackListener) getActivity ();
            listener.OnDetailBack ();
            return true;
        }

        return super.onOptionsItemSelected (item);
    }

    // TODO fix this poor implementation of data passing
    public void setMovieDB (MovieDB theMovie)
    {
        theMovieDetails = theMovie;
    }
}
