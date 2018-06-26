package com.multiscreen.siams.project6;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InputActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText StudentNrInput,TitleInput, GradeInput, ResultDateInput;
    private TextView mEmptyStateTextView;
    Spinner CourseCodeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        StudentNrInput = (EditText)findViewById(R.id.StudentNrInput);
        CourseCodeInput = (Spinner)findViewById(R.id.CourseCodeInput);
        ResultDateInput = (EditText) findViewById(R.id.ResultDateInput);
        GradeInput = (EditText)findViewById(R.id.GradeInput);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        String[] item = new String[]{"DBD","ENG","FTE","LB","MOA","NED","PRG","REK"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item);
        //set the spinners adapter to the previously created one.
        CourseCodeInput.setAdapter(adapter);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    String Studentnr = "" + StudentNrInput.getText();
                    String CourseCode = "" + CourseCodeInput.getSelectedItem();
                    String ResultDate = "" + ResultDateInput.getText();
                    String Grade = "" + GradeInput.getText();

                    SendDataGrade send = new SendDataGrade(Studentnr, CourseCode, Grade, ResultDate);
                    send.execute();

                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(intent);
                } else {
                    int i = 0;
                    // Update empty state with no connection error message
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });
    }

    private class SendDataGrade extends AsyncTask<Void, Void, Void> {
        String URL = "";
        private String Studentnumber,  ID, Name, Grade, Date;

        public SendDataGrade(String studentNumebr, String id, String grade, String date) {
            ID = id;
            Grade = grade;
            Date = date;
            Studentnumber = studentNumebr;
        }

        // Gets the data from the json file via a background worker
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                URL = "http://mgroesink-001-site7.itempurl.com/api/Results/0283884/";
                String LOG_TAG = ResultActivity.class.getName();
                //Log.e(LOG_TAG, "doInBackground: URL " + URL);
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("StudentNr", Studentnumber);
                jsonBody.put("CourseCode", ID);
                jsonBody.put("ResultDate", Date);
                jsonBody.put("Score", Grade);

                Log.e(LOG_TAG, "jsonbody " + jsonBody);
                final String mRequestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY on response", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {

                            responseString = String.valueOf(response.statusCode);

                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
