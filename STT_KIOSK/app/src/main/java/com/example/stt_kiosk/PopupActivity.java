package com.example.stt_kiosk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PopupActivity extends Activity {
    DecimalFormat formatter = new DecimalFormat("###,###");
    DialogDessert dialogDessert;
    DialogDrink dialogDrink;
    DialogDetail dialogDetail;
    DialogTopping dialogTopping;

    TextView popupName;
    TextView popupPrice;
    TextView popupExp;
    ImageView popupImg;
    ArrayList<ListViewBtnItem> items;
    ListViewBtnAdapter list_adapter;
    Button detail_btn;
    Button dessert_change;
    Button drink_change;
    Button topping_change;

    public static TextView dessert_name;
    public TextView dessert_price;
    public static TextView drink_name;
    public TextView drink_price;
    public static TextView topping_name;
    public TextView topping_price;

    static int dessert_save;
    static int drink_save;
    static int[] topping_save;
    public static ArrayList<String> nutrient_save;
    public static String allergy_save;
    public static String origin_save;

    static String des_name;
    static String des_price;
    static String dri_name;
    static String dri_price;

    public static TextView total_price;
    String total_str;
    public static int total_int;
    public static int t_price;
    public static String cat;

    LinearLayout popup_dessert;
    LinearLayout popup_drink;
    LinearLayout popup_topping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        dessert_name = (TextView) findViewById(R.id.dessert_selected);
        dessert_price = (TextView) findViewById(R.id.dessert_price);
        drink_name = (TextView) findViewById(R.id.drink_selected);
        drink_price = (TextView) findViewById(R.id.drink_price);
        topping_name = (TextView) findViewById(R.id.topping_selected);
        topping_price = (TextView) findViewById(R.id.topping_price);

        //UI 객체생성
        popupName = (TextView)findViewById(R.id.popup_name);
        popupPrice = (TextView)findViewById(R.id.popup_price);
        popupExp = (TextView) findViewById(R.id.popup_exp);
        popupImg = findViewById(R.id.popup_img);
        //데이터 가져오기
        Intent intent = getIntent();
        byte[] arr = intent.getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        popupImg.setImageBitmap(image);
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String exp = intent.getStringExtra("exp");
//        nutrient_save = new ArrayList<>();
        nutrient_save = intent.getStringArrayListExtra("nutrient");
        cat = intent.getStringExtra("cat");
        allergy_save = intent.getStringExtra("allergy");
        origin_save = intent.getStringExtra("origin");
        popupName.setText(name);
        popupPrice.setText(price);
        popupExp.setText(exp);

        popup_dessert = findViewById(R.id.popup_dessert);
        popup_drink = findViewById(R.id.popup_drink);
        popup_topping = findViewById(R.id.popup_topping);

        if(cat.equals("버거")){
            dessert_name.setText(null);
            drink_name.setText(null);
            popup_dessert.setVisibility(View.GONE);
            popup_drink.setVisibility(View.GONE);
            popup_topping.setVisibility(View.VISIBLE);
        }else if(cat.equals("세트")){
            popup_dessert.setVisibility(View.VISIBLE);
            popup_drink.setVisibility(View.VISIBLE);
            popup_topping.setVisibility(View.VISIBLE);
        }else {
            dessert_name.setText(null);
            drink_name.setText(null);
            topping_name.setText(null);
            popup_dessert.setVisibility(View.GONE);
            popup_drink.setVisibility(View.GONE);
            popup_topping.setVisibility(View.GONE);
        }

        dessert_change = findViewById(R.id.dessert_change);
        drink_change = findViewById(R.id.drink_change);
        topping_change = findViewById(R.id.topping_change);

        detail_btn = findViewById(R.id.detail_btn);

        dessert_save = R.id.dessert_menu1;
        drink_save = R.id.drink_menu1;
        topping_save = new int[]{0, 0, 0, 0};

        total_price = findViewById(R.id.total_price);
        total_str = popupPrice.getText().toString();
        total_str = total_str.substring(0,total_str.length()-1);
        total_str = total_str.replace(",", "");
//        String price_str[] = total_str.split(",");
//        total_str = "";
//        for(int i=0; i < price_str.length; i++){
//            total_str += price_str[i];
//        }
        total_int = Integer.parseInt(total_str);
        total_price.setText(formatter.format(total_int)+"원");
    }

    //취소 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }

    //확인 버튼 클릭
    public void mOnOK(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "OK Popup");
        setResult(RESULT_OK, intent);

        ListViewBtnItem item;
        item = new ListViewBtnItem();
        String Name_item = (String) popupName.getText().toString();
        String Price_item = (String) popupPrice.getText().toString();
        items = (ArrayList<ListViewBtnItem>) MainActivity.getList();
        item.setMenu(Name_item);
        item.setNumber("1");
        item.setPrice(Price_item);
        items.add(item);
        MainActivity.setList(items);
        list_adapter = MainActivity.getList_adapter();
        list_adapter.notifyDataSetChanged();
        MainActivity.setList_adapter(list_adapter);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void onChangeClicked(View v) {
        switch (v.getId()){
            case R.id.dessert_change :
                dialogDessert = new DialogDessert(this);
                dialogDessert.setDialogListener(new DialogDessert.DialogDessertListener() {
                    @Override
                    public void onApplyClicked(String name, String price, int save) {
                        dessert_name.setText(name);
                        if(name.equals("양념감자(어니언)") || name.equals("화이어윙(2조각)") || name.equals("치킨휠레(2조각)")
                                || name.equals("지파이 고소한맛") || name.equals("지파이 하바네로") || name.equals("쉑쉑치킨(어니언)")){
                            dessert_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        }else if(name.equals("선데아이스크림(딸기)") || name.equals("토네이도(초코쿠키)")){
                            dessert_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        }else{
                            dessert_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                        }
                        dessert_price.setText("+"+price+"원");
                        dessert_save = save;
                        addPrice();
                    }
                });
                dialogDessert.setCancelable(false);
                dialogDessert.show();
                break;
            case R.id.drink_change :
                dialogDrink = new DialogDrink(this);
                dialogDrink.setDialogListener(new DialogDrink.DialogDrinkListener() {

                    @Override
                    public void onApplyClicked(String name, String price, int save) {
                        drink_name.setText(name);
                        drink_price.setText("+"+price+"원");
                        drink_save = save;
                        addPrice();
                    }
                });
                dialogDrink.setCancelable(false);
                dialogDrink.show();
                break;
            case R.id.detail_btn :
                dialogDetail = new DialogDetail(this);
                dialogDetail.setCancelable(false);
                dialogDetail.show();
                break;

            case R.id.topping_change :
                dialogTopping = new DialogTopping(this);
                dialogTopping.setDialogListener(new DialogTopping.DialogToppingListener() {

                    @Override
                    public void onApplyClicked(String name, String price, int c_bacon, int c_tomato, int c_cheese, int c_beef) {
                        topping_name.setText(name);
                        topping_price.setText("+"+price+"원");
                        topping_save[0] = c_bacon;
                        topping_save[1] = c_tomato;
                        topping_save[2] = c_cheese;
                        topping_save[3] = c_beef;
                        addPrice();
                    }
                });
                dialogTopping.setCancelable(false);
                dialogTopping.show();
                break;
        }
    }

    public static int getDessertR() {
        return dessert_save;
    }
    public static int getDrinkR() {
        return drink_save;
    }
    public static int getTopping(int idx) {
        return topping_save[idx];
    }
    public static String getNutrient(int idx) {
        return nutrient_save.get(idx);
    }
    public static String getAllergy(){
        return allergy_save;
    }
    public static String getOrigin(){
        return origin_save;
    }


    public void addPrice() {
        String dessert = (String) dessert_price.getText();
        String drink = (String) drink_price.getText();
        String topping = (String) topping_price.getText();
        String total = (String) popupPrice.getText();
        total_int = Integer.parseInt(total.substring(0,total.indexOf("원")).replace(",", ""));
        total_int += Integer.parseInt(dessert.substring(1, dessert.indexOf("원")).replace(",", ""));
        total_int += Integer.parseInt(drink.substring(1, drink.indexOf("원")).replace(",", ""));
        total_int += Integer.parseInt(topping.substring(1, topping.indexOf("원")).replace(",", ""));
        total_price.setText(""+formatter.format(total_int)+"원");
    }
    public static TextView getTotalPrice(){
        return total_price;
    }
}