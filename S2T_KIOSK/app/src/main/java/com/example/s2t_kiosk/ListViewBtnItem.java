package com.example.s2t_kiosk;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.s2t_kiosk.PopupActivity.cat;
import static com.example.s2t_kiosk.PopupActivity.dessert_name;
import static com.example.s2t_kiosk.PopupActivity.drink_name;
import static com.example.s2t_kiosk.PopupActivity.topping_save;
import static com.example.s2t_kiosk.PopupActivity.total_int;

public class ListViewBtnItem {
    private byte[] img_list;
    private String menu_list;
    private String number_list;
    private String price_list;
    private String dessert_list = dessert_name.getText().toString();
    private String drink_list = drink_name.getText().toString();
//    private String topping_list = topping_name.getText().toString().replace("\n", " / ");
    private int[] topping_list = topping_save;
    public String cat_list = cat;
    public int real_price_list = total_int;
    public void setImg(byte[] img){ img_list = img; }
    public void setMenu(String text){
        menu_list = text;
    }

    public void setNumber(String text){
        number_list = text;
    }

    public void setPrice(String text){
        price_list = text;
    }


    public byte[] getImg() {return this.img_list; }

    public String getMenu(){
        return this.menu_list;
    }

    public String getNumber(){
        return this.number_list;
    }

    public String getPrice(){
        return this.price_list;
    }

    public String getDessert(){
        return this.dessert_list;
    }
    public String getDrink(){
        return this.drink_list;
    }
    public int[] getTopping() { return this.topping_list; }

    public boolean isEmptyTopping(int[] list){
        for(int i = 0; i<list.length ; i++){
            if(list[i] != 0)
                return false;
        }
        return true;
    }
    public int firstTopping(int[] list){
        for(int i = 0; i<list.length ; i++){
            if(list[i] != 0)
                return i;
        }
        return 100;
    }
    public void goneLayout(LinearLayout l){
        l.setVisibility(View.GONE);
    }
    public void visibleLayout(LinearLayout l){
        l.setVisibility(View.VISIBLE);
    }

    public void setOptionText(TextView t, String s){
        t.setText(s);
    }
}
