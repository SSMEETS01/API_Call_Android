package com.multiscreen.siams.project6;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClassmatesActivity extends AppCompatActivity implements LoaderCallbacks<List<Students>>{

    public static final String LOG_TAG = ClassmatesActivity.class.getName();
    private StudentsAdapter adapter;
    private static final String schoolapi =
            "http://mgroesink-001-site7.itempurl.com/api/Students/0283884/";
    private static final int School_Loader_ID = 1;
    private TextView mEmptyStateTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmates);

        //Listview voor studenten defineren.
        ListView lvStudents = (ListView)findViewById(R.id.lvClassmates);
        //EmptyTextView defineren.
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        //De Emptyview van de listview setten
        lvStudents.setEmptyView(mEmptyStateTextView);

        //new studentadapter
        adapter = new StudentsAdapter(this, new ArrayList<Students>());

        //Update listview with adapter
        lvStudents.setAdapter(adapter);


        // Check the network connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Getting details from current network connection
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // if there is a network connection
        if (networkInfo != null && networkInfo.isConnected()) {
            // Define new loader manager
            LoaderManager loaderManager = getLoaderManager();

            // initialize loader
            loaderManager.initLoader(School_Loader_ID,null, this);
        } else {
            // If there is no internet connection remove the load+ing indicator
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update text from emptyview with no internet connectie
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Students>> onCreateLoader(int i, Bundle bundle) {
        // New loader with url
        return new StudentsLoader(this, schoolapi);
    }

    @Override
    public void onLoadFinished(Loader<List<Students>> loader, List<Students> students) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // De tekst in de emptyTextview update met een error message dat er geen studen is gevonden
        mEmptyStateTextView.setText(R.string.no_Students);
        // If there is a valid list of {@link student}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (students != null && !students.isEmpty()) {
            adapter.addAll(students);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Students>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }
}
