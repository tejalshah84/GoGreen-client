package adapter;

import android.content.Context;
import android.database.Cursor;
import android.hardware.camera2.params.StreamConfigurationMap;

import util.DatabaseHandler;

/**
 * Created by amanleenpuri on 4/29/16.
 */
public class ProxyUser {
    private static final ProxyUser sInstance = new ProxyUser();

    public static ProxyUser getInstance(){
        return sInstance;
    }

    public void addUser(String userName, int id, Context context){
        DatabaseHandler mydb = new DatabaseHandler(context);
        mydb.addUser(userName, id);
    }

    public String getUsername(Context context){
        DatabaseHandler mydb = new DatabaseHandler(context);
        String userName = mydb.getUsername();
        return userName;
    }

    public int getUserId(Context context){
        DatabaseHandler mydb = new DatabaseHandler(context);
        int userId = mydb.getUserId();
        return userId;
    }

    public static boolean validateUser(Context context){
        String userName = getInstance().getUsername(context);
        int userId = getInstance().getUserId(context);
        if(userName.isEmpty() || userId==0){
            return false;
        }
        return true;
    }

}
