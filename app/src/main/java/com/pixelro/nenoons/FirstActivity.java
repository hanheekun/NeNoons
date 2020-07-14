package com.pixelro.nenoons;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FirstActivity extends BaseActivity{

    private static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViewById(R.id.btn_first_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 카메라 사용 권한 얻기
                int rc = ActivityCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.CAMERA);
                if (rc != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FirstActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);

                }

                // PACKAGE_USAGE_STATS 사용 권한 얻기
                rc = ActivityCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.PACKAGE_USAGE_STATS);
                if (rc != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FirstActivity.this, new String[]{Manifest.permission.PACKAGE_USAGE_STATS}, REQUEST_CAMERA);
                }


                if (!FirstActivity.this.hasPermission(FirstActivity.this)) {
                    Log.i("MainActivity", "The user may not allow the access to apps usage. ");
                    //Toast.makeText((Context)FirstActivity.this, (CharSequence)"Failed to retrieve app usage statistics. You may need to enable access for this app through Settings > Security > Apps with usage access", 1).show();
                    FirstActivity.this.startActivity(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
                }

                finish();
            }
        });

    }

    public static boolean hasPermission(@NonNull final Context context) {
        // Usage Stats is theoretically available on API v19+, but official/reliable support starts with API v21.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }

        final AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        if (appOpsManager == null) {
            return false;
        }

        final int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) {
            return false;
        }

        // Verify that access is possible. Some devices "lie" and return MODE_ALLOWED even when it's not.
        final long now = System.currentTimeMillis();
        final UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);
        return (stats != null && !stats.isEmpty());
    }


    boolean checkForPermission(){
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
