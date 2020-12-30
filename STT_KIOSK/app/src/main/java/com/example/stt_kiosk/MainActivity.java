package com.example.stt_kiosk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {

    public static String serverUrl = "http://172.30.1.16:80/";

    FragmentPagerAdapter adapterViewPager;

    Button clicked_category;

    ListView listview;
    public static ListViewBtnAdapter list_adapter;
    public static ArrayList<ListViewBtnItem> items;
    static TextView total_price;

    boolean IsMicOn = false;

    ViewPager vpPager;

    ImageView mic_btn;
    ImageButton back_btn;
    ImageButton next_btn;

    CircleIndicator indicator;

    RecommandAdapter adapter;
    BurgerAdapter adapter2;
    SetAdapter adapter3;
    DessertAdapter adapter4;
    DrinkAdapter adapter5;
    ChickenAdapter adapter6;

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
    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;
    String mJsonString1;

    TextView guide_start;
    TextView guide_start5;

    LinearLayout search_guide;
    LinearLayout menuboard;
    TableLayout searchboard;
    LinearLayout searchboard_layout;
    ImageView close;
    ImageView close2;

    ArrayList<ImageView> firstPageImg = new ArrayList<>();
    ArrayList<TextView> firstPageName = new ArrayList<>();
    ArrayList<TextView> firstPagePrice = new ArrayList<>();
    private Context context;
    ArrayList<String> nutrient;
    String allergy;
    String origin;
    public static String sqlString;

    //    for Dialogflow
    private String uuid = UUID.randomUUID().toString();
    private SessionsClient sessionsClient;
    private SessionName session;

    Intent intentSTT;
    SpeechRecognizer sttrec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        adapterViewPager = new FragmentAdapter(getSupportFragmentManager());
//        vpPager.setAdapter(adapterViewPager);
        changeView(1);
        this.context = getApplicationContext();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 5);
        }
        ImageView help = (ImageView) findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogHelp dialogHelp = new DialogHelp(MainActivity.this);
                dialogHelp.setCancelable(false);
                dialogHelp.show();
            }
        });

        menuboard = (LinearLayout) findViewById(R.id.menuboard);
        searchboard = (TableLayout) findViewById(R.id.searchboard);
        searchboard_layout = (LinearLayout) findViewById(R.id.searchboard_layout);
        search_guide = (LinearLayout) findViewById(R.id.search_guide);
        guide_start = (TextView) findViewById(R.id.guide_start);
        guide_start5 = (TextView) findViewById(R.id.guide_start5);
        close = (ImageView) findViewById(R.id.close);
        close2 = (ImageView) findViewById(R.id.close2);

        mic_btn = (ImageView) findViewById(R.id.mic_btn);
        Glide.with(this).load(R.raw.mic_off).into(mic_btn);
        mic_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(menuboard.getVisibility() == View.VISIBLE) {
                    menuboard.setVisibility(View.GONE);
                    search_guide.setVisibility(View.VISIBLE);
                    guide_start.setText("음성인식 검색을 시작합니다.");
                    guide_start5.setText("");
                }
                if(IsMicOn == false){
                    Glide.with(v).load(R.raw.mic_on).into(mic_btn);
                    IsMicOn = true;
                    Log.d("stt before", "before function");
                    inputVoice(stt);
                    Log.d("stt after", "after function");
                }
                else{
                    Glide.with(v).load(R.raw.mic_off).into(mic_btn);
                    IsMicOn = false;
                    sttrec.destroy();
                }
            }
        });

        final Button rec_btn = (Button) findViewById(R.id.rec_btn) ;
        clicked_category = rec_btn;
        rec_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_category.setBackgroundResource(R.drawable.radiustop);
                clicked_category.setTextColor(Color.parseColor("#ffffff"));
                rec_btn.setBackgroundResource(R.drawable.radiustopwhite);
                rec_btn.setTextColor(Color.parseColor("#272727"));
                clicked_category = rec_btn;
                changeView(1) ;
            }
        });

        final Button bg_btn = (Button) findViewById(R.id.bg_btn) ;
        bg_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_category.setBackgroundResource(R.drawable.radiustop);
                clicked_category.setTextColor(Color.parseColor("#ffffff"));
                bg_btn.setBackgroundResource(R.drawable.radiustopwhite);
                bg_btn.setTextColor(Color.parseColor("#272727"));
                clicked_category = bg_btn;
                changeView(2) ;
            }
        });
        final Button set_btn = (Button) findViewById(R.id.set_btn) ;
        set_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_category.setBackgroundResource(R.drawable.radiustop);
                clicked_category.setTextColor(Color.parseColor("#ffffff"));
                set_btn.setBackgroundResource(R.drawable.radiustopwhite);
                set_btn.setTextColor(Color.parseColor("#272727"));
                clicked_category = set_btn;
                changeView(3) ;
            }
        });

        final Button des_btn = (Button) findViewById(R.id.des_btn) ;
        des_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_category.setBackgroundResource(R.drawable.radiustop);
                clicked_category.setTextColor(Color.parseColor("#ffffff"));
                des_btn.setBackgroundResource(R.drawable.radiustopwhite);
                des_btn.setTextColor(Color.parseColor("#272727"));
                clicked_category = des_btn;
                changeView(4) ;
            }
        });

        final Button dri_btn = (Button) findViewById(R.id.dri_btn) ;
        dri_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_category.setBackgroundResource(R.drawable.radiustop);
                clicked_category.setTextColor(Color.parseColor("#ffffff"));
                dri_btn.setBackgroundResource(R.drawable.radiustopwhite);
                dri_btn.setTextColor(Color.parseColor("#272727"));
                clicked_category = dri_btn;
                changeView(5) ;
            }
        });

        final Button chi_btn = (Button) findViewById(R.id.chi_btn) ;
        chi_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_category.setBackgroundResource(R.drawable.radiustop);
                clicked_category.setTextColor(Color.parseColor("#ffffff"));
                chi_btn.setBackgroundResource(R.drawable.radiustopwhite);
                chi_btn.setTextColor(Color.parseColor("#272727"));
                clicked_category = chi_btn;
                changeView(6) ;
            }
        });

        back_btn = (ImageButton) findViewById(R.id.back_btn);
        next_btn = (ImageButton) findViewById(R.id.next_btn);
        back_btn.setOnClickListener(myListener);
        next_btn.setOnClickListener(myListener);

        items = new ArrayList<ListViewBtnItem>();
        listview = (ListView) findViewById(R.id.list);
        list_adapter = new ListViewBtnAdapter(this, R.layout.listview, items);
        listview.setAdapter(list_adapter);
        total_price = (TextView) findViewById(R.id.total_price);

        mArrayList = new ArrayList<>();

        MainActivity.GetData task = new MainActivity.GetData();
        task.execute(serverUrl + "getjson.php");
        URLConnector request = null;
        stt = findViewById(R.id.stt_window);
        initV2Dialogflow();
        NetworkUtil.setNetworkPolicy();
    }

    private void changeView(int index) {

        // 자식(Children) 뷰들에 대한 참조 획득.
        vpPager = (ViewPager) findViewById(R.id.vpPager) ;
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        // index에 따라 자식(Children) 뷰 들의 visibility 설정.
        switch (index) {
            case 1 :
                adapter = new RecommandAdapter(getLayoutInflater());
                vpPager.setAdapter(adapter);
                break ;
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

//    public void MicOnClick(View v){
//        if(IsMicOn == false){
//            Glide.with(this).load(R.raw.mic_on).into(mic_btn);
//            IsMicOn = true;
//            Log.d("stt before", "before function");
//            inputVoice(stt);
//            Log.d("stt after", "after function");
//        }
//        else{
//            Glide.with(this).load(R.raw.mic_off).into(mic_btn);
//            IsMicOn = false;
//        }
//    }


    public void MenuOnClick(View v)
    {
        intent = new Intent(this, com.example.stt_kiosk.PopupActivity.class);
        stream = new ByteArrayOutputStream();
        String cat = (String) clicked_category.getText();
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
            case R.id.menu_btn1:
                name = (TextView) findViewById(R.id.menu_name1);
                price = (TextView) findViewById(R.id.menu_price1);
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
                img = (ImageView) findViewById(R.id.menu_img1);
                break;
            case R.id.menu_btn2:
                name = (TextView) findViewById(R.id.menu_name2);
                price = (TextView) findViewById(R.id.menu_price2);
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
                img = findViewById(R.id.menu_img2);
                break;
            case R.id.menu_btn3:
                name = (TextView) findViewById(R.id.menu_name3);
                price = (TextView) findViewById(R.id.menu_price3);
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
                img = findViewById(R.id.menu_img3);
                break;
            case R.id.menu_btn4:
                name = (TextView) findViewById(R.id.menu_name4);
                price = (TextView) findViewById(R.id.menu_price4);
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
                img = findViewById(R.id.menu_img4);
                break;
            case R.id.menu_btn5:
                name = (TextView) findViewById(R.id.menu_name5);
                price = (TextView) findViewById(R.id.menu_price5);
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
                img = findViewById(R.id.menu_img5);
                break;
            case R.id.menu_btn6:
                name = (TextView) findViewById(R.id.menu_name6);
                price = (TextView) findViewById(R.id.menu_price6);
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
                img = findViewById(R.id.menu_img6);
                break;
            case R.id.menu_btn7:
                name = (TextView) findViewById(R.id.menu_name7);
                price = (TextView) findViewById(R.id.menu_price7);
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
                img = findViewById(R.id.menu_img7);
                break;
            case R.id.menu_btn8:
                name = (TextView) findViewById(R.id.menu_name8);
                price = (TextView) findViewById(R.id.menu_price8);
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
                img = findViewById(R.id.menu_img8);
                break;
            case R.id.menu_btn9:
                name = (TextView) findViewById(R.id.menu_name9);
                price = (TextView) findViewById(R.id.menu_price9);
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
                img = findViewById(R.id.menu_img9);
                break;
            case R.id.menu_btn11:
                name = (TextView) findViewById(R.id.menu_name11);
                price = (TextView) findViewById(R.id.menu_price11);
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
                img = findViewById(R.id.menu_img11);
                break;
            case R.id.menu_btn12:
                name = (TextView) findViewById(R.id.menu_name12);
                price = (TextView) findViewById(R.id.menu_price12);
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
                img = findViewById(R.id.menu_img12);
                break;
            case R.id.menu_btn13:
                name = (TextView) findViewById(R.id.menu_name13);
                price = (TextView) findViewById(R.id.menu_price13);
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
                img = findViewById(R.id.menu_img13);
                break;
            case R.id.menu_btn14:
                name = (TextView) findViewById(R.id.menu_name14);
                price = (TextView) findViewById(R.id.menu_price14);
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
                img = findViewById(R.id.menu_img14);
                break;
            case R.id.menu_btn15:
                name = (TextView) findViewById(R.id.menu_name15);
                price = (TextView) findViewById(R.id.menu_price15);
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
                img = findViewById(R.id.menu_img15);
                break;
            case R.id.menu_btn16:
                name = (TextView) findViewById(R.id.menu_name16);
                price = (TextView) findViewById(R.id.menu_price16);
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
                img = findViewById(R.id.menu_img16);
                break;
            case R.id.menu_btn17:
                name = (TextView) findViewById(R.id.menu_name17);
                price = (TextView) findViewById(R.id.menu_price17);
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
                img = findViewById(R.id.menu_img17);
                break;
            case R.id.menu_btn18:
                name = (TextView) findViewById(R.id.menu_name18);
                price = (TextView) findViewById(R.id.menu_price18);
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
                img = findViewById(R.id.menu_img18);
                break;
            case R.id.menu_btn19:
                name = (TextView) findViewById(R.id.menu_name19);
                price = (TextView) findViewById(R.id.menu_price19);
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
                img = findViewById(R.id.menu_img19);
                break;
            case R.id.menu_btn21:
                name = (TextView) findViewById(R.id.menu_name21);
                price = (TextView) findViewById(R.id.menu_price21);
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
                img = findViewById(R.id.menu_img21);
                break;
            case R.id.menu_btn22:
                name = (TextView) findViewById(R.id.menu_name22);
                price = (TextView) findViewById(R.id.menu_price22);
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
                img = findViewById(R.id.menu_img22);
                break;
            case R.id.menu_btn23:
                name = (TextView) findViewById(R.id.menu_name23);
                price = (TextView) findViewById(R.id.menu_price23);
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
                img = findViewById(R.id.menu_img23);
                break;
            case R.id.menu_btn24:
                name = (TextView) findViewById(R.id.menu_name24);
                price = (TextView) findViewById(R.id.menu_price24);
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
                img = findViewById(R.id.menu_img24);
                break;
            case R.id.menu_btn25:
                name = (TextView) findViewById(R.id.menu_name25);
                price = (TextView) findViewById(R.id.menu_price25);
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
                img = findViewById(R.id.menu_img25);
                break;
            case R.id.menu_btn26:
                name = (TextView) findViewById(R.id.menu_name26);
                price = (TextView) findViewById(R.id.menu_price26);
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
                img = findViewById(R.id.menu_img26);
                break;
            case R.id.menu_btn27:
                name = (TextView) findViewById(R.id.menu_name27);
                price = (TextView) findViewById(R.id.menu_price27);
                exp = db_cat.get(24).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(24).get(5));
                nutrient.add(db_cat.get(24).get(6));
                nutrient.add(db_cat.get(24).get(7));
                nutrient.add(db_cat.get(24).get(8));
                nutrient.add(db_cat.get(24).get(9));
                nutrient.add(db_cat.get(24).get(10));
                allergy = db_cat.get(24).get(11);
                origin = db_cat.get(24).get(12);
                img = findViewById(R.id.menu_img27);
                break;
            case R.id.menu_btn28:
                name = (TextView) findViewById(R.id.menu_name28);
                price = (TextView) findViewById(R.id.menu_price28);
                exp = db_cat.get(25).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(25).get(5));
                nutrient.add(db_cat.get(25).get(6));
                nutrient.add(db_cat.get(25).get(7));
                nutrient.add(db_cat.get(25).get(8));
                nutrient.add(db_cat.get(25).get(9));
                nutrient.add(db_cat.get(25).get(10));
                allergy = db_cat.get(25).get(11);
                origin = db_cat.get(25).get(12);
                img = findViewById(R.id.menu_img28);
                break;
            case R.id.menu_btn29:
                name = (TextView) findViewById(R.id.menu_name29);
                price = (TextView) findViewById(R.id.menu_price29);
                exp = db_cat.get(26).get(4);
                nutrient = new ArrayList<String>();
                nutrient.add(db_cat.get(26).get(5));
                nutrient.add(db_cat.get(26).get(6));
                nutrient.add(db_cat.get(26).get(7));
                nutrient.add(db_cat.get(26).get(8));
                nutrient.add(db_cat.get(26).get(9));
                nutrient.add(db_cat.get(26).get(10));
                allergy = db_cat.get(26).get(11);
                origin = db_cat.get(26).get(12);
                img = findViewById(R.id.menu_img29);
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
        intent = new Intent(this, com.example.stt_kiosk.PopupActivity.class);
        stream = new ByteArrayOutputStream();
        ArrayList<ArrayList<String>> db_cat = new ArrayList<>();
        db_cat = DB_result_search;
        String cat = (String) "";

        switch (v.getId()){
            case R.id.search_btn1:
                name = (TextView) findViewById(R.id.search_name1);
                price = (TextView) findViewById(R.id.search_price1);
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
                img = (ImageView) findViewById(R.id.search_img1);
                break;
            case R.id.search_btn2:
                name = (TextView) findViewById(R.id.search_name2);
                price = (TextView) findViewById(R.id.search_price2);
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
                img = findViewById(R.id.search_img2);
                break;
            case R.id.search_btn3:
                name = (TextView) findViewById(R.id.search_name3);
                price = (TextView) findViewById(R.id.search_price3);
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
                img = findViewById(R.id.search_img3);
                break;
            case R.id.search_btn4:
                name = (TextView) findViewById(R.id.search_name4);
                price = (TextView) findViewById(R.id.search_price4);
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
                img = findViewById(R.id.search_img4);
                break;
            case R.id.menu_btn5:
                name = (TextView) findViewById(R.id.search_name5);
                price = (TextView) findViewById(R.id.search_price5);
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
                img = findViewById(R.id.search_img5);
                break;
            case R.id.search_btn6:
                name = (TextView) findViewById(R.id.search_name6);
                price = (TextView) findViewById(R.id.search_price6);
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
                img = findViewById(R.id.search_img6);
                break;
            case R.id.search_btn7:
                name = (TextView) findViewById(R.id.search_name7);
                price = (TextView) findViewById(R.id.search_price7);
                cat = db_cat.get(6).get(0);
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
                img = findViewById(R.id.search_img7);
                break;
            case R.id.search_btn8:
                name = (TextView) findViewById(R.id.search_name8);
                price = (TextView) findViewById(R.id.search_price8);
                cat = db_cat.get(7).get(0);
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
                img = findViewById(R.id.search_img8);
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

    public static void setTotalPrice(int total_int){
        DecimalFormat formatter = new DecimalFormat("###,###");
        total_price.setText(formatter.format(total_int)+"원");

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

    public void OnclickOrder(View view) {
        if(!list_adapter.isEmpty()){
            Intent order_intent = new Intent(getApplicationContext(), OrderActivity.class);
            startActivity(order_intent);
        }
    }

    public void OnClickClose(View view){
        sttrec.destroy();
        IsMicOn = false;
        Glide.with(view).load(R.raw.mic_off).into(mic_btn);
        search_guide.setVisibility(View.GONE);
        searchboard_layout.setVisibility(View.GONE);
        menuboard.setVisibility(View.VISIBLE);
        stt.setText("음성인식 검색 기능을 사용하시려면\n마이크 버튼을 눌러주세요.");
    }

    private class GetData extends AsyncTask<String, Void, String>{
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
                stt.setText(errorString);
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
            intentSTT = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
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
                    search_guide.setVisibility(View.VISIBLE);
                    searchboard_layout.setVisibility(View.GONE);
//                    toast("오류발생 : " + error);
                    switch(error){
                        case 1:
                            stt.setText("Network Timeout");
                            break;
                        case 2:
                            stt.setText("Network Error");
                            break;
                        case 3:
                            stt.setText("Record Error");
                            break;
                        case 4:
                            stt.setText("Server Error");
                            break;
                        case 5:
                            stt.setText("Client Error");
                            break;
                        case 6:
                            stt.setText("아무 것도 못들었어요\n마이크 버튼을 누르고\n다시 한 번 말씀해주세요.");
                            break;
                        case 7:
                            stt.setText("마이크 버튼을 누르고\n다시 한 번 말씀해주세요.");
                            break;
                        case 8:
                            stt.setText("Busy Error");
                            break;
                        case 9:
                            stt.setText("Permission");
                            break;
                        default :
                            stt.setText("error,,,");
                    }
                    Log.d("onError", "error : "+ error);
                    Glide.with(context).load(R.raw.mic_off).into(mic_btn);
                    IsMicOn = false;
                    sttrec.destroy();
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);
                    stt.setText( result.get(0) + "\n");
                    Glide.with(context).load(R.raw.mic_off).into(mic_btn);
                    IsMicOn = false;
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
    private void getResult(int keyword){
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
                setSearchResult(keyword);
                Log.d(TAG, "database result =" + DB_item.get(1));
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        if(DB_result_search.size() == 0 ){
            search_guide.setVisibility(View.VISIBLE);
            searchboard_layout.setVisibility(View.GONE);
            guide_start.setText("검색 결과가 없습니다.");
            guide_start5.setText("마이크 버튼을 다시 누른 후 말씀해 주세요.");


        }


    }

    void setSearchResult(int keyword) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        ArrayList<ImageView> searchboardImg = new ArrayList();
        ArrayList<TextView> searchboardName = new ArrayList();
        ArrayList<TextView> searchboardPrice = new ArrayList();
        ArrayList<TextView> searchboardInfo = new ArrayList();
        ArrayList<LinearLayout> searchboardBtn = new ArrayList();

        for(int i=1; i<9; i++) {
            int imgID = context.getResources().getIdentifier("search_img"+i, "id", "com.example.stt_kiosk");
            int nameID = context.getResources().getIdentifier("search_name"+i, "id", "com.example.stt_kiosk");
            int priceID = context.getResources().getIdentifier("search_price"+i, "id", "com.example.stt_kiosk");
            int infoID = context.getResources().getIdentifier("search_info"+i, "id", "com.example.stt_kiosk");
            int btnID = context.getResources().getIdentifier("search_btn"+i, "id", "com.example.stt_kiosk");
            searchboardImg.add((ImageView) findViewById(imgID));
            searchboardName.add((TextView) findViewById(nameID));
            searchboardPrice.add((TextView) findViewById(priceID));
            searchboardInfo.add((TextView) findViewById(infoID));
            searchboardBtn.add((LinearLayout) findViewById(btnID));
        }
        search_guide.setVisibility(View.GONE);
        searchboard_layout.setVisibility(View.VISIBLE);
        int db_size = DB_result_search.size();
        db_size = min(db_size,8);
        int i=0;
        for(; i<db_size; i++) {
            searchboardImg.get(i).setImageResource(context.getResources().getIdentifier(DB_result_search.get(i).get(3), "drawable", "com.example.stt_kiosk"));
            searchboardImg.get(i).setVisibility(View.VISIBLE);
            searchboardName.get(i).setText(DB_result_search.get(i).get(1));
            searchboardPrice.get(i).setText(formatter.format(Integer.parseInt(DB_result_search.get(i).get(2)))+"원");
            searchboardBtn.get(i).setEnabled(true);
            if(keyword > 0) {
                searchboardInfo.get(i).setText(DB_result_search.get(i).get(keyword));
            }
            else {
                searchboardInfo.get(i).setText("");
            }
        }
        for(; i < 8; i++){
            searchboardImg.get(i).setImageResource(R.drawable.burger_az);
            searchboardImg.get(i).setVisibility(View.INVISIBLE);
            searchboardName.get(i).setText("");
            searchboardPrice.get(i).setText("");
            searchboardInfo.get(i).setText("");
            searchboardBtn.get(i).setEnabled(false);
        }
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
        int keyword = 0;
        if(response != null) {
            String dialogReply = response.getQueryResult().getFulfillmentText();
            String[] parseReply = dialogReply.split("/");
            if(parseReply.length < 3){
                stt.setText(dialogReply);
            }
            else {
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
                                keyword = 6;
                                search_keys[1] = "calorie";
                                break;
                            case "당류" :
                                keyword = 9;
                                search_keys[1] = "sugar";
                                break;
                            case "나트륨" :
                                keyword = 8;
                                search_keys[1] = "sodium";
                                break;
                            case "포화지방" :
                                keyword = 10;
                                search_keys[1] = "saturatedfat";
                                break;
                            case "총중량" :
                                keyword = 5;
                                search_keys[1] = "totalweight";
                                break;
                            case "단백질" :
                                keyword = 7;
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
                    getResult(keyword);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        else {
            stt.setText("There was some communication issue. Please Try again!");
        }
    }
}