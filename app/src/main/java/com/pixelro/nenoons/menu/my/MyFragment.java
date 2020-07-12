package com.pixelro.nenoons.menu.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.account.AccountActivity;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment implements View.OnClickListener {

    private MyViewModel myViewModel;
    private View mView;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        mView = root;
//        final TextView textView = root.findViewById(R.id.text_my);
//        myViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        mView.findViewById(R.id.button_my_logout).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_my_logout:

                // logout, reset first login
                sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(EYELAB.APPDATA.ACCOUNT.FIRST_LOGIN);
                editor.remove(EYELAB.APPDATA.ACCOUNT.LOGINNING);
                editor.remove(EYELAB.APPDATA.ACCOUNT.TOKEN);
                editor.commit();

                Intent mainIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(mainIntent);

                break;
        }
    }
}
