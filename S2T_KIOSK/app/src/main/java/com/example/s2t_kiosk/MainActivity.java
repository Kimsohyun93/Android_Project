package com.example.s2t_kiosk;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.relex.circleindicator.CircleIndicator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {
    public static String serverUrl = "http://172.30.1.17:80/";
    public static Context mContext;
    ListView listview;
    public static ListViewBtnAdapter list_adapter;
    public static ArrayList<ListViewBtnItem> items;
    static TextView total_price;
    static TextView total_cnt;

    LinearLayout clicked_category;
    ImageView clicked_img;
    TextView clicked_txt;
    View clicked_view;
    LinearLayout next_btn;
    LinearLayout back_btn;
    ViewPager vpPager;
    CircleIndicator indicator;
    FragmentPagerAdapter adapterViewPager;
    private Context context;

    ImageView stt_btn;
    LinearLayout menuboard;
    LinearLayout stt_popup;
    LinearLayout stt_start;
    LinearLayout stt_ing;
    LinearLayout stt_result;

    int mic_status = 0;
    ImageView mic_btn;

    //    RecommandAdapter adapter;
    BurgerAdapter adapter2;
    SetAdapter adapter3;
    DessertAdapter adapter4;
    DrinkAdapter adapter5;
    ChickenAdapter adapter6;

    LinearLayout menuwindow;
    LinearLayout rec_start;
    LinearLayout rec_ing;
    LinearLayout rec_result;
    ImageView rec_back;

    TextView name;
    TextView price;
    String exp;
    ImageView img;
    BitmapDrawable image;

    Intent intent;
    Bitmap sendBitmap;
    ByteArrayOutputStream stream;
    byte[] byteArray;

    int cnt = 1;
    String category_db;
    String name_db;
    String price_db;
    String image_db;
    String descript_db;
    String totalweight_db;
    String calorie_db;
    String protein_db;
    String sodium_db;
    String sugar_db;
    String saturatedfat_db;
    String allergy_db;
    String origin_db;
    ArrayList<String> nutrient;
    String allergy;
    String origin;
    private static String TAG = "phptest_MainActivity";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE ="price";
    private static final String TAG_IMAGE ="image";
    private static final String TAG_DESCRIPT ="descript";
    private static final String TAG_TOTALWEIGHT = "totalweight";
    private static final String TAG_CALORIE = "calorie";
    private static final String TAG_PROTEIN = "protein";
    private static final String TAG_SODIUM = "sodium";
    private static final String TAG_SUGAR = "sugar";
    private static final String TAG_SATURATEDFAT = "saturatedfat";
    private static final String TAG_ALLERGY = "allergy";
    private static final String TAG_ORIGIN = "origin";
    public static ArrayList<ArrayList<String>> DB_result_search = null;
    public static ArrayList<ArrayList<String>> DB_result_burger = null;
    public static ArrayList<ArrayList<String>> DB_result_chicken = null;
    public static ArrayList<ArrayList<String>> DB_result_dessert = null;
    public static ArrayList<ArrayList<String>> DB_result_drink = null;
    public static ArrayList<ArrayList<String>> DB_result_set = null;
    public static ArrayList<String> DB_item = null;

    TextView stt;
    TextView stt_title;
    TableLayout stt_table;
    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;

    private String uuid = UUID.randomUUID().toString();
    private SessionsClient sessionsClient;
    private SessionName session;
    SpeechRecognizer sttrec = SpeechRecognizer.createSpeechRecognizer(this);
//    static MainActivity.InsertData task_rec;

    ImageView rec_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = getApplicationContext();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 5);
        }
        mArrayList = new ArrayList<>();

        final LinearLayout rec_btn = (LinearLayout) findViewById(R.id.rec_btn);
        final ImageView rec_img = (ImageView) findViewById(R.id.rec_img);
        final TextView rec_txt = (TextView) findViewById(R.id.rec_txt);
        final View rec_view = (View) findViewById(R.id.rec_view);
        final LinearLayout init_window = (LinearLayout) findViewById(R.id.init_window);
        final LinearLayout home_btn = (LinearLayout) findViewById(R.id.home_btn);
        final LinearLayout call_btn = (LinearLayout) findViewById(R.id.call_btn);

        rec_progress = findViewById(R.id.rec_progress);

        menuboard = findViewById(R.id.menuboard);
        menuboard.setVisibility(VISIBLE);
        stt_btn = findViewById(R.id.stt_btn);
        stt_btn.setVisibility(VISIBLE);
        stt_popup = findViewById(R.id.stt_popup);
        stt_popup.setVisibility(GONE);

        mic_btn = findViewById(R.id.mic_btn);

        clicked_category = rec_btn;
        clicked_img = rec_img;
        clicked_txt = rec_txt;
        clicked_view = rec_view;
        stt_btn.setVisibility(GONE);

        rec_start = findViewById(R.id.rec_start);
        rec_ing = findViewById(R.id.rec_ing);
        rec_result = findViewById(R.id.rec_result);
        rec_back = findViewById(R.id.rec_back);
        rec_back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                rec_start.setVisibility(VISIBLE);
                rec_ing.setVisibility(GONE);
                rec_result.setVisibility(GONE);
            }
        });

        init_window.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                init_window.setVisibility(View.GONE);
                changeView(1);
            }
        });

        home_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(order_intent);
            }
        });

        call_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = getLayoutInflater().inflate(R.layout.call,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
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
        });

        rec_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCategory(clicked_img, clicked_txt, clicked_view);
                rec_img.setImageResource(R.drawable.recommend_on);
                rec_txt.setTextColor(Color.parseColor("#E40202"));
                rec_view.setBackgroundColor(Color.parseColor("#E40202"));
                clicked_category = rec_btn;
                clicked_img = rec_img;
                clicked_txt = rec_txt;
                clicked_view = rec_view;
                stt_btn.setVisibility(GONE);
                changeView(1) ;
                rec_start.setVisibility(VISIBLE);
                rec_ing.setVisibility(GONE);
                rec_result.setVisibility(GONE);
            }
        });

        final LinearLayout bg_btn = (LinearLayout) findViewById(R.id.bg_btn);
        final ImageView bg_img = (ImageView) findViewById(R.id.bg_img);
        final TextView bg_txt = (TextView) findViewById(R.id.bg_txt);
        final View bg_view = (View) findViewById(R.id.bg_view);

        bg_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCategory(clicked_img, clicked_txt, clicked_view);
                bg_img.setImageResource(R.drawable.burger_on);
                bg_txt.setTextColor(Color.parseColor("#E40202"));
                bg_view.setBackgroundColor(Color.parseColor("#E40202"));
                clicked_category = bg_btn;
                clicked_img = bg_img;
                clicked_txt = bg_txt;
                clicked_view = bg_view;
                stt_btn.setVisibility(VISIBLE);
                changeView(2) ;
            }
        });
        final LinearLayout set_btn = (LinearLayout) findViewById(R.id.set_btn) ;
        final ImageView set_img = (ImageView) findViewById(R.id.set_img);
        final TextView set_txt = (TextView) findViewById(R.id.set_txt);
        final View set_view = (View) findViewById(R.id.set_view);

        set_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCategory(clicked_img, clicked_txt, clicked_view);
                set_img.setImageResource(R.drawable.set_on);
                set_txt.setTextColor(Color.parseColor("#E40202"));
                set_view.setBackgroundColor(Color.parseColor("#E40202"));
                clicked_category = set_btn;
                clicked_img = set_img;
                clicked_txt = set_txt;
                clicked_view = set_view;
                stt_btn.setVisibility(VISIBLE);
                changeView(3) ;
            }
        });

        final LinearLayout des_btn = (LinearLayout) findViewById(R.id.des_btn) ;
        final ImageView des_img = (ImageView) findViewById(R.id.des_img);
        final TextView des_txt = (TextView) findViewById(R.id.des_txt);
        final View des_view = (View) findViewById(R.id.des_view);

        des_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCategory(clicked_img, clicked_txt, clicked_view);
                des_img.setImageResource(R.drawable.dessert_on);
                des_txt.setTextColor(Color.parseColor("#E40202"));
                des_view.setBackgroundColor(Color.parseColor("#E40202"));
                clicked_category = des_btn;
                clicked_img = des_img;
                clicked_txt = des_txt;
                clicked_view = des_view;
                stt_btn.setVisibility(VISIBLE);
                changeView(4) ;
            }
        });

        final LinearLayout dri_btn = (LinearLayout) findViewById(R.id.dri_btn) ;
        final ImageView dri_img = (ImageView) findViewById(R.id.dri_img);
        final TextView dri_txt = (TextView) findViewById(R.id.dri_txt);
        final View dri_view = (View) findViewById(R.id.dri_view);

        dri_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCategory(clicked_img, clicked_txt, clicked_view);
                dri_img.setImageResource(R.drawable.drink_on);
                dri_txt.setTextColor(Color.parseColor("#E40202"));
                dri_view.setBackgroundColor(Color.parseColor("#E40202"));
                clicked_category = dri_btn;
                clicked_img = dri_img;
                clicked_txt = dri_txt;
                clicked_view = dri_view;
                stt_btn.setVisibility(VISIBLE);
                changeView(5) ;
            }
        });

        final LinearLayout chi_btn = (LinearLayout) findViewById(R.id.chi_btn) ;
        final ImageView chi_img = (ImageView) findViewById(R.id.chi_img);
        final TextView chi_txt = (TextView) findViewById(R.id.chi_txt);
        final View chi_view = (View) findViewById(R.id.chi_view);

        chi_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCategory(clicked_img, clicked_txt, clicked_view);
                chi_img.setImageResource(R.drawable.chicken_on);
                chi_txt.setTextColor(Color.parseColor("#E40202"));
                chi_view.setBackgroundColor(Color.parseColor("#E40202"));
                clicked_category = chi_btn;
                clicked_img = chi_img;
                clicked_txt = chi_txt;
                clicked_view = chi_view;
                stt_btn.setVisibility(VISIBLE);
                changeView(6) ;
            }
        });

        items = new ArrayList<ListViewBtnItem>();
        listview = (ListView) findViewById(R.id.list);
        list_adapter = new ListViewBtnAdapter(this, R.layout.listview, items);
        listview.setAdapter(list_adapter);
        total_price = (TextView) findViewById(R.id.total_price);
        total_cnt = (TextView) findViewById(R.id.total_cnt);

        back_btn = (LinearLayout) findViewById(R.id.back_btn);
        next_btn = (LinearLayout) findViewById(R.id.next_btn);
        back_btn.setOnClickListener(myListener);
        next_btn.setOnClickListener(myListener);

        stt = findViewById(R.id.stt_window);
        MainActivity.GetData task = new MainActivity.GetData();
        task.execute(serverUrl + "getjson.php");
        URLConnector request = null;
        initV2Dialogflow();
        NetworkUtil.setNetworkPolicy();
