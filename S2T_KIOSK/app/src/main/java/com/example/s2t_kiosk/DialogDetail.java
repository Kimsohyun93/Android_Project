package com.example.s2t_kiosk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DialogDetail extends Dialog {
    DialogDetail m_oDialog;
    public DialogDetail(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }
    DecimalFormat formatter = new DecimalFormat("###,###");
    public static TextView weight_content;
    public static TextView calorie_content;
    public static TextView protein_content;
    public static TextView natrium_content;
    public static TextView sugar_content;
    public static TextView fat_content;
    public static TextView allergy_content;
    public static TextView origin_content;

    String totalweight = com.example.s2t_kiosk.PopupActivity.getNutrient(0);
    String calorie = com.example.s2t_kiosk.PopupActivity.getNutrient(1);
    String protein= com.example.s2t_kiosk.PopupActivity.getNutrient(2);
    String natrium = com.example.s2t_kiosk.PopupActivity.getNutrient(3);
    String sugar = com.example.s2t_kiosk.PopupActivity.getNutrient(4);
    String fat = com.example.s2t_kiosk.PopupActivity.getNutrient(5);
    String allergy = com.example.s2t_kiosk.PopupActivity.getAllergy();
    String origin = com.example.s2t_kiosk.PopupActivity.getOrigin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.width = 500;
        lpWindow.height = 780;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_detail);
        weight_content = (TextView) findViewById(R.id.weight_content);
        calorie_content = (TextView) findViewById(R.id.calorie_content);
        protein_content = (TextView) findViewById(R.id.protein_content);
        natrium_content = (TextView) findViewById(R.id.natrium_content);
        sugar_content = (TextView) findViewById(R.id.sugar_content);
        fat_content = (TextView) findViewById(R.id.fat_content);
        allergy_content = (TextView) findViewById(R.id.allergy_content);
        origin_content = (TextView) findViewById(R.id.origin_content);

        m_oDialog = this;

        weight_content.setText(""+formatter.format(Integer.parseInt(totalweight)));
        calorie_content.setText(""+formatter.format(Integer.parseInt(calorie)));
        protein_content.setText(""+formatter.format(Integer.parseInt(protein)));
        natrium_content.setText(""+formatter.format(Integer.parseInt(natrium)));
        sugar_content.setText(""+formatter.format(Integer.parseInt(sugar)));
        fat_content.setText(""+formatter.format(Integer.parseInt(fat)));
        LinearLayout allergy_layout = (LinearLayout) findViewById(R.id.allergy_layout);
        LinearLayout origin_layout = (LinearLayout) findViewById(R.id.origin_layout);
        if(allergy.equals("-")){
            allergy_layout.setVisibility(View.GONE);
        }
        else{
            allergy_layout.setVisibility(View.VISIBLE);
            allergy_content.setText(allergy);
        }
        if(origin.equals("-")){
            origin_layout.setVisibility(View.GONE);
        }
        else{
            origin_layout.setVisibility(View.VISIBLE);
            origin_content.setText(origin);
        }

        ImageButton oBtn = (ImageButton)this.findViewById(R.id.close_btn);
        oBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickBtn(v);
            }
        });
    }
    public void onClickBtn(View _oView) {
        this.dismiss();
    }

}