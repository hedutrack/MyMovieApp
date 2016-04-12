package com.hayseed.mymovieapp;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class ReviewsFragment extends Fragment
{
    private static final String TAG = "ReviewsFragment";

    private ImageButton btnPref;
    private ImageButton btnNext;
    private TextView    location;
    private TextView    reviewContent;
    private TextView    textAuthor;
    private MovieDB   theMovieDetails;
    private JSONArray reviews;
    private int       currentReview;

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate (R.layout.fragment_reviews, container, false);

        container.requestDisallowInterceptTouchEvent (true);

        final int moviePos = this.getArguments ().getInt (Defines.MoviePos);
        final MovieBucket bucket = MovieBucket.getInstance (getActivity ());
        theMovieDetails = bucket.getMovie (moviePos);
        if (theMovieDetails == null)
        {
            return rootView;
        }

        reviews = theMovieDetails.getReviews ();
        if (reviews == null)
        {
            return rootView;
        }

        location = (TextView) rootView.findViewById (R.id.reviewsLocation);
        textAuthor = (TextView) rootView.findViewById (R.id.textAuthor);

        reviewContent = (TextView) rootView.findViewById (R.id.textReview);
        reviewContent.setMovementMethod (new ScrollingMovementMethod ());
        reviewContent.setFocusable (true);

        btnPref = (ImageButton) rootView.findViewById (R.id.btnPrev);
        btnPref.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View v)
            {
                Log.d (TAG, "Pref button");

                btnNext.setEnabled (true);
                currentReview--;

                if (currentReview == 0) btnPref.setEnabled (false);

                updatePosition (currentReview);
                updateAuthor (currentReview);
                updateContent (currentReview);
            }
        });

        btnNext = (ImageButton) rootView.findViewById (R.id.btnNext);
        btnNext.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View v)
            {
                Log.d (TAG, "Next button");

                currentReview++;
                if (currentReview+1 >= reviews.length ())
                {
                    btnNext.setEnabled (false);
                }
                else
                {
                    btnNext.setEnabled (true);
                    btnNext.setFocusable (true);
                }

                btnPref.setEnabled (true);

                updatePosition (currentReview);
                updateAuthor (currentReview);
                updateContent (currentReview);

            }
        });

        currentReview = 0;

        int reviewsCount = reviews.length ();
        if (reviewsCount > 1)
        {
            btnNext.setEnabled (true);
        }
        else
        {
            btnNext.setEnabled (false);
        }

        btnPref.setEnabled (false);

        updatePosition (currentReview);
        updateAuthor (currentReview);
        updateContent (currentReview);

        return rootView;
    }

    private void updateAuthor (int index)
    {
        try
        {
            JSONObject review = reviews.getJSONObject (index);
            String s = review.getString ("author");
            textAuthor.setText (s);
            return;
        }
        catch (JSONException e)
        {
            e.printStackTrace ();
            return;
        }
    }
    private void updatePosition (int index)
    {
        StringBuilder buffer = new StringBuilder ();
        buffer.append (Integer.toString (index+1));
        buffer.append (" of ");
        buffer.append (Integer.toString (reviews.length ()));

        location.setText (buffer.toString ());
    }

    private void updateContent (int index)
    {
        try
        {
            JSONObject review = reviews.getJSONObject (index);
            String s = review.getString ("content");
            reviewContent.setText (s);
            reviewContent.scrollTo (0, 0);
            return;
        }
        catch (JSONException e)
        {
            e.printStackTrace ();
            return;
        }
    }
}
