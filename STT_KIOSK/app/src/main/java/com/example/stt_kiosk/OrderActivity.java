package com.example.stt_kiosk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    public static ArrayList<ListViewBtnItem> items;
    public static ListViewBtnAdapter list_adapter;
    boolean flag_step1 = false;
    boolean flag_step2 = false;
    String payment;
    ImageView select_eat;
    ImageView select_togo;
    ImageView select_card;
    ImageView select_mobile;
    ImageView select_cash;
    TextView step1;
    TextView step2;
    TextView step3;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        items = MainActivity.getList();

        ListView listview = (ListView) findViewById(R.id.order_list);
        list_adapter = new ListViewBtnAdapter(this, R.layout.listview, items);
        listview.setAdapter(list_adapter);

        step1 = (TextView) findViewById(R.id.step1);
        step2 = (TextView) findViewById(R.id.step2);
        step3 = (TextView) findViewById(R.id.step3);
        select_eat = (ImageView) findViewById(R.id.image_eat);
        select_togo = (ImageView) findViewById(R.id.image_togo);
        select_card = (ImageView) findViewById(R.id.image_card);
        select_mobile= (ImageView) findViewById(R.id.image_mobile);
        select_cash = (ImageView) findViewById(R.id.image_cash);
    }

    public void OnclickClose(View view) {
        Intent order_intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(order_intent);
    }

    public void OnClickBack(View view) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }

    public void OnClickPackage(View view) {

        switch(view.getId()){
            case R.id.eat :
                select_eat.setImageResource(R.drawable.market_check);
                select_togo.setImageResource(R.drawable.box);
                step1.setTextColor(Color.parseColor("#252525"));
                step2.setTextColor(Color.parseColor("#E40202"));
                step3.setTextColor(Color.parseColor("#252525"));
                flag_step1 = true;
                break;
            case R.id.togo :
                select_eat.setImageResource(R.drawable.market);
                select_togo.setImageResource(R.drawable.box_check);
                step1.setTextColor(Color.parseColor("#252525"));
                step2.setTextColor(Color.parseColor("#E40202"));
                step3.setTextColor(Color.parseColor("#252525"));
                flag_step1 = true;
                break;
        }
    }

    public void OnClickPayment(View view) {
        if(flag_step1) {
            switch (view.getId()) {
                case R.id.card:
                    select_card.setImageResource(R.drawable.card_check);
                    select_mobile.setImageResource(R.drawable.phone);
                    select_cash.setImageResource(R.drawable.money);
                    step1.setTextColor(Color.parseColor("#252525"));
                    step2.setTextColor(Color.parseColor("#252525"));
                    step3.setTextColor(Color.parseColor("#E40202"));
                    payment = "card";
                    flag_step2 = true;
                    break;
                case R.id.mobile:
                    select_card.setImageResource(R.drawable.card);
                    select_mobile.setImageResource(R.drawable.phone_check);
                    select_cash.setImageResource(R.drawable.money);
                    step1.setTextColor(Color.parseColor("#252525"));
                    step2.setTextColor(Color.parseColor("#252525"));
                    step3.setTextColor(Color.parseColor("#E40202"));
                    payment = "mobile";
                    flag_step2 = true;
                    break;
                case R.id.cash:
                    select_card.setImageResource(R.drawable.card);
                    select_mobile.setImageResource(R.drawable.phone);
                    select_cash.setImageResource(R.drawable.money_check);
                    step1.setTextColor(Color.parseColor("#252525"));
                    step2.setTextColor(Color.parseColor("#252525"));
                    step3.setTextColor(Color.parseColor("#E40202"));
                    payment = "cash";
                    flag_step2 = true;
                    break;
            }
        }
        else{
            builder = new AlertDialog.Builder(this);
            builder.setTitle("알람").setMessage("포장을 선택해주세요");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void OnClickFinal(View view) {
        if(flag_step1 && flag_step2){
            if(payment.equals("card")){
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("결제").setMessage("아래의 단말기에 카드를 꽂아 주십시오.");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
            }else if(payment.equals("mobile")){
                builder = new AlertDialog.Builder(this);
                builder.setTitle("결제").setMessage("아래의 단말기에 바코드를 인식시켜 주십시오.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else if(payment.equals("cash")){
                builder = new AlertDialog.Builder(this);
                builder.setTitle("결제").setMessage("현금결제를 위해 직원에게 문의하십시오.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
}