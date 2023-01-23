package edu.uncc.midtermapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.uncc.midtermapp.models.Question;

public class MainActivity extends AppCompatActivity implements WelcomeFragment.WelcomeFragmentInterface, TriviaFragment.TriviaFragmentInterface {

    public static final String TAG = "deep";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerView,new WelcomeFragment())
                .commit();

    }

    @Override
    public void sendDataToTrivia(ArrayList<Question> q) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView,new TriviaFragment().newInstance(q),"trivia_frag")
                .commit();
    }

    @Override
    public void goToStatsPage(Integer val,Integer val2) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView,new StatsFragment().newInstance(val,val2),"stats_frag")
                .commit();
    }
}