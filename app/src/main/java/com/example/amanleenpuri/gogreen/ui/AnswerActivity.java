package com.example.amanleenpuri.gogreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;

import adapter.ProxyUser;
import model.GreenEntry;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.remote.GreenRESTInterface;

/**
 * Created by amrata on 4/4/16.
 */
public class AnswerActivity extends AppCompatActivity {

    private Button b;
    private String QuestionText = "";
    private int QuestionId = 0;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_layout);
        ProxyUser pUser = ProxyUser.getInstance();
        userId = pUser.getUserId(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            QuestionText = extras.getString("QTEXT");
            QuestionId = extras.getInt("QID");
        }
        final TextView QuestionTV = (TextView) findViewById(R.id.questionTV) ;
        final EditText answerET = (EditText) findViewById(R.id.answerET);
        answerET.setTag("Answer");
        Button b = (Button) findViewById(R.id.postAnswerButton);

        QuestionTV.setText(QuestionText);

        if (b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText invalidEditText = checkIfEntered(answerET);
                    if (invalidEditText != null) {
                        Toast toast= Toast.makeText(getApplicationContext(), invalidEditText.getTag() + " field cannot be empty.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }else {

                        final GreenEntry GE = new GreenEntry();
                        GE.setPostType("Answer");
                        GE.setPostedByUserId(userId);
                        GE.setPostMessage(answerET.getText().toString());
                        GE.setQuestionIdForAnswers(QuestionId);

                        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
                        Call<GreenEntry> createAnswerCall = greenRESTInterface.createAnswer(GE);
                        createAnswerCall.enqueue(new Callback<GreenEntry>() {
                            @Override
                            public void onResponse(Call<GreenEntry> call, Response<GreenEntry> response) {
                                if (response.isSuccessful()) {
                                    GreenEntry responseAnswer= response.body();
                                    Log.v("%%%%%ANSWER",responseAnswer.toString());


                                    Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.e("PostAnswer", "Error in response " + response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<GreenEntry> call, Throwable t) {
                                Log.e("PostAnswer", "Failure to post Answer", t);
                            }
                        });
                    }

                }
            });

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
