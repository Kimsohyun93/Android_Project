package com.example.s2t_kiosk;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.Distribution;

import java.text.DecimalFormat;

public class DialogTopping extends Dialog {
    DecimalFormat formatter = new DecimalFormat("###,###");
    TextView price_topping;

    String name;
    String price;

    int p_bacon; int c_bacon=0;
    int p_tomato; int c_tomato=0;
    int p_cheese; int c_cheese=0;
    int p_patty; int c_patty=0;
    int p_topping=0;
    ImageView bacon_check;
    ImageView tomato_check;
    ImageView patty_check;
    ImageView cheese_check;

    private Context context;
    DialogTopping m_oDialog;

    private DialogToppingListener dialogToppingListener;

    public DialogTopping(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    interface DialogToppingListener{
        void onApplyClicked(String name, String price, int c_bacon, int c_tomato, int c_cheese, int c_patty);
//        void onNagativeClicked();
    }
    public void setDialogListener(DialogToppingListener dialogToppingListener){
        this.dialogToppingListener = dialogToppingListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.width = 580;
        lpWindow.height = 600;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_topping);

        m_oDialog = this;

        c_bacon = PopupActivity.getTopping(0);
        c_tomato = PopupActivity.getTopping(1);
        c_cheese = PopupActivity.getTopping(2);
        c_patty = PopupActivity.getTopping(3);

        price_topping = findViewById(R.id.price_topping);

        String getPrice = (String) ((TextView)findViewById(R.id.bacon_price)).getText();
        p_bacon = Integer.parseInt(getPrice);
        getPrice = (String) ((TextView)findViewById(R.id.tomato_price)).getText();
        p_tomato = Integer.parseInt(getPrice);
        getPrice = (String) ((TextView)findViewById(R.id.cheese_price)).getText();
        p_cheese = Integer.parseInt(getPrice);
        getPrice = (String) ((TextView)findViewById(R.id.patty_price)).getText();
        p_patty = Integer.parseInt(getPrice.replace(",", ""));

        p_topping = c_bacon * p_bacon + c_tomato * p_tomato + c_cheese * p_cheese + c_patty * p_patty;
        price_topping.setText(""+formatter.format(p_topping));
        LinearLayout btn1 = (LinearLayout)this.findViewById(R.id.bacon_btn);
        LinearLayout btn2 = (LinearLayout)this.findViewById(R.id.tomato_btn);
        LinearLayout btn3 = (LinearLayout)this.findViewById(R.id.patty_btn);
        LinearLayout btn4 = (LinearLayout)this.findViewById(R.id.cheese_btn);
        bacon_check = (ImageView) findViewById(R.id.bacon_check);
        tomato_check = (ImageView) findViewById(R.id.tomato_check);
        patty_check = (ImageView) findViewById(R.id.patty_check);
        cheese_check = (ImageView) findViewById(R.id.cheese_check);
        if(c_bacon == 1){
            bacon_check.setVisibility(View.VISIBLE);
        }
        if(c_tomato == 1){
            tomato_check.setVisibility(View.VISIBLE);
        }
        if(c_cheese ==1){
            cheese_check.setVisibility(View.VISIBLE);
        }
        if(c_patty == 1){
            patty_check.setVisibility(View.VISIBLE);
        }

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickTopping(v);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickTopping(v);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickTopping(v);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickTopping(v);
            }
        });
        Button oBtn = (Button)this.findViewById(R.id.topping_apply);
        oBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickBtn(v);
            }
        });
        Button oBtn1 = (Button)this.findViewById(R.id.topping_close_btn);
        oBtn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickClose(v);
            }
        });


    }

    public void onClickBtn(View v) {
        switch (v.getId()) {
            case R.id.topping_apply:
                name = "";
                int cnt = 0;
                if(c_bacon == 1){
                    name += "베이컨";
                    cnt++;
                }
                if(c_tomato == 1){
                    if(cnt > 0) name += ", ";
                    name += "토마토";
                    cnt++;
                }
                if(c_cheese == 1){
                    if(cnt > 0) name += ", ";
                    name += "엘치즈";
                    cnt++;
                }
                if(c_patty == 1){
                    if(cnt > 0) name += ", ";
                    name += "비프패티";
                    cnt++;
                }
                price = (String) price_topping.getText();
                dialogToppingListener.onApplyClicked(name, price, c_bacon, c_tomato, c_cheese, c_patty);
                this.dismiss();
                break;
        }
    }

    public void onClickClose(View v) {
        switch (v.getId()) {
            case R.id.topping_close_btn:
                this.dismiss();
        }
    }

    public void onClickTopping(View v){
        switch (v.getId()){
            case R.id.bacon_btn :
                if(c_bacon == 0){
                    c_bacon = 1;
                    bacon_check.setVisibility(View.VISIBLE);
                    p_topping += p_bacon;
                }
                else if(c_bacon == 1){
                    c_bacon = 0;
                    bacon_check.setVisibility(View.INVISIBLE);
                    p_topping -= p_bacon;
                }
                break;
            case R.id.tomato_btn :
                if(c_tomato == 0){
                    c_tomato = 1;
                    tomato_check.setVisibility(View.VISIBLE);
                    p_topping += p_tomato;
                }
                else if(c_tomato == 1){
                    c_tomato = 0;
                    tomato_check.setVisibility(View.INVISIBLE);
                    p_topping -= p_tomato;
                }
                break;
            case R.id.patty_btn :
                if(c_patty == 0){
                    c_patty = 1;
                    patty_check.setVisibility(View.VISIBLE);
                    p_topping += p_patty;
                }
                else if(c_patty == 1){
                    c_patty = 0;
                    patty_check.setVisibility(View.INVISIBLE);
                    p_topping -= p_patty;
                }
                break;
            case R.id.cheese_btn :
                if(c_cheese == 0){
                    c_cheese = 1;
                    cheese_check.setVisibility(View.VISIBLE);
                    p_topping += p_cheese;
                }
                else if(c_cheese == 1){
                    c_cheese = 0;
                    cheese_check.setVisibility(View.INVISIBLE);
                    p_topping -= p_cheese;
                }
                break;
        }
            price_topping.setText(formatter.format(p_topping));
    }

}
