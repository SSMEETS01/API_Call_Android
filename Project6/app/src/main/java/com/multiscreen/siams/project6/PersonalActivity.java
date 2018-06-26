package com.multiscreen.siams.project6;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Myself>> {

    private static final String LOG_TAG = PersonalActivity.class.getSimpleName();

    private static final String USGS_REQUEST_URL =
            "http://mgroesink-001-site7.itempurl.com/api/Students/0283884/X93AXX2B7D";
    private static final int student_LOADER_ID = 1;

    TextView tvGeslacht, tvVoornaam, tvAchternaam, tvAdres, tvPostcode, tvKlas,
            tvStudentnr, tvCrebo, tvOpleiding,tvGeboortedatum, tvGeboorteplaats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        tvVoornaam = (TextView)findViewById(R.id.tvVoornaam);
        tvAchternaam = (TextView)findViewById(R.id.tvAchternaam);
        tvGeslacht = (TextView)findViewById(R.id.tvGender);
        tvAdres = (TextView)findViewById(R.id.tvAddress);
        tvPostcode = (TextView)findViewById(R.id.tvPostalCode);
        tvStudentnr = (TextView)findViewById(R.id.tvStudentNr);
        tvCrebo = (TextView)findViewById(R.id.tvCrevo);
        tvKlas = (TextView)findViewById(R.id.tvKlas);
        tvOpleiding = (TextView)findViewById(R.id.tvOpleiding);
        tvGeboortedatum = (TextView)findViewById(R.id.tvBirthDate);
        tvGeboorteplaats = (TextView)findViewById(R.id.tvBirthPlace);

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
            loaderManager.initLoader(student_LOADER_ID,null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<Myself>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new MyselfLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Myself>> loader, List<Myself> students) {



        tvVoornaam.setText(students.get(0).Voornaam);
        tvAchternaam.setText(students.get(0).Achternaam);
        tvGeslacht.setText(students.get(0).geslacht);
        tvAdres.setText(students.get(0).Adres);
        tvPostcode.setText(students.get(0).Postcode);
        tvKlas.setText(students.get(0).Klas);
        tvStudentnr.setText(students.get(0).Studentnr);
        tvCrebo.setText(students.get(0).Crebo);
        tvOpleiding.setText(students.get(0).Opleiding);
        tvGeboortedatum.setText(students.get(0).Geboorttedatum);
        tvGeboorteplaats.setText(students.get(0).GeboortePlaats);
    }

    @Override
    public void onLoaderReset(Loader<List<Myself>> loader) {
        // Loader reset, so we can clear out our existing data.
        //mAdapter.clear();
    }

    public static List<Myself> fetchStudentData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link student}s
        List<Myself> students = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link student}s
        return students;
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
    private static List<Myself> extractFeatureFromJson(String myselfJSON) {

        ArrayList<Myself> myself = new ArrayList<>();

        try {

            //Create an JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject reader = new JSONObject(myselfJSON);

            String voornaam = reader.getString("FirstName");
            String achternaam = reader.getString("LastName");
            String geslacht = reader.getString("Gender");
            String adres = reader.getString("Address");
            String Postcode = reader.getString("PostalCode");
            String Studentnr = reader.getString("StudentNr");
            String Woonplaats = reader.getString("Residence");
            String Crebo = reader.getString("Crebo");
            String Opleiding = reader.getString("Education");
            String Klas = reader.getString("Class");

            String[] datum = reader.getString("BirthDate").split("T");

            String date = datum[0];

            String plaats = reader.getString("BirthPlace");

            Myself objMyself = new Myself(voornaam, achternaam, adres, Postcode, Woonplaats, date, plaats, Klas, Crebo, Opleiding, Studentnr, geslacht );

            myself.add(objMyself);


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problemen met het uitvoeren van JSON query", e);
        }

        // Return the list of students
        return myself;
    }
}
