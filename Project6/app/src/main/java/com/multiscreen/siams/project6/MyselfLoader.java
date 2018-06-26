package com.multiscreen.siams.project6;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class MyselfLoader extends AsyncTaskLoader<List<Myself>> {

    private static final String LOG_TAG = StudentsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link StudentsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public MyselfLoader(Context context, String url) {
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
    public List<Myself> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Myself> earthquakes = PersonalActivity.fetchStudentData(mUrl);
        return earthquakes;
    }
}
