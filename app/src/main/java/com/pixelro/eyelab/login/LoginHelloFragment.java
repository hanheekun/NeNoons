package com.pixelro.eyelab.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pixelro.eyelab.MainActivity;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.test.TestActivity;

public class LoginHelloFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = LoginHelloFragment.class.getSimpleName();
    private View mView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_hello, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_login_hello_login).setOnClickListener(this);
        view.findViewById(R.id.button_login_hello_join).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login_hello_login:
            case R.id.button_login_hello_join:
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
