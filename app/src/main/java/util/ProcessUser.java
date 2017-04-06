package util;

import android.content.Context;
import android.util.Log;

import com.example.amanleenpuri.gogreen.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adapter.ProxyUser;
import model.User;

/**
 * Created by Tejal Shah.
 */
public class ProcessUser {

    private ArrayList<User> followData = new ArrayList<User>();
    static int currentUserId;

    ProcessUser(){
    }

    public ProcessUser(Context context){
        currentUserId = ProxyUser.getInstance().getUserId(context);
    }

    public ArrayList<User> fetchFollowers(){
        return null;
    }

    public static ArrayList<User> fetchFollowing(){
        return null;
    }
}
