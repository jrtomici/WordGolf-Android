package com.bignerdranch.android.wordgolf;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by johntomici on 12/8/17.
 */

public class GameFragment extends Fragment {
    public static GameFragment newInstance() {
        return new GameFragment();
    }

    private EditText mNextField;
    private Button mSubmit;
    private String mCurrentWord = "";
    private String mPriorWord;
    private String mDone;
    private TextView mPriorTextView;
    private String mHistory;
    private ScrollView mScroll;
    private int mScore = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game, container, false);

        final String mStartWord = getActivity().getIntent().getExtras().getString("StartWord");
        final String mGoalWord = getActivity().getIntent().getExtras().getString("GoalWord");
        mPriorWord = mStartWord;
        mHistory = mStartWord;
        mDone = mGoalWord;
        final long startTime = System.currentTimeMillis();


        mScroll = (ScrollView) view.findViewById(R.id.scroll);


        mPriorTextView = (TextView) view.findViewById(R.id.prior_word);

        mNextField = (EditText) view.findViewById(R.id.next_word);
        mNextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mCurrentWord = s.toString().toLowerCase();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mSubmit = (Button) view.findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
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

                    //check if words are the same
                    if (mPriorWord.equals(mCurrentWord)) {
                        Toast toast = Toast.makeText(getContext(), R.string.same_error, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                        mNextField.setText("");
                    }

                    //check if words are in dictionary using wordExists method
                    else if (!wordExists(mCurrentWord, list)) {
                        Toast toast = Toast.makeText(getContext(), R.string.legal_error, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                        mNextField.setText("");
                    }

                    //check word lengths
                    else if (mPriorWord.length() != mCurrentWord.length()) {
                        Toast toast = Toast.makeText(getContext(), R.string.length_error, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                        mNextField.setText("");
                    }

                    else {
                        //count different letters between current and prior words
                        int diffLetters = 0;
                        for (int i = 0; i < mCurrentWord.length(); i++)
                            if (mCurrentWord.charAt(i) != mPriorWord.charAt(i))
                                diffLetters++;

                        //sort the chars in each word alphabetically to check if scramble
                        char[] currentArray = mCurrentWord.toCharArray();
                        char[] priorArray = mPriorWord.toCharArray();
                        Arrays.sort(currentArray);
                        Arrays.sort(priorArray);

                        //check if one letter changed
                        if (diffLetters == 1) {
                            mScore++;
                            if (mCurrentWord.equals(mDone)) {
                                long endTime = System.currentTimeMillis();
                                int timeScore = (int)(endTime - startTime) / 1000;
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setCancelable(false);
                                builder.setMessage("Score: " + mScore + "\nSeconds: " + timeScore);
                                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else {
                                mPriorWord = mCurrentWord;
                                mHistory = mHistory + "\n" + mPriorWord;
                                mNextField.setText("");
                                updateWord();
                            }
                        }

                        //check if valid scramble
                        else if (!(Arrays.toString(currentArray).equals(
                                Arrays.toString(priorArray)))) {
                            Toast toast = Toast.makeText(getContext(), R.string.letter_error, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                            mNextField.setText("");
                        } else {
                            mScore++;
                            if (mCurrentWord.equals(mDone)) {
                                long endTime = System.currentTimeMillis();
                                int timeScore = (int)(endTime - startTime) / 1000;
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setCancelable(false);
                                builder.setMessage("Score: " + mScore + "\nSeconds: " + timeScore);
                                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                mPriorWord = mCurrentWord;
                                mHistory = mHistory + "\n" + mPriorWord;
                                mNextField.setText("");
                                updateWord();
                            }
                        }
                    }
                } catch (IOException e){
                    Toast toast = Toast.makeText(getContext(), "Dictionary not found", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });

        updateWord();

        return view;
    }



    private void updateWord() {
        final String mGoal = getActivity().getIntent().getExtras().getString("GoalWord");
        mPriorTextView.setText(mHistory + "\n?\n" + mGoal);
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.fullScroll(View.FOCUS_DOWN);
            }
        });
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
