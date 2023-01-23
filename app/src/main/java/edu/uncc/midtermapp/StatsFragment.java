package edu.uncc.midtermapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StatsFragment extends Fragment {

    private static final String TOTAL_COUNT = "total_count";
    private static final String CORRECT_COUNT = "correct_count";

    private Integer totalCount;
    private Integer correctCount;

    public StatsFragment() {
        // Required empty public constructor
    }


    public static StatsFragment newInstance(Integer val,Integer val2) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putInt(TOTAL_COUNT, val);
        args.putInt(CORRECT_COUNT, val2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalCount = getArguments().getInt(TOTAL_COUNT);
            correctCount = getArguments().getInt(CORRECT_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView head = view.findViewById(R.id.textViewTriviaStatus);
        head.setText(correctCount+" out of "+totalCount+" questions were answered correctly from the first attempt!");
    }
}