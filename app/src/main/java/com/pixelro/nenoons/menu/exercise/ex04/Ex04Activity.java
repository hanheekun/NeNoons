package com.pixelro.nenoons.menu.exercise.ex04;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.menu.exercise.ExCancelDialog;

public class Ex04Activity extends AppCompatActivity {

    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

    public int mCurLevel = EX_LEVEL_L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_04);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_ex_04, new Ex04AFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        ExCancelDialog dlg = new ExCancelDialog(Ex04Activity.this);
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
