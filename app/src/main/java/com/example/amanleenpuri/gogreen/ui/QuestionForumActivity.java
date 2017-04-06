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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amanleenpuri.gogreen.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class QuestionForumActivity extends AppCompatActivity{
    private Button b1;
    private Button b2;
    private TextView ansTextView;
    ArrayList<GreenEntry> qaData;
    //ArrayList<GreenEntry> ansData;
    ListView qaList;
    ListView ansList;
    private int QuestID;
    private String QuestTXT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_forum);

        qaList=(ListView)findViewById(R.id.qa_list);
        //ansList = (ListView)findViewById(R.id.ans_list);
        qaData = new ArrayList<>();

        if(savedInstanceState==null){
            Bundle arguments = new Bundle();
            Bundle extras = getIntent().getExtras();
            qaData = (ArrayList<GreenEntry>) extras.getSerializable("Q_DATA");
            //qaHash = (HashMap<Integer, ArrayList<GreenEntry>>) extras.getSerializable("QA_HASH");
           }

        qaList.setAdapter(new QAListViewAdapter(getApplicationContext(), qaData));
    }


    private ArrayList setQaList(){

        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
        Call<List<GreenEntry>> getAllQuestions = greenRESTInterface.getQuestions(2);
        getAllQuestions.enqueue(new Callback<List<GreenEntry>>() {

            List<GreenEntry> arrGE=new ArrayList<GreenEntry>();
            @Override
            public void onResponse(Call<List<GreenEntry>> call, Response<List<GreenEntry>> response) {
                if (response.isSuccessful()) {

                    arrGE = response.body();
                    for(int i=0;i<arrGE.size();i++){
                        qaData.add(arrGE.get(i));

                    }
                } else {
                    Log.e("Signup", "Error in response " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<GreenEntry>> call, Throwable t) {
                Log.e("QA_Forum", "Failure to fetch Questions", t);
            }
        });

        return qaData;
    }



    private class QAListViewAdapter extends ArrayAdapter<GreenEntry> {

        QAListViewAdapter(Context context, ArrayList<GreenEntry> list){
            super(context, android.R.layout.simple_list_item_1,list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GreenEntry ge = getItem(position);

            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.question_item, parent, false);
            }


            final int QID = ge.getPostId();


            TextView question=(TextView) convertView.findViewById(R.id.tv_question);
            Button viewAnsB=(Button) convertView.findViewById(R.id.viewAnsButton);

            final View cv = convertView;
            final String qText = ge.getPostMessage();

            question.setText("Q. "+ ge.getPostMessage());
            //viewAnsB.setText("Answers " +ansData.size());
            viewAnsB.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ArrayList ansData = new ArrayList<>();
                    ansData= setAnsList(cv,QID,qText);

                }
            });

            return convertView;
        }
    }

     public ArrayList<GreenEntry> setAnsList(final View cv,int qId, String qText){

        final int QID = qId;
        final String QTX =  qText;
         QuestID = qId;
         QuestTXT = qText;

         final ArrayList<GreenEntry> ansData = new ArrayList<GreenEntry>();
        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
        Call<List<GreenEntry>> getAllAnswers = greenRESTInterface.getAnsForQ(qId);
        getAllAnswers.enqueue(new Callback<List<GreenEntry>>() {

            List<GreenEntry> arrGE=new ArrayList<GreenEntry>();
            @Override
            public void onResponse(Call<List<GreenEntry>> call, Response<List<GreenEntry>> response) {
                if (response.isSuccessful()) {

                    arrGE = response.body();
                    for(int i=0;i<arrGE.size();i++){
                        ansData.add(arrGE.get(i));

                    }

                    ansList = (ListView) cv.findViewById(R.id.ans_list);
                    final LinearLayout ll=(LinearLayout)cv.findViewById(R.id.ans_layout);
                    ansList.setAdapter(new ANSListViewAdapter(getApplicationContext(), ansData));
                    if(ansData.size()>0) {
                        if(ll.getVisibility() == View.VISIBLE){
                            ll.setVisibility(View.GONE);
                        }else {
                            ll.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        Intent intent = new Intent(QuestionForumActivity.this, AnswerActivity.class);
                        intent.putExtra("QTEXT",QTX);
                        intent.putExtra("QID", QID);
                        startActivity(intent);
                    }

                } else {
                    Log.e("Signup", "Error in response " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<GreenEntry>> call, Throwable t) {
                Log.e("QA_Forum", "Failure to fetch Questions", t);
            }
        });

        return ansData;
    }

    private class ANSListViewAdapter extends ArrayAdapter<GreenEntry> {
        ArrayList<GreenEntry> tempArr;
        int qid=0;

        ANSListViewAdapter(Context context, ArrayList<GreenEntry> list){
            super(context, android.R.layout.simple_list_item_1,list);
            this.tempArr = list;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.answer_item, parent, false);
            }
            TextView answer=(TextView) convertView.findViewById(R.id.tv_answerText);
            Button ansButton=(Button) convertView.findViewById(R.id.answerButton);


            StringBuilder temp=new StringBuilder();

            for(int i =0; i<tempArr.size();i++) {
                //GreenEntry ge = getItem(position);
                GreenEntry ge = tempArr.get(i);
                temp.append("Ans. " + ge.getPostMessage() +" [Answered By : "+ge.getPostByUserName()+"]"+"\n");
                position=position++;
            }

            answer.setText(temp.toString());

            ansButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AnswerActivity.class);
                    intent.putExtra("QTEXT",QuestTXT);
                    intent.putExtra("QID", QuestID);
                    startActivity(intent);
                }
            });


            return convertView;
        }
    }
}
