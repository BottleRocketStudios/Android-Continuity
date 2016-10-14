package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;
import com.bottlerocketstudios.continuitysample.core.ui.BaseFragment;
import com.bottlerocketstudios.continuitysample.core.ui.LocationActivity;
import com.bottlerocketstudios.continuitysample.core.ui.ResponseStatusTranslator;
import com.bottlerocketstudios.continuitysample.core.ui.dialog.SampleDialogFragment;
import com.bottlerocketstudios.continuitysample.databinding.LegislatorSearchResultFragmentBinding;
import com.bottlerocketstudios.continuitysample.legislator.presenter.LegislatorSearchResultPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorSearchResultViewModel;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * Created on 9/13/16.
 */
public class LegislatorSearchResultFragment extends BaseFragment {

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final String ARG_SEARCH_QUERY = "searchQuery";
    private static final String ARG_SEARCH_MODE = "zipCode";
    private static final String SAVED_LAYOUT_MANAGER = "layoutManager";

    private static final String DIALOG_TAG_ERROR = "errorDialog";
    private static final int DIALOG_ID_PERMANENT_PERMISSION_FAILURE = 1;
    private static final int DIALOG_ID_TEMPORARY_PERMISSION_FAILURE = 2;
    private static final int DIALOG_ID_LOCATION_SETTING_FAILURE = 3;
    private static final int DIALOG_ID_PLAY_SERVICES_FAILURE = 4;
    private static final int DIALOG_ID_PERMISSION_RATIONALE = 5;
    private static final int DIALOG_ID_NETWORK_ERROR = 6;

    private LegislatorSearchResultFragmentBinding mFragmentBinding;
    private LegislatorSearchResultPresenter mLegislatorSearchResultPresenter;
    private LegislatorSearchRecyclerAdapter mLegislatorSearchRecyclerAdapter;
    private LinearLayoutManager mResultLayoutManager;
    private Listener mFragmentListener;

