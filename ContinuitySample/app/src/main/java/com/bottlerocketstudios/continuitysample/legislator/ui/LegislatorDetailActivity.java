package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.BaseActivity;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

public class LegislatorDetailActivity extends BaseActivity {

    public static final String EXTRA_LEGISLATOR = "legislator";
    private static final String FRAG_LEGISLATOR_DETAIL = "legislatorDetailFrag";

    public static Intent newIntent(Context context, LegislatorViewModel legislatorViewModel) {
        Intent intent = new Intent(context, LegislatorDetailActivity.class);
        intent.putExtra(EXTRA_LEGISLATOR, legislatorViewModel);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LegislatorViewModel legislatorViewModel = getIntent().getParcelableExtra(EXTRA_LEGISLATOR);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.base_content, LegislatorDetailFragment.newInstance(legislatorViewModel), FRAG_LEGISLATOR_DETAIL)
                    .commit();
        }
        mAppBarController.setAppBarTitle(legislatorViewModel.formatFullName(this));
    }
}
