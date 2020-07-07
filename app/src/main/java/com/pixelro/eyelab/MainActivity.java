package com.pixelro.eyelab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixelro.eyelab.menu.exercise.ExerciseFragment;
import com.pixelro.eyelab.menu.home.HomeFragment;
import com.pixelro.eyelab.menu.my.MyFragment;
import com.pixelro.eyelab.test.TestActivity;
import com.pixelro.eyelab.test.TestDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private long mBackKeyPressedTime = 0;
    private SharedPreferences sharedPreferences;

    private FragmentManager fragmentManager;
    private Fragment fa, fb, fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                //R.id.navigation_home, R.id.navigation_care, R.id.navigation_my)
//                R.id.navigation_home, R.id.navigation_my)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_test_fragment);
//        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        sharedPreferences = getSharedPreferences(EYELAB.APPDATA.NAME_LOGIN,MODE_PRIVATE);
        if (sharedPreferences.getBoolean(EYELAB.APPDATA.LOGIN.FIRST_LOGIN,true)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(EYELAB.APPDATA.LOGIN.FIRST_LOGIN, false);
            editor.commit();

            FirstDialog dlg = new FirstDialog(this);
            dlg.setOnResultEventListener(new FirstDialog.OnResultEventListener() {
                @Override
                public void ResultEvent(boolean result) {
                    if (result){
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        startActivity(intent);
                    }
                }
            });
            dlg.showDialog();
        }

        fa = new HomeFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_test_fragment, fa).commit();

    }

    @Override
    public void onBackPressed() {

        // 두번 눌러 종료 추가
        if (System.currentTimeMillis() > mBackKeyPressedTime + 2000) {
            mBackKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        (new Handler()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        finishAffinity();
                    }
                }
                , 200);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                if(fa == null) {
                    fa = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.nav_test_fragment, fa).commit();
                }

                if(fa != null) fragmentManager.beginTransaction().show(fa).commit();
                if(fb != null) fragmentManager.beginTransaction().hide(fb).commit();
                if(fc != null) fragmentManager.beginTransaction().hide(fc).commit();

                ((HomeFragment)fa).refrashBoard();

                return true;
            case R.id.navigation_exercise:
                if(fb == null) {
                    fb = new ExerciseFragment();
                    fragmentManager.beginTransaction().add(R.id.nav_test_fragment, fb).commit();
                }

                if(fa != null) fragmentManager.beginTransaction().hide(fa).commit();
                if(fb != null) fragmentManager.beginTransaction().show(fb).commit();
                if(fc != null) fragmentManager.beginTransaction().hide(fc).commit();
                return true;
            case R.id.navigation_my:
                if(fc == null) {
                    fc = new MyFragment();
                    fragmentManager.beginTransaction().add(R.id.nav_test_fragment, fc).commit();
                }

                if(fa != null) fragmentManager.beginTransaction().hide(fa).commit();
                if(fb != null) fragmentManager.beginTransaction().hide(fb).commit();
                if(fc != null) fragmentManager.beginTransaction().show(fc).commit();
                return true;
        }

        return false;
    }
}
