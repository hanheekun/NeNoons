package com.pixelro.eyelab.menu.care;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.care.o2o.O2OActivity;

public class CareFragment extends Fragment implements View.OnClickListener {

    private CareViewModel careViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        careViewModel =
                ViewModelProviders.of(this).get(CareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_care, container, false);

        root.findViewById(R.id.button_care_o2o).setOnClickListener(this);
        //root.findViewById(R.id.button_care_info).setOnClickListener(this);


//        final TextView textView = root.findViewById(R.id.text_care);
//        careViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_care_o2o:
                Intent intent = new Intent(getActivity(), O2OActivity.class);
                startActivity(intent);
                break;
            //case R.id.button_care_info:

                //break;
        }
    }
}
