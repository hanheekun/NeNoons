package com.pixelro.nenoons.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.PersonalProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.account.AccountHelloFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Test07ResultFragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;

    private TextView TvAge;
    private TextView TvResult;
    private int mDistance;

    private Button BtnReturn;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TestProfile mTestProfile;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_07_result, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mDistance = ((TestActivity)getActivity()).mCurrentDistance;

        TvAge = (TextView)mView.findViewById(R.id.textView_test_09_age);
        TvResult = (TextView)mView.findViewById(R.id.textView_test_09_result);

        BtnReturn = (Button)view.findViewById(R.id.button_test_07_return);
        BtnReturn.setOnClickListener(this);

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_TEST,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(EYELAB.APPDATA.TEST.LAST_DISTANCE,mDistance);
        editor.commit();

        mTestProfile = ((TestActivity)getActivity()).mTestProfile;

        // 거리에 따른 나이
        if (mDistance <= 22){
            TvAge.setText("노안나이 44세 이하");
        }
        else if(mDistance >= 23 && mDistance <= 30){
            TvAge.setText("노안나이 40대 중반");
        }
        else if(mDistance >= 31 && mDistance <= 37){
            TvAge.setText("노안나이 40대 후반");
        }
        else if(mDistance >= 38 && mDistance <= 47){
            TvAge.setText("노안나이 50세");
        }
        else if(mDistance >= 48 && mDistance <= 57){
            TvAge.setText("노안나이 50대 초반");
        }
        else if(mDistance >= 58 && mDistance <= 67){
            TvAge.setText("노안나이 50대 중반");
        }
        else if(mDistance >= 68){
            TvAge.setText("노안나이 56세 이상");
        }
//        if (mDistance <= 23){
//            TvAge.setText("노안나이 40대 이하");
//        }
//        else if(mDistance >= 24 && mDistance <= 28){
//            TvAge.setText("노안나이 40대 중반");
//        }
//        else if(mDistance >= 29 && mDistance <= 34){
//            TvAge.setText("노안나이 50대 초반");
//        }
//        else if(mDistance >= 35 && mDistance <= 45){
//            TvAge.setText("노안나이 50대 중반");
//        }
//        else if(mDistance >= 46){
//            TvAge.setText("노안나이 60대 이상");
//        }

        // 거리에 따른 설명
        if (mDistance <= 20){
            TvResult.setText("눈건강 상태가 좋습니다.\n항상 지금과 같은 상태를 유지하세요");
        }
        else if(mDistance >= 21 && mDistance <= 25){
            TvResult.setText("노안이 시작될 수 있습니다.\n항상 눈건강에 유의하세요");
        }
        else if(mDistance >= 26 && mDistance <= 30){
            TvResult.setText("노안이 시작되어 보입니다.\n항상 눈건강에 유의하세요");
        }
        else if(mDistance >= 31 && mDistance <= 40){
            TvResult.setText("노안이 진행중으로 보입니다.\n항상 눈건강에 유의하세요");
        }
        else if(mDistance >= 41 && mDistance <= 50){
            TvResult.setText("노안이 많이 진행 되어 보입니다.\n항상 눈건강에 유의하세요");
        }
        else if(mDistance >= 51){
            TvResult.setText("노안이 심하므로,\n안경원, 안과를 추천드립니다.");
        }

        // 서버연결 20200715

        /////////////////////////////////////////////////////////////////////////
        // 측정 기록 전송
        /////////////////////////////////////////////////////////////////////////
        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatNowDate = new SimpleDateFormat("yyyyMMddHHmmss");
        mTestProfile.date = formatNowDate.format(nowDate);

        String token = getToken(getActivity());

        // TestProfile 전송



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_test_07_return:
                getActivity().finish();
                break;
        }

    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }
}
