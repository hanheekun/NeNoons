package com.pixelro.eyelab.ui.care;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.eyelab.R;

public class CareFragment extends Fragment {

    private CareViewModel careViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        careViewModel =
                ViewModelProviders.of(this).get(CareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_care, container, false);
        final TextView textView = root.findViewById(R.id.text_care);
        careViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
