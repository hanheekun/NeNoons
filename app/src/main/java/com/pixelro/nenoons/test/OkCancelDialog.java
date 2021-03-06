package com.pixelro.nenoons.test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.pixelro.nenoons.R;

public class OkCancelDialog {

    private Context context;
    private String title;

    public OkCancelDialog(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    public interface OnResultEventListener {
        public void ResultEvent(boolean result);
    }

    private OnResultEventListener onResultEventListener;

    public void setOnResultEventListener(OnResultEventListener listener){

        onResultEventListener = listener;

    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void showDialog() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.getWindow().setDimAmount(0.7f);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_ok_cancel);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button okButton = (Button) dlg.findViewById(R.id.button_dialog_ok_cancel_ok);
        final Button cancelButton = (Button) dlg.findViewById(R.id.button_dialog_ok_cancel_cancel);
        final TextView tvTitle = (TextView) dlg.findViewById(R.id.textView_dialog_ok_cancel_title);
        tvTitle.setText(title);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "확인 했습니다.", Toast.LENGTH_SHORT).show();

                onResultEventListener.ResultEvent(true);

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                onResultEventListener.ResultEvent(false);

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });


    }
}