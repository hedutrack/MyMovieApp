package com.hayseed.mymovieapp;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hayseed.mymovieapp.utils.URLConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GridFragment.OnImageSelectedListener
{
    private static ArrayList<MovieDB> movieList;
    private MovieBucket bucket;
    private ProgressDialog progress;
    private static String currentSortOrder = "";

    private ViewGroup gridViewLayout;
    private ViewGroup movieDetailLayout;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        bucket = MovieBucket.getInstance (this);

        if (savedInstanceState != null) return;

        gridViewLayout = (ViewGroup) findViewById (R.id.activity_main_gridview);
        if (gridViewLayout != null)
        {
            // The fragment (to display the posters)
            GridFragment gridFragment = new GridFragment ();
            //gridFragment.setAdapterData (new ArrayList<MovieDB> ());

            // Get the fragment manager
            FragmentManager     fragmentManager      = getFragmentManager ();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

            // Add the fragment.  The magic lies in associating the fragment to a container. In this
            // case, the container is defined in activity_main.xml.  There is a method for not
            // associating the fragment to a container, but I'm not sure of the meaning of that.
            fragmentTransaction.replace (gridViewLayout.getId (), gridFragment, GridFragment.class.getName ());

            //fragmentTransaction.addToBackStack (GridFragment.class.getName ());
            fragmentTransaction.commit ();

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences (this);
            String sortOrder = sharedPrefs.getString ("Sort", "popularity.desc");

            new DiscoverMovies ().execute (sortOrder);
        }

        movieDetailLayout = (ViewGroup) findViewById (R.id.activity_main_detail);
        if (movieDetailLayout != null)
        {
            MovieDetailFragment detailFragment = new MovieDetailFragment ();

            Bundle bundle = new Bundle ();
            bundle.putInt (Defines.MoviePos, 0);
            detailFragment.setArguments (bundle);

            // Get the fragment manager
            FragmentManager     fragmentManager      = getFragmentManager ();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

            fragmentTransaction.replace (movieDetailLayout.getId (), detailFragment, MovieDetailFragment.class.getName ());

            //fragmentTransaction.addToBackStack (MovieDetailFragment.class.getName ());
            fragmentTransaction.commit ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        if (id == R.id.action_settings)
        {
            startActivity (new Intent (this, SettingsActivity.class));
            return true;
        }
        
        return super.onOptionsItemSelected (item);
    }

    @Override
    protected void onPause ()
    {
        super.onPause ();

        bucket.saveFavs ();
    }

    @Override
    public void onBackPressed ()
    {
        FragmentManager fragmentManager = getFragmentManager ();
        int i = fragmentManager.getBackStackEntryCount ();

        if (fragmentManager.getBackStackEntryCount() > 1 )
        {
            fragmentManager.popBackStack();
        }
        else
        {
            super.onBackPressed ();
        }
    }

    /**
     * Callback from GridFragment, and represents the movie image selected.  This will
     * transition to a detail view of the movie.
     *
     * <p/>
     * See <a href="http://developer.android.com/guide/components/fragments.html">Android Fragments</a>
     *
     * @param pos
     */
    public void OnImageSelected (Integer pos)
    {
        MovieDetailFragment detailFragment;

        // Get the fragment manager
        FragmentManager     fragmentManager      = getFragmentManager ();
        detailFragment = (MovieDetailFragment) fragmentManager.findFragmentByTag (MovieDetailFragment.class.getName ());
        if (detailFragment == null)
        {
            detailFragment = new MovieDetailFragment ();

            Bundle bundle = new Bundle ();
            bundle.putInt (Defines.MoviePos, pos);
            detailFragment.setArguments (bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

            fragmentTransaction.replace (detailFragment.getId (), detailFragment);

            fragmentTransaction.addToBackStack (MovieDetailFragment.class.getName ());
            fragmentTransaction.commit ();
            return;
        }
    }

    @Override
    protected void onResume ()
    {
        super.onResume ();

        // This seems hokey.  The intent of this piece is to detect the resume generated
        // when finished with the YouTube activity.  If the code is omitted, another
        // copy of GridFragment is added to the backstack, on top of the copy already
        // present.
        //FragmentManager fragmentManager = getFragmentManager ();
        //if (fragmentManager.getBackStackEntryCount () > 1) return;

        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences (this);
        //String sortOrder = sharedPrefs.getString ("Sort", "popularity.desc");

        //new DiscoverMovies ().execute (sortOrder);
    }

    private class DiscoverMovies extends AsyncTask <String, Void, ArrayList<MovieDB>>
    {
        private final String TAG = "DiscoverMovies";

        @Override
        protected void onPreExecute ()
        {
            super.onPreExecute ();

            progress = new ProgressDialog (MainActivity.this);
            progress.setTitle ("Retrieving movies");
            progress.setMessage ("Wait");
            progress.show ();
        }

        @Override
        protected ArrayList<MovieDB> doInBackground (String... params)
        {
            // get the moviedb key
            String movieKey = null;

            String sortOrder = params [0];
            if (currentSortOrder.equals (sortOrder)) return movieList;

            currentSortOrder = sortOrder;

            if (currentSortOrder.equals (Defines.SortOrderFavs))
            {
                movieList = bucket.getFavs ();
                return movieList;
            }

            try
            {
                InputStream in = getAssets ().open ("parms.txt");
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
            uri.scheme ("http").authority ("api.themoviedb.org").appendPath ("3")
                    .appendPath ("discover")
                    .appendPath ("movie")
                    .appendQueryParameter ("sort_by", params[0])
                    .appendQueryParameter ("api_key", movieKey);

            Log.d (TAG, "uri=" + uri.toString ());

            // issue the request and retrieve the response
            URLConnection.sendRequest (uri.toString ());
            String response = URLConnection.getResponse ();
            URLConnection.closeConnection ();

            // response into a json object

            if (response == null) return null;

            try
            {
                JSONObject o = new JSONObject (response);
                JSONArray array = o.getJSONArray ("results");

                movieList = new ArrayList<> ();

                // TODO Building of MovieDB should be in the Bucket
                for (int i = 0; i < array.length (); i++)
                {
                    JSONObject detail = array.getJSONObject (i);
                    String id = detail.getString ("id");
                    String poster_path = detail.getString ("poster_path");

                    MovieDB movie = new MovieDB (id, poster_path);
                    movie.setOriginalTitle (detail.getString ("original_title"));
                    movie.setOverview (detail.getString ("overview"));
                    movie.setVoteAverage (detail.getString ("vote_average"));
                    movie.setReleaseDate (detail.getString ("release_date"));

                    movieList.add (movie);

                    Log.d (TAG, i + " " + id + " " + poster_path );
                }

                return movieList;
            }
            catch (JSONException e)
            {
                e.printStackTrace ();
            }

            return null;
        }

        @Override
        protected void onPostExecute (ArrayList<MovieDB> movieDBs)
        {
            super.onPostExecute (movieDBs);

            progress.dismiss ();

            if (movieDBs == null)
            {
                Toast.makeText (getApplicationContext (), "No movie posters", Toast.LENGTH_LONG).show ();
                return;
            }

            MovieBucket bucket = MovieBucket.getInstance (getApplicationContext ());
            bucket.setMovies (movieDBs);

            // Get the fragment manager
            FragmentManager     fragmentManager      = getFragmentManager ();
            GridFragment gridFragment = (GridFragment) fragmentManager.findFragmentByTag (GridFragment.class.getName ());
            if (gridFragment != null)
            {
                gridFragment.setAdapterData (movieDBs);
            }

        }
    }
}
