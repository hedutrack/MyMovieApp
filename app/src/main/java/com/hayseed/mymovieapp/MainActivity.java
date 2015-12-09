package com.hayseed.mymovieapp;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.hayseed.mymovieapp.utils.URLConnection;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GridFragment.OnImageSelectedListener, MovieDetailFragment.OnMovieDetailBackListener
{
    private ArrayList<MovieDB> movieList;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        new DiscoverMovies ().execute ("popularity.desc");

        //Toolbar theToolBar = (Toolbar) findViewById (R.id.main_toolbar);
        //setSupportActionBar (theToolBar);

/*        // The fragment (to display the posters)
        GridFragment gridFragment = new GridFragment ();

        // Get the fragment manager
        FragmentManager     fragmentManger      = getFragmentManager ();
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction ();

        // Add the fragment.  The magic lies in associating the fragment to a container. In this
        // case, the container is defined in activity_main.xml.  There is a method for not
        // associating the fragment to a container, but I'm not sure of the meaning of that.
        fragmentTransaction.add (R.id.content_frame, gridFragment);
        //fragmentTransaction.addToBackStack (null);
        fragmentTransaction.commit ();*/
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

    /**
     * Callback from GridFragment, and represents the movie image selected.  This will
     * transition to a detail view of the movie
     *
     * <p/>
     * See <a href="http://developer.android.com/guide/components/fragments.html">Android Fragments</a>
     *
     * @param pos
     */
    public void OnImageSelected (Integer pos)
    {
        MovieDetailFragment detailFragment = new MovieDetailFragment ();

        detailFragment.setMovieDB (movieList.get (pos));

        Bundle b = new Bundle ();
        b.putInt ("Position", pos);

        detailFragment.setArguments (b);

        //Toast.makeText (this, String.valueOf (pos), Toast.LENGTH_LONG).show ();
        FragmentManager fragmentManager = getFragmentManager ();

        FragmentTransaction transaction = fragmentManager.beginTransaction ();
        transaction.replace (R.id.content_frame, detailFragment);
        //transaction.add (detailFragment, "detail");
        //transaction.add (R.id.content_frame, detailFragment);

        transaction.addToBackStack (null);
        transaction.commit ();


    }

    /**
     * Callback from MovieDetailFragment.  Used to signal back button.
     */
    public void OnDetailBack ()
    {
        FragmentManager fragmentManager = getFragmentManager ();
        fragmentManager.popBackStack ();
        //getSupportActionBar ().setDisplayShowTitleEnabled (true);
        //getSupportActionBar ().setTitle ("MyMovieApp");
        //getSupportActionBar ().setDisplayOptions (0);
    }

    private class DiscoverMovies extends AsyncTask <String, Void, ArrayList<MovieDB>>
    {
        private final String TAG = "DiscoverMovies";

        @Override
        protected ArrayList<MovieDB> doInBackground (String... params)
        {
            // get the moviedb key
            String movieKey = null;

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

            // The fragment (to display the posters)
            GridFragment gridFragment = new GridFragment ();
            gridFragment.setAdapterData (movieDBs);

            // Get the fragment manager
            FragmentManager     fragmentManger      = getFragmentManager ();
            FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction ();

            // Add the fragment.  The magic lies in associating the fragment to a container. In this
            // case, the container is defined in activity_main.xml.  There is a method for not
            // associating the fragment to a container, but I'm not sure of the meaning of that.
            fragmentTransaction.add (R.id.content_frame, gridFragment);

            fragmentTransaction.addToBackStack (null);
            fragmentTransaction.commit ();

        }
    }
}
