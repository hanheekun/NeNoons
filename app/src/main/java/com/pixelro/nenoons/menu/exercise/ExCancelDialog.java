package com.pixelro.nenoons.menu.exercise;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;

import org.w3c.dom.Text;

public class ExCancelDialog extends Fragment {

    protected SharedPreferencesManager mSm;
    private View mView;

    private Context context;
    // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
    final Dialog dlg;

    public ExCancelDialog(Context context) {
        this.context = context;
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dlg = new Dialog(context);

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

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.getWindow().setDimAmount(0.7f);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_ex_cancel);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        mSm = new SharedPreferencesManager(context);
        Button okButton = (Button) dlg.findViewById(R.id.button_exit_ok);
        Button cancelButton = (Button) dlg.findViewById(R.id.button_exit_cancel);
        TextView Tv_exit = (TextView) dlg.findViewById(R.id.textView5);

        Typeface face = mSm.getFontTypeface();
        okButton.setTypeface(face);
        cancelButton.setTypeface(face);
        Tv_exit.setTypeface(face);

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
                //Toast.makeText(context, "확인 했습니다.", Toast.LENGTH_SHORT).show();
                onResultEventListener.ResultEvent(false);
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}