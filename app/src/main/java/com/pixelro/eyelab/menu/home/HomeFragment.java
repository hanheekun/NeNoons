package com.pixelro.eyelab.menu.home;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.test.TestActivity;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        root.findViewById(R.id.view_main_age_result_btn).setOnClickListener(this);
        root.findViewById(R.id.imageView_app_link_test).setOnClickListener(this);

        final long now = System.currentTimeMillis();
        final UsageStatsManager mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(getActivity().USAGE_STATS_SERVICE);
        final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);

        if (stats == null || stats.isEmpty()) {
            // Usage access is not enabled
        }
        else {

            long tatalTimeMS = 0;

            for (UsageStats stat:stats) {
                tatalTimeMS += stat.getTotalTimeInForeground();
            }
            //Toast.makeText(getActivity(),"total time" + (tatalTimeMS/1000)/3600,Toast.LENGTH_SHORT).show();
            long totalMin = (tatalTimeMS/1000)/60;
            int min = (int)(totalMin % 60);
            int hour = (int)(totalMin / 60);

            Toast.makeText(getActivity(),"Hour : " + hour + ", Min : " + min,Toast.LENGTH_SHORT).show();
        }

        return root;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.view_main_age_result_btn:
                Intent intent = new Intent(getContext(), TestActivity.class);
                startActivity(intent);
                break;
            case R.id.imageView_app_link_test:
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.lilysnc.pixelro.integral"); startActivity( launchIntent );
                break;
        }
    }
}
