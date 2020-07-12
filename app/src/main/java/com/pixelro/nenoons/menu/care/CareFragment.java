package com.pixelro.nenoons.menu.care;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.menu.care.o2o.O2OActivity;
import com.pixelro.nenoons.menu.home.AddressWebViewActivity;

import java.io.IOException;
import java.util.List;

public class CareFragment extends Fragment implements View.OnClickListener {

    private CareViewModel careViewModel;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    private TextView TvAdress;

    Geocoder geocoder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        careViewModel =
                ViewModelProviders.of(this).get(CareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_care, container, false);

        root.findViewById(R.id.button_care_o2o).setOnClickListener(this);
        root.findViewById(R.id.button_care_address).setOnClickListener(this);

        TvAdress = (TextView)root.findViewById(R.id.textView_care_address);

        //root.findViewById(R.id.button_care_info).setOnClickListener(this);


//        final TextView textView = root.findViewById(R.id.text_care);
//        careViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        geocoder = new Geocoder(getActivity().getApplicationContext());

        return root;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_care_o2o:
                Intent intent = new Intent(getActivity(), O2OActivity.class);
                startActivity(intent);
                break;
            case R.id.button_care_address:
                Intent i = new Intent(getContext(), AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                break;
            //case R.id.button_care_info:

                //break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){

            case SEARCH_ADDRESS_ACTIVITY:

                if(resultCode == Activity.RESULT_OK){

                    String data = intent.getExtras().getString("data");
                    if (data != null)

                        data = data.substring(data.indexOf(',')+2,data.lastIndexOf('(')-1);



                        List<Address> list = null;

                    try {
                        list = geocoder.getFromLocationName(
                                data, // 지역 이름
                                10); // 읽을 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                            TvAdress.setText("해당되는 주소 정보는 없습니다");
                        } else {
                            TvAdress.setText(list.get(0).getLatitude() + ", " + list.get(0).getLongitude());
                            //          list.get(0).getCountryName();  // 국가명
                            //          list.get(0).getLatitude();        // 위도
                            //          list.get(0).getLongitude();    // 경도

                        }
                    }

                }
                break;

        }

    }
}
