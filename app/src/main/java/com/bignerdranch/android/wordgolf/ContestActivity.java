package com.bignerdranch.android.wordgolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by johntomici on 12/11/17.
 */

public class ContestActivity extends AppCompatActivity {

    private Button mOkayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);


        mOkayButton = (Button) findViewById(R.id.okay_button);
        mOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContestActivity.this, SetupActivity.class));
            }
        });

    }
}
