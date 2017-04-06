package com.example.amanleenpuri.gogreen.ui;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;

import adapter.ProxyUser;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.remote.GreenRESTInterface;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProxyUser pUser = ProxyUser.getInstance();
        String userName = null;
        userName = pUser.getUsername(getApplicationContext());
        final int userId = pUser.getUserId(getApplicationContext());

//        if ((userName.isEmpty() || userId == 0) ){

            if(userId==0 ){
                setContentView(R.layout.activity_login);
                Button loginBtn = (Button) findViewById(R.id.btn_login);
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User user = new User();
                        EditText usernameEt = (EditText) findViewById(R.id.et_username);
                        EditText passwordEt = (EditText) findViewById(R.id.et_password);
                        final String userName = usernameEt.getText().toString();
                        String password = passwordEt.getText().toString();
                        if (userName.isEmpty()) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Please enter a user name!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                        if (password.isEmpty()) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Please enter a password!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                        if (!userName.isEmpty() && !password.isEmpty()) {
                            //TODO: AUTHENTICATE USER FROM SERVER

                            user.setUsername(userName);
                            user.setPassword(password);

                            GreenRESTInterface greenRESTInterface = ((GoGreenApplication) getApplication()).getGoGreenApiService();
                            Call<User> authenticateUserCall = greenRESTInterface.authenticateUser(user);
                            authenticateUserCall.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
    //                            System.out.println("********* RESPONSE 1 ="+response.toString());
    //                            System.out.println("********* RESPONSE 2="+response.isSuccessful());
    //                            System.out.println("********* RESPONSE 3="+response.body());
                                    if (response.isSuccessful()) {
                                        User res = response.body();
                                        ProxyUser pUser = ProxyUser.getInstance();
                                        pUser.addUser(userName, res.getUserId(), getApplicationContext());
                                        Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
                                        startActivity(i);
                                    } else {
                                        Log.e("Timeline", "Error in response " + response.errorBody());
                                        Toast toast = Toast.makeText(getApplicationContext(), "Sorry! Ivalid user-name or password!", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                        toast.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.e("Login", "Failure to authenticate user", t);

                                    Toast toast = Toast.makeText(getApplicationContext(), "on failure", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                }
                            });
                        }
                    }
                });

                TextView signUpTv = (TextView) findViewById(R.id.tv_signup);
                signUpTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(i);
                    }
                });

        }else{
            Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
            startActivity(i);
        }
    }
}
