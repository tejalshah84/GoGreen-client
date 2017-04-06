package com.example.amanleenpuri.gogreen.ui;

import android.app.Activity;

/**
 * Created by amrata on 4/26/16.
 */

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;



import com.example.amanleenpuri.gogreen.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import util.ValidateText;

public class EditProfile_old extends AppCompatActivity implements View.OnClickListener {

    private static final String SERVICE_URL = "http://192.168.0.6:8080/GoGreen_Server/rest/user";
    ImageView proPic;
    EditText firstNameET;
    EditText lastNameET;
    Spinner roleTypeSP;
    Spinner interestAreaSP;
    EditText cityET;
    EditText stateET;
    Button registerB;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_editprofile);

        Resources res = getResources();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(res.getString(R.string.EditProfileLabel));
        setSupportActionBar(toolbar);

        proPic = (ImageView) findViewById(R.id.profilePic);
        firstNameET = (EditText) findViewById(R.id.first_name);
        lastNameET = (EditText) findViewById(R.id.last_name);
        roleTypeSP = (Spinner) findViewById(R.id.role_spinner);
        interestAreaSP = (Spinner) findViewById(R.id.interest_spinner);
        cityET = (EditText) findViewById(R.id.city_name);
        stateET = (EditText) findViewById(R.id.state_name);


        String[] roleTypeSPArr = new String[]{"Plant Lover", "Horticulturist", "Environmentalist"};
        ArrayAdapter<String> adapterSP1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roleTypeSPArr);
        roleTypeSP.setAdapter(adapterSP1);

        String[] interestAreaSPArr = new String[]{"Indoor", "Outdoor"};
        ArrayAdapter<String> adapterSP2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, interestAreaSPArr);
        interestAreaSP.setAdapter(adapterSP2);

        //registerB = (Button) findViewById(R.id.bn_post);
        //registerB.setOnClickListener(this);

    }



    public void registerUser(View view) throws JSONException, IOException {
        new Thread(new Runnable() {
            public void run() {

                try{
                    String fName = firstNameET.getText().toString();
                    String lName = lastNameET.getText().toString();
                    Drawable profilePic = proPic.getDrawable();
                    String role = roleTypeSP.getSelectedItem().toString();
                    String interest = interestAreaSP.getSelectedItem().toString();
                    String city = cityET.getText().toString();
                    String state = stateET.getText().toString();
                    JSONObject jsonObject = new JSONObject();

                    URL url = new URL("http://172.29.92.223:8080/GoGreenServer/test");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                    // Put Http parameter labels with value of Name Edit View control
                    jsonObject.put("firstName", fName);
                    jsonObject.put("lastName", lName);
                    //jsonObject.put("proPicture", profilePic);
                    jsonObject.put("roleType", role);
                    if(interest.equals("Indoor")){
                        jsonObject.put("userInterest", 1);
                    }else{
                        jsonObject.put("userInterest", 2);
                    }

                    jsonObject.put("city",city);
                    jsonObject.put("state",state);

                    Log.d("JSONinputString", jsonObject.toString());
                    //invokeWS(jsonObject);

//                            String inputString = inputValue.getText().toString();
                    String inputString = "{\"name\":\"Komal\", \"age\":26}";

                    //inputString = URLEncoder.encode(inputString, "UTF-8");


                    connection.setDoOutput(true);
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(jsonObject.toString());
                    out.close();
                    //connection.disconnect();

                    // connection.setDoInput(true);
                    int responseCode = connection.getResponseCode();
                    Log.d("Response Code = ",String.valueOf(responseCode));
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String returnString="";
                    String studentObj = "";

                    Log.d("BEFOREWHILE", inputString);

                    while ((returnString = in.readLine()) != null)
                    {
//                      doubledValue= Integer.parseInt(returnString);
                        Log.d("ReturnString", returnString);
                        studentObj = returnString;
                    }
                    in.close();


//                            final Integer finalDoubledValue = doubledValue;
                    final String stu = studentObj;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i("%%%%%%%SER", stu);

                            lastNameET.setText(stu);

                        }
                    });

                }catch(Exception e)
                {
                    Log.d("Exception",e.toString());
                }

            }
        }).start();
    }
    private void invokeWS(JSONObject jsonObject) throws UnsupportedEncodingException {
        StringEntity entity = new StringEntity(jsonObject.toString());
        //ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        AsyncHttpClient client = new AsyncHttpClient();

        Log.i("SER", "http://172.29.92.223.6:8080/GoGreenServer/rest/user/new" + entity);
        Log.i("SER", "http://172.29.92.223:8080/GoGreenServer/rest/user/new" + jsonObject);

        client.post(getApplicationContext(), "http://172.29.92.223:8080/GoGreenServer/rest/user/new", entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                try {
                    Log.i("SER", "HERE!");
                    String login = obj.getString("fName");
                    int ID = obj.getInt("id");

                    //user.setUserId(obj.getInt("userid"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "404 - Nie odnaleziono serwera!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "500 - Coś poszło nie tak po stronie serwera!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getApplicationContext(), "Podano niepoprawne dane!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*public void invokeWS(RequestParams params){

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.0.6:8080/GoGreen_Server/rest/user/sample",params ,new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v("%%%%%%%statusCode",String.valueOf(statusCode));
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject();
                    Log.v("%%%%%%%getBoolean", obj.toString());

                    // When the JSON response has status boolean value assigned with true
                    //if(obj.getBoolean("status")){
                    if(statusCode == 200){
                        // Set Default Values for Edit View controls
                        //setDefaultValues();

                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else{
                        //errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("%%%%%%%statusCode",String.valueOf(statusCode));
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

    @Override
    public void onClick(View v) {
   /*     Log.d("%%%%%%%%%%", "1st Step");
        switch (v.getId()){
            case R.id.bn_post:
                Log.d("%%%%%%%%%%", "2nd Step");

                new Thread(new Runnable() {
                    public void run() {

                        try{

                            URL url = new URL("http://192.168.0.6:8080/GoGreenServer/test");
                            URLConnection connection = url.openConnection();
                            Log.d("%%%%%%%%%%", connection.toString());
                            String fName = firstNameET.getText().toString();
                            String lName = lastNameET.getText().toString();
                            //Drawable profilePic = proPic.getDrawable();
                            String role = roleTypeSP.getSelectedItem().toString();
                            String interest = interestAreaSP.getSelectedItem().toString();
                            String city = cityET.getText().toString();
                            String state = stateET.getText().toString();

                            StringBuilder inputString = new StringBuilder();
                            inputString.append(fName).append("-").append(lName).append("-").append(role).append("-");
                            inputString.append(interest).append("-").append(city).append("-").append(state).append("-");
                            //inputString.append(profilePic.toString());


                            //String inputString = inputValue.getText().toString();
                            //inputString = URLEncoder.encode(inputString, "UTF-8");

                            Log.d("%%%%%%%%%%inputString", inputString.toString());

                            connection.setDoOutput(true);
                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                            out.write(inputString.toString());
                            out.close();

                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            String returnString="";
                            Integer doubledValue =0;
                            doubledValue =0;

                            while ((returnString = in.readLine()) != null)
                            {
                                doubledValue= Integer.parseInt(returnString);
                            }
                            in.close();


                            //runOnUiThread(new Runnable() {
                              //  public void run() {
                                  //  inputValue.setText(doubledValue.toString());
                               // }
                            //});

                        }catch(Exception e)
                        {
                            Log.d("Exception",e.toString());
                        }

                    }
                }).start();

                break;
        }*/
    }



}