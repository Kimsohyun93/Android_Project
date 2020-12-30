package com.example.stt_kiosk;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class DialogHelp extends Dialog {
    //    Context context;
    DialogHelp m_oDialog;

    public DialogHelp(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.width = 500;
        lpWindow.height = 780;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_help);

        m_oDialog = this;

        ImageButton oBtn = (ImageButton)this.findViewById(R.id.close_btn);
        oBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClickBtn(v);
            }
        });
    }
    public void onClickBtn(View _oView) {
        this.dismiss();
    }
}