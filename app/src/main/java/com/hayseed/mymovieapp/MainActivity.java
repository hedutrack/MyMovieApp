package com.hayseed.mymovieapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
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
    private static final String TAG = MainActivity.class.getName ();

    private boolean portMode;
    private static ArrayList<MovieDB> movieList;
    private        MovieBucket        bucket;
    private        ProgressDialog     progress;
    private static String currentSortOrder = "";
    private int moviePos;

    private ViewGroup gridViewLayout;
    private ViewGroup movieDetailLayout;

    private GridFragment        gridFragment;
    private MovieDetailFragment detailFragment;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        Log.d (TAG, "onCreate");

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        bucket = MovieBucket.getInstance (this);

        FragmentManager fragmentManager = getFragmentManager ();
        if (fragmentManager.getBackStackEntryCount () > 0)
        {
            fragmentManager.popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        moviePos = 0;
        if (savedInstanceState != null)
        {
            System.out.println (MainActivity.class.getSimpleName () + " savedInstanceState");
            moviePos = savedInstanceState.getInt (Defines.MoviePos);
        }

        gridViewLayout = (ViewGroup) findViewById (R.id.activity_main_gridview);
        if (gridViewLayout != null)
        {
            portMode = true;

            if (gridFragment == null) gridFragment = new GridFragment ();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

            // Add the fragment.  The magic lies in associating the fragment to a container. In this
            // case, the container is defined in activity_main.xml.  There is a method for not
            // associating the fragment to a container, but I'm not sure of the meaning of that.
            fragmentTransaction.replace (gridViewLayout.getId (), gridFragment, GridFragment.class.getName ());
            fragmentTransaction.commit ();

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences (this);
            String sortOrder = sharedPrefs.getString ("Sort", "popularity.desc");

            new DiscoverMovies ().execute (sortOrder);
        }

        movieDetailLayout = (ViewGroup) findViewById (R.id.activity_main_detail);
        if (movieDetailLayout != null)
        {
            portMode = false;
            if (detailFragment == null) detailFragment = new MovieDetailFragment ();

            Bundle bundle = new Bundle ();
            bundle.putInt (Defines.MoviePos, moviePos);
            detailFragment.setArguments (bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

            fragmentTransaction.replace (movieDetailLayout.getId (), detailFragment, MovieDetailFragment.class.getName ());
            fragmentTransaction.commit ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        Log.d (TAG, "onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        Log.d (TAG, "onOptionsItemSelected");

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
        Log.d (TAG, "onPause");

        super.onPause ();

        bucket.saveFavs ();
    }

    @Override
    public void onBackPressed ()
    {
        Log.d (TAG, "onBackPressed");

        FragmentManager fragmentManager = getFragmentManager ();
        int i = fragmentManager.getBackStackEntryCount ();

        // Test for an empty backstack
        if (i == 0)
        {
            super.onBackPressed ();
            return;
        }

        FragmentManager.BackStackEntry f = fragmentManager.getBackStackEntryAt (i - 1);

        if (f.getName ().equals (MovieDetailFragment.class.getName ()) )
        {
            fragmentManager.popBackStack();
            return;
        }

        if (f.getName ().equals (ReviewsFragment.class.getName ()))
        {
            fragmentManager.popBackStack ();
            return;
        }

        super.onBackPressed ();

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
        Log.d (TAG, "onImageSelected");

        moviePos =  pos;

        if (portMode)
        {
            FragmentManager     fragmentManager      = getFragmentManager ();

            Bundle bundle = new Bundle ();
            bundle.putInt (Defines.MoviePos, pos);

            detailFragment = new MovieDetailFragment ();
            detailFragment.setArguments (bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

            fragmentTransaction.replace (R.id.activity_main_gridview, detailFragment, MovieDetailFragment.class.getName ());

            fragmentTransaction.addToBackStack (MovieDetailFragment.class.getName ());
            fragmentTransaction.commit ();
            return;
        }

        detailFragment.updateView (pos);
    }

    @Override
    protected void onResume ()
    {
        Log.d (TAG, "onResume");

        super.onResume ();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        Log.d (TAG, "onSaveInstanceState");

        super.onSaveInstanceState (outState);

        outState.putInt (Defines.MoviePos, moviePos);
    }

    @Override
    public void onSaveInstanceState (Bundle outState, PersistableBundle outPersistentState)
    {
        Log.d (TAG, "pnSaveInstanceState");

        super.onSaveInstanceState (outState, outPersistentState);

        outState.putInt (Defines.MoviePos, moviePos);
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

            MovieDetailFragment detailFragment = (MovieDetailFragment) fragmentManager.findFragmentByTag (MovieDetailFragment.class.getName ());
            if (detailFragment != null)
            {
                if (movieDBs.size () > 0) detailFragment.updateView (moviePos);
            }

        }
    }
}