    public static LegislatorSearchResultFragment newZipSearchInstance(String zipCode) {
        LegislatorSearchResultFragment fragment = new LegislatorSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_QUERY, zipCode);
        args.putSerializable(ARG_SEARCH_MODE, LegislatorSearchResultPresenter.SearchMode.ZIP_CODE);
        fragment.setArguments(args);
        return fragment;
    }

    public static LegislatorSearchResultFragment newNameSearchInstance(String name) {
        LegislatorSearchResultFragment fragment = new LegislatorSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_QUERY, name);
        args.putSerializable(ARG_SEARCH_MODE, LegislatorSearchResultPresenter.SearchMode.NAME);
        fragment.setArguments(args);
        return fragment;
    }

    public static LegislatorSearchResultFragment newLocalSearchInstance() {
        LegislatorSearchResultFragment fragment = new LegislatorSearchResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SEARCH_MODE, LegislatorSearchResultPresenter.SearchMode.LOCATION);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LocationActivity locationActivity = activityCastOrThrow(context, LocationActivity.class);

        //Create or retrieve the presenter
        mLegislatorSearchResultPresenter = getPresenterRepository().with(this, LegislatorSearchResultPresenter.class).build();
        mLegislatorSearchResultPresenter.setLocationActivity(locationActivity);

        mFragmentListener = activityCastOrThrow(context, Listener.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.legislator_search_result_fragment, container, false);

        mFragmentBinding.setPresenter(mLegislatorSearchResultPresenter);

        mResultLayoutManager = new LinearLayoutManager(getContext());
        mFragmentBinding.lsrfRecyclerView.setLayoutManager(mResultLayoutManager);
        mLegislatorSearchRecyclerAdapter = new LegislatorSearchRecyclerAdapter();
        mLegislatorSearchRecyclerAdapter.setPresenter(mLegislatorSearchResultPresenter);
        mFragmentBinding.lsrfRecyclerView.setAdapter(mLegislatorSearchRecyclerAdapter);

        mLegislatorSearchResultPresenter.bindListener(mPresenterListener);

        return mFragmentBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLegislatorSearchResultPresenter.startSearching((LegislatorSearchResultPresenter.SearchMode) getArguments().getSerializable(ARG_SEARCH_MODE), getArguments().getString(ARG_SEARCH_QUERY, null));
    }

    private LegislatorSearchResultPresenter.Listener mPresenterListener = new LegislatorSearchResultPresenter.Listener() {

        @Override
        public void bindLegislatorSearchViewModel(LegislatorSearchResultViewModel legislatorSearchResultViewModel) {
            mFragmentBinding.setLegislatorSearchViewModel(legislatorSearchResultViewModel);
        }

        @Override
        public void bindLegislatorList(ObservableList<LegislatorViewModel> legislatorList) {
            mLegislatorSearchRecyclerAdapter.swapList(legislatorList);
        }

        @Override
        public boolean hasPermission(String permission) {
            return PermissionChecker.checkSelfPermission(getContext(), permission) == PermissionChecker.PERMISSION_GRANTED;
        }

        @Override
        public boolean shouldShowPermissionRationale(String permission) {
            return LegislatorSearchResultFragment.this.shouldShowRequestPermissionRationale(permission);
        }

        @Override
        public void showPermissionRationale(String permission) {
            new SampleDialogFragment.Builder()
                    .setDialogId(DIALOG_ID_PERMISSION_RATIONALE)
                    .setTitle(getString(R.string.lsrf_location_permission_title))
                    .setMessage(getString(R.string.lsrf_permission_rationale))
                    .setPositiveText(getString(R.string.ok))
                    .setNegativeText(getString(R.string.cancel))
                    .setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    mLegislatorSearchResultPresenter.permissionRationaleAgreed();
                                    break;
                                default:
                                    mFragmentListener.closeSearchResults();
                                    break;
                            }
                        }
                    })
                    .build()
                    .show(getChildFragmentManager(), DIALOG_TAG_ERROR);
        }

        @Override
        public void requestPermission(String permission) {
            LegislatorSearchResultFragment.this.requestPermissions(new String[]{permission}, REQUEST_CODE_PERMISSION);
        }

        @Override
        public void onPermanentLocationPermissionFailure() {
            new SampleDialogFragment.Builder()
                    .setDialogId(DIALOG_ID_PERMANENT_PERMISSION_FAILURE)
                    .setTitle(getString(R.string.lsrf_location_permission_title))
                    .setMessage(getString(R.string.lsrf_permanent_location_permission_failure))
                    .setPositiveText(getString(R.string.ok))
                    .setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFragmentListener.closeSearchResults();
                        }
                    })
                    .build()
                    .show(getChildFragmentManager(), DIALOG_TAG_ERROR);
        }

        @Override
        public void onTemporaryLocationPermissionFailure() {
            new SampleDialogFragment.Builder()
                    .setDialogId(DIALOG_ID_TEMPORARY_PERMISSION_FAILURE)
                    .setTitle(getString(R.string.lsrf_location_permission_title))
                    .setMessage(getString(R.string.lsrf_temporary_location_permission_failure))
                    .setPositiveText(getString(R.string.ok))
                    .setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFragmentListener.closeSearchResults();
                        }
                    })
                    .build()
                    .show(getChildFragmentManager(), DIALOG_TAG_ERROR);
        }

        @Override
        public void onLocationSettingFailure() {
            new SampleDialogFragment.Builder()
                    .setDialogId(DIALOG_ID_LOCATION_SETTING_FAILURE)
                    .setTitle(getString(R.string.lsrf_location_setting_failure_title))
                    .setMessage(getString(R.string.lsrf_location_setting_failure))
                    .setPositiveText(getString(R.string.ok))
                    .setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFragmentListener.closeSearchResults();
                        }
                    })
                    .build()
                    .postShow(getChildFragmentManager(), DIALOG_TAG_ERROR);
        }

        @Override
        public void onPermanentGooglePlayServicesFailure() {
            new SampleDialogFragment.Builder()
                    .setDialogId(DIALOG_ID_PLAY_SERVICES_FAILURE)
                    .setTitle(getString(R.string.lsrf_play_services_failure_title))
                    .setMessage(getString(R.string.lsrf_play_services_failure))
                    .setPositiveText(getString(R.string.ok))
                    .setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFragmentListener.closeSearchResults();
                        }
                    })
                    .build()
                    .show(getChildFragmentManager(), DIALOG_TAG_ERROR);
        }

        @Override
        public void showNetworkError(ResponseStatus responseStatus) {
            new SampleDialogFragment.Builder()
                    .setDialogId(DIALOG_ID_NETWORK_ERROR)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(ResponseStatusTranslator.getErrorString(getContext(), responseStatus))
                    .setPositiveText(getString(R.string.ok))
                    .setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFragmentListener.closeSearchResults();
                        }
                    })
                    .build()
                    .show(getChildFragmentManager(), DIALOG_TAG_ERROR);
        }

        @Override
        public void launchLegislatorDetail(View photo, LegislatorViewModel legislatorViewModel) {
            Intent intent = LegislatorDetailActivity.newIntent(getActivity(), legislatorViewModel);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), photo, getString(R.string.transition_legislator_detail));
                Bundle optionsBundle = options.toBundle();
                startActivity(intent, optionsBundle);
            } else {
                startActivity(intent);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            mLegislatorSearchResultPresenter.handlePermissionResult(permissions, grantResults);
        }
    }

    public interface Listener {
        void closeSearchResults();
    }

}
