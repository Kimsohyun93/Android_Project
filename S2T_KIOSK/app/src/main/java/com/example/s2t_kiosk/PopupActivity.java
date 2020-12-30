package com.example.s2t_kiosk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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


import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.s2t_kiosk.MainActivity.list_adapter;

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
    ArrayList<ListViewBtnItem> order_items;
    ListViewBtnAdapter list_adapter;
    ImageView detail_btn;

    public static TextView dessert_name;
    public String dessert_price = "0";
    public static TextView drink_name;
    public String drink_price = "0";
    public static TextView topping_name;
    public String topping_price = "0";

    static int dessert_save;
    static int dessert_check;
    static int drink_save;
    static int drink_check;
    static int[] topping_save;
    public static ArrayList<String> nutrient_save;
    public static String allergy_save;
    public static String origin_save;

    static String des_name;
    static String des_price;
    static String dri_name;
    static String dri_price;

    public static TextView total_price;
    public static TextView total_cnt;
    String total_str;
    int cnt;
    public static int total_int;
    public static int t_price;
    public static String cat;

    LinearLayout popup_dessert;
    LinearLayout popup_drink;
    LinearLayout popup_topping;
    LinearLayout popup_option;

    View viewDessert;
    View viewDrink;

    ByteArrayOutputStream stream;
    byte[] byteArray;

    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dessert_name = (TextView) findViewById(R.id.dessert_selected);
        drink_name = (TextView) findViewById(R.id.drink_selected);
        topping_name = (TextView) findViewById(R.id.topping_selected);

        //UI 객체생성
        popupName = (TextView)findViewById(R.id.popup_name);
        popupPrice = (TextView)findViewById(R.id.popup_price);
        popupExp = (TextView) findViewById(R.id.popup_exp);
        popupImg = (ImageView) findViewById(R.id.popup_img);
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
        popup_option = findViewById(R.id.popup_option);
        viewDessert = findViewById(R.id.viewDessert);
        viewDrink = findViewById(R.id.viewDrink);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();


        if(cat.equals("버거")){
            popup_option.setVisibility(View.VISIBLE);
            dessert_name.setText(null);
            drink_name.setText(null);
            popup_dessert.setVisibility(View.GONE);
            viewDessert.setVisibility(View.GONE);
            popup_drink.setVisibility(View.GONE);
            viewDrink.setVisibility(View.GONE);
            popup_topping.setVisibility(View.VISIBLE);
            width = (int) (dm.widthPixels * 0.8); // Display 사이즈의 90%
            height = (int) (dm.heightPixels * 0.5); // Display 사이즈의 90%

        }else if(cat.equals("세트")){
            popup_option.setVisibility(View.VISIBLE);
            popup_dessert.setVisibility(View.VISIBLE);
            viewDessert.setVisibility(View.VISIBLE);
            popup_drink.setVisibility(View.VISIBLE);
            viewDrink.setVisibility(View.VISIBLE);
            popup_topping.setVisibility(View.VISIBLE);
            width = (int) (dm.widthPixels * 0.8); // Display 사이즈의 90%
            height = (int) (dm.heightPixels * 0.7); // Display 사이즈의 90%

        }else {
            popup_option.setVisibility(View.GONE);
            width = (int) (dm.widthPixels * 0.8); // Display 사이즈의 90%
            height = (int) (dm.heightPixels * 0.4); // Display 사이즈의 90%
        }
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        detail_btn = findViewById(R.id.detail_btn);

        dessert_save = R.id.dessert_menu0;
        dessert_check = R.id.dessert_check0;
        drink_save = R.id.drink_menu0;
        drink_check = R.id.drink_check0;
        topping_save = new int[]{0, 0, 0, 0};

        total_price = findViewById(R.id.total_price);
        total_str = popupPrice.getText().toString();
        total_str = total_str.replace(",", "");
        total_str = total_str.replace("원", "");
        total_int = Integer.parseInt(total_str);
        total_price.setText(""+formatter.format(total_int));
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
        stream = new ByteArrayOutputStream();
        item = new ListViewBtnItem();
        String Name_item = (String) popupName.getText().toString();
        String Price_item = (String) popupPrice.getText().toString();
        BitmapDrawable order_image = (BitmapDrawable) popupImg.getDrawable();
        Bitmap bitmap = order_image.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byteArray = stream.toByteArray();
        items = (ArrayList<ListViewBtnItem>) MainActivity.getList();
        item.setImg(byteArray);
        item.setMenu(Name_item);
        item.setNumber("1");
        item.setPrice(Price_item);
        items.add(item);
        MainActivity.setList(items);
        list_adapter = MainActivity.getList_adapter();
        list_adapter.notifyDataSetChanged();
        MainActivity.setList_adapter(list_adapter);
//        cnt++;
//        total_cnt.setText(cnt);

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
            case R.id.popup_dessert :
                dialogDessert = new DialogDessert(this);
                dialogDessert.setDialogListener(new DialogDessert.DialogDessertListener() {
                    @Override
                    public void onApplyClicked(String name, String price, int save, int checked) {
                        dessert_name.setText(name);
                        dessert_price = price.replaceAll("원", "");
                        dessert_save = save;
                        dessert_check = checked;
                        addPrice();
                    }
                });
                dialogDessert.setCancelable(false);
                dialogDessert.show();
                break;
            case R.id.popup_drink :
                dialogDrink = new DialogDrink(this);
                dialogDrink.setDialogListener(new DialogDrink.DialogDrinkListener() {

                    @Override
                    public void onApplyClicked(String name, String price, int save, int checked) {
                        drink_name.setText(name);
                        drink_price = price.replaceAll("원", "");
                        drink_save = save;
                        drink_check = checked;
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

            case R.id.popup_topping :
                dialogTopping = new DialogTopping(this);
                dialogTopping.setDialogListener(new DialogTopping.DialogToppingListener() {

                    @Override
                    public void onApplyClicked(String name, String price, int c_bacon, int c_tomato, int c_cheese, int c_beef) {
                        topping_name.setText(name);
                        topping_price = price;
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
    public static int getDessertCheck() {return dessert_check; }
    public static int getDrinkR() {
        return drink_save;
    }
    public static int getDrinkCheck() {return drink_check; }
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
        String total = (String) popupPrice.getText();
        total = total.replaceAll("원", "");
        total_int = Integer.parseInt(total.replace(",", ""));
        total_int += Integer.parseInt(dessert_price.replace(",", ""));
        total_int += Integer.parseInt(drink_price.replace(",", ""));
        total_int += Integer.parseInt(topping_price.replace(",", ""));
        total_price.setText(""+formatter.format(total_int));
    }
    public static TextView getTotalPrice(){
        return total_price;
    }
}