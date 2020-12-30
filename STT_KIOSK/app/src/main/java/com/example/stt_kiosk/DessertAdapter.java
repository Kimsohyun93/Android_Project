package com.example.stt_kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.stt_kiosk.MainActivity.DB_result_dessert;

public class DessertAdapter extends PagerAdapter {

    private static String TAG = "phptest_DessertAdapter";

    LayoutInflater inflater;
    Context context;
    DecimalFormat formatter = new DecimalFormat("###,###");
    int dbsize = DB_result_dessert.size();

    public DessertAdapter(LayoutInflater inflater, Context context) {

        // TODO Auto-generated constructor stub



        //전달 받은 LayoutInflater를 멤버변수로 전달

        this.inflater=inflater;
        this.context = context;
    }



    //PagerAdapter가 가지고 잇는 View의 개수를 리턴

    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴

    @Override

    public int getCount() {

        // TODO Auto-generated method stub

        if(dbsize%9 == 0){
            return (int)(dbsize/9);
        }
        return (int)(dbsize/9)+1; //이미지 개수 리턴(그림이 10개라서 10을 리턴)

    }



    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출

    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.

    //첫번째 파라미터 : ViewPager

    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)

    @Override

    public Object instantiateItem(ViewGroup container, int position) {

        // TODO Auto-generated method stub



        View view=null;


        //새로운 View 객체를 Layoutinflater를 이용해서 생성

        //만들어질 View의 설계는 res폴더>>layout폴더>>viewpater_childview.xml 레이아웃 파일 사용

        view= inflater.inflate(R.layout.fragment_first, null);


        //만들어진 View안에 있는 ImageView 객체 참조

        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        ArrayList<ImageView> firstPageImg = new ArrayList<>();
        ArrayList<TextView> firstPageName = new ArrayList<>();
        ArrayList<TextView> firstPagePrice = new ArrayList<>();
        ArrayList<LinearLayout> firstPageBtn = new ArrayList<>();

        ArrayList<ImageView> secondPageImg = new ArrayList<>();
        ArrayList<TextView> secondPageName = new ArrayList<>();
        ArrayList<TextView> secondPagePrice = new ArrayList<>();
        ArrayList<LinearLayout> secondPageBtn = new ArrayList<>();

        ArrayList<ImageView> thirdPageImg = new ArrayList<>();
        ArrayList<TextView> thirdPageName = new ArrayList<>();
        ArrayList<TextView> thirdPagePrice = new ArrayList<>();
        ArrayList<LinearLayout> thirdPageBtn = new ArrayList<>();

        int firstIdx = Math.min(9, dbsize);
        int secondIdx = Math.min(9, dbsize-9);
        int thirdIdx = Math.min(9, dbsize-18);

        for(int i = 1; i < 10; i++){
            int imgID = context.getResources().getIdentifier("menu_img"+i, "id", "com.example.stt_kiosk");
            int nameID = context.getResources().getIdentifier("menu_name"+i, "id", "com.example.stt_kiosk");
            int priceID = context.getResources().getIdentifier("menu_price"+i, "id", "com.example.stt_kiosk");
            int btnID = context.getResources().getIdentifier("menu_btn"+i, "id", "com.example.stt_kiosk");
            firstPageImg.add((ImageView) view.findViewById(imgID));
            firstPageName.add((TextView) view.findViewById(nameID));
            firstPagePrice.add((TextView) view.findViewById(priceID));
            firstPageBtn.add((LinearLayout) view.findViewById(btnID));
        }


        switch (position){
            case 0:
                int i = 0;
                for(; i < firstIdx; i++){
                    firstPageImg.get(i).setImageResource(context.getResources().getIdentifier(DB_result_dessert.get(i).get(3), "drawable", "com.example.stt_kiosk"));
                    firstPageName.get(i).setText(DB_result_dessert.get(i).get(1));
                    firstPagePrice.get(i).setText(formatter.format(Integer.parseInt(DB_result_dessert.get(i).get(2)))+"원");
                }
                for(; i < 9; i++){
                    firstPageImg.get(i).setImageResource(R.drawable.white);
                    firstPageName.get(i).setText("");
                    firstPagePrice.get(i).setText("");
                    firstPageBtn.get(i).setEnabled(false);;
                }
                break;
            case 1:
                view= inflater.inflate(R.layout.fragment_second, null);
                for(int j = 11; j < 20; j++){
                    int imgID = context.getResources().getIdentifier("menu_img"+j, "id", "com.example.stt_kiosk");
                    int nameID = context.getResources().getIdentifier("menu_name"+j, "id", "com.example.stt_kiosk");
                    int priceID = context.getResources().getIdentifier("menu_price"+j, "id", "com.example.stt_kiosk");
                    int btnID = context.getResources().getIdentifier("menu_btn"+j, "id", "com.example.stt_kiosk");
                    secondPageImg.add((ImageView) view.findViewById(imgID));
                    secondPageName.add((TextView) view.findViewById(nameID));
                    secondPagePrice.add((TextView) view.findViewById(priceID));
                    secondPageBtn.add((LinearLayout) view.findViewById(btnID));
                }
                int j = 0;
                for(; j < secondIdx; j++) {
                    secondPageImg.get(j).setImageResource(context.getResources().getIdentifier(DB_result_dessert.get(j + 9).get(3), "drawable", "com.example.stt_kiosk"));
                    secondPageName.get(j).setText(DB_result_dessert.get(j + 9).get(1));
                    secondPagePrice.get(j).setText(formatter.format(Integer.parseInt(DB_result_dessert.get(j + 9).get(2))) + "원");
                }
                for(; j < 9; j++){
                    secondPageImg.get(j).setImageResource(R.drawable.white);
                    secondPageName.get(j).setText("");
                    secondPagePrice.get(j).setText("");
                    secondPageBtn.get(j).setEnabled(false);
                }
                break;
            case 2:
                view= inflater.inflate(R.layout.fragment_third, null);
                for(int k = 21; k < 30; k++){
                    int imgID = context.getResources().getIdentifier("menu_img"+k, "id", "com.example.stt_kiosk");
                    int nameID = context.getResources().getIdentifier("menu_name"+k, "id", "com.example.stt_kiosk");
                    int priceID = context.getResources().getIdentifier("menu_price"+k, "id", "com.example.stt_kiosk");
                    int btnID = context.getResources().getIdentifier("menu_btn"+k, "id", "com.example.stt_kiosk");
                    thirdPageImg.add((ImageView) view.findViewById(imgID));
                    thirdPageName.add((TextView) view.findViewById(nameID));
                    thirdPagePrice.add((TextView) view.findViewById(priceID));
                    thirdPageBtn.add((LinearLayout) view.findViewById(btnID));
                }
                int k = 0;
                for(; k < thirdIdx; k++) {
                    thirdPageImg.get(k).setImageResource(context.getResources().getIdentifier(DB_result_dessert.get(k + 18).get(3), "drawable", "com.example.stt_kiosk"));
                    thirdPageName.get(k).setText(DB_result_dessert.get(k + 18).get(1));
                    thirdPagePrice.get(k).setText(formatter.format(Integer.parseInt(DB_result_dessert.get(k + 18).get(2))) + "원");
                }
                for(; k < 9; k++){
                    thirdPageImg.get(k).setImageResource(R.drawable.white);
                    thirdPageName.get(k).setText("");
                    thirdPagePrice.get(k).setText("");
                    thirdPageBtn.get(k).setEnabled(false);
                }
                break;
        }





        //ViewPager에 만들어 낸 View 추가

        container.addView(view);



        //Image가 세팅된 View를 리턴

        return view;

    }



    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.

    //첫번째 파라미터 : ViewPager

    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)

    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)

    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {

        // TODO Auto-generated method stub



        //ViewPager에서 보이지 않는 View는 제거

        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시

        container.removeView((View)object);



    }



    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드

    @Override

    public boolean isViewFromObject(View v, Object obj) {

        // TODO Auto-generated method stub

        return v==obj;

    }



}