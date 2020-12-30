package com.example.stt_kiosk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DialogTopping extends Dialog {
    DecimalFormat formatter = new DecimalFormat("###,###");
    TextView count_bacon; TextView cnt_bacon; ImageButton plus_bacon; ImageButton minus_bacon;
    TextView count_tomato; TextView cnt_tomato; ImageButton plus_tomato; ImageButton minus_tomato;
    TextView count_cheese; TextView cnt_cheese; ImageButton plus_cheese; ImageButton minus_cheese;
    TextView count_beef; TextView cnt_beef; ImageButton plus_beef; ImageButton minus_beef;
    TextView price_topping;

    String name;
    String price;

    int p_bacon; int c_bacon;
    int p_tomato; int c_tomato;
    int p_cheese; int c_cheese;
    int p_beef; int c_beef;
    int p_topping;


    private Context context;
    DialogTopping m_oDialog;

    private DialogToppingListener dialogToppingListener;

    public DialogTopping(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    interface DialogToppingListener{
        void onApplyClicked(String name, String price, int c_bacon, int c_tomato, int c_cheese, int c_beef);
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
        lpWindow.height = 1000;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_topping);

        m_oDialog = this;

        c_bacon = PopupActivity.getTopping(0);
        c_tomato = PopupActivity.getTopping(1);
        c_cheese = PopupActivity.getTopping(2);
        c_beef = PopupActivity.getTopping(3);

        count_bacon = findViewById(R.id.count_bacon); cnt_bacon = findViewById(R.id.cnt_bacon);
        count_tomato = findViewById(R.id.count_tomato); cnt_tomato = findViewById(R.id.cnt_tomato);
        count_cheese = findViewById(R.id.count_cheese); cnt_cheese = findViewById(R.id.cnt_cheese);
        count_beef = findViewById(R.id.count_beef); cnt_beef = findViewById(R.id.cnt_beef);

        count_bacon.setText(""+c_bacon); cnt_bacon.setText(""+c_bacon);
        count_tomato.setText(""+c_tomato); cnt_tomato.setText(""+c_tomato);
        count_cheese.setText(""+c_cheese); cnt_tomato.setText(""+c_tomato);
        count_beef.setText(""+c_beef); cnt_beef.setText(""+c_beef);

        plus_bacon = findViewById(R.id.plus_bacon); minus_bacon = findViewById(R.id.minus_bacon);
        plus_tomato = findViewById(R.id.plus_tomato); minus_tomato = findViewById(R.id.minus_tomato);
        plus_cheese = findViewById(R.id.plus_cheese); minus_cheese = findViewById(R.id.minus_cheese);
        plus_beef = findViewById(R.id.plus_beef); minus_beef = findViewById(R.id.minus_beef);

        price_topping = findViewById(R.id.price_topping);

        String getPrice = (String) ((TextView)findViewById(R.id.price_bacon)).getText();
        p_bacon = Integer.parseInt((getPrice.substring(2,getPrice.indexOf("원"))).replace(",", ""));
        getPrice = (String) ((TextView)findViewById(R.id.price_tomato)).getText();
        p_tomato = Integer.parseInt((getPrice.substring(2,getPrice.indexOf("원"))).replace(",", ""));
        getPrice = (String) ((TextView)findViewById(R.id.price_cheese)).getText();
        p_cheese = Integer.parseInt((getPrice.substring(2,getPrice.indexOf("원"))).replace(",", ""));
        getPrice = (String) ((TextView)findViewById(R.id.price_beef)).getText();
        p_beef = Integer.parseInt((getPrice.substring(2,getPrice.indexOf("원"))).replace(",", ""));

        p_topping = c_bacon * p_bacon + c_tomato * p_tomato + c_cheese * p_cheese + c_beef * p_beef;
        price_topping.setText(""+formatter.format(p_topping));

        plus_bacon.setOnClickListener(listener); minus_bacon.setOnClickListener(listener);
        plus_tomato.setOnClickListener(listener); minus_tomato.setOnClickListener(listener);
        plus_cheese.setOnClickListener(listener); minus_cheese.setOnClickListener(listener);
        plus_beef.setOnClickListener(listener); minus_beef.setOnClickListener(listener);

        Button oBtn = (Button)this.findViewById(R.id.topping_apply);
        oBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickBtn(v);
            }
        });
        ImageView oBtn1 = (ImageView)this.findViewById(R.id.topping_close_btn);
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
                if(c_bacon > 0){
                    name += "베이컨(" + c_bacon + ")";
                    cnt++;
                }
                if(c_tomato > 0){
                    if(cnt > 0) name += "\n";
                    name += "토마토(" + c_tomato + ")";
                    cnt++;
                }
                if(c_cheese > 0){
                    if(cnt > 0) name += "\n";
                    name += "엘치즈(" + c_cheese + ")";
                    cnt++;
                }
                if(c_beef > 0){
                    if(cnt > 0) name += "\n";
                    name += "비프패티(" + c_beef + ")";
                    cnt++;
                }
                price = (String) price_topping.getText();
                dialogToppingListener.onApplyClicked(name, price, c_bacon, c_tomato, c_cheese, c_beef);
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

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.plus_bacon :
                   count_bacon.setText(""+(++c_bacon));
                   cnt_bacon.setText(""+c_bacon);
                   p_topping += p_bacon;
                   break;
                case R.id.minus_bacon :
                    if(c_bacon > 0){
                        count_bacon.setText(""+(--c_bacon));
                        cnt_bacon.setText(""+c_bacon);
                        p_topping -= p_bacon;
                    }
                    break;
                case R.id.plus_tomato :
                    count_tomato.setText(""+(++c_tomato));
                    cnt_tomato.setText(""+c_tomato);
                    p_topping += p_tomato;
                    break;
                case R.id.minus_tomato :
                    if(c_tomato > 0){
                        count_tomato.setText(""+(--c_tomato));
                        cnt_tomato.setText(""+c_tomato);
                        p_topping -= p_tomato;
                    }
                    break;
                case R.id.plus_cheese :
                    count_cheese.setText(""+(++c_cheese));
                    cnt_cheese.setText(""+c_cheese);
                    p_topping += p_cheese;
                    break;
                case R.id.minus_cheese :
                    if(c_cheese > 0){
                        count_cheese.setText(""+(--c_cheese));
                        cnt_cheese.setText(""+c_cheese);
                        p_topping -= p_cheese;
                    }
                    break;
                case R.id.plus_beef :
                    count_beef.setText(""+(++c_beef));
                    cnt_beef.setText(""+c_beef);
                    p_topping += p_beef;
                    break;
                case R.id.minus_beef :
                    if(c_beef > 0){
                        count_beef.setText(""+(--c_beef));
                        cnt_beef.setText(""+c_beef);
                        p_topping -= p_beef;
                    }
                    break;
            }
            price_topping.setText(formatter.format(p_topping));
        }
    };
}
