package com.bignerdranch.android.wordgolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johntomici on 12/8/17.
 */

public class SetupFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int mWordLength = 3;
    private EditText mStartField;
    private String mStartWord = "";
    private EditText mGoalField;
    private String mGoalWord = "";
    private Button mBeginButton;


    public static SetupFragment newInstance() {
        return new SetupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);


        NumberPicker np = (NumberPicker) view.findViewById(R.id.num_picker);
        np.setMinValue(3);
        np.setMaxValue(7);
        np.setWrapSelectorWheel(false);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                mWordLength = newVal;
            }
        });
        //mWordLength = np.getValue();


        mStartField = (EditText) view.findViewById(R.id.start_word);
        mStartField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mStartWord = s.toString().toLowerCase();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mGoalField = (EditText) view.findViewById(R.id.goal_word);
        mGoalField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mGoalWord = s.toString().toLowerCase();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });


        mBeginButton = (Button) view.findViewById(R.id.begin);
        mBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    //put dictionary into ArrayList
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(getActivity().getAssets().open("dict.txt")));
                    String str;
                    List<String> list = new ArrayList<String>();
                    while ((str = in.readLine()) != null)
                        if (str.length() < 8)
                            list.add(str);

                    //check word lengths
                    if (mStartWord.length() != mWordLength ||
                            mGoalWord.length() != mWordLength) {
                        Toast toast = Toast.makeText(getContext(), R.string.length_error, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }

                    //check if words are the same
                    else if (mStartWord.equals(mGoalWord)) {
                        Toast toast = Toast.makeText(getContext(), R.string.same_error, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }

                    //check if words are in dictionary using wordExists method
                    else if (!wordExists(mStartWord, list) ||
                            !wordExists(mGoalWord, list)) {
                        Toast toast = Toast.makeText(getContext(), R.string.legal_error, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }


                    else {
                        Intent intent = new Intent(getActivity(), GameActivity.class);
                        intent.putExtra("StartWord", mStartWord);
                        intent.putExtra("GoalWord", mGoalWord);
                        startActivity(intent);
                        getActivity().finish();
                    }

                } catch (IOException e){
                    Toast toast = Toast.makeText(getContext(), "Dictionary not found", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }

            }
        });



        return view;
    }

    public static boolean wordExists(String word, List list) throws IOException{
        return wordExists(word, list, 0, list.size() - 1);
    }

    //recursive binary search of dictionary
    public static boolean wordExists(String word, List list,
                                     int low, int high) throws IOException {

        if (low > high) return false;
        int mid = (low + high) / 2;
        if (word.compareTo(list.get(mid).toString()) == 0)
            return true;
        if (word.compareTo(list.get(mid).toString()) < 0)
            return wordExists(word, list, low, mid - 1);
        if (word.compareTo(list.get(mid).toString()) > 0)
            return wordExists(word, list, mid + 1, high);
        return false;

    }
}
