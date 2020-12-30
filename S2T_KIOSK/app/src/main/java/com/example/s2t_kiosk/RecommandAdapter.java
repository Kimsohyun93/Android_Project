package com.example.s2t_kiosk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import java.text.DecimalFormat;

public class RecommandAdapter extends PagerAdapter {



    LayoutInflater inflater;



    public RecommandAdapter(LayoutInflater inflater) {

        // TODO Auto-generated constructor stub



        //전달 받은 LayoutInflater를 멤버변수로 전달

        this.inflater=inflater;

    }



    //PagerAdapter가 가지고 잇는 View의 개수를 리턴

    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴

    @Override

    public int getCount() {

        // TODO Auto-generated method stub

        return 2; //이미지 개수 리턴(그림이 10개라서 10을 리턴)

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

        DecimalFormat formatter = new DecimalFormat("###,###");

        //만들어진 View안에 있는 ImageView 객체 참조

        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        ImageView img1 = (ImageView) view.findViewById(R.id.menu_img1);
        ImageView img2 = (ImageView) view.findViewById(R.id.menu_img2);
        ImageView img3 = (ImageView) view.findViewById(R.id.menu_img3);
        ImageView img4 = (ImageView) view.findViewById(R.id.menu_img4);
        ImageView img5 = (ImageView) view.findViewById(R.id.menu_img5);
        ImageView img6 = (ImageView) view.findViewById(R.id.menu_img6);
        ImageView img7 = (ImageView) view.findViewById(R.id.menu_img7);
        ImageView img8 = (ImageView) view.findViewById(R.id.menu_img8);
        ImageView img9 = (ImageView) view.findViewById(R.id.menu_img9);

        TextView n1 = (TextView) view.findViewById(R.id.menu_name1);
        TextView n2 = (TextView) view.findViewById(R.id.menu_name2);
        TextView n3 = (TextView) view.findViewById(R.id.menu_name3);
        TextView n4 = (TextView) view.findViewById(R.id.menu_name4);
        TextView n5 = (TextView) view.findViewById(R.id.menu_name5);
        TextView n6 = (TextView) view.findViewById(R.id.menu_name6);
        TextView n7 = (TextView) view.findViewById(R.id.menu_name7);
        TextView n8 = (TextView) view.findViewById(R.id.menu_name8);
        TextView n9 = (TextView) view.findViewById(R.id.menu_name9);

        TextView p1 = (TextView) view.findViewById(R.id.menu_price1);
        TextView p2 = (TextView) view.findViewById(R.id.menu_price2);
        TextView p3 = (TextView) view.findViewById(R.id.menu_price3);
        TextView p4 = (TextView) view.findViewById(R.id.menu_price4);
        TextView p5 = (TextView) view.findViewById(R.id.menu_price5);
        TextView p6 = (TextView) view.findViewById(R.id.menu_price6);
        TextView p7 = (TextView) view.findViewById(R.id.menu_price7);
        TextView p8 = (TextView) view.findViewById(R.id.menu_price8);
        TextView p9 = (TextView) view.findViewById(R.id.menu_price9);

        switch (position){
            case 0:
                img1.setImageResource(R.drawable.burger_az);
                img2.setImageResource(R.drawable.burger_az);
                img3.setImageResource(R.drawable.burger_az);
                img4.setImageResource(R.drawable.burger_az);
                img5.setImageResource(R.drawable.burger_az);
                img6.setImageResource(R.drawable.burger_az);
                img7.setImageResource(R.drawable.burger_az);
                img8.setImageResource(R.drawable.burger_az);
                img9.setImageResource(R.drawable.burger_az);

                n1.setText("AZ버거");

                int price1 = 3000;
                p1.setText(formatter.format(price1)+"원");


                break;
            case 1:
                view= inflater.inflate(R.layout.fragment_second, null);

                ImageView img11 = (ImageView) view.findViewById(R.id.menu_img11);
                ImageView img12 = (ImageView) view.findViewById(R.id.menu_img12);
                ImageView img13 = (ImageView) view.findViewById(R.id.menu_img13);
                ImageView img14 = (ImageView) view.findViewById(R.id.menu_img14);
                ImageView img15 = (ImageView) view.findViewById(R.id.menu_img15);
                ImageView img16 = (ImageView) view.findViewById(R.id.menu_img16);
                ImageView img17 = (ImageView) view.findViewById(R.id.menu_img17);
                ImageView img18 = (ImageView) view.findViewById(R.id.menu_img18);
                ImageView img19 = (ImageView) view.findViewById(R.id.menu_img19);

                TextView n11 = (TextView) view.findViewById(R.id.menu_name11);
                TextView n12 = (TextView) view.findViewById(R.id.menu_name12);
                TextView n13 = (TextView) view.findViewById(R.id.menu_name13);
                TextView n14 = (TextView) view.findViewById(R.id.menu_name14);
                TextView n15 = (TextView) view.findViewById(R.id.menu_name15);
                TextView n16 = (TextView) view.findViewById(R.id.menu_name16);
                TextView n17 = (TextView) view.findViewById(R.id.menu_name17);
                TextView n18 = (TextView) view.findViewById(R.id.menu_name18);
                TextView n19 = (TextView) view.findViewById(R.id.menu_name19);

                img11.setImageResource(R.drawable.burger_chicken);
                img12.setImageResource(R.drawable.burger_chicken);
                img13.setImageResource(R.drawable.burger_chicken);
                img14.setImageResource(R.drawable.burger_chicken);
                img15.setImageResource(R.drawable.burger_chicken);
                img16.setImageResource(R.drawable.burger_chicken);
                img17.setImageResource(R.drawable.burger_chicken);
                img18.setImageResource(R.drawable.burger_chicken);
                img19.setImageResource(R.drawable.burger_chicken);

                n11.setText("치킨버거1");
                n12.setText("치킨버거2");

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