package com.example.s2t_kiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class PopupActivityRec extends Activity {
    ArrayList<LinearLayout> layout = new ArrayList<>();
    ArrayList<ImageView> check = new ArrayList<>();
    ArrayList<TextView> txt = new ArrayList<>();
    ArrayList<Integer> layout_id = new ArrayList<>();
    ArrayList<Integer> check_id = new ArrayList<>();
    ArrayList<Integer> txt_id = new ArrayList<>();
    ArrayList<String> rec_name = new ArrayList<>();
    ArrayList<Integer> rec_star = new ArrayList<>();
    public static String toCSV;
    DialogRec dialogRec;
    public static String rec_title;
    public static char star;
    char old_star;
    TextView rec_num;
    int check_i;
    int sum ;
    Button ok_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rec_popup);

        //UI 객체생성
        rec_num = (TextView) findViewById(R.id.rec_num);
        ok_rec = (Button) findViewById(R.id.ok_rec);
        toCSV ="";
        sum = 0;

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        for(int i = 0; i < 85; i++){
            int layoutID = getApplicationContext().getResources().getIdentifier("layout"+i, "id", "com.example.s2t_kiosk");
            int checkID = getApplicationContext().getResources().getIdentifier("check"+i,"id", "com.example.s2t_kiosk");
            int txtID = getApplicationContext().getResources().getIdentifier("txt"+i, "id", "com.example.s2t_kiosk");
            layout.add((LinearLayout) this.findViewById(layoutID));
            check.add((ImageView) this.findViewById(checkID));
            txt.add((TextView) this.findViewById(txtID));
            layout_id.add(layoutID);
            check_id.add(checkID);
            txt_id.add(txtID);
        }


    }

    public void menuOnClick(View v){
        int getid = v.getId();
        star = '5';
        for(int i=0; i < 85; i++){
            if(getid == layout_id.get(i)){
                rec_title = txt.get(i).getText().toString();
                check_i = i;
                if(toCSV.contains(rec_title)){
                    star = toCSV.charAt((toCSV.indexOf(rec_title)+rec_title.length()+1));
                    old_star = star;
                    dialogRec = new DialogRec(this);
                    dialogRec.setCancelable(false);
                    dialogRec.show();
                    dialogRec.setDialogListener1(new DialogRec.DialogRecListener1() {
                        @Override
                        public void onApplyClicked(String name, char new_star) {
                            toCSV = toCSV.replaceAll(name+","+old_star,name+","+new_star);
                        }
                    });
                    dialogRec.setDialogListener2(new DialogRec.DialogRecListener2() {
                        @Override
                        public void onCancelClicked(String name, char star) {
                            toCSV = toCSV.replaceAll(name+","+old_star+"/","");
                            check.get(check_i).setVisibility(View.INVISIBLE);
                            sum--;
                            rec_num.setText(""+sum);
                            if(sum < 5) ok_rec.setBackgroundResource(R.drawable.gray_background);
                        }
                    });
                }
                else{
                    dialogRec = new DialogRec(this);
                    dialogRec.setCancelable(false);
                    dialogRec.show();
                    dialogRec.setDialogListener1(new DialogRec.DialogRecListener1() {
                        @Override
                        public void onApplyClicked(String name, char star) {
                            toCSV += name + ",";
                            toCSV += star + "/";
                            check.get(check_i).setVisibility(View.VISIBLE);
                            sum++;
                            rec_num.setText(""+sum);
                            if(sum >= 5) ok_rec.setBackgroundResource(R.drawable.payment_background);
                        }
                    });

                    dialogRec.setDialogListener2(new DialogRec.DialogRecListener2() {
                        @Override
                        public void onCancelClicked(String name, char star) {

                        }
                    });
                }
            }
        }
        System.out.println(toCSV);
    }

    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    public void mOnOK(View v){
        //데이터 전달하기
        if(sum >= 5){
            ((MainActivity)MainActivity.mContext).rec_start.setVisibility(GONE);
            ((MainActivity)MainActivity.mContext).rec_ing.setVisibility(VISIBLE);
            ((MainActivity)MainActivity.mContext).rec_result.setVisibility(GONE);
            Glide.with(((MainActivity)MainActivity.mContext).rec_progress).load(R.raw.loading_rec).into(((MainActivity)MainActivity.mContext).rec_progress);
            Intent intent = new Intent();
            intent.putExtra("result", "OK Popup");
            setResult(RESULT_OK, intent);
            finish();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)MainActivity.mContext).execRec(toCSV);
                }
            }, 3000 );

            //액티비티(팝업) 닫기

        }

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



}