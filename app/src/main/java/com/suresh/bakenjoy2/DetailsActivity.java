package com.suresh.bakenjoy2;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.suresh.bakenjoy2.Fragments.InstructionsFragment;
import com.suresh.bakenjoy2.Utils.Constants;

public class DetailsActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mFragmentManager = getSupportFragmentManager();
        Intent sentIntent = getIntent();
        mBundle = sentIntent.getExtras();
        if (findViewById(R.id.details_frame_layout) != null) {
            if (savedInstanceState != null) {
                return;
            }

            if (mBundle != null) {
                Toast.makeText(getApplicationContext(),mBundle.toString(),Toast.LENGTH_SHORT).show();

            InstructionsFragment instructionsFragment = new InstructionsFragment();
            instructionsFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_frame_layout, instructionsFragment).commit();
            }
        }
    }

}
