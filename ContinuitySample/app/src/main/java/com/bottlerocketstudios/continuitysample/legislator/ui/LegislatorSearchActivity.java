package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.BaseLocationActivity;

public class LegislatorSearchActivity extends BaseLocationActivity implements LegislatorSearchInputFragment.Listener, LegislatorSearchResultFragment.Listener {

    private static final String FRAG_LEGISLATOR_SEARCH_INPUT = "legislatorSearchInpug";
    private static final String FRAG_LEGISLATOR_SEARCH_RESULT = "legislatorSearchResult";

    public static Intent newIntent(Context context) {
        return new Intent(context, LegislatorSearchActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.base_content, LegislatorSearchInputFragment.newInstance(), FRAG_LEGISLATOR_SEARCH_INPUT)
                    .commit();
        }
    }

    @Override
    public void launchSearchByName(String name) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_content, LegislatorSearchResultFragment.newNameSearchInstance(name))
                .addToBackStack(FRAG_LEGISLATOR_SEARCH_RESULT)
                .commit();
    }

    @Override
    public void launchSearchByZip(String zip) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_content, LegislatorSearchResultFragment.newZipSearchInstance(zip))
                .addToBackStack(FRAG_LEGISLATOR_SEARCH_RESULT)
                .commit();
    }

    @Override
    public void launchSearchByLocation() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_content, LegislatorSearchResultFragment.newLocalSearchInstance())
                .addToBackStack(FRAG_LEGISLATOR_SEARCH_RESULT)
                .commit();
    }

    @Override
    public void closeSearchResults() {
        getSupportFragmentManager().popBackStack();
    }
}
