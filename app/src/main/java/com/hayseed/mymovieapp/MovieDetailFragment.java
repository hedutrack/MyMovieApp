package com.hayseed.mymovieapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hayseed.mymovieapp.utils.URLConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

    private boolean init;

    private int moviePos;
    private MovieBucket bucket;
    private MovieDB theMovieDetails;

    private Button btnFavs;
    private Button  btnReviews;
    private Context c;
    private ImageView imageView;
    private TextView tvTextTitle;
    private TextView tvTextRating;
    private TextView tvTextReleaseDate;
    private TextView tvTextPlot;

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        bucket = MovieBucket.getInstance (getActivity ());
    }

    @Override
    /**
     *
     */
    public View onCreateView (LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate (R.layout.fragment_detail, container, false);

        c = rootView.getContext ();

        tvTextTitle       = (TextView) rootView.findViewById (R.id.textTitle);
        tvTextRating      = (TextView) rootView.findViewById (R.id.textRating);
        tvTextReleaseDate = (TextView) rootView.findViewById (R.id.textReleaseDate);
        tvTextPlot        = (TextView) rootView.findViewById (R.id.textPlot);
        imageView = (ImageView) rootView.findViewById (R.id.imageView);

        /*
        This snippet exists to refresh the fragment when it comes back from the
        ReviewsFragment.  The getArguments would result in setting moviePos to 0.
        Should be using savedInstanceState
         */
        if (!init) moviePos = this.getArguments ().getInt (Defines.MoviePos);
        init = true;

        btnReviews = (Button) rootView.findViewById (R.id.btnReviews);
        btnReviews.setEnabled (false);

        btnReviews.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View v)
            {
                ReviewsFragment reviewsFragment = new ReviewsFragment ();
                Bundle bundle = new Bundle ();
                bundle.putInt (Defines.MoviePos, moviePos);
                reviewsFragment.setArguments (bundle);

                // Get the fragment manager
                FragmentManager fragmentManager = getFragmentManager ();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

                fragmentTransaction.replace (container.getId (), reviewsFragment);

                fragmentTransaction.addToBackStack (ReviewsFragment.class.getName ());
                fragmentTransaction.commit ();
            }
        });

        btnFavs = (Button) rootView.findViewById (R.id.btnFavs);
        btnFavs.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View v)
            {
                bucket.toggleFavs (theMovieDetails);
                String s = (String) btnFavs.getText ();
                if (s.equals (Defines.SetFav)) s = Defines.UnsetFav;
                else s = Defines.SetFav;

                btnFavs.setText (s);
            }
        });

        theMovieDetails = bucket.getMovie (moviePos);
        if (theMovieDetails == null) return rootView;


        if (bucket.isFav (theMovieDetails.getId ()))
        {
            btnFavs.setText (Defines.UnsetFav);
        }
        else
        {
            btnFavs.setText (Defines.SetFav);
        }

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
        Context c = rootView.getContext ();
        Picasso.with (c).load (uri.toString ()).into (imageView);

        new MovieArtifacts ().execute (theMovieDetails.getId (), Videos);
        new MovieArtifacts ().execute (theMovieDetails.getId (), Reviews);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        inflater.inflate (R.menu.movie_detail_menu, menu);
    }

    @Override
    public void onDestroy ()
    {
        super.onDestroy ();
    }

    @Override
    public void onResume ()
    {
        super.onResume ();

        if (theMovieDetails == null) return;

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

        Picasso.with (c).load (uri.toString ()).into (imageView);
    }

    public void updateView (int moviePos)
    {
        this.moviePos = moviePos;

        theMovieDetails = MovieBucket.getInstance (getActivity ()).getMovie (moviePos);
        if (theMovieDetails == null) return;

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

        Picasso.with (c).load (uri.toString ()).into (imageView);

        if (bucket.isFav (theMovieDetails.getId ()))
        {
            btnFavs.setText (Defines.UnsetFav);
        }
        else
        {
            btnFavs.setText (Defines.SetFav);
        }

        new MovieArtifacts ().execute (theMovieDetails.getId (), Videos);
        new MovieArtifacts ().execute (theMovieDetails.getId (), Reviews);

        FragmentManager fragmentManager = getFragmentManager ();

        int i = fragmentManager.getBackStackEntryCount ();

        // Test for an empty backstack
        if (i == 0)
        {
            return;
        }

        FragmentManager.BackStackEntry f = fragmentManager.getBackStackEntryAt (i - 1);

        if (f.getName ().equals (ReviewsFragment.class.getName ()) )
        {
            fragmentManager.popBackStack();
        }
    }

    private class MovieArtifacts extends AsyncTask<String, Void, JSONArray>
    {
        private final String TAG = "MovieArtifacts";

        private String artifact;

        @Override
        protected JSONArray doInBackground (String... params)
        {
            artifact = params [1];

            if (artifact.equals (Reviews))
            {
                if (theMovieDetails.reviewCount () > 0) return theMovieDetails.getReviews ();
            }

            if (artifact.equals (Videos))
            {
                if (theMovieDetails.trailerCount () > 0) return theMovieDetails.getTrailers ();
            }

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

            try
            {
                JSONObject o = new JSONObject (response);
                JSONArray array = o.getJSONArray ("results");
                return array;
            }
            catch (JSONException e)
            {
                e.printStackTrace ();
            }

            return null;
        }

        @Override
        protected void onPostExecute (JSONArray response)
        {
            super.onPostExecute (response);

            if (response == null) return;

            if (artifact.equals (Reviews))
            {
                processReviewJSON (response);
                return;
            }

            if (artifact.equals (Videos))
            {
                processTrailerJSON (response);
                return;
            }
        }

        private void processReviewJSON (JSONArray array)
        {
            Log.d (TAG, "Review response " + array);

            theMovieDetails.setReviews (array);

            int count = array.length ();
            String s = Integer.toString (count) + (count == 1 ? " review" : " reviews");
            btnReviews.setText (s);

            if (count > 0) btnReviews.setEnabled (true);
            else btnReviews.setEnabled (false);
        }

        private void processTrailerJSON (JSONArray array)
        {
            Log.d (TAG, "Trailer response " + array);

            theMovieDetails.setTrailers (array);

            View.OnClickListener listener = new View.OnClickListener ()
            {
                @Override
                public void onClick (View v)
                {
                    String tag =  (String) v.getTag ();

                    Uri.Builder uri = new Uri.Builder ();
                    uri.scheme ("http").authority ("youtube.com")
                            .appendPath ("watch")
                            .appendQueryParameter ("v", tag);

                    startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse (uri.toString ())));
                }
            };

            // Iterate through trailer response and dynamically build buttons
            try
            {
                RelativeLayout ll = (RelativeLayout) getView ().findViewById (R.id.fragment_detail);
                LinearLayout trailerLayout = (LinearLayout) getView ().findViewById (R.id.trailerLayout);

                if (trailerLayout != null)
                {
                    trailerLayout.removeAllViewsInLayout ();
                }

                for (int i = 0; i < array.length (); i++)
                {
                    JSONObject o = array.getJSONObject (i);
                    String youTubeKey = o.getString ("key");
                    String buttonText = o.getString ("name");

                    Button button = new Button (getActivity ());
                    button.setId (i + 1);
                    button.setTag (youTubeKey);
                    button.setText (buttonText);
                    button.setEnabled (true);
                    button.setOnClickListener (listener);


                    trailerLayout.addView (button);
                }
            }
            catch (Exception e)
            {
                return;
            }
        }
    }
}
