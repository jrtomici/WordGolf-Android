package com.bignerdranch.android.wordgolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button mPlayButton;
    private Button mContestButton;
    private Button mHowButton;
    private Button mAboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SetupActivity.class));
            }
        });

        mContestButton = (Button) findViewById(R.id.contest_button);
        mContestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ContestActivity.class));
            }
        });

        mHowButton = (Button) findViewById(R.id.how_button);
        mHowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, HowActivity.class));
            }
        });

        mAboutButton = (Button) findViewById(R.id.about_button);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, AboutActivity.class));

            }
        });
    }
}
