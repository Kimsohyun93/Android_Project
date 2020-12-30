package com.example.stt_kiosk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogDessert extends Dialog {
    TextView dessert_name;
    TextView dessert_price;
    String name;
    String price;

    int save_menu;
    int pre_menu;
    LinearLayout save_menu_layout;
    LinearLayout pre_menu_layout;

    private Context context;
    DialogDessert m_oDialog;

    private DialogDessertListener dialogDessertListener;

    public DialogDessert(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    interface DialogDessertListener{
        void onApplyClicked(String name, String price, int save);
//        void onNagativeClicked();
    }
    public void setDialogListener(DialogDessertListener dialogDessertListener){
        this.dialogDessertListener = dialogDessertListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.width = 750;
        lpWindow.height = 900;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_dessert);

        m_oDialog = this;

        ArrayList<LinearLayout> dessert_menu = new ArrayList<>();
        for(int i=1; i<21; i++){
            int menuID = context.getResources().getIdentifier("dessert_menu" + i, "id", "com.example.stt_kiosk");
            LinearLayout menuView = (LinearLayout) this.findViewById(menuID);
            menuView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    itemClicked(v);
                }
            });
            dessert_menu.add(menuView);
        }

        Button oBtn = (Button)this.findViewById(R.id.dessert_apply);
        oBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickBtn(v);
            }
        });
        ImageView oBtn1 = (ImageView)this.findViewById(R.id.dessert_close_btn);
        oBtn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickClose(v);
            }
        });

        save_menu = PopupActivity.getDessertR();
        pre_menu = PopupActivity.getDessertR();

        switch (save_menu){
            case R.id.dessert_menu1:
                selectDessertMenu(1);
                break;

            case R.id.dessert_menu2:
                selectDessertMenu(2);
                break;

            case R.id.dessert_menu3:
                selectDessertMenu(3);
                break;

            case R.id.dessert_menu4:
                selectDessertMenu(4);
                break;

            case R.id.dessert_menu5:
                selectDessertMenu(5);
                break;

            case R.id.dessert_menu6:
                selectDessertMenu(6);
                break;

            case R.id.dessert_menu7:
                selectDessertMenu(7);
                break;

            case R.id.dessert_menu8:
                selectDessertMenu(8);
                break;

            case R.id.dessert_menu9:
                selectDessertMenu(9);
                break;

            case R.id.dessert_menu10:
                selectDessertMenu(10);
                break;

            case R.id.dessert_menu11:
                selectDessertMenu(11);
                break;

            case R.id.dessert_menu12:
                selectDessertMenu(12);
                break;

            case R.id.dessert_menu13:
                selectDessertMenu(13);
                break;

            case R.id.dessert_menu14:
                selectDessertMenu(14);
                break;

            case R.id.dessert_menu15:
                selectDessertMenu(15);
                break;

            case R.id.dessert_menu16:
                selectDessertMenu(16);
                break;

            case R.id.dessert_menu17:
                selectDessertMenu(17);
                break;

            case R.id.dessert_menu18:
                selectDessertMenu(18);
                break;

            case R.id.dessert_menu19:
                selectDessertMenu(19);
                break;

            case R.id.dessert_menu20:
                selectDessertMenu(20);
                break;
        }
    }

    public void itemClicked(View v){
        switch (v.getId()){
            case R.id.dessert_menu1:
                selectDessertMenu(1);
                break;

            case R.id.dessert_menu2:
                selectDessertMenu(2);
                break;

            case R.id.dessert_menu3:
                selectDessertMenu(3);
                break;

            case R.id.dessert_menu4:
                selectDessertMenu(4);
                break;

            case R.id.dessert_menu5:
                selectDessertMenu(5);
                break;

            case R.id.dessert_menu6:
                selectDessertMenu(6);
                break;

            case R.id.dessert_menu7:
                selectDessertMenu(7);
                break;

            case R.id.dessert_menu8:
                selectDessertMenu(8);
                break;

            case R.id.dessert_menu9:
                selectDessertMenu(9);
                break;

            case R.id.dessert_menu10:
                selectDessertMenu(10);
                break;

            case R.id.dessert_menu11:
                selectDessertMenu(11);
                break;

            case R.id.dessert_menu12:
                selectDessertMenu(12);
                break;

            case R.id.dessert_menu13:
                selectDessertMenu(13);
                break;

            case R.id.dessert_menu14:
                selectDessertMenu(14);
                break;

            case R.id.dessert_menu15:
                selectDessertMenu(15);
                break;

            case R.id.dessert_menu16:
                selectDessertMenu(16);
                break;

            case R.id.dessert_menu17:
                selectDessertMenu(17);
                break;

            case R.id.dessert_menu18:
                selectDessertMenu(18);
                break;

            case R.id.dessert_menu19:
                selectDessertMenu(19);
                break;

            case R.id.dessert_menu20:
                selectDessertMenu(20);
                break;
        }
    }
    public void onClickBtn(View v) {
        switch (v.getId()) {
            case R.id.dessert_apply:
                dialogDessertListener.onApplyClicked(name, price, save_menu);
                this.dismiss();
                break;
        }
    }

    public void onClickClose(View v) {
        switch (v.getId()) {
            case R.id.dessert_close_btn:
                this.dismiss();
        }
    }

    public void selectDessertMenu(int idx){
        int dname_id = context.getResources().getIdentifier("dessert_name"+idx, "id", "com.example.stt_kiosk");
        int dprice_id = context.getResources().getIdentifier("dessert_price"+idx, "id", "com.example.stt_kiosk");
        dessert_name = (TextView) findViewById(dname_id);
        dessert_price = (TextView) findViewById(dprice_id);
        name = dessert_name.getText().toString();
        price = dessert_price.getText().toString();
        price = price.substring(2,price.indexOf("원"));
        save_menu = context.getResources().getIdentifier("dessert_menu"+idx, "id", "com.example.stt_kiosk");
        save_menu_layout = (LinearLayout)findViewById(save_menu);
        pre_menu_layout = (LinearLayout)findViewById(pre_menu);
        save_menu_layout.setBackgroundResource(R.drawable.select_border);
        if(pre_menu != save_menu){
            pre_menu_layout.setBackgroundColor(Color.argb(0,0,0,0));
            pre_menu = save_menu;
        }
    }
}
