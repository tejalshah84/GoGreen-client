package com.example.amanleenpuri.gogreen.ui;

import android.app.Activity;

/**
 * Created by amrata on 4/26/16.
 */

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;



import com.example.amanleenpuri.gogreen.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import adapter.ProxyUser;

import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ImagePicker;
import util.ValidateText;
import ws.remote.GreenRESTInterface;

public class EditProfileActivity extends AppCompatActivity {

    private static final String SERVICE_URL = "http://192.168.0.6:8080/GoGreen_Server/rest/user";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_FOR_AVATAR = 0;
    private String IMAGE_BIT_MAP_IN_STRING = "";
    private ImageView imageView ;
    private Resources res;
    private String roleTypeSelection=null;
    private String interestAreaSelection = null;
    private String test1 = "Outdoor";


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        res = getResources();

        Intent extras = getIntent();
        User userCurrentValues = new User();
        userCurrentValues =(User)extras.getSerializableExtra("USER_DETAILS_OBJECT");
        roleTypeSelection=userCurrentValues.getRoleType();
        interestAreaSelection = userCurrentValues.getInterestArea();

        ProxyUser pUser = ProxyUser.getInstance();
        String userName = pUser.getUsername(getApplicationContext());
        final int userId = pUser.getUserId(getApplicationContext());
        System.out.println("*********** userId="+userId);
        System.out.println("*********** userName="+userName);
        if(userName.isEmpty() || userId==0){
            Intent i = new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(i);

        }else{

            final Spinner roleTypeSp;
            final Spinner interestAreaSp;
            final EditText firstNameEt;
            final EditText lastNameEt;
            final EditText cityEt;
            final EditText stateEt;

            setContentView(R.layout.appbar_editprofile);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.EditProfileLabel);
            setSupportActionBar(toolbar);

            firstNameEt = (EditText)findViewById(R.id.et_firstNameEditProfile);
            firstNameEt.setTag("First Name");
            firstNameEt.setText(userCurrentValues.getFirstName());

            lastNameEt = (EditText)findViewById(R.id.et_lastNameEditProfile);
            lastNameEt.setTag("Last Name");
            lastNameEt.setText(userCurrentValues.getLastName());

            cityEt = (EditText)findViewById(R.id.et_cityEditProfile);
            cityEt.setTag("City");
            cityEt.setText(userCurrentValues.getCity());

            stateEt = (EditText)findViewById(R.id.et_stateEditProfile);
            stateEt.setTag("State");
            stateEt.setText(userCurrentValues.getState());

            imageView = (ImageView)findViewById(R.id.iv_profilePicEditProfile);
            IMAGE_BIT_MAP_IN_STRING = userCurrentValues.getImageURL();
            imageView.setImageBitmap(getStringToBitMap(IMAGE_BIT_MAP_IN_STRING));

            roleTypeSp = (Spinner)findViewById(R.id.sp_roleSpinnerEditProfile);
            final String[] roleTypeArr = res.getStringArray(R.array.roleType_array);
            ArrayAdapter<String> adapterRoleType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roleTypeArr);
            roleTypeSp.setAdapter(adapterRoleType);
            if (!(userCurrentValues.getRoleType()).equals(null)) {
                int spinnerPosition = adapterRoleType.getPosition(userCurrentValues.getRoleType());
                roleTypeSp.setSelection(spinnerPosition);
            }
            roleTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                String roleSelection1;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    roleSelection1 = roleTypeArr[position];
                    updateSelection("roleType",roleSelection1);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            interestAreaSp = (Spinner)findViewById(R.id.sp_interestAreaSpinnerEditProfile);
            final String[] interestAreaArr = res.getStringArray(R.array.interestArea_array);
            ArrayAdapter<String> adapterInterestArea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, interestAreaArr);
            interestAreaSp.setAdapter(adapterInterestArea);
            if (!(userCurrentValues.getInterestArea()).equals(null)) {
                int spinnerPosition = adapterInterestArea.getPosition(userCurrentValues.getInterestArea());
                interestAreaSp.setSelection(spinnerPosition);
            }
            interestAreaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                String interestAreaSelection1;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    interestAreaSelection1 = interestAreaArr[position];
                    System.out.print("Strings matching :: "+interestAreaSelection1.equals(test1));
                    updateSelection("interestArea",interestAreaSelection1);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
//            roleSelection = roleTypeSp.getSelectedItem().toString();
//            interestAreaSelection = interestAreaSp.getSelectedItem().toString();

            ImageButton cameraBtn = (ImageButton)findViewById(R.id.btn_camera);
            cameraBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    clickpic();
                }
            });

            ImageButton uploadImageBtn = (ImageButton)findViewById(R.id.btn_uploadImage);
            uploadImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upload();
                }
            });

            Button editProfileBtn = (Button) findViewById(R.id.btn_editProfile);
            editProfileBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {

                    EditText invalidEditText = checkIfEntered(firstNameEt, lastNameEt, cityEt, stateEt);
                    if (invalidEditText != null) {
                        Toast toast= Toast.makeText(getApplicationContext(), invalidEditText.getTag() + " field cannot be empty.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }else if(roleTypeSelection.isEmpty()){
                        Toast toast= Toast.makeText(getApplicationContext(), " Select a role Type!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }else if(interestAreaSelection.isEmpty()){
                        Toast toast= Toast.makeText(getApplicationContext(), " Select an area of Interest!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }else{
                        User user = new User();
                        user.setUserId(userId);
                        user.setFirstName(firstNameEt.getText().toString());
                        user.setLastName(lastNameEt.getText().toString());
                        user.setCity(cityEt.getText().toString());
                        user.setState(stateEt.getText().toString());
                        user.setRoleId(roleTypeSelection);
                        user.setInterestArea(interestAreaSelection);
                        user.setImageURL(IMAGE_BIT_MAP_IN_STRING);
                        String jsonString = "";
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            jsonString = mapper.writeValueAsString(user);
                            System.out.println(jsonString);

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
                        Call<User> editUserCall = greenRESTInterface.editUser(user);
                        editUserCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User res = response.body();
                                    Intent i = new Intent(EditProfileActivity.this, TimelineActivity.class);
                                    startActivity(i);
                                } else {
                                    Log.e("Timeline", "Error in response " + response.errorBody());
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("Edit profile", "Failure to fetch user", t);
                            }
                        });
                    }
                }
            });
        }
    }

    private void updateSelection(String type, String value){
        if(type.equalsIgnoreCase("roleType")){
            roleTypeSelection = value;
        }else if(type.equalsIgnoreCase("interestArea")){
            interestAreaSelection = value;
        }
    }

    private void upload() {
        // Image location URL
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        Intent intent = ImagePicker.getPickImageIntent(EditProfileActivity.this);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FOR_AVATAR);
    }



    private void clickpic() {
        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            IMAGE_BIT_MAP_IN_STRING = getBitMapToString(imageBitmap);
            imageView.setImageBitmap(imageBitmap);
           }//else if(resultCode == RESULT_OK) {
        if (requestCode == PICK_IMAGE_FOR_AVATAR && resultCode == RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                try {
                    Bitmap imageBitmap = getBitmapFromUri(uri);
                    IMAGE_BIT_MAP_IN_STRING = getBitMapToString(imageBitmap);
                    imageView.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public String getBitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap getStringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    EditText checkIfEntered(EditText... allInputFields) {
        for (EditText editText : allInputFields) {
            if (TextUtils.isEmpty(editText.getText())) {
                return editText;
            }
        }
        return null;
    }


}
