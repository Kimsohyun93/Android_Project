package com.example.s2t_kiosk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    public static ArrayList<ListViewBtnItem> items;
    public static OrderListAdapter list_adapter;
    boolean flag_step1 = false;
    boolean flag_step2 = false;
    String payment;
    AlertDialog.Builder builder;
    static TextView order_total_price;
    static TextView order_total_cnt;
    LinearLayout back_order;
    LinearLayout home_btn;
    LinearLayout call_btn;
    LinearLayout pack_btn;
    ImageView pack_img;
    ImageView card_img;
    ImageView cash_img;
    TextView wait_number;
    TextView order_list;
    int IsOff;
    String order_num;
    String sqlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        items = MainActivity.getList();

        ListView listview = (ListView) findViewById(R.id.list_order);
        list_adapter = new OrderListAdapter(this, R.layout.listview_order, items);
        listview.setAdapter(list_adapter);
        back_order = (LinearLayout) findViewById(R.id.back_order);
        home_btn = (LinearLayout) findViewById(R.id.home_btn);
        call_btn = (LinearLayout) findViewById(R.id.call_btn);
        pack_btn = (LinearLayout) findViewById(R.id.pack_btn);
        pack_img = (ImageView) findViewById(R.id.pack_img);
        order_total_price = (TextView) findViewById(R.id.order_total_price);
        order_total_cnt = (TextView) findViewById(R.id.order_total_cnt);
        IsOff = 1;
//        System.out.println(sqlString);

    }

    public void OrderPHP(){
        try {
            sqlString = OrderListAdapter.cart;
            System.out.println(sqlString);
            URLConnector order_request = new URLConnector(MainActivity.serverUrl+"order.php");
            String order_result = order_request.PhPtest(sqlString);
            order_num = order_result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void OrderNumber(View view){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_number,null);
        wait_number = (TextView) dialogView.findViewById(R.id.wait_number);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = 400;
        params.height = 400;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_radius);
        wait_number.setText(order_num);

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    OnclickClose(view);
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 6500);

    }
    public void OnclickCash(View view){
        OrderPHP();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cash,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        cash_img = (ImageView) dialogView.findViewById(R.id.cash_img);
        Glide.with(view).load(R.raw.cash).into(cash_img);

        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = 600;
        params.height = 550;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_radius);

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    OrderNumber(view);
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 6500);


    }

    public void OnclickCard(View view){
        OrderPHP();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_card,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        card_img = (ImageView) dialogView.findViewById(R.id.card_img);
        Glide.with(view).load(R.raw.card).into(card_img);

        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = 600;
        params.height = 550;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_radius);

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    OrderNumber(view);
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 6500);

    }

    public void OnclickClose(View view) {
        OrderListAdapter.cart = "";
        Intent order_intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(order_intent);
    }

    public void OnclickCall(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.call,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = 500;
        params.height = 250;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_radius);

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 2000);
    }

    public void OnClickBack(View view) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }

    public void OnClickPack(View view){
        if(IsOff == 1){
            pack_img.setImageResource(R.drawable.pack_on);
            IsOff = 0;
        }
        else{
            pack_img.setImageResource(R.drawable.pack_off);
            IsOff = 1;
        }
    }

//    public void OnClickFinal(View view) {
//        if(flag_step1 && flag_step2){
//            if(payment.equals("card")){
//                builder = new AlertDialog.Builder(this);
//                builder.setTitle("결제").setMessage("아래의 단말기에 카드를 꽂아 주십시오.");
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }else if(payment.equals("mobile")){
//                builder = new AlertDialog.Builder(this);
//                builder.setTitle("결제").setMessage("아래의 단말기에 바코드를 인식시켜 주십시오.");
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }else if(payment.equals("cash")){
//                builder = new AlertDialog.Builder(this);
//                builder.setTitle("결제").setMessage("현금결제를 위해 직원에게 문의하십시오.");
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        }
//    }
    public static void setTotalPrice(int total_int){
        DecimalFormat formatter = new DecimalFormat("###,###");
        order_total_price.setText(formatter.format(total_int));
    }
    public static void setTotalCnt(int cnt){
        order_total_cnt.setText(Integer.toString(cnt));
    }
    public static ArrayList<ListViewBtnItem> getList() {
        return items;
    }

    public static void setList(ArrayList<ListViewBtnItem> list) {
        items = list;
    }

    public static OrderListAdapter getList_adapter() {
        return list_adapter;
    }

    public static void setList_adapter(OrderListAdapter adapter){
        list_adapter = adapter;
    }
}