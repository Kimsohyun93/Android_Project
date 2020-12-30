package com.example.s2t_kiosk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogRec extends Dialog {
    DialogRec m_oDialog;
    private Context context;

    private DialogRec.DialogRecListener1 dialogRecListener1;

    public DialogRec(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    interface DialogRecListener1{
        void onApplyClicked(String name, char star);
//        void onNagativeClicked();
    }
    public void setDialogListener1(DialogRec.DialogRecListener1 dialogRecListener1){
        this.dialogRecListener1 = dialogRecListener1;
    }

    private DialogRec.DialogRecListener2 dialogRecListener2;

    interface DialogRecListener2{
        void onCancelClicked(String name, char star);
    }
    public void setDialogListener2(DialogRec.DialogRecListener2 dialogRecListener2){
        this.dialogRecListener2 = dialogRecListener2;
    }

    //    String totalweight = com.example.s2t_kiosk.PopupActivity.getNutrient(0);
//    String calorie = com.example.s2t_kiosk.PopupActivity.getNutrient(1);
//    String protein= com.example.s2t_kiosk.PopupActivity.getNutrient(2);
//    String natrium = com.example.s2t_kiosk.PopupActivity.getNutrient(3);
//    String sugar = com.example.s2t_kiosk.PopupActivity.getNutrient(4);
//    String fat = com.example.s2t_kiosk.PopupActivity.getNutrient(5);
//    String allergy = com.example.s2t_kiosk.PopupActivity.getAllergy();
//    String origin = com.example.s2t_kiosk.PopupActivity.getOrigin();
    TextView star_title;
    public static char star;
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.width = 500;
        lpWindow.height = 780;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.rec_star);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);

        star = PopupActivityRec.star;

        if(star == '1'){
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.star_gray);
            star3.setImageResource(R.drawable.star_gray);
            star4.setImageResource(R.drawable.star_gray);
            star5.setImageResource(R.drawable.star_gray);
        }
        else if(star == '2'){
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.star);
            star3.setImageResource(R.drawable.star_gray);
            star4.setImageResource(R.drawable.star_gray);
            star5.setImageResource(R.drawable.star_gray);
        }
        else if(star == '3'){
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.star);
            star3.setImageResource(R.drawable.star);
            star4.setImageResource(R.drawable.star_gray);
            star5.setImageResource(R.drawable.star_gray);
        }
        else if(star == '4'){
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.star);
            star3.setImageResource(R.drawable.star);
            star4.setImageResource(R.drawable.star);
            star5.setImageResource(R.drawable.star_gray);
        }
        else {
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.star);
            star3.setImageResource(R.drawable.star);
            star4.setImageResource(R.drawable.star);
            star5.setImageResource(R.drawable.star);
        }



        m_oDialog = this;
        star_title = (TextView) findViewById(R.id.star_title);

        star_title.setText(PopupActivityRec.rec_title);

        Button cancel = (Button)this.findViewById(R.id.cancel_star);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogRecListener2.onCancelClicked(PopupActivityRec.rec_title, star);
                onClickBtn(v);
            }
        });

        Button ok = (Button)this.findViewById(R.id.ok_star);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRecListener1.onApplyClicked(PopupActivityRec.rec_title, star);
                onClickBtn(v);
            }
        });

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star = '1';
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star_gray);
                star3.setImageResource(R.drawable.star_gray);
                star4.setImageResource(R.drawable.star_gray);
                star5.setImageResource(R.drawable.star_gray);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star = '2';
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star_gray);
                star4.setImageResource(R.drawable.star_gray);
                star5.setImageResource(R.drawable.star_gray);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star = '3';
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star_gray);
                star5.setImageResource(R.drawable.star_gray);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star = '4';
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star_gray);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star = '5';
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);
            }
        });


    }

    public void onClickBtn(View _oView) {
        this.dismiss();
    }


    public static void setStar(char s){
        star = s;
    }

}