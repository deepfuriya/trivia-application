package edu.uncc.midtermapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.midtermapp.models.Answer;
import edu.uncc.midtermapp.models.Question;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomeFragment extends Fragment {
    private ArrayList<Question> mTriviaQuestions = new ArrayList<Question>();
    String TAG = "deep";

    public final OkHttpClient client = new OkHttpClient();


    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Request request = new Request.Builder()
                        .url("https://www.theappsdr.com/api/trivia")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                       if (response.isSuccessful()){
                           String string = response.body().string();

                           try {
                               ArrayList<Question> arrayListQuestion = new ArrayList<>();

                               JSONObject json = new JSONObject(string);
                               JSONArray jsonList = json.getJSONArray("questions");
                               for (int i = 0; i < jsonList.length(); i++) {
                                   JSONObject jsonObjectBox = jsonList.getJSONObject(i);
                                   JSONArray answers = jsonObjectBox.optJSONArray("answers");

                                   ArrayList<Answer> answerArrayList = new ArrayList<>();

                                   for (int j = 0; j < answers.length(); j++) {
                                       JSONObject answerJsonObj = answers.getJSONObject(j);
                                       answerArrayList.add(new Answer(answerJsonObj.getString("answer_id"),answerJsonObj.getString("answer_text")));
                                   }

                                   arrayListQuestion.add(new Question(jsonObjectBox.getString("question_id").toString(),jsonObjectBox.getString("question_text").toString(),jsonObjectBox.getString("question_url").toString(),answerArrayList));
                               }
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       welcomeFragmentListener.sendDataToTrivia(arrayListQuestion);
                                   }
                               });

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                    }
                });


            }
        });
    }

    WelcomeFragmentInterface welcomeFragmentListener;

    interface WelcomeFragmentInterface{
        void sendDataToTrivia(ArrayList<Question> q);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        welcomeFragmentListener = (WelcomeFragmentInterface) context;
    }
}