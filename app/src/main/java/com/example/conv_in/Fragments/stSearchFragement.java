/*********************************************************
 * Name : 정재욱
 * Student ID : 20163155
 * Program ID : starFragment.java
 * Description : **********************************************************/


package com.example.conv_in.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.conv_in.R;
import com.example.conv_in.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*********************************************************
 * 즐겨찾기 목록을 띄어주는 Fragment 파일
 * 파이어베이스 안에 있는 Emart CU Seven GS Mini 의 자식들을 찾아
 * star (즐겨찾기의 상태를 나타냄)를 찾아 체크되어 있는 부분들을 리스트뷰로 만듦
 * Description : **********************************************************/
public class stSearchFragement extends Fragment {
    private String[] data = {"Emart","CU","Seven","GS","Mini"};
    private ArrayList<String> databasename;
    private  int count =0;
    private TextView titletext ;
    private TextView pricetext;
    private ImageView imageView2;
    private TextView plustext;
    private ImageView hideimg;
    private TextView hidetitle;
    private  ImageButton starbutton;
    private LinearLayout linearLayout;
    private Animation anim;
    private ImageView hidest;
    private String value;
    private boolean[] check = new boolean[10000];
    private ArrayList<Integer> check2 = new ArrayList<Integer>();
    private listadapter adapter;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.listv2,container,false);
        final ListView listView = (ListView) view.findViewById(R.id.listview);
        final ArrayList<Item> item = new ArrayList<Item>();
        databasename =  new ArrayList<String>();
        item.clear();
        Bundle bundle = getArguments();
        value = bundle.getString("value2");
        Log.d("palse",value);
        /*********************************************************
         * 이 부분이 star의 상태를 체크하여 리스트뷰로 전환하는 부분
         * 각각의 파이어베으스의 자식들을 찾아냄.
         * 그 후 Arraylist에 해당 메뉴에 맞는 이름을 저장
         * ArrayList를 사용한 이유는 예를 들어 Emart와 Seven에 같은 상품명을 가진 경우 어떤 편의점의 상품인지 구별하기위함.
         * 검색화면이기 때문에 즐겨찾기 내에 있는 것들로만 검색
         * Description : **********************************************************/
        for(int i =0; i<5; i++) {
            FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
            final DatabaseReference reference = mdatabase.getReference(data[i]);
            reference.orderByChild("star").equalTo("Yes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count=0;
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        Item temp = datas.getValue(Item.class);
                        if(temp.getTitle().indexOf(value)>-1){
                            String[] pass = String.valueOf(datas.getRef()).split("/");
                            System.out.println(pass[3]);
                            databasename.add(pass[3]);
                            System.out.println(temp.getTitle()+", "+temp.getSrc()+", "+temp.getStar());
                            item.add(temp);
                            adapter = new listadapter(getActivity(), 0, item);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }

            });
        }
        /*********************************************************
         * 스크롤이 될 때 check[postion] 안에 값이 true면 false 로 변환
         * 이 문장은 스크롤이 될 때 증정상품을 보여줬다면 다시 클릭 할 때 오작동 발생을 방지.
         * Description : **********************************************************/
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(check2 != null){
                    for(int i=0; i<check2.size(); i++){
                        check[check2.get(i)] = false;
                    }
                    check2 = new ArrayList<Integer>();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });
        return view;
    }

    public class listadapter extends ArrayAdapter<Item> {
        private ArrayList<Item> staritem;
        private String aaa;
        public listadapter(Context context,int k,ArrayList<Item> item2){
            super(context,k,item2);
            this.staritem= item2;

        }

        public void add(ArrayList<Item> data){
            this.staritem =data;
        }
        /*********************************************************
         * 각각의 리스트뷰의 image 설정과 text 설정을 담당
         * 상품이미지를 모두 drawable 파일에 넣어 이미지 받아오는 것은 파이어베이스를 쓰는 목적이 없기 때문에
         * Glide 메소드 사용하며 데이터베이스에 있는 URL를 다운 받아옴
         * 추가로 즐겨찾기 메뉴에서는 상품이 Emart에서 추가 된 것인지 Seven에서 추가된 것인지
         * 모르기 때문에 리스트마다 해당 편의점 사진을 추가.
         * * Description : **********************************************************/
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customliststore,null);

            final View finalConvertView = convertView;
            titletext = (TextView) convertView.findViewById(R.id.titletext); // 상품명
            pricetext = (TextView) convertView.findViewById(R.id.pricetext); // 가격명
            imageView2 = (ImageView) convertView.findViewById(R.id.image2); // 상품 이미지
            plustext = (TextView) convertView.findViewById(R.id.plustext); // 상품 증정 종류
            hideimg = (ImageView) convertView.findViewById(R.id.hideimg); // 숨김 이미지(증정)
            hidetitle = (TextView) convertView.findViewById(R.id.hidetitle); // 숨김 상품명(증정)
            starbutton = (ImageButton) finalConvertView.findViewById(R.id.starimage); // 즐겨찾기 버튼
            hidest = (ImageView) convertView.findViewById(R.id.hidestore);


            titletext.setText(staritem.get(position).getTitle());
            pricetext.setText(staritem.get(position).getPrice());

            hideimg.setVisibility(convertView.GONE);
            hidetitle.setVisibility(convertView.GONE);
            hidest.setVisibility(convertView.VISIBLE);
            if (staritem.get(position).getSrc().indexOf("emart")>-1){
                hidest.setImageResource(R.drawable.eimage);

            }else if (staritem.get(position).getSrc().indexOf("cu")>-1){
                hidest.setImageResource(R.drawable.cimage);

            }else if (staritem.get(position).getSrc().indexOf("gs")>-1){
                hidest.setImageResource(R.drawable.gimage);

            }else if (staritem.get(position).getSrc().indexOf("mini")>-1){
                hidest.setImageResource(R.drawable.mimage);

            }
            else if (staritem.get(position).getSrc().indexOf("7-eleven")>-1){
                hidest.setImageResource(R.drawable.simage);
            }
            if(staritem.get(position).getPlus().equals("덤 증정")){
                plustext.setText("증정행사");
            }else{
                plustext.setText(staritem.get(position).getPlus());
            }
            if(staritem.get(position).getStar().equals("notcheck")){
                starbutton.setImageResource(android.R.drawable.btn_star_big_off);
            }else{
                starbutton.setImageResource(android.R.drawable.btn_star_big_on);

            }
            if(staritem.get(position).getSrc()!=null){
                Glide.with(convertView).load(staritem.get(position).getSrc()).into(imageView2);
            }else{
                Glide.with(finalConvertView).load("https://image.shutterstock.com/image-vector/no-image-vector-isolated-on-260nw-1481369594.jpg").override(72,80).into(imageView2);
            }
            /*********************************************************
             * 편의점 1+1 행사 뿐만 아니라 2+1, 덤 증정 행사까지 포함하기 때문에
             * 증정 행사에 해당 할 경우 애니메이션 효과를 지정하여 해당 뷰를 오른쪽으로 밀어지게끔 구현
             * Description : **********************************************************/
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideimg = (ImageView) finalConvertView.findViewById(R.id.hideimg); // 숨김 이미지(증정)
                    hidetitle = (TextView) finalConvertView.findViewById(R.id.hidetitle); // 숨김 상품명(증정)
                    linearLayout = (LinearLayout) finalConvertView.findViewById(R.id.pagelayout);
                    if(check[position] ==false&&staritem.get(position).getTitle2()!=null){
                        anim = AnimationUtils.loadAnimation
                                (getContext(), // 현재화면의 제어권자
                                        R.anim.trans);   // 에니메이션 설정 파일
                        linearLayout.startAnimation(anim);// 레이아웃 이동
                        hidetitle.setVisibility(finalConvertView.VISIBLE);
                        hideimg.setVisibility(finalConvertView.VISIBLE);

                        String k = staritem.get(position).getTitle2();
                        if(k.indexOf(")")>-1){
                            String[] title2 = k.split("\\)");
                            hidetitle.setText(title2[0] + "\n"+title2[1]);
                        }else {
                            hidetitle.setText(k);
                        }
                        if(staritem.get(position).getSrc2() == null){
                            Glide.with(finalConvertView).load("https://image.shutterstock.com/image-vector/no-image-vector-isolated-on-260nw-1481369594.jpg").override(72,80).into(hideimg);
                        }else{
                            Glide.with(finalConvertView).load(staritem.get(position).getSrc2()).override(72,80).into(hideimg);
                        }
                        check[position] = true;
                        check2.add(position);

                    }else if(check[position] == true && staritem.get(position).getTitle2()!=null ){
                        anim = AnimationUtils.loadAnimation
                                (getContext(), // 현재화면의 제어권자
                                        R.anim.trans2);   // 에니메이션 설정 파일
                        linearLayout.startAnimation(anim);
                        hidetitle.setVisibility(finalConvertView.INVISIBLE);
                        check[position] = false;
                    }

                }
            });
            /*********************************************************
             * 즐겨찾기를 위한 별 버튼을 클릭했을 경우 해당 클릭한 부분의 상품명과
             * 해당 파이어베이스와 같은 값이 있는지 알아보고 같다면 해당 부분의 즐겨찾기를 체크하는 부분의 값을 변경
             * 위에서 저장한 databasename ArrayList를 사용하여 해당 편의점에 맞게
             * 데이버베이스 값을 변경해주는 부분
             * * Description : **********************************************************/
            starbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titletext = (TextView) finalConvertView.findViewById(R.id.titletext);
                    starbutton = (ImageButton) finalConvertView.findViewById(R.id.starimage);
                    if (staritem.get(position).getSrc().indexOf("emart")>-1){
                        aaa="Emart";
                    }else if (staritem.get(position).getSrc().indexOf("cu")>-1){
                        aaa="CU";
                    }else if (staritem.get(position).getSrc().indexOf("gs")>-1){
                        aaa="GS";
                    }else if (staritem.get(position).getSrc().indexOf("mini")>-1){
                        aaa="Mini";
                    }else if (staritem.get(position).getSrc().indexOf("7-eleven")>-1){
                        aaa="Seven";
                    }
                        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference reference = mdatabase.getReference(aaa);
                        reference.orderByChild("title").equalTo(titletext.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot datas1 : dataSnapshot.getChildren()) {
                                    Item temp = datas1.getValue(Item.class);
                                    String[] pass = String.valueOf(datas1.getRef()).split("/");
                                    if (temp.getStar().equals("notcheck")&& databasename.get(position).equals(pass[3])) {
                                        System.out.println(pass[3]+pass[4]+", "+temp.getTitle());
                                        reference.child(pass[4]).child("star").setValue("Yes");
                                        starbutton.setImageResource(android.R.drawable.btn_star_big_on);
                                        staritem.get(position).setStar("Yes");
                                    } else if (temp.getStar().equals("Yes")&& databasename.get(position).equals(pass[3])) {
                                        System.out.println(pass[3]+pass[4]+", "+temp.getTitle());
                                        reference.child(pass[4]).child("star").setValue("notcheck");
                                        starbutton.setImageResource(android.R.drawable.btn_star_big_off);
                                        staritem.get(position).setStar("notcheck");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                throw databaseError.toException();
                            }

                        });
                    }

            });


            return convertView;
        }

    }
}
