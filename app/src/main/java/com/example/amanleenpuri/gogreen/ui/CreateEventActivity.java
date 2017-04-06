package com.example.amanleenpuri.gogreen.ui;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.NotificationCompat;

import android.text.TextUtils;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.ProxyUser;
import model.Event;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.remote.GreenRESTInterface;

/**
 * Created by amrata on 4/4/16.
 */
public class CreateEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText eventTitle;
    private EditText eventDescription;
    public static FragmentManager fragmentManager;
    private static TextView datetv;
    private int userId;
    private static Date datepicked;
    private static Date today;

    public static int count = 0;
    private String userName;

    private static TextView startTimetv;
    private static TextView endTimetv;
    private EditText enterLocation;
    public static boolean mMapIsTouched = false;
    private GoogleMap googleMap;
    Spinner interestAreaSP;
    private LatLng ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_layout);
        ProxyUser pUser = ProxyUser.getInstance();
        userName = pUser.getUsername(getApplicationContext());
        userId = pUser.getUserId(getApplicationContext());

        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        fragmentManager = getSupportFragmentManager();
        datetv = (TextView) findViewById(R.id.eventDateTextView);
        startTimetv = (TextView) findViewById(R.id.eventStartTimeTextView);
        endTimetv = (TextView) findViewById(R.id.eventEndTimeTextView);

        eventTitle=(EditText) findViewById(R.id.eventNameEditText);
        eventTitle.setTag("Event Name");

        eventDescription = (EditText) findViewById(R.id.eventDetailEditText);
        eventDescription.setTag("Event DEscription");

        enterLocation = (EditText) findViewById(R.id.enterLocEditText);
        enterLocation.setTag("Event Location");

        interestAreaSP = (Spinner) findViewById(R.id.interest_spinner);

        Calendar cToday = Calendar.getInstance();
        cToday.set(Calendar.HOUR_OF_DAY, 0);
        cToday.set(Calendar.MINUTE, 0);
        cToday.set(Calendar.SECOND, 0);
        cToday.set(Calendar.MILLISECOND, 0);

        today = cToday.getTime();



        ImageButton eventDatebtn = (ImageButton)findViewById(R.id.iv_eventDate);
        eventDatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        ImageButton eventStartTime = (ImageButton)findViewById(R.id.iv_eventStartTime);
        eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePickerDialog(v);
            }
        });

        ImageButton eventEndTime = (ImageButton)findViewById(R.id.iv_eventEndTime);
        eventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePickerDialog(v);
            }
        });

        Resources res = getResources();
        String[] interestAreaArr = res.getStringArray(R.array.interestArea_array);
        ArrayAdapter<String> adapterInterestArea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, interestAreaArr);
        interestAreaSP.setAdapter(adapterInterestArea);



        Button publishButton = (Button)findViewById(R.id.publishEventButton);
        publishButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Event event = new Event();

                EditText invalidEditText =  checkIfEntered(eventTitle, eventDescription, enterLocation);


                if(invalidEditText != null){
                    showToast(invalidEditText.getTag() + " field cannot be empty.");
                }else if(!(startTimetv.getText().toString()).isEmpty() && !(endTimetv.getText().toString()).isEmpty()) {
                    //createEvent=false;
                }

               /* if(!(startTimetv.getText().toString()).isEmpty() && !(endTimetv.getText().toString()).isEmpty()) {
                    System.out.println("-------Start="+startTimetv.getText().toString());
                    System.out.println("-------end="+endTimetv.getText().toString());
                    String startTime[] = (startTimetv.getText().toString()).split(":");
                    String endTime[] = (endTimetv.getText().toString()).split(":");

                    if (Integer.parseInt(startTime[0]) > Integer.parseInt(endTime[0])){
                        showToast("End time should be after start time!");

                    }else if(Integer.parseInt(startTime[0]) == Integer.parseInt(endTime[0])){
                        if(Integer.parseInt(startTime[1]) > Integer.parseInt(endTime[1])){
                            showToast("End time should be after start time!");

                        }
                    }*/
                    //showToast("Please pick start and end time!");
                    //createEvent=false;
                //}else {
                    event.setEventDate(datetv.getText().toString());
                    event.setEventStartTime(startTimetv.getText().toString());
                    event.setEventEndTime(endTimetv.getText().toString());

                    event.setEventTitle(eventTitle.getText().toString());
                    event.setEventDescription(eventDescription.getText().toString());
                    event.setEventLocation(enterLocation.getText().toString());
                    event.setEventHostedById(userId);
                        String interest = interestAreaSP.getSelectedItem().toString();
                        if (interest.equals("Indoor")) {
                            event.setInterestAreaId(1);
                        } else {
                            event.setInterestAreaId(2);
                        }


