package com.pixelro.eyelab.menu.exercise.ex01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.exercise.ExCancelDialog;

public class Ex01Activity extends AppCompatActivity {

    public final static int EX_TIME_30 = 30;
    public final static int EX_TIME_1 = 60;
    public final static int EX_TIME_2 = 120;
    public final static int EX_TIME_5 = 300;
    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

    public int setTimeSec = EX_TIME_30;
    public int setLevel = EX_LEVEL_L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_01);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_ex_01, new Ex01AFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        ExCancelDialog dlg = new ExCancelDialog(Ex01Activity.this);
        dlg.setOnResultEventListener(new ExCancelDialog.OnResultEventListener() {
            @Override
            public void ResultEvent(boolean result) {
                if (result){
                    finish();
                }
                else {
                }
            }
        });
        dlg.showDialog();
    }
}
