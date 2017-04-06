package com.example.amanleenpuri.gogreen.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.GreenEntry;
import model.Notification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.remote.GreenRESTInterface;


/**
 * Created by Tejal Shah on 4/5/2016.
 */
public class NotificationActivity extends AppCompatActivity {

    //ImageView cancel;
    ArrayList<model.Notification> noteData;
    ListView noteList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        //cancel= (ImageView) findViewById(R.id.notification_cancel);
        noteList=(ListView)findViewById(R.id.notification_list);

        if(savedInstanceState==null){
            Log.v("!!!!!!!!!!!!!!!!","I AM HERE");
            Bundle arguments = new Bundle();
            Bundle extras = getIntent().getExtras();
            if(extras!=null) {

                noteData = (ArrayList<Notification>) extras.getSerializable("NOTIFS");
                Log.v("!!!!!!!!!!!!!!!!",noteData.toString());
            }


        }
        /*try {
            noteData = setNoteList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //Log.v("@@@@@@@@@@@",noteData.toString());

        noteList.setAdapter(new NoteListViewAdapter(this,noteData));
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NotificationActivity.this, TimelineActivity.class);
//                startActivity(intent);
//            }
//        });
    }

//    private ArrayList setNoteList() throws InterruptedException {
//        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
//        Call<List<Notification>> getNs = greenRESTInterface.getAllNotifications();
//        getNs.enqueue(new Callback<List<Notification>>() {
//
//            List<Notification> arrN=new ArrayList<Notification>();
//            @Override
//            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
//                if (response.isSuccessful()) {
//                    Log.v("##############",response.body().toString());
//                    arrN = response.body();
//                    for(int i=0;i<arrN.size();i++){
//                        noteData.add(arrN.get(i));
//                        Log.v("##############",arrN.get(i).toString());
//                    }
//                } else {
//                    Log.e("Signup", "Error in response " + response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Notification>> call, Throwable t) {
//                Log.e("NOTIFICATIONS", "Failure to fetch Notifications", t);
//            }
//        });
//         /*Thread t1 = new Thread(new Runnable() {
//            public void run() {
//
//                try{
//                    JSONObject jsonObject = new JSONObject();
//                    URL url = new URL("http://192.168.0.6:8080/GoGreenServer/AllNotificationsServlet");
//                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
//                   // while ((returnString = in.readLine()) != null)
//                    //{
////                      doubledValue= Integer.parseInt(returnString);
//                      //  Log.d("ReturnString", returnString);
//      //                  JSONArray jsonArray = new JSONArray (returnString);
//    //                    if (jsonArray != null) {
//  //                          int len = jsonArray.length();
////                            for (int i=0;i<len;i++){
//
//                            //    noteData.add((Notification)jsonArray.get(i));
//                          //  }
//                        //}
//                        //JSONObject notifications = new JSONObject(returnString);
//                        //noteData = (ArrayList<Notification>)returnString;
//
//                    //}
//                    //noteData.add(new Gson().fromJson(returnString, new TypeToken<List<model.Notification>>(){}.getType()));
//                    Gson gson = new Gson();
//                    noteData = gson.fromJson(in, new TypeToken<List<Notification>>(){}.getType());
//                    Log.v("$$$$$$$$$$$",noteData.toString());
//                    for(int i=0;i<noteData.size();i++){
//                        Log.v("$$$$$$$$$$$",noteData.get(i).getNotificationMessage());
//                    }
//
//                    in.close();
//
//
//                }catch(Exception e)
//                {
//                    Log.d("Exception",e.toString());
//                }
//
//            }
//        });
//        t1.start();
//        t1.join();*/
//        /*noteData.add(new Notification("Amrata","Diseased Sunflowers"));
//        noteData.add(new Notification("Amanleen","Use Roundup"));
//        noteData.add(new Notification("Amrata","Bad Soil"));
//        noteData.add(new Notification("Amanleen","Temporary"));
//        noteData.add(new Notification("Amrata","Event:Mission Peak Plantation Camp"));*/
//        return noteData;
//    }

    private class NoteListViewAdapter extends ArrayAdapter<model.Notification> {

        NoteListViewAdapter(Context context, ArrayList<model.Notification> list){
            super(context, android.R.layout.simple_list_item_1,list);
            Log.v("%%%%%%%%%%%%%","Inside NOTILISTVIEW ADAPTER");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            model.Notification p = getItem(position);
            //Log.v("%%%%%%%%%%%%%",p.toString());

            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, parent, false);
            }
            TextView fromView=(TextView) convertView.findViewById(R.id.note_from);
            Button nb = (Button) convertView.findViewById(R.id.viewNotifDetails);
            final TextView subjectView=(TextView) convertView.findViewById(R.id.note_subject);

            fromView.setText(p.getByUserName());
            subjectView.setText(p.getNotificationMessage());

            final int eventId = p.getEventId();
            Log.v ("############",String.valueOf(eventId));
            final String host =p.getByUserName();
//            nb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            nb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  if(subjectView.getText().toString().contains("event") || subjectView.getText().toString().contains("Event")){

                        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
                        Call<model.Event> getEvent = greenRESTInterface.getAnEvent(eventId);
                        Log.v ("%%%%%%%%%%%%%",String.valueOf(eventId));
                        getEvent.enqueue(new Callback<Event>() {


                            @Override
                            public void onResponse(Call<Event> call, Response<Event> response) {


                                if (response.isSuccessful()) {
                                    Event e =new Event();
                                    Log.v("%%%%%%%%%%%%%",response.body().toString()+"##############");
                                    e = response.body();
                                    Log.v("%%%%%%%%%%%%%",e.toString()+"##############");
                                    Intent intent = new Intent(NotificationActivity.this, EventDetailsActivity.class);
                                    intent.putExtra("EventObj",e);
                                    intent.putExtra("Host",host);
                                    startActivity(intent);
                                } else {
                                    Log.e("Signup", "Error in response " + response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<Event> call, Throwable t) {
                                Log.e("NOTIFICATIONS", "Failure to fetch Notifications", t);
                            }
                        });

                        //Event e = new Event("Event:Plantation Camp","100 peope are invited","43600 Mission Blvd,Fremont, CA 94539","23 APR, 2016","09:00","13:00",3);

                        //Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                        //intent.putExtra("EventObj",e);

                        //startActivity(intent);

                }
            });

            return convertView;
        }
    }
}
