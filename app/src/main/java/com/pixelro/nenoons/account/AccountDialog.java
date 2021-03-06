package com.pixelro.nenoons.account;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.pixelro.nenoons.R;

public class AccountDialog {

    private Context context;

    public AccountDialog(Context context) {
        this.context = context;
    }

    public AccountDialog(Context context, String message, String button) {
        this.context = context;
        showDialog(message, button);
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void showDialog(String message, String button) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.getWindow().setDimAmount(0.7f);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_account);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        TextView TvMessage =  (TextView)dlg.findViewById(R.id.textView_account_dialog);
        TvMessage.setText(message);
        Button BtnButton =  (Button)dlg.findViewById(R.id.button_dialog_next);
        BtnButton.setText(button);

        final Button okButton = (Button) dlg.findViewById(R.id.button_dialog_next);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "확인 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

    }
}