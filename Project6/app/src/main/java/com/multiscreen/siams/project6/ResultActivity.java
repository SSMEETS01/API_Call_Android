package com.multiscreen.siams.project6;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ResultActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Grade>> {

    TextView newGrade;
    public static final String LOG_TAG = ResultActivity.class.getName();
    private GradeAdapter mAdapter;
    private static final String schoolapi =
            "http://mgroesink-001-site7.itempurl.com/api/Results/0283884/X93AXX2B7D";
    private static final int School_Loader_ID = 1;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        newGrade = (TextView) findViewById(R.id.newGrade);

        newGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputActivity.class);
                startActivity(intent);
            }
        });
        ListView lvGrades = (ListView)findViewById(R.id.lvGrades);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        lvGrades.setEmptyView(mEmptyStateTextView);

        mAdapter = new GradeAdapter(this, new ArrayList<Grade>());

        lvGrades.setAdapter(mAdapter);


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(School_Loader_ID,null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }
    @Override
    public Loader<List<Grade>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new GradeLoader(this, schoolapi);
    }

    @Override
    public void onLoadFinished(Loader<List<Grade>> loader, List<Grade> students) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No students found."
        mEmptyStateTextView.setText(R.string.no_Students);

        // If there is a valid list of {@link student}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (students != null && !students.isEmpty()) {
            mAdapter.addAll(students);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Grade>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public static List<Grade> fetchResultData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link studente}s
        List<Grade> Grades = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link studente}s
        return Grades;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the student JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Students} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Grade> extractFeatureFromJson(String studentJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(studentJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding studentes to
        List<Grade> Grades = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(studentJSON);

            // For each student in the studenteArray, create an {@link student} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                // Get a single studente at position i within the list of students
                JSONObject currentStudent = baseJsonResponse.getJSONObject(i);

                // Extract the value for the key called "place"
                String CourseCode = currentStudent.getString("CourseCode");

                // Extract the value for the key called "url;

                String[] datum = currentStudent.getString("ResultDate").split("T");

                String Score = currentStudent.getString("Result");

                // Create a new {@link studente} object with the magnitude, location, time,
                // and url from the JSON response.
                Grade Grade = new Grade(CourseCode, Score, datum[0]);

                // Add the new {@link studente} to the list of students.
                Grades.add(Grade);
            }

        }catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the student JSON results", e);
        }

        // Return the list of students
        return Grades;
    }
}
