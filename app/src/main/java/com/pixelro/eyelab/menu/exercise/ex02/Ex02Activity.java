package com.pixelro.eyelab.menu.exercise.ex02;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.exercise.ExCancelDialog;
import com.pixelro.eyelab.menu.exercise.ex01.Ex01AFragment;

public class Ex02Activity extends AppCompatActivity {

    public final static int EX_LEVEL_L = 0;
    public final static int EX_LEVEL_M = 1;
    public final static int EX_LEVEL_H = 2;

    public int curLevel = EX_LEVEL_L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_02);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_ex_02, new Ex02AFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        ExCancelDialog dlg = new ExCancelDialog(Ex02Activity.this);
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
