package com.pixelro.eyelab.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;

public class AccountTosFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountTosFragment.class.getSimpleName();
    private View mView;

    private ImageView IvTos1Expand;
    private ImageView IvTos2Expand;
    private ImageView IvTos3Expand;
    private ImageView IvTos4Expand;
    private LinearLayout SvTos1;
    private LinearLayout SvTos2;
    private LinearLayout SvTos3;
    private LinearLayout SvTos4;
    private LinearLayout LlTos1;
    private CheckBox CbTos1;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_tos, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_account_tos_next).setOnClickListener(this);

        IvTos1Expand = view.findViewById(R.id.imageView_tos_1_expand);
        IvTos2Expand = view.findViewById(R.id.imageView_tos_2_expand);
        IvTos3Expand = view.findViewById(R.id.imageView_tos_3_expand);
        IvTos4Expand = view.findViewById(R.id.imageView_tos_4_expand);
        IvTos1Expand.setOnClickListener(this);
        IvTos2Expand.setOnClickListener(this);
        IvTos3Expand.setOnClickListener(this);
        IvTos4Expand.setOnClickListener(this);
        SvTos1 = view.findViewById(R.id.scrollView_tos_1);
        SvTos2 = view.findViewById(R.id.scrollView_tos_2);
        SvTos3 = view.findViewById(R.id.scrollView_tos_3);
        SvTos4 = view.findViewById(R.id.scrollView_tos_4);
        LlTos1 = view.findViewById(R.id.linearLayout_tos_1_bg);
        CbTos1 = view.findViewById(R.id.checkBox_tos_1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_account_tos_next:
                NavHostFragment.findNavController(AccountTosFragment.this).navigate(R.id.action_navigation_account_tos_to_navigation_account_id);
                break;
            case R.id.imageView_tos_1_expand:
                if (SvTos1.getVisibility() == View.GONE){
                    SvTos1.setVisibility(View.VISIBLE);
                }
                else {
                    SvTos1.setVisibility(View.GONE);
                }
                break;
            case R.id.imageView_tos_2_expand:
                if (SvTos2.getVisibility() == View.GONE){
                    SvTos2.setVisibility(View.VISIBLE);
                }
                else {
                    SvTos2.setVisibility(View.GONE);
                }
                break;
            case R.id.imageView_tos_3_expand:
                if (SvTos3.getVisibility() == View.GONE){
                    SvTos3.setVisibility(View.VISIBLE);
                }
                else {
                    SvTos3.setVisibility(View.GONE);
                }
                break;
            case R.id.imageView_tos_4_expand:
                if (SvTos4.getVisibility() == View.GONE){
                    SvTos4.setVisibility(View.VISIBLE);
                }
                else {
                    SvTos4.setVisibility(View.GONE);
                }
                break;
        }
    }
}
