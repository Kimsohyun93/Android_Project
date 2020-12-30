package com.example.s2t_kiosk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogDrink extends Dialog {
    TextView drink_name;
    TextView drink_price;
    String name;
    String price;

    int save_menu;
    int pre_menu;
    LinearLayout save_menu_layout;
    LinearLayout pre_menu_layout;

    int save_checked;
    int pre_checked;
    ImageView pre_menu_checked;
    ImageView save_menu_checked;

    private Context context;
    DialogDrink m_oDialog;

    private DialogDrinkListener dialogDrinkListener;

    public DialogDrink(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    interface DialogDrinkListener{
        void onApplyClicked(String name, String price, int save, int checked);
//        void onNagativeClicked();
    }
    public void setDialogListener(DialogDrinkListener dialogDrinkListener){
        this.dialogDrinkListener = dialogDrinkListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.width = 700;
        lpWindow.height = 1000;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_drink);

        m_oDialog = this;

        ArrayList<LinearLayout> drink_menu = new ArrayList<>();
        for(int i=0; i<20; i++){
            int menuID = context.getResources().getIdentifier("drink_menu" + i, "id", "com.example.s2t_kiosk");
            LinearLayout menuView = (LinearLayout) this.findViewById(menuID);
            menuView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) { itemClicked(v); }
            });
            drink_menu.add(menuView);
        }

        Button oBtn = (Button)this.findViewById(R.id.drink_apply);
        oBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickBtn(v);
            }
        });
        Button oBtn1 = (Button)this.findViewById(R.id.drink_close_btn);
        oBtn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickClose(v);
            }
        });

        save_menu = com.example.s2t_kiosk.PopupActivity.getDrinkR();
        save_checked = com.example.s2t_kiosk.PopupActivity.getDrinkCheck();
        pre_menu = com.example.s2t_kiosk.PopupActivity.getDrinkR();
        pre_checked = com.example.s2t_kiosk.PopupActivity.getDrinkCheck();

        switch (save_menu){
            case R.id.drink_menu0:
                selectDrinkMenu(0);
                break;

            case R.id.drink_menu1:
                selectDrinkMenu(1);
                break;

            case R.id.drink_menu2:
                selectDrinkMenu(2);
                break;

            case R.id.drink_menu3:
                selectDrinkMenu(3);
                break;

            case R.id.drink_menu4:
                selectDrinkMenu(4);
                break;

            case R.id.drink_menu5:
                selectDrinkMenu(5);
                break;

            case R.id.drink_menu6:
                selectDrinkMenu(6);
                break;

            case R.id.drink_menu7:
                selectDrinkMenu(7);
                break;

            case R.id.drink_menu8:
                selectDrinkMenu(8);
                break;

            case R.id.drink_menu9:
                selectDrinkMenu(9);
                break;

            case R.id.drink_menu10:
                selectDrinkMenu(10);
                break;

            case R.id.drink_menu11:
                selectDrinkMenu(11);
                break;

            case R.id.drink_menu12:
                selectDrinkMenu(12);
                break;

            case R.id.drink_menu13:
                selectDrinkMenu(13);
                break;

            case R.id.drink_menu14:
                selectDrinkMenu(14);
                break;

            case R.id.drink_menu15:
                selectDrinkMenu(15);
                break;

            case R.id.drink_menu16:
                selectDrinkMenu(16);
                break;

            case R.id.drink_menu17:
                selectDrinkMenu(17);
                break;

            case R.id.drink_menu18:
                selectDrinkMenu(18);
                break;

            case R.id.drink_menu19:
                selectDrinkMenu(19);
                break;
        }
    }

    public void itemClicked(View v){
        switch (v.getId()){
            case R.id.drink_menu0:
                selectDrinkMenu(0);
                break;
            case R.id.drink_menu1:
                selectDrinkMenu(1);
                break;

            case R.id.drink_menu2:
                selectDrinkMenu(2);
                break;

            case R.id.drink_menu3:
                selectDrinkMenu(3);
                break;

            case R.id.drink_menu4:
                selectDrinkMenu(4);
                break;

            case R.id.drink_menu5:
                selectDrinkMenu(5);
                break;

            case R.id.drink_menu6:
                selectDrinkMenu(6);
                break;

            case R.id.drink_menu7:
                selectDrinkMenu(7);
                break;

            case R.id.drink_menu8:
                selectDrinkMenu(8);
                break;

            case R.id.drink_menu9:
                selectDrinkMenu(9);
                break;

            case R.id.drink_menu10:
                selectDrinkMenu(10);
                break;

            case R.id.drink_menu11:
                selectDrinkMenu(11);
                break;

            case R.id.drink_menu12:
                selectDrinkMenu(12);
                break;

            case R.id.drink_menu13:
                selectDrinkMenu(13);
                break;

            case R.id.drink_menu14:
                selectDrinkMenu(14);
                break;

            case R.id.drink_menu15:
                selectDrinkMenu(15);
                break;

            case R.id.drink_menu16:
                selectDrinkMenu(16);
                break;

            case R.id.drink_menu17:
                selectDrinkMenu(17);
                break;

            case R.id.drink_menu18:
                selectDrinkMenu(18);
                break;

            case R.id.drink_menu19:
                selectDrinkMenu(19);
                break;
        }
    }
    public void onClickBtn(View v) {
        switch (v.getId()) {
            case R.id.drink_apply:
                dialogDrinkListener.onApplyClicked(name, price, save_menu, save_checked);
                this.dismiss();
                break;
        }
    }

    public void onClickClose(View v) {
        switch (v.getId()) {
            case R.id.drink_close_btn:
                this.dismiss();
        }
    }

    public void selectDrinkMenu(int idx){
        int dname_id = context.getResources().getIdentifier("drink_name"+idx, "id", "com.example.s2t_kiosk");
        int dprice_id = context.getResources().getIdentifier("drink_price"+idx, "id", "com.example.s2t_kiosk");
        drink_name = (TextView) findViewById(dname_id);
        drink_price = (TextView) findViewById(dprice_id);
        name = drink_name.getText().toString();
        price = drink_price.getText().toString();
        save_menu = context.getResources().getIdentifier("drink_menu"+idx, "id", "com.example.s2t_kiosk");
        save_menu_layout = (LinearLayout)findViewById(save_menu);
        pre_menu_layout = (LinearLayout)findViewById(pre_menu);
        save_checked = context.getResources().getIdentifier("drink_check"+idx, "id", "com.example.s2t_kiosk");
        save_menu_checked = findViewById(save_checked);
        pre_menu_checked = findViewById(pre_checked);
        save_menu_checked.setVisibility(View.VISIBLE);
        if(pre_menu != save_menu){
            pre_menu_checked.setVisibility(View.INVISIBLE);
            pre_menu = save_menu;
            pre_checked = save_checked;
        }
    }
}
