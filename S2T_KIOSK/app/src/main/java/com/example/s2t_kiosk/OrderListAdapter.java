package com.example.s2t_kiosk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter {
    static String cart="";
    DecimalFormat formatter = new DecimalFormat("###,###");
    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.
    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener ;
//    int total_int = 0;


    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    OrderListAdapter(Context context, int resource, ArrayList<ListViewBtnItem> list) {
        super(context, resource, list) ;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;

    }

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(position == 0){
            cart = "";
        }
        final int pos = position ;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        final ImageView order_img = (ImageView) convertView.findViewById(R.id.order_img);
        final TextView order_list = (TextView) convertView.findViewById(R.id.order_list);
        final TextView order_cnt = (TextView) convertView.findViewById(R.id.order_cnt);
        final TextView order_option = (TextView) convertView.findViewById(R.id.order_option);
        final TextView price_order = (TextView) convertView.findViewById(R.id.price_order);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewBtnItem listViewItem = (ListViewBtnItem) getItem(position);

//        formatter.format(t_price)+"원"
        // 아이템 내 각 위젯에 데이터 반영
        ArrayList<ListViewBtnItem> items = OrderActivity.getList();
        listViewItem.setPrice(""+formatter.format(listViewItem.real_price_list)+"원");
        byte[] arr = listViewItem.getImg();
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        order_img.setImageBitmap(image);
        order_list.setText(listViewItem.getMenu());
        order_cnt.setText(listViewItem.getNumber());
        price_order.setText(listViewItem.getPrice());
        String dessert_str ;
        String drink_str ;
        String topping_str = "";

        if(items.get(position).cat_list.equals("세트")){
            dessert_str = items.get(position).getDessert();
            drink_str = ", " + items.get(position).getDrink();
            if(!items.get(position).isEmptyTopping(items.get(position).getTopping())){
                int[] topping_list = items.get(position).getTopping();
                topping_str = ", ";
                for(int i=0;i<topping_list.length;i++){
                    if(items.get(position).getTopping()[i] != 0){
                        if(i == items.get(position).firstTopping(items.get(position).getTopping())) {
                            if (i == 0)
                                topping_str += "베이컨";
                            else if (i == 1)
                                topping_str += "토마토";
                            else if (i == 2)
                                topping_str += "엘치즈";
                            else
                                topping_str += "비프패티";
                        }
                        else {
                            if(i == 1)
                                topping_str += ", " + "토마토";
                            else if(i == 2)
                                topping_str += ", " + "엘치즈";
                            else
                                topping_str += ", " + "비프패티";
                        }
                    }
                }
            }
            items.get(position).setOptionText(order_option, " _ " + dessert_str + drink_str + topping_str);
            cart += items.get(position).getMenu() + " _ " + dessert_str + drink_str + topping_str + "*" + items.get(position).getNumber();
            if(position < items.size()-1){
                cart += "/";
            }
        }
        else if(items.get(position).cat_list.equals("버거")) {
            if (!items.get(position).isEmptyTopping(items.get(position).getTopping())) {
                int[] topping_list = items.get(position).getTopping();
                topping_str = " _ ";
                for (int i = 0; i < topping_list.length; i++) {
                    if (items.get(position).getTopping()[i] != 0) {
                        if (i == items.get(position).firstTopping(items.get(position).getTopping())) {
                            if (i == 0)
                                topping_str += "베이컨";
                            else if (i == 1)
                                topping_str += "토마토";
                            else if (i == 2)
                                topping_str += "엘치즈";
                            else
                                topping_str += "비프패티";
                        } else {
                            if (i == 1)
                                topping_str += ", " + "토마토";
                            else if (i == 2)
                                topping_str += ", " + "엘치즈";
                            else
                                topping_str += ", " + "비프패티";
                        }
                    }
                }
                items.get(position).setOptionText(order_option, topping_str);
                cart += items.get(position).getMenu() + " _ " + topping_str + "*" + items.get(position).getNumber();
                if(position < items.size()-1){
                    cart += "/";
                }
            }
            else{
                items.get(position).setOptionText(order_option, "");
                cart += items.get(position).getMenu() + "*" + items.get(position).getNumber();
                if(position < items.size()-1){
                    cart += "/";
                }
            }
        }
        else {
            items.get(position).setOptionText(order_option, "");
            cart += items.get(position).getMenu() + "*" + items.get(position).getNumber();
            if(position < items.size()-1){
                cart += "/";
            }
        }


        int total_int = 0;
        int cnt = 0;
        for(int j=0; j<items.size(); j++){
            cnt += Integer.parseInt(items.get(j).getNumber());
        }
        for(int i=0;i<items.size();i++){
            total_int += (items.get(i).real_price_list * Integer.parseInt(items.get(i).getNumber()));
        }


        OrderActivity.setTotalPrice(total_int);
        OrderActivity.setTotalCnt(cnt);

        return convertView;
    }

}