////createEvent
                    GreenRESTInterface greenRESTInterface = ((GoGreenApplication) getApplication()).getGoGreenApiService();
                    Call<Event> createEventCall = greenRESTInterface.createEvent(event);
                    Log.v("EVENT%%%%%%%%%%%%%%", event.toString());
                    createEventCall.enqueue(new Callback<Event>() {
                        @Override
                        public void onResponse(Call<Event> call, Response<Event> response) {
                            if (response.isSuccessful()) {
                                Event res = response.body();

                                //eventId = eventResponse.getInt("eventId");
                                insertNotification(res.getEventId());
                                Toast.makeText(CreateEventActivity.this,
                                        "Event will be published to all users of GoGreen", Toast.LENGTH_SHORT).show();

                                //Intent i = new Intent(CreateEventActivity.this, TimelineActivity.class);
                                //startActivity(i);
                            } else {
                                Log.e("CREATE_EVENT", "Error in response " + response.errorBody());
                                Toast toast = Toast.makeText(getApplicationContext(), "ERROR in response Event", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Event> call, Throwable t) {
                            Log.e("Event Creation", "Failure to create an event", t);

                            Toast toast = Toast.makeText(getApplicationContext(), "on failure", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

               // }
                }

        });
    }

    public void showToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    EditText checkIfEntered(EditText... allInputFields) {
        for (EditText editText : allInputFields) {
            if (TextUtils.isEmpty(editText.getText())) {
                return editText;
            }
        }
        return null;
    }


    private void insertNotification(int eId) {

        final int eventId = eId;
        Log.v("*********","INSIDE INSERT FUNCTION");
        model.Notification n = new model.Notification();
        n.setEventId(eId);
        n.setGreenEntryId(0);
        n.setNotificationMessage("Event "+ eventTitle.getText().toString()+" created by "+userName);

        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
        Call<model.Notification> createNotifictionCall = greenRESTInterface.createNotification(n);
        createNotifictionCall.enqueue(new Callback<model.Notification>() {
            @Override
            public void onResponse(Call<model.Notification> call, Response<model.Notification> response) {
                if (response.isSuccessful()) {
                    model.Notification responseAnswer= response.body();
                    Log.v("%%%%%NOTIFICATION",responseAnswer.toString());

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
                    r.play();

                    // intent triggered, you can add other intent for other actions
                    Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    // this is it, we'll build the notification!
                    // in the addAction method, if you don't want any icon, just set the first param to 0
                    NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_cast_light, "Previous", pIntent).build();
                    android.app.Notification mNotification = new android.app.Notification.Builder(getApplicationContext())

                            .setContentTitle("New Post!")
                            .setContentText("There is an event for you on GoGreen!")
                            .setSmallIcon(R.drawable.ic_cast_light)
                            .setContentIntent(pIntent)
                            .setSound(soundUri)
                            //.addAction(action)
                            .build();


                    mNotification.defaults |= android.app.Notification.DEFAULT_VIBRATE;
                    //use the above default or set custom valuse as below
                    long[] vibrate = {0, 200, 100, 200};
                    mNotification.vibrate = vibrate;

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    // If you want to hide the notification after it was selected, do the code below
                    // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(0, mNotification);
                    count++;


                    Intent i = new Intent(CreateEventActivity.this, TimelineActivity.class);
                    i.putExtra("BADGE_COUNT",count);
                    startActivity(i);
                } else {
                    Log.e("PostNotification", "Error in response " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<model.Notification> call, Throwable t) {
                Log.e("PostNotification", "Failure to post Notification", t);
            }

        });

//        new Thread(new Runnable() {
//            public void run() {
//                Log.v("*********","INSIDE RUN");
//
//                try{
//
//
//                    String eTitle = eventTitle.getText().toString();
//
//
//                    JSONObject jsonObject = new JSONObject();
//
//                    URL url = new URL("http://192.168.0.6:8080/GoGreenServer/NotificationServlet");
//                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//
//
//                    // Put Http parameter labels with value of Name Edit View control
//                    jsonObject.put("notificationMessage", "Event "+ eTitle+" created by "+userName);
//                    jsonObject.put("eventId", eventId);
//                    jsonObject.put("greenEntryId", 0);
//
//
//                    Log.d("JSONinputString", jsonObject.toString());
//
//                    connection.setDoOutput(true);
//                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//                    out.write(jsonObject.toString());
//                    out.close();
//
//                    //connection.disconnect();
//
//                    // connection.setDoInput(true);
//                    int responseCode = connection.getResponseCode();
//                    Log.d("Response Code = ",String.valueOf(responseCode));
//                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                    String returnString="";
//
//                    while ((returnString = in.readLine()) != null)
//                    {
////                      doubledValue= Integer.parseInt(returnString);
//                        Log.d("ReturnString", returnString);
//
//                    }
//                    in.close();
//
//                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
//                    r.play();
//
//                    // intent triggered, you can add other intent for other actions
//                    Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
//                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//
//                    // this is it, we'll build the notification!
//                    // in the addAction method, if you don't want any icon, just set the first param to 0
//                    NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_cast_light, "Previous", pIntent).build();
//                    Notification mNotification = new Notification.Builder(getApplicationContext())
//
//                            .setContentTitle("New Post!")
//                            .setContentText("There is an event for you on GoGreen!")
//                            .setSmallIcon(R.drawable.ic_cast_light)
//                            .setContentIntent(pIntent)
//                            .setSound(soundUri)
//                            //.addAction(action)
//                            .build();
//
//
//                    mNotification.defaults |= Notification.DEFAULT_VIBRATE;
//                    //use the above default or set custom valuse as below
//                    long[] vibrate = {0, 200, 100, 200};
//                    mNotification.vibrate = vibrate;
//
//                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//                    // If you want to hide the notification after it was selected, do the code below
//                    // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//                    notificationManager.notify(0, mNotification);
//                    count++;
//                    new TimelineActivity().updateNotificationsBadge(count);
//
//                   /* final String responseObj = returnString;
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Log.i("%%%%%%%NotificationID", responseObj);
//
//                        }
//                    });*/
//
//                }catch(Exception e)
//                {
//                    Log.d("Exception",e.toString());
//                }
//
//            }
//        }).start();
    }

    public void showLocation(View v){
        if(!enterLocation.getText().equals("")){
            ll = getLocationFromAddress(this,enterLocation.getText().toString());
            Log.v("%%%%%MY ADDR %%%%%%%",ll.toString());

            //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.location_map)).getMap();

            SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                    .findFragmentById(R.id.location_map);
            mapFragment.getMapAsync(this);

            //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.location_map);
            //mapFragment.getMapAsync((OnMapReadyCallback) getApplicationContext());
        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = new StartTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = new EndTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }



    @Override
    public void onMapReady(GoogleMap gMap) {
        this.googleMap=gMap;
        googleMap.addMarker(new MarkerOptions()
                .position(ll)
                .title(eventTitle.getText().toString()));
        googleMap.addMarker(new MarkerOptions().position(ll));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker


            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //String eventDate = String.valueOf(month)
              //      + "/" + String.valueOf(day)+"/"+String.valueOf(year);
            Calendar cPicked = Calendar.getInstance();
            cPicked.set(Calendar.YEAR, year);
            cPicked.set(Calendar.MONTH, month);
            cPicked.set(Calendar.DAY_OF_MONTH, day);
            datepicked = cPicked.getTime();



            datetv.setText(  new StringBuilder()
                    .append(month + 1).append("/").append(day).append("/")
                    .append(year));
            datetv.setVisibility(View.VISIBLE);
        }
    }


    public static class StartTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String eventTime = String.valueOf(hourOfDay)
                    + ":" + String.valueOf(minute);


            startTimetv.setText(eventTime);
            startTimetv.setVisibility(View.VISIBLE);
        }


    }

    public static class EndTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String eventTime = String.valueOf(hourOfDay)
                    + ":" + String.valueOf(minute);

            endTimetv.setText(eventTime);
            endTimetv.setVisibility(View.VISIBLE);
        }


    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }



}
