package com.example.amanleenpuri.gogreen.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.amanleenpuri.gogreen.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import model.Event;

/**
 * Created by amrata on 4/22/16.
 */
public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static TextView eventTitle;
    private static TextView eventDetail;
    private static TextView hostedBy;
    private static TextView eventDate;
    private static TextView eventTime;
    private static TextView eventVenue;
    private GoogleMap googleMap;
    private LatLng ltln;
    private Event ev;
    private String hostName = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_layout);
        ev = new Event();
        eventTitle = (TextView) findViewById(R.id.tv_eventName);
        eventDetail = (TextView) findViewById(R.id.tv_eventDetail);
        hostedBy = (TextView) findViewById(R.id.tv_hostName);
        eventDate = (TextView) findViewById(R.id.tv_eventDate);
        eventTime = (TextView) findViewById(R.id.tv_eventTime);
        eventVenue = (TextView) findViewById(R.id.tv_Loc);

       // Bundle extras = getIntent().getExtras();
        Intent ei = getIntent();
        //if (extras != null) {
            ev = (Event) ei.getSerializableExtra("EventObj");
        hostName =  ei.getStringExtra("Host");
        //}

        //Event ev= getIntent().getParcelableExtra("EventObj");
        eventTitle.setText(ev.getEventTitle());
        eventDetail.setText(ev.getEventDescription());
        hostedBy.setText(hostName);
        eventDate.setText(ev.getEventDate());
        eventTime.setText(ev.getEventStartTime()+" - "+ev.getEventEndTime());
        eventVenue.setText(ev.getEventLocation());

        ltln = getLocationFromAddress(EventDetailsActivity.this, eventVenue.getText().toString());
        Log.v("%%%%%MY ADDR %%%%%%%", ev.toString());
        Log.v("%%%%%MY ADDR %%%%%%%", eventVenue.getText().toString());
        Log.v("%%%%%MY ADDR %%%%%%%", ltln.toString());

        //37.531063, -121.913610
        //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.location_map)).getMap();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.location_map_takeMeTo);
       mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onMapReady(GoogleMap gMap) {
        this.googleMap = gMap;
        googleMap.addMarker(new MarkerOptions()
                .position(ltln)
                .title(eventTitle.getText().toString()));
        googleMap.addMarker(new MarkerOptions().position(ltln));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ltln));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onStart() {
       super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "EventDetails Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.amanleenpuri.gogreen.ui/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "EventDetails Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.amanleenpuri.gogreen.ui/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//      client.disconnect();
    }
}

