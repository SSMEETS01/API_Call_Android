package com.multiscreen.siams.project6;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class GradeLoader extends AsyncTaskLoader<List<Grade>> {
    private static final String LOG_TAG = GradeLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link GradeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public GradeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Grade> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Grade> Grades = ResultActivity.fetchResultData(mUrl);
        return Grades;
    }
}
