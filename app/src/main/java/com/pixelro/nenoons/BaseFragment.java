package com.pixelro.nenoons;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment  extends Fragment {
    protected final static String TAG = BaseFragment.class.getSimpleName();
    protected View mView;
    protected SharedPreferencesManager mSm;
    protected Context mContext;
    protected ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        mSm = new SharedPreferencesManager(getActivity());
        mContext = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }
}