//        task_rec = new MainActivity.InsertData();
        mContext = this;
    }

    public void sttPopup(View v){
        mic_status = 0;
        Glide.with(v).load(R.raw.mic_dot).into(mic_btn);
        stt_title = findViewById(R.id.stt_title);
        stt_title.setText("마이크 버튼을 누르고\n원하시는 내용을 말씀해 주세요");
        stt_start = findViewById(R.id.stt_start);
        stt_start.setVisibility(VISIBLE);
        stt_ing = findViewById(R.id.stt_ing);
        stt_ing.setVisibility(GONE);
        stt_result = findViewById(R.id.stt_result);
        stt_result.setVisibility(GONE);
        stt_popup.setVisibility(VISIBLE);
        menuboard.setVisibility(GONE);
        stt_btn.setVisibility(GONE);
    }

    public void sttClose(View v){
        mic_status = 0;
//        Glide.with(v).load(R.raw.mic_dot).into(mic_btn);
        stt_start = findViewById(R.id.stt_start);
        stt_ing = findViewById(R.id.stt_ing);
        stt_ing.setVisibility(GONE);
        stt_result = findViewById(R.id.stt_result);
        stt_ing.setVisibility(GONE);
        stt_popup.setVisibility(GONE);
        menuboard.setVisibility(VISIBLE);
        stt_btn.setVisibility(VISIBLE);
    }

    public void sttRecog(View v){
        switch(mic_status) {
            case 0 :
                mic_status = 1;
                inputVoice(stt);
                Glide.with(v).load(R.raw.mic_on).into(mic_btn);
                stt_start.setVisibility(GONE);
                stt_ing.setVisibility(VISIBLE);
                stt_result.setVisibility(GONE);
                break;
            case 1:
                mic_status = 0;
                sttrec.destroy();
                Glide.with(v).load(R.raw.mic_off).into(mic_btn);
                stt_start.setVisibility(GONE);
                stt_ing.setVisibility(GONE);
                stt_start.setVisibility(VISIBLE);
                break;
            case 2:
                mic_status = 1;
                inputVoice(stt);
                Glide.with(v).load(R.raw.mic_on).into(mic_btn);
                stt_start.setVisibility(GONE);
                stt_result.setVisibility(GONE);
                stt_ing.setVisibility(VISIBLE);
                break;
        }
    }

    private void changeCategory(ImageView clicked_img, TextView clicked_txt, View clicked_view){
        String txt = clicked_txt.getText().toString();
        switch (txt){
            case "버거" :
                clicked_img.setImageResource(R.drawable.burger_off);
                clicked_txt.setTextColor(Color.parseColor("#FF878787"));
                clicked_view.setBackground(null);
                break;
            case "추천" :
                clicked_img.setImageResource(R.drawable.recommend_off);
                clicked_txt.setTextColor(Color.parseColor("#FF878787"));
                clicked_view.setBackground(null);
                break;
            case "세트" :
                clicked_img.setImageResource(R.drawable.set_off);
                clicked_txt.setTextColor(Color.parseColor("#FF878787"));
                clicked_view.setBackground(null);
                break;
            case "디저트" :
                clicked_img.setImageResource(R.drawable.dessert_off);
                clicked_txt.setTextColor(Color.parseColor("#FF878787"));
                clicked_view.setBackground(null);
                break;
            case "음료" :
                clicked_img.setImageResource(R.drawable.drink_off);
                clicked_txt.setTextColor(Color.parseColor("#FF878787"));
                clicked_view.setBackground(null);
                break;
            case "치킨" :
                clicked_img.setImageResource(R.drawable.chicken_off);
                clicked_txt.setTextColor(Color.parseColor("#FF878787"));
                clicked_view.setBackground(null);
                break;
        }
    }

    private void changeView(int index) {

        menuwindow = (LinearLayout) findViewById(R.id.menuwindow);
//        rec_start = (LinearLayout) findViewById(R.id.rec_start);
//        rec_ing = findViewById(R.id.rec_ing);
//        rec_result = findViewById(R.id.rec_result);
        // 자식(Children) 뷰들에 대한 참조 획득.
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

        // index에 따라 자식(Children) 뷰 들의 visibility 설정.
        if(index == 1) {
            menuwindow.setVisibility(GONE);
            rec_start.setVisibility(VISIBLE);
        } else {
            rec_start.setVisibility(GONE);
            rec_ing.setVisibility(GONE);
            rec_result.setVisibility(GONE);
            menuwindow.setVisibility(VISIBLE);
            switch (index) {
                case 2 :
                    adapter2 = new BurgerAdapter(getLayoutInflater(),this.context);
                    vpPager.setAdapter(adapter2);
                    break ;
                case 3 :
                    adapter3 = new SetAdapter(getLayoutInflater(),this.context);
                    vpPager.setAdapter(adapter3);
                    break ;
                case 4 :
                    adapter4 = new DessertAdapter(getLayoutInflater(),this.context);
                    vpPager.setAdapter(adapter4);
                    break;
                case 5 :
                    adapter5 = new DrinkAdapter(getLayoutInflater(),this.context);
                    vpPager.setAdapter(adapter5);
                    break;
                case 6 :
                    adapter6 = new ChickenAdapter(getLayoutInflater(),this.context);
                    vpPager.setAdapter(adapter6);
                    break;
            }
            indicator.setViewPager(vpPager);
        }

    }


    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position;
            switch (v.getId()) {
                case R.id.back_btn:
                    position = vpPager.getCurrentItem();
                    vpPager.setCurrentItem(position - 1, true);
                    break;
                case R.id.next_btn:
                    position = vpPager.getCurrentItem();
                    vpPager.setCurrentItem(position + 1, true);
                    break;
            }
        }
    };

    public void RecOnClick(View v)
    {
        intent = new Intent(this, com.example.s2t_kiosk.PopupActivityRec.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }

    public void MenuOnClick(View v)
    {
        intent = new Intent(this, com.example.s2t_kiosk.PopupActivity.class);
        stream = new ByteArrayOutputStream();
        String cat = (String) clicked_txt.getText();
        ArrayList<ArrayList<String>> db_cat = new ArrayList<>();
        if(cat.equals("버거")){
            db_cat = DB_result_burger;
        }
        else if(cat.equals("세트")){
            db_cat = DB_result_set;
        }
        else if(cat.equals("디저트")){
            db_cat = DB_result_dessert;
        }
        else if(cat.equals("음료")){
            db_cat = DB_result_drink;
        }
        else if(cat.equals("치킨")){
            db_cat = DB_result_chicken;
        }

        switch (v.getId()){
            case R.id.menu_btn0:
                name = (TextView) findViewById(R.id.menu_name0);
                price = (TextView) findViewById(R.id.menu_price0);
                exp = db_cat.get(0).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(0).get(5));
                nutrient.add(db_cat.get(0).get(6));
                nutrient.add(db_cat.get(0).get(7));
                nutrient.add(db_cat.get(0).get(8));
                nutrient.add(db_cat.get(0).get(9));
                nutrient.add(db_cat.get(0).get(10));
                allergy = db_cat.get(0).get(11);
                origin = db_cat.get(0).get(12);
                img = (ImageView) findViewById(R.id.menu_img0);
                break;
            case R.id.menu_btn1:
                name = (TextView) findViewById(R.id.menu_name1);
                price = (TextView) findViewById(R.id.menu_price1);
                exp = db_cat.get(1).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(1).get(5));
                nutrient.add(db_cat.get(1).get(6));
                nutrient.add(db_cat.get(1).get(7));
                nutrient.add(db_cat.get(1).get(8));
                nutrient.add(db_cat.get(1).get(9));
                nutrient.add(db_cat.get(1).get(10));
                allergy = db_cat.get(1).get(11);
                origin = db_cat.get(1).get(12);
                img = findViewById(R.id.menu_img1);
                break;
            case R.id.menu_btn2:
                name = (TextView) findViewById(R.id.menu_name2);
                price = (TextView) findViewById(R.id.menu_price2);
                exp = db_cat.get(2).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(2).get(5));
                nutrient.add(db_cat.get(2).get(6));
                nutrient.add(db_cat.get(2).get(7));
                nutrient.add(db_cat.get(2).get(8));
                nutrient.add(db_cat.get(2).get(9));
                nutrient.add(db_cat.get(2).get(10));
                allergy = db_cat.get(2).get(11);
                origin = db_cat.get(2).get(12);
                img = findViewById(R.id.menu_img2);
                break;
            case R.id.menu_btn3:
                name = (TextView) findViewById(R.id.menu_name3);
                price = (TextView) findViewById(R.id.menu_price3);
                exp = db_cat.get(3).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(3).get(5));
                nutrient.add(db_cat.get(3).get(6));
                nutrient.add(db_cat.get(3).get(7));
                nutrient.add(db_cat.get(3).get(8));
                nutrient.add(db_cat.get(3).get(9));
                nutrient.add(db_cat.get(3).get(10));
                allergy = db_cat.get(3).get(11);
                origin = db_cat.get(3).get(12);
                img = findViewById(R.id.menu_img3);
                break;
            case R.id.menu_btn4:
                name = (TextView) findViewById(R.id.menu_name4);
                price = (TextView) findViewById(R.id.menu_price4);
                exp = db_cat.get(4).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(4).get(5));
                nutrient.add(db_cat.get(4).get(6));
                nutrient.add(db_cat.get(4).get(7));
                nutrient.add(db_cat.get(4).get(8));
                nutrient.add(db_cat.get(4).get(9));
                nutrient.add(db_cat.get(4).get(10));
                allergy = db_cat.get(4).get(11);
                origin = db_cat.get(4).get(12);
                img = findViewById(R.id.menu_img4);
                break;
            case R.id.menu_btn5:
                name = (TextView) findViewById(R.id.menu_name5);
                price = (TextView) findViewById(R.id.menu_price5);
                exp = db_cat.get(5).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(5).get(5));
                nutrient.add(db_cat.get(5).get(6));
                nutrient.add(db_cat.get(5).get(7));
                nutrient.add(db_cat.get(5).get(8));
                nutrient.add(db_cat.get(5).get(9));
                nutrient.add(db_cat.get(5).get(10));
                allergy = db_cat.get(5).get(11);
                origin = db_cat.get(5).get(12);
                img = findViewById(R.id.menu_img5);
                break;
            case R.id.menu_btn6:
                name = (TextView) findViewById(R.id.menu_name6);
                price = (TextView) findViewById(R.id.menu_price6);
                exp = db_cat.get(6).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(6).get(5));
                nutrient.add(db_cat.get(6).get(6));
                nutrient.add(db_cat.get(6).get(7));
                nutrient.add(db_cat.get(6).get(8));
                nutrient.add(db_cat.get(6).get(9));
                nutrient.add(db_cat.get(6).get(10));
                allergy = db_cat.get(6).get(11);
                origin = db_cat.get(6).get(12);
                img = findViewById(R.id.menu_img6);
                break;
            case R.id.menu_btn7:
                name = (TextView) findViewById(R.id.menu_name7);
                price = (TextView) findViewById(R.id.menu_price7);
                exp = db_cat.get(7).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(7).get(5));
                nutrient.add(db_cat.get(7).get(6));
                nutrient.add(db_cat.get(7).get(7));
                nutrient.add(db_cat.get(7).get(8));
                nutrient.add(db_cat.get(7).get(9));
                nutrient.add(db_cat.get(7).get(10));
                allergy = db_cat.get(7).get(11);
                origin = db_cat.get(7).get(12);
                img = findViewById(R.id.menu_img7);
                break;
            case R.id.menu_btn8:
                name = (TextView) findViewById(R.id.menu_name8);
                price = (TextView) findViewById(R.id.menu_price8);
                exp = db_cat.get(8).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(8).get(5));
                nutrient.add(db_cat.get(8).get(6));
                nutrient.add(db_cat.get(8).get(7));
                nutrient.add(db_cat.get(8).get(8));
                nutrient.add(db_cat.get(8).get(9));
                nutrient.add(db_cat.get(8).get(10));
                allergy = db_cat.get(8).get(11);
                origin = db_cat.get(8).get(12);
                img = findViewById(R.id.menu_img8);
                break;
            case R.id.menu_btn9:
                name = (TextView) findViewById(R.id.menu_name9);
                price = (TextView) findViewById(R.id.menu_price9);
                exp = db_cat.get(9).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(9).get(5));
                nutrient.add(db_cat.get(9).get(6));
                nutrient.add(db_cat.get(9).get(7));
                nutrient.add(db_cat.get(9).get(8));
                nutrient.add(db_cat.get(9).get(9));
                nutrient.add(db_cat.get(9).get(10));
                allergy = db_cat.get(9).get(11);
                origin = db_cat.get(9).get(12);
                img = findViewById(R.id.menu_img9);
                break;
            case R.id.menu_btn10:
                name = (TextView) findViewById(R.id.menu_name10);
                price = (TextView) findViewById(R.id.menu_price10);
                exp = db_cat.get(10).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(10).get(5));
                nutrient.add(db_cat.get(10).get(6));
                nutrient.add(db_cat.get(10).get(7));
                nutrient.add(db_cat.get(10).get(8));
                nutrient.add(db_cat.get(10).get(9));
                nutrient.add(db_cat.get(10).get(10));
                allergy = db_cat.get(10).get(11);
                origin = db_cat.get(10).get(12);
                img = findViewById(R.id.menu_img10);
                break;
            case R.id.menu_btn11:
                name = (TextView) findViewById(R.id.menu_name11);
                price = (TextView) findViewById(R.id.menu_price11);
                exp = db_cat.get(11).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(11).get(5));
                nutrient.add(db_cat.get(11).get(6));
                nutrient.add(db_cat.get(11).get(7));
                nutrient.add(db_cat.get(11).get(8));
                nutrient.add(db_cat.get(11).get(9));
                nutrient.add(db_cat.get(11).get(10));
                allergy = db_cat.get(11).get(11);
                origin = db_cat.get(11).get(12);
                img = findViewById(R.id.menu_img11);
                break;
            case R.id.menu_btn12:
                name = (TextView) findViewById(R.id.menu_name12);
                price = (TextView) findViewById(R.id.menu_price12);
                exp = db_cat.get(12).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(12).get(5));
                nutrient.add(db_cat.get(12).get(6));
                nutrient.add(db_cat.get(12).get(7));
                nutrient.add(db_cat.get(12).get(8));
                nutrient.add(db_cat.get(12).get(9));
                nutrient.add(db_cat.get(12).get(10));
                allergy = db_cat.get(12).get(11);
                origin = db_cat.get(12).get(12);
                img = findViewById(R.id.menu_img12);
                break;
            case R.id.menu_btn13:
                name = (TextView) findViewById(R.id.menu_name13);
                price = (TextView) findViewById(R.id.menu_price13);
                exp = db_cat.get(13).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(13).get(5));
                nutrient.add(db_cat.get(13).get(6));
                nutrient.add(db_cat.get(13).get(7));
                nutrient.add(db_cat.get(13).get(8));
                nutrient.add(db_cat.get(13).get(9));
                nutrient.add(db_cat.get(13).get(10));
                allergy = db_cat.get(13).get(11);
                origin = db_cat.get(13).get(12);
                img = findViewById(R.id.menu_img13);
                break;
            case R.id.menu_btn14:
                name = (TextView) findViewById(R.id.menu_name14);
                price = (TextView) findViewById(R.id.menu_price14);
                exp = db_cat.get(14).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(14).get(5));
                nutrient.add(db_cat.get(14).get(6));
                nutrient.add(db_cat.get(14).get(7));
                nutrient.add(db_cat.get(14).get(8));
                nutrient.add(db_cat.get(14).get(9));
                nutrient.add(db_cat.get(14).get(10));
                allergy = db_cat.get(14).get(11);
                origin = db_cat.get(14).get(12);
                img = findViewById(R.id.menu_img14);
                break;
            case R.id.menu_btn15:
                name = (TextView) findViewById(R.id.menu_name15);
                price = (TextView) findViewById(R.id.menu_price15);
                exp = db_cat.get(15).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(15).get(5));
                nutrient.add(db_cat.get(15).get(6));
                nutrient.add(db_cat.get(15).get(7));
                nutrient.add(db_cat.get(15).get(8));
                nutrient.add(db_cat.get(15).get(9));
                nutrient.add(db_cat.get(15).get(10));
                allergy = db_cat.get(15).get(11);
                origin = db_cat.get(15).get(12);
                img = findViewById(R.id.menu_img15);
                break;
            case R.id.menu_btn16:
                name = (TextView) findViewById(R.id.menu_name16);
                price = (TextView) findViewById(R.id.menu_price16);
                exp = db_cat.get(16).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(16).get(5));
                nutrient.add(db_cat.get(16).get(6));
                nutrient.add(db_cat.get(16).get(7));
                nutrient.add(db_cat.get(16).get(8));
                nutrient.add(db_cat.get(16).get(9));
                nutrient.add(db_cat.get(16).get(10));
                allergy = db_cat.get(16).get(11);
                origin = db_cat.get(16).get(12);
                img = findViewById(R.id.menu_img16);
                break;
            case R.id.menu_btn17:
                name = (TextView) findViewById(R.id.menu_name17);
                price = (TextView) findViewById(R.id.menu_price17);
                exp = db_cat.get(17).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(17).get(5));
                nutrient.add(db_cat.get(17).get(6));
                nutrient.add(db_cat.get(17).get(7));
                nutrient.add(db_cat.get(17).get(8));
                nutrient.add(db_cat.get(17).get(9));
                nutrient.add(db_cat.get(17).get(10));
                allergy = db_cat.get(17).get(11);
                origin = db_cat.get(17).get(12);
                img = findViewById(R.id.menu_img17);
                break;
            case R.id.menu_btn18:
                name = (TextView) findViewById(R.id.menu_name18);
                price = (TextView) findViewById(R.id.menu_price18);
                exp = db_cat.get(18).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(18).get(5));
                nutrient.add(db_cat.get(18).get(6));
                nutrient.add(db_cat.get(18).get(7));
                nutrient.add(db_cat.get(18).get(8));
                nutrient.add(db_cat.get(18).get(9));
                nutrient.add(db_cat.get(18).get(10));
                allergy = db_cat.get(18).get(11);
                origin = db_cat.get(18).get(12);
                img = findViewById(R.id.menu_img18);
                break;
            case R.id.menu_btn19:
                name = (TextView) findViewById(R.id.menu_name19);
                price = (TextView) findViewById(R.id.menu_price19);
                exp = db_cat.get(19).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(19).get(5));
                nutrient.add(db_cat.get(19).get(6));
                nutrient.add(db_cat.get(19).get(7));
                nutrient.add(db_cat.get(19).get(8));
                nutrient.add(db_cat.get(19).get(9));
                nutrient.add(db_cat.get(19).get(10));
                allergy = db_cat.get(19).get(11);
                origin = db_cat.get(19).get(12);
                img = findViewById(R.id.menu_img19);
                break;
            case R.id.menu_btn20:
                name = (TextView) findViewById(R.id.menu_name20);
                price = (TextView) findViewById(R.id.menu_price20);
                exp = db_cat.get(20).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(20).get(5));
                nutrient.add(db_cat.get(20).get(6));
                nutrient.add(db_cat.get(20).get(7));
                nutrient.add(db_cat.get(20).get(8));
                nutrient.add(db_cat.get(20).get(9));
                nutrient.add(db_cat.get(20).get(10));
                allergy = db_cat.get(20).get(11);
                origin = db_cat.get(20).get(12);
                img = findViewById(R.id.menu_img20);
                break;
            case R.id.menu_btn21:
                name = (TextView) findViewById(R.id.menu_name21);
                price = (TextView) findViewById(R.id.menu_price21);
                exp = db_cat.get(21).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(21).get(5));
                nutrient.add(db_cat.get(21).get(6));
                nutrient.add(db_cat.get(21).get(7));
                nutrient.add(db_cat.get(21).get(8));
                nutrient.add(db_cat.get(21).get(9));
                nutrient.add(db_cat.get(21).get(10));
                allergy = db_cat.get(21).get(11);
                origin = db_cat.get(21).get(12);
                img = findViewById(R.id.menu_img21);
                break;
            case R.id.menu_btn22:
                name = (TextView) findViewById(R.id.menu_name22);
                price = (TextView) findViewById(R.id.menu_price22);
                exp = db_cat.get(22).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(22).get(5));
                nutrient.add(db_cat.get(22).get(6));
                nutrient.add(db_cat.get(22).get(7));
                nutrient.add(db_cat.get(22).get(8));
                nutrient.add(db_cat.get(22).get(9));
                nutrient.add(db_cat.get(22).get(10));
                allergy = db_cat.get(22).get(11);
                origin = db_cat.get(22).get(12);
                img = findViewById(R.id.menu_img22);
                break;
            case R.id.menu_btn23:
                name = (TextView) findViewById(R.id.menu_name23);
                price = (TextView) findViewById(R.id.menu_price23);
                exp = db_cat.get(23).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(23).get(5));
                nutrient.add(db_cat.get(23).get(6));
                nutrient.add(db_cat.get(23).get(7));
                nutrient.add(db_cat.get(23).get(8));
                nutrient.add(db_cat.get(23).get(9));
                nutrient.add(db_cat.get(23).get(10));
                allergy = db_cat.get(23).get(11);
                origin = db_cat.get(23).get(12);
                img = findViewById(R.id.menu_img23);
                break;
        }
        image = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = image.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byteArray = stream.toByteArray();
        intent.putExtra("image", byteArray);
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("price", price.getText().toString());
        intent.putExtra("cat", cat);
        intent.putExtra("exp", exp);
        intent.putExtra("nutrient",nutrient);
        intent.putExtra("allergy", allergy);
        intent.putExtra("origin", origin);
        startActivityForResult(intent, 1);
    }

    public void SearchOnClick(View v)
    {
        intent = new Intent(this, com.example.s2t_kiosk.PopupActivity.class);
        stream = new ByteArrayOutputStream();
        ArrayList<ArrayList<String>> db_cat = new ArrayList<>();
        db_cat = DB_result_search;
        String cat = (String) "";

        switch (v.getId()){
            case R.id.stt_menu_btn0:
                name = (TextView) findViewById(R.id.stt_menu_name0);
                price = (TextView) findViewById(R.id.stt_menu_price0);
                cat = db_cat.get(0).get(0);
                exp = db_cat.get(0).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(0).get(5));
                nutrient.add(db_cat.get(0).get(6));
                nutrient.add(db_cat.get(0).get(7));
                nutrient.add(db_cat.get(0).get(8));
                nutrient.add(db_cat.get(0).get(9));
                nutrient.add(db_cat.get(0).get(10));
                allergy = db_cat.get(0).get(11);
                origin = db_cat.get(0).get(12);
                img = (ImageView) findViewById(R.id.stt_menu_img0);
                break;
            case R.id.stt_menu_btn1:
                name = (TextView) findViewById(R.id.stt_menu_name1);
                price = (TextView) findViewById(R.id.stt_menu_price1);
                cat = db_cat.get(1).get(0);
                exp = db_cat.get(1).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(1).get(5));
                nutrient.add(db_cat.get(1).get(6));
                nutrient.add(db_cat.get(1).get(7));
                nutrient.add(db_cat.get(1).get(8));
                nutrient.add(db_cat.get(1).get(9));
                nutrient.add(db_cat.get(1).get(10));
                allergy = db_cat.get(1).get(11);
                origin = db_cat.get(1).get(12);
                img = (ImageView) findViewById(R.id.stt_menu_img1);
                break;
            case R.id.stt_menu_btn2:
                name = (TextView) findViewById(R.id.stt_menu_name2);
                price = (TextView) findViewById(R.id.stt_menu_price2);
                cat = db_cat.get(2).get(0);
                exp = db_cat.get(2).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(2).get(5));
                nutrient.add(db_cat.get(2).get(6));
                nutrient.add(db_cat.get(2).get(7));
                nutrient.add(db_cat.get(2).get(8));
                nutrient.add(db_cat.get(2).get(9));
                nutrient.add(db_cat.get(2).get(10));
                allergy = db_cat.get(2).get(11);
                origin = db_cat.get(2).get(12);
                img = findViewById(R.id.stt_menu_img2);
                break;
            case R.id.stt_menu_btn3:
                name = (TextView) findViewById(R.id.stt_menu_name3);
                price = (TextView) findViewById(R.id.stt_menu_price3);
                cat = db_cat.get(3).get(0);
                exp = db_cat.get(3).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(3).get(5));
                nutrient.add(db_cat.get(3).get(6));
                nutrient.add(db_cat.get(3).get(7));
                nutrient.add(db_cat.get(3).get(8));
                nutrient.add(db_cat.get(3).get(9));
                nutrient.add(db_cat.get(3).get(10));
                allergy = db_cat.get(3).get(11);
                origin = db_cat.get(3).get(12);
                img = findViewById(R.id.stt_menu_img3);
                break;
            case R.id.stt_menu_btn4:
                name = (TextView) findViewById(R.id.stt_menu_name4);
                price = (TextView) findViewById(R.id.stt_menu_price4);
                cat = db_cat.get(4).get(0);
                exp = db_cat.get(4).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(4).get(5));
                nutrient.add(db_cat.get(4).get(6));
                nutrient.add(db_cat.get(4).get(7));
                nutrient.add(db_cat.get(4).get(8));
                nutrient.add(db_cat.get(4).get(9));
                nutrient.add(db_cat.get(4).get(10));
                allergy = db_cat.get(4).get(11);
                origin = db_cat.get(4).get(12);
                img = findViewById(R.id.stt_menu_img4);
                break;
            case R.id.stt_menu_btn5:
                name = (TextView) findViewById(R.id.stt_menu_name5);
                price = (TextView) findViewById(R.id.stt_menu_price5);
                cat = db_cat.get(5).get(0);
                exp = db_cat.get(5).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(5).get(5));
                nutrient.add(db_cat.get(5).get(6));
                nutrient.add(db_cat.get(5).get(7));
                nutrient.add(db_cat.get(5).get(8));
                nutrient.add(db_cat.get(5).get(9));
                nutrient.add(db_cat.get(5).get(10));
                allergy = db_cat.get(5).get(11);
                origin = db_cat.get(5).get(12);
                img = findViewById(R.id.stt_menu_img5);
                break;
            case R.id.rec_menu_btn0:
                name = (TextView) findViewById(R.id.rec_menu_name0);
                price = (TextView) findViewById(R.id.rec_menu_price0);
                cat = db_cat.get(0).get(0);
                exp = db_cat.get(0).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(0).get(5));
                nutrient.add(db_cat.get(0).get(6));
                nutrient.add(db_cat.get(0).get(7));
                nutrient.add(db_cat.get(0).get(8));
                nutrient.add(db_cat.get(0).get(9));
                nutrient.add(db_cat.get(0).get(10));
                allergy = db_cat.get(0).get(11);
                origin = db_cat.get(0).get(12);
                img = (ImageView) findViewById(R.id.rec_menu_img0);
                break;
            case R.id.rec_menu_btn1:
                name = (TextView) findViewById(R.id.rec_menu_name1);
                price = (TextView) findViewById(R.id.rec_menu_price1);
                cat = db_cat.get(1).get(0);
                exp = db_cat.get(1).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(1).get(5));
                nutrient.add(db_cat.get(1).get(6));
                nutrient.add(db_cat.get(1).get(7));
                nutrient.add(db_cat.get(1).get(8));
                nutrient.add(db_cat.get(1).get(9));
                nutrient.add(db_cat.get(1).get(10));
                allergy = db_cat.get(1).get(11);
                origin = db_cat.get(1).get(12);
                img = (ImageView) findViewById(R.id.rec_menu_img1);
                break;
            case R.id.rec_menu_btn2:
                name = (TextView) findViewById(R.id.rec_menu_name2);
                price = (TextView) findViewById(R.id.rec_menu_price2);
                cat = db_cat.get(2).get(0);
                exp = db_cat.get(2).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(2).get(5));
                nutrient.add(db_cat.get(2).get(6));
                nutrient.add(db_cat.get(2).get(7));
                nutrient.add(db_cat.get(2).get(8));
                nutrient.add(db_cat.get(2).get(9));
                nutrient.add(db_cat.get(2).get(10));
                allergy = db_cat.get(2).get(11);
                origin = db_cat.get(2).get(12);
                img = findViewById(R.id.rec_menu_img2);
                break;
            case R.id.rec_menu_btn3:
                name = (TextView) findViewById(R.id.rec_menu_name3);
                price = (TextView) findViewById(R.id.rec_menu_price3);
                cat = db_cat.get(3).get(0);
                exp = db_cat.get(3).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(3).get(5));
                nutrient.add(db_cat.get(3).get(6));
                nutrient.add(db_cat.get(3).get(7));
                nutrient.add(db_cat.get(3).get(8));
                nutrient.add(db_cat.get(3).get(9));
                nutrient.add(db_cat.get(3).get(10));
                allergy = db_cat.get(3).get(11);
                origin = db_cat.get(3).get(12);
                img = findViewById(R.id.rec_menu_img3);
                break;
            case R.id.rec_menu_btn4:
                name = (TextView) findViewById(R.id.rec_menu_name4);
                price = (TextView) findViewById(R.id.rec_menu_price4);
                cat = db_cat.get(4).get(0);
                exp = db_cat.get(4).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(4).get(5));
                nutrient.add(db_cat.get(4).get(6));
                nutrient.add(db_cat.get(4).get(7));
                nutrient.add(db_cat.get(4).get(8));
                nutrient.add(db_cat.get(4).get(9));
                nutrient.add(db_cat.get(4).get(10));
                allergy = db_cat.get(4).get(11);
                origin = db_cat.get(4).get(12);
                img = findViewById(R.id.rec_menu_img4);
                break;
            case R.id.rec_menu_btn5:
                name = (TextView) findViewById(R.id.rec_menu_name5);
                price = (TextView) findViewById(R.id.rec_menu_price5);
                cat = db_cat.get(5).get(0);
                exp = db_cat.get(5).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(5).get(5));
                nutrient.add(db_cat.get(5).get(6));
                nutrient.add(db_cat.get(5).get(7));
                nutrient.add(db_cat.get(5).get(8));
                nutrient.add(db_cat.get(5).get(9));
                nutrient.add(db_cat.get(5).get(10));
                allergy = db_cat.get(5).get(11);
                origin = db_cat.get(5).get(12);
                img = findViewById(R.id.rec_menu_img5);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
        image = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = image.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byteArray = stream.toByteArray();
        intent.putExtra("image", byteArray);
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("price", price.getText().toString());
        intent.putExtra("cat", cat);
        intent.putExtra("exp", exp);
        intent.putExtra("nutrient",nutrient);
        intent.putExtra("allergy", allergy);
        intent.putExtra("origin", origin);
        startActivityForResult(intent, 1);
    }

    private class GetData extends AsyncTask<String, Void, String> {
        //        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("preexecute", "here");
//            progressDialog = ProgressDialog.show(MainActivity.this,
//                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null){
                Log.d("postexecute", "null");
//                stt.setText(errorString);
                Log.d("db error", errorString);
            }
            else {

                mJsonString = result;
                Log.d("postexecute", result);
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    public void inputVoice(TextView txt) {
        Log.d("inputVoice", "in function");
        try {
            Intent intentSTT = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentSTT.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
            intentSTT.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            sttrec = SpeechRecognizer.createSpeechRecognizer(this);
            sttrec.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
//                    toast("음성입력시작");
                }

                @Override
                public void onBeginningOfSpeech() {
                    Log.d("STTstart", "start here");
                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {
//                    toast("음성입력종료");
                    Log.d("STTend", "end here");
                }

                @Override
                public void onError(int error) {
//                    toast("오류발생 : " + error);
                    String errText = "음성인식에 실패했습니다\n마이크를 한 번 더\n터치하여 말씀해 주세요";
                    switch(error){
                        case 1:
                            Log.d("stt_error","Network Timeout");
                            break;
                        case 2:
                            Log.d("stt_error","Network Error");
                            break;
                        case 3:
                            Log.d("stt_error","Record Error");
                            break;
                        case 4:
                            Log.d("stt_error","Server Error");
                            break;
                        case 5:
                            Log.d("stt_error","Client Error");
                            break;
                        case 6:
                            errText = "아무 것도 못들었어요\n마이크 버튼을 누르고 다시 말씀해 주세요";
//                            Log.d("stt_error","아무 것도 못들었어요\n마이크 버튼을 누르고\n다시 한 번 말씀해주세요.");
                            break;
                        case 7:
                            errText = "어떤 내용인지 잘 모르겠어요\n마이크 버튼을 누르고 다시 말씀해 주세요";
//                            Log.d("stt_error","마이크 버튼을 누르고\n다시 한 번 말씀해주세요.");
                            break;
                        case 8:
                            Log.d("stt_error","Busy Error");
                            break;
                        case 9:
                            Log.d("stt_error","Permission");
                            break;
                        default :
                            Log.d("stt_error","error,,,");
                    }
                    Log.d("onError", "error : "+ error);
                    Glide.with(context).load(R.raw.mic_off).into(mic_btn);
                    mic_status = 0;
                    stt_title.setText(errText);
                    stt_start.setVisibility(VISIBLE);
                    stt_ing.setVisibility(GONE);
                    stt_result.setVisibility(GONE);
                    sttrec.destroy();
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(stt_start.getVisibility() == GONE) {
                        stt_start.setVisibility(GONE);
                        stt_ing.setVisibility(GONE);
                        stt_result.setVisibility(VISIBLE);
                    }
                    else {
                        stt_ing.setVisibility(GONE);
                        stt_result.setVisibility(GONE);
                    }
                    stt.setText( result.get(0) + "\n");
                    Glide.with(context).load(R.raw.mic_off).into(mic_btn);
                    mic_status = 2;
                    sttrec.destroy();
                    sendMessage();
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
            sttrec.startListening(intentSTT);
            new CountDownTimer(5000,1000){
                public void onTick(long m){
                }
                public void onFinish(){
                    sttrec.stopListening();
                }
            }.start();
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
//
//    class InsertData extends AsyncTask<String, Void, String>{
////        ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            progressDialog = ProgressDialog.show(MainActivity.this,
////                    "Please Wait", null, true, true);
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
////            progressDialog.dismiss();
////            mTextViewResult.setText(result);
//            Log.d(TAG, "POST response  - " + result);
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String recInput = (String)params[1];
//
//            String serverURL = (String)params[0];
//            String postParameters = "recInput=" + recInput;
//
//
//            try {
//
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.connect();
//
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(postParameters.getBytes("UTF-8"));
//                outputStream.flush();
//                outputStream.close();
//
//
//                int responseStatusCode = httpURLConnection.getResponseCode();
//                Log.d(TAG, "POST response code - " + responseStatusCode);
//
//                InputStream inputStream;
//                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
//                    inputStream = httpURLConnection.getInputStream();
//                }
//                else{
//                    inputStream = httpURLConnection.getErrorStream();
//                }
//
//
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//
//                while((line = bufferedReader.readLine()) != null){
//                    sb.append(line);
//                }
//
//
//                bufferedReader.close();
//
//
//                return sb.toString();
//
//
//            } catch (Exception e) {
//
//                Log.d(TAG, "InsertData: Error ", e);
//
//                return new String("Error: " + e.getMessage());
//            }
//
//        }
//    }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            DB_result_burger = new ArrayList<ArrayList<String>>();
            DB_result_set = new ArrayList<ArrayList<String>>();
            DB_result_drink = new ArrayList<ArrayList<String>>();
            DB_result_dessert = new ArrayList<ArrayList<String>>();
            DB_result_chicken = new ArrayList<ArrayList<String>>();


            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                category_db = item.getString(TAG_CATEGORY);
                name_db = item.getString(TAG_NAME);
                price_db = item.getString(TAG_PRICE);
                image_db = item.getString(TAG_IMAGE);
                descript_db = item.getString(TAG_DESCRIPT);
                totalweight_db = item.getString(TAG_TOTALWEIGHT);
                calorie_db = item.getString(TAG_CALORIE);
                protein_db = item.getString(TAG_PROTEIN);
                sodium_db = item.getString(TAG_SODIUM);
                sugar_db = item.getString(TAG_SUGAR);
                saturatedfat_db = item.getString(TAG_SATURATEDFAT);
                allergy_db = item.getString(TAG_ALLERGY);
                origin_db = item.getString(TAG_ORIGIN);
                DB_item = new ArrayList<String>();
                DB_item.add(category_db);
                DB_item.add(name_db);
                DB_item.add(price_db);
                DB_item.add(image_db);
                DB_item.add(descript_db);
                DB_item.add(totalweight_db);
                DB_item.add(calorie_db);
                DB_item.add(protein_db);
                DB_item.add(sodium_db);
                DB_item.add(sugar_db);
                DB_item.add(saturatedfat_db);
                DB_item.add(allergy_db);
                DB_item.add(origin_db);
                if(category_db.equals("세트")){
                    DB_result_set.add(DB_item);
                }
                else if(category_db.equals("버거")){
                    DB_result_burger.add(DB_item);
                }
                else if(category_db.equals("디저트")){
                    DB_result_dessert.add(DB_item);
                }
                else if(category_db.equals("음료")){
                    DB_result_drink.add(DB_item);
                }
                else if(category_db.equals("치킨")){
                    DB_result_chicken.add(DB_item);
                }
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private void getResult(String sttORrec){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            DB_result_search= new ArrayList<ArrayList<String>>();

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                category_db = item.getString(TAG_CATEGORY);
                name_db = item.getString(TAG_NAME);
                price_db = item.getString(TAG_PRICE);
                image_db = item.getString(TAG_IMAGE);
                descript_db = item.getString(TAG_DESCRIPT);
                totalweight_db = item.getString(TAG_TOTALWEIGHT);
                calorie_db = item.getString(TAG_CALORIE);
                protein_db = item.getString(TAG_PROTEIN);
                sodium_db = item.getString(TAG_SODIUM);
                sugar_db = item.getString(TAG_SUGAR);
                saturatedfat_db = item.getString(TAG_SATURATEDFAT);
                allergy_db = item.getString(TAG_ALLERGY);
                origin_db = item.getString(TAG_ORIGIN);
                DB_item = new ArrayList<String>();
                DB_item.add(category_db);
                DB_item.add(name_db);
                DB_item.add(price_db);
                DB_item.add(image_db);
                DB_item.add(descript_db);
                DB_item.add(totalweight_db);
                DB_item.add(calorie_db);
                DB_item.add(protein_db);
                DB_item.add(sodium_db);
                DB_item.add(sugar_db);
                DB_item.add(saturatedfat_db);
                DB_item.add(allergy_db);
                DB_item.add(origin_db);
                DB_result_search.add(DB_item);
                setSearchResult(sttORrec);
                Log.d(TAG, "database result =" + DB_item.get(1));
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    void setSearchResult(String sttORrec) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        ArrayList<ImageView> searchboardImg = new ArrayList();
        ArrayList<TextView> searchboardName = new ArrayList();
        ArrayList<TextView> searchboardPrice = new ArrayList();
        ArrayList<TextView> searchboardInfo = new ArrayList();
        ArrayList<LinearLayout> searchboardBtn = new ArrayList();
        for(int i=0; i<6; i++) {
            int imgID = context.getResources().getIdentifier(sttORrec+"_menu_img"+i, "id", "com.example.s2t_kiosk");
            int nameID = context.getResources().getIdentifier(sttORrec+"_menu_name"+i, "id", "com.example.s2t_kiosk");
            int priceID = context.getResources().getIdentifier(sttORrec+"_menu_price"+i, "id", "com.example.s2t_kiosk");
            int btnID = context.getResources().getIdentifier(sttORrec+"_menu_btn"+i, "id", "com.example.s2t_kiosk");
            searchboardImg.add((ImageView) findViewById(imgID));
            searchboardName.add((TextView) findViewById(nameID));
            searchboardPrice.add((TextView) findViewById(priceID));
            searchboardBtn.add((LinearLayout) findViewById(btnID));
        }

        int db_size = DB_result_search.size();
        db_size = min(db_size,6);
        int i=0;
        for(; i<db_size; i++) {
            searchboardImg.get(i).setImageResource(context.getResources().getIdentifier(DB_result_search.get(i).get(3), "drawable", "com.example.s2t_kiosk"));
            searchboardImg.get(i).setVisibility(View.VISIBLE);
            searchboardName.get(i).setText(DB_result_search.get(i).get(1));
            searchboardPrice.get(i).setText(formatter.format(Integer.parseInt(DB_result_search.get(i).get(2)))+"원");
            searchboardBtn.get(i).setEnabled(true);
//            searchboardInfo.get(i).setText(DB_result_search.get(i).get(1));
        }
        for(; i < 6; i++){
            searchboardImg.get(i).setImageResource(R.drawable.burger_az);
            searchboardImg.get(i).setVisibility(View.INVISIBLE);
            searchboardName.get(i).setText("");
            searchboardPrice.get(i).setText("");
//            searchboardInfo.get(i).setText("");
            searchboardBtn.get(i).setEnabled(false);
        }
        // 이거 안먹어,,,,,고쳐야해
//        if(db_size == 0) {
//            stt_title.setText("검색 결과가 없습니다\n아래의 내용을 참고하여\n마이크 버튼을 누르고 말씀해 주세요");
//            stt_start.setVisibility(VISIBLE);
//            stt_ing.setVisibility(GONE);
//            stt_result.setVisibility(GONE);
//            mic_status = 0;
//        }
    }


    private void initV2Dialogflow() {
        try {
            InputStream dialogstream = getResources().openRawResource(R.raw.hystt);
            GoogleCredentials credentials = GoogleCredentials.fromStream(dialogstream);
            String projectID = ((ServiceAccountCredentials)credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectID, uuid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String msg = stt.getText().toString();
        if(msg.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter your query!", Toast.LENGTH_LONG).show();
        }
        else {
            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("ko-KR")).build();
            new RequestJavaV2Task(MainActivity.this, session, sessionsClient, queryInput).execute();
        }
    }

    public void callbackV2(DetectIntentResponse response) {
        stt_table = findViewById(R.id.stt_table);
        if(response != null) {
            String dialogReply = response.getQueryResult().getFulfillmentText();
            String[] parseReply = dialogReply.split("/");
            if(parseReply.length < 3){
                stt.setText(dialogReply);
                stt_table.setVisibility(View.INVISIBLE);
            }
            else {
                stt_table.setVisibility(VISIBLE);
                String[] search_keys = parseReply[2].split(",");
                String sqlString = "select * from menuboard ";
                switch (parseReply[0]) {
                    case "search_menu" :
                        sqlString += ("where name like (\"%" + search_keys[0]+"%\")");
                        break;
                    case "search_best_menu" :
                        if(!search_keys[0].equals("메뉴")) {
                            sqlString += "where category = \"" + search_keys[0] + "\" ";
                        }
                        sqlString += "order by sales DESC, price DESC LIMIT 8";
                        break;
                    case "search_new_menu" :
                        sqlString += "where name in (select name from new";
                        // New라는 테이블 만들어서 카테고리, 이름만 넣어두고, 그 테이블에 있는 친구들 전부 menuboard에서 찾아오기
                        if(!search_keys[0].equals("메뉴")) {
                            sqlString += " where category = \"" + search_keys[0] + "\"";
                        }
                        sqlString += ")";
                        break;
                    case "search_by_nutrient" :
                        switch (search_keys[1]) {
                            case "열량" :
                                search_keys[1] = "calorie";
                                break;
                            case "당류" :
                                search_keys[1] = "sugar";
                                break;
                            case "나트륨" :
                                search_keys[1] = "sodium";
                                break;
                            case "포화지방" :
                                search_keys[1] = "saturatedfat";
                                break;
                            case "총중량" :
                                search_keys[1] = "totalweight";
                                break;
                            case "단백질" :
                                search_keys[1] = "protein";
                                break;
                        }
                        String nutrientCateg;
                        if(!search_keys[0].equals("메뉴")) {
                            nutrientCateg = ("category = \"" + search_keys[0] + "\" and ");
                        }
                        else {
                            nutrientCateg = "";
                        }
                        if(search_keys[2].equals("AdjNutrient")) { // 영양성분이 ~인 경우
//                            select * from menuboard where category = "버거" and totalweight[nutrient] = 500[number];
//                            select * from menuboard where nutrient = number;
                            sqlString += ("where " + nutrientCateg + search_keys[1] + " = " + search_keys[3]);
                        }
                        else {
                            if(search_keys[3].equals("number")) {
                                nutrientCateg = nutrientCateg.substring(0,nutrientCateg.lastIndexOf("\"")+1);
                                if(!nutrientCateg.equals("")) {
                                    nutrientCateg = "where " + nutrientCateg;
                                }
                                // Top5
                                if(search_keys[2].equals("많은") || search_keys[2].equals("높은")) {
//                                    select * from menuboard where category = "버거" order by totalweight[nutrient] desc, price desc limit 8;
//                                    select * from menuboard order by totalweight[nutrient] desc, price desc limit 8;
                                    sqlString += (nutrientCateg + " order by " + search_keys[1] + " desc, price desc limit 8");
                                }
                                else {
//                                    select * from menuboard where category = "버거" order by totalweight asc, price desc limit 8;
//                                    select * from menuboard order by totalweight asc, price desc limit 8;
                                    sqlString += (nutrientCateg + " order by " + search_keys[1] + " asc, price desc limit 8");
                                }
                            }
                            else {
                                if(search_keys[2].equals("이상")) {
//                                    select * from menuboard where category = "버거" and totalweight >= 700 order by totalweight asc, price desc limit 8;
//                                    select * from menuboard where totalweight >= 700 order by totalweight asc, price desc limit 8;
                                    sqlString += ("where " + nutrientCateg + search_keys[1] + " >= " + search_keys[3] + " order by " + search_keys[1] + " asc, price desc limit 8");
                                }
                                else {
//                                    select * from menuboard where category = "버거" and totalweight <= 700 order by totalweight desc, price desc limit 8;
//                                    select * from menuboard where totalweight <= 700 order by totalweight desc, price desc limit 8;
                                    sqlString += ("where " + nutrientCateg + search_keys[1] + " <= " + search_keys[3] + " order by " + search_keys[1] + " desc, price desc limit 8");
                                }
                            }
                        }
                        break;
                    case "search_by_hotcold" :
//                        select * from menuboard where name in (select name from hotcold where hot = 1);
                        if(search_keys[1].equals("뜨거운")) {
                            search_keys[1] = "1";
                        }
                        else {
                            search_keys[1] = "0";
                        }
                        sqlString += ("where name in (select name from hotcold where hot = \"" + search_keys[1]);
                        if(!search_keys[0].equals("메뉴")) {
                            sqlString += ("\" and category = \"" + search_keys[0]);
                        }
                        sqlString += "\")";
                        break;
                    case "search_by_ingredient" :
                        sqlString += "where ";
                        if(!search_keys[0].equals("메뉴")) {
                            sqlString += ("category = \"" + search_keys[0] + "\" and ");
                        }
                        if(search_keys[2].equals("들어간")) {
                            sqlString += ("(allergy like (\"%" + search_keys[1] + "%\") or ingredient like (\"%" + search_keys[1] + "%\"))");
                        }
                        else {
                            sqlString += ("not (allergy like (\"%" + search_keys[1] + "%\") or ingredient like (\"%" + search_keys[1] + "%\"))");
                        }
                        break;
                    case "search_by_price" :
                        String priceCateg;
                        if(!search_keys[0].equals("메뉴")) {
                            priceCateg = ("category = \"" + search_keys[0] + "\" and ");
                        }
                        else {
                            priceCateg = "";
                        }
                        if(search_keys[1].equals("AdjPrice")) { // 가격이 ~인 경우
//                            Select * from menuboard where category = "버거" price = 3000 order by name desc limit 8;
//                            Select * from menuboard where price = 3000 order by name desc limit 8;
                            sqlString += ("where " + priceCateg + "price = " + search_keys[2]);
                        }
                        else {
                            if(search_keys[2].equals("number")) {
                                priceCateg = priceCateg.substring(0,priceCateg.lastIndexOf("\"")+1);
                                if(!priceCateg.equals("")) {
                                    priceCateg = "where " + priceCateg;
                                }
                                // Top5
                                if(search_keys[1].equals("비싼")) {
//                                    Select * from menuboard where category = "버거" order by price desc, name desc limit 8;
//                                    Select * from menuboard order by price desc, name desc limit 8;
                                    sqlString += (priceCateg + " order by price desc, name desc limit 8");
                                }
                                else {
//                                    Select * from menuboard where category = "버거" order by price asc, name desc limit 8;
//                                    Select * from menuboard order by price asc, name desc limit 8;
                                    sqlString += (priceCateg + " order by price asc, name desc limit 8");
                                }
                            }
                            else {
                                if(search_keys[1].equals("이상")) {
//                                    Select * from menuboard where category = "버거" and price >= 3000 order by price asc, name desc limit 8;
//                                    Select * from menuboard where price >= 3000 order by price asc, name desc limit 8;
                                    sqlString += ("where " + priceCateg + " price >= " + search_keys[2] + " order by price asc, name desc limit 8");
                                }
                                else {
//                                    select * from menuboard where category = "버거" and totalweight <= 700 order by totalweight desc, price desc limit 8;
//                                    select * from menuboard where totalweight <= 700 order by totalweight desc, price desc limit 8;
                                    sqlString += ("where " + priceCateg + " price <= " + search_keys[2] + " order by price desc, name desc limit 8");
                                }
                            }
                        }
                        break;
                    case "search_by_spicy" :
                        if(search_keys[1].equals("매운")) {
                            search_keys[1] = "1";
                        }
                        else {
                            search_keys[1] = "0";
                        }
                        sqlString += ("where spicy = \"" + search_keys[1]+"\"");
                        if(!search_keys[0].equals("메뉴")) {
                            sqlString += (" and category = \"" + search_keys[0] + "\"");
                        }
                        sqlString += " order by sales DESC, price DESC LIMIT 8";
                        break;
                }
                sqlString += ";";
                stt.setText(parseReply[1]);
                try {
                    URLConnector search_request = new URLConnector(serverUrl+"selectjson.php");
                    String search_result = search_request.PhPtest(sqlString);
                    mJsonString = search_result;
                    getResult("stt");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        else {
            stt.setText("There was some communication issue. Please Try again!");
        }
    }

    public void OnclickOrder(View view) {
        if(!list_adapter.isEmpty()){
            Intent order_intent = new Intent(getApplicationContext(), OrderActivity.class);
            startActivity(order_intent);
        }
    }
    public static void setTotalPrice(int total_int){
        DecimalFormat formatter = new DecimalFormat("###,###");
        total_price.setText(formatter.format(total_int)+"원");

    }
    public static void setTotalCnt(int cnt){
        total_cnt.setText(cnt+" 개");
    }
    public static ArrayList<ListViewBtnItem> getList() {
        return items;
    }

    public static void setList(ArrayList<ListViewBtnItem> list) {
        items = list;
    }

    public static ListViewBtnAdapter getList_adapter() {
        return list_adapter;
    }

    public static void setList_adapter(ListViewBtnAdapter adapter){
        list_adapter = adapter;
    }

    public void execRec(String tocsv){

//        String search_result = task_rec.execute(serverUrl + "exec.php", tocsv);
//        System.out.println(task_rec.execute(serverUrl + "exec.php",tocsv));
        tocsv = tocsv.replaceAll("\n","");
        try {
//            rec_start.setVisibility(GONE);
//            rec_ing.setVisibility(VISIBLE);
//            rec_result.setVisibility(GONE);
            URLConnector search_request = new URLConnector(serverUrl+"exec.php");
            String search_result = search_request.PhPtest(tocsv);
            mJsonString = search_result;
            getResult("rec");
            rec_start.setVisibility(GONE);
            rec_ing.setVisibility(GONE);
            rec_result.setVisibility(VISIBLE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void RecBack() {
        rec_start.setVisibility(VISIBLE);
        rec_ing.setVisibility(GONE);
        rec_result.setVisibility(GONE);
    }

    public void removeList(View v){
        items = new ArrayList<ListViewBtnItem>();
        listview = (ListView) findViewById(R.id.list);
        list_adapter = new ListViewBtnAdapter(this, R.layout.listview, items);
        listview.setAdapter(list_adapter);
        setTotalPrice(0);
        setTotalCnt(0);
    }
}