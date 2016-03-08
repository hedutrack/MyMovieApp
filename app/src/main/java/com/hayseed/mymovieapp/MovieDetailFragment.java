package com.hayseed.mymovieapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hayseed.mymovieapp.utils.URLConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class MovieDetailFragment extends Fragment
{
    private static final String Reviews = "reviews";
    private static final String Videos = "videos";

    private Button  btnTrailer;
    private Integer selectedItem;
    private int moviePos;
    private MovieDB theMovieDetails;
    private String youTubeKey;

    public interface OnMovieDetailBackListener
    {
        public void OnDetailBack ();
    }

    public MovieDetailFragment ()
    {
    }

    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach (activity);
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
    }

    @Override
    /**
     *
     */
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate (R.layout.fragment_detail, container, false);



        TextView tvTextTitle       = (TextView) rootView.findViewById (R.id.textTitle);
        TextView tvTextRating      = (TextView) rootView.findViewById (R.id.textRating);
        TextView tvTextReleaseDate = (TextView) rootView.findViewById (R.id.textReleaseDate);
        TextView tvTextPlot        = (TextView) rootView.findViewById (R.id.textPlot);

        btnTrailer          = (Button)   rootView.findViewById (R.id.btnTrailer);
        btnTrailer.setEnabled (false);

        int moviePos = getArguments ().getInt ("moviePos");
        MovieBucket bucket = MovieBucket.getInstance ();
        MovieDB movieDB = bucket.getMovie (moviePos);

        tvTextTitle.setText (movieDB.getOriginalTitle ());
        tvTextRating.setText (movieDB.getVoteAverage ());
        tvTextReleaseDate.setText (movieDB.getReleaseDate ());
        tvTextPlot.setText (movieDB.getOverview ());

        String posterPath = movieDB.getPosterPath ();

        Uri.Builder uri = new Uri.Builder ();
        uri.scheme ("http").authority ("image.tmdb.org")
                .appendPath ("t")
                .appendPath ("p")
                .appendPath ("w185")
                .appendEncodedPath (posterPath);

        ImageView imageView = (ImageView) rootView.findViewById (R.id.imageView);
        Context c = rootView.getContext ();
        Picasso.with (c).load (uri.toString ()).into (imageView);

        new MovieArtifacts ().execute (movieDB.getId (), Videos);
        new MovieArtifacts ().execute (movieDB.getId (), Reviews);

        btnTrailer.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View v)
            {
                Uri.Builder uri = new Uri.Builder ();
                uri.scheme ("http").authority ("youtube.com")
                        .appendPath ("watch")
                        .appendQueryParameter ("v", youTubeKey);

                startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse (uri.toString ())));
            }
        });
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

    private class MovieArtifacts extends AsyncTask<String, Void, String>
    {
        private final String TAG = "MovieArtifacts";

        private String artifact;

        @Override
        protected String doInBackground (String... params)
        {
            artifact = params [1];

            String movieKey = null;

            try
            {
                InputStream in = getActivity ().getAssets ().open ("parms.txt");
                Properties p = new Properties ();
                p.load (in);
                movieKey = p.getProperty ("themoviedb");
            }
            catch (IOException e)
            {
                e.printStackTrace ();
                return null;
            }
            // build the uri

            Uri.Builder uri = new Uri.Builder ();
            uri.scheme ("http").authority ("api.themoviedb.org")
                    .appendPath ("3")
                    .appendPath ("movie")
                    .appendPath (params[0])
                    .appendPath (artifact)
                    .appendQueryParameter ("api_key", movieKey);

            // issue the request and retrieve the response
            URLConnection.sendRequest (uri.toString ());
            String response = URLConnection.getResponse ();
            URLConnection.closeConnection ();

            if (response == null)
            {
                return null;
            }

            return response;
        }

        @Override
        protected void onPostExecute (String response)
        {
            super.onPostExecute (response);

            if (response == null) return;

            if (artifact.equals (Reviews))
            {
                Log.d (TAG, "Review response " + response);
                return;
            }

            JSONObject o = null;
            JSONArray array = null;

            try
            {
                o = new JSONObject (response);
                array = o.getJSONArray ("results");

                // TODO handle more than one trailer

                o = array.getJSONObject (0);
                youTubeKey = o.getString ("key");
            }
            catch (JSONException e)
            {
                return;
            }

            if (artifact.equals (Reviews))
            {
                processReviewJSON (array);
                return;
            }

            if (artifact.equals (Videos))
            {
                Log.d (TAG, "Trailer response " + response);
                processTrailerJSON (array);
                return;
            }
        }

        private void processReviewJSON (JSONArray array)
        {

        }

        private void processTrailerJSON (JSONArray array)
        {
            try
            {
                JSONObject o = array.getJSONObject (0);
                youTubeKey = o.getString ("key");
                btnTrailer.setEnabled (true);
            }
            catch (Exception e)
            {
                return;
            }
        }
    }
}
