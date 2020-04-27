package com.pixelro.eyelab.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;

public class
AccountHelloFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_hello, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_account_hello_login).setOnClickListener(this);
        view.findViewById(R.id.button_account_hello_join).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_account_hello_login:
                NavHostFragment.findNavController(AccountHelloFragment.this).navigate(R.id.action_navigation_account_hello_to_navigation_account_login);
                break;
            case R.id.button_account_hello_join:
                NavHostFragment.findNavController(AccountHelloFragment.this).navigate(R.id.action_navigation_account_hello_to_navigation_account_join);
                break;
        }
    }
}
