package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.os.Bundle;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.BaseActivity;

public class FavoriteLegislatorListActivity extends BaseActivity {

    private static final String FRAG_FAVORITE_LEGISLATORS = "representativeList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.base_content, FavoriteLegislatorListFragment.newInstance(), FRAG_FAVORITE_LEGISLATORS)
                    .commit();
        }
    }
}
