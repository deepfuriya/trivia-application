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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.midtermapp.models.Answer;
import edu.uncc.midtermapp.models.Question;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.squareup.picasso.Picasso;

public class TriviaFragment extends Fragment {
    private ArrayList<Question> mTriviaQuestions = new ArrayList<Question>();

    private static final String QUESTION_VALUE = "question";
    public final OkHttpClient client = new OkHttpClient();


    String TAG = "deep";

    Integer counter = 0;
    Integer finalCount = 0;

    ArrayAdapter<Answer> adapter;

    private JSONObject jsonQuestion;

    TextView topStatus;
    TextView textViewTriviaQuestion;
    ListView listViewAnswers;
    ImageView imageViewQuestion;

    public TriviaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TriviaFragment newInstance(ArrayList<Question> q) {
        TriviaFragment fragment = new TriviaFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_VALUE,q);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTriviaQuestions = (ArrayList<Question>) getArguments().getSerializable(QUESTION_VALUE);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trivia, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topStatus = view.findViewById(R.id.textViewTriviaTopStatus);
        textViewTriviaQuestion = view.findViewById(R.id.textViewTriviaQuestion);
        listViewAnswers = view.findViewById(R.id.listViewAnswers);
        imageViewQuestion = view.findViewById(R.id.imageViewQuestion);
        setQuestion(counter);
    }

    public void setQuestion(Integer num){

        if (num != mTriviaQuestions.size()) {

            final Integer[] check = {1};
            Question questionList = mTriviaQuestions.get(num);

            textViewTriviaQuestion.setText(questionList.question_text);
            
            topStatus.setText("Question " + (num + 1) + " of " + mTriviaQuestions.size());

            if (questionList.question_url != "") {
                Picasso.get().load(questionList.question_url).into(imageViewQuestion);
            }

            ArrayList<Answer> answerArrayList = questionList.getAnswers();


            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, answerArrayList);
            listViewAnswers.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listViewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    FormBody formBody = new FormBody.Builder()
                            .add("question_id", mTriviaQuestions.get(num).question_id)
                            .add("answer_id", answerArrayList.get(i).answer_id)
                            .build();

                    Request request = new Request.Builder()
                            .url("https://www.theappsdr.com/api/trivia/checkAnswer")
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String string = response.body().string();
                                try {
                                    JSONObject json = new JSONObject(string);
                                    if (json.getString("isCorrectAnswer") != "true") {
                                        check[0]++;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "Answer is not correct !!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                counter++;
                                                if (check[0] == 1){
                                                    finalCount++;
                                                }
                                                setQuestion(counter);

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });

                }
            });

        }else{
            triviaFragmentListener.goToStatsPage(finalCount,mTriviaQuestions.size());
        }
    }

    TriviaFragmentInterface triviaFragmentListener;

    interface TriviaFragmentInterface{
        void goToStatsPage(Integer val, Integer val2);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        triviaFragmentListener = (TriviaFragmentInterface) context;
    }
}