package com.bignerdranch.android.wordgolf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by johntomici on 12/7/17.
 */

public class SetupActivity extends SingleFragmentActivity {



    @Override
    protected Fragment createFragment() {
        return SetupFragment.newInstance();
    }

}
