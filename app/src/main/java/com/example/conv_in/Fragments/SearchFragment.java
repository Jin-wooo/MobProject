/*********************************************************
 * Name : 정재욱
 * Student ID : 20163155
 * Program ID : SearchFragment.java
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

public class SearchFragment extends Fragment {
   private ArrayList<Item> item2;
   public  int count = 0;
    public int[] checked;
    private boolean[] check = new boolean[4000];
    private String value;
    private ArrayList<Integer> check2 = new ArrayList<Integer>();
    private TextView titletext ;
    private TextView pricetext;
    private ImageView imageView2;
    private TextView plustext;
    private ImageView hideimg;
    private TextView hidetitle;
    private  ImageButton starbutton;
    private LinearLayout linearLayout;
    private Animation anim;
    private ListView listView;
    private String k;
    private listadapter adapter;
   public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = null;
       Bundle bundle = getArguments();
       checked = bundle.getIntArray("checked");
       value = bundle.getString("value");
       Log.d("palse",value);
       view = inflater.inflate(R.layout.listv2,container,false);
       /*********************************************************
        * 검색을 했을 때 MainActivity에서 넘어온 값을 저장하여 해당 편의점 명에 맞는 데이터베이스의 자식들을 찾음
        * onDataChange 함수 안에서 찾고 싶어하는 value 값이 속했는지 확인하여 리스트뷰를 만듦
        * 이 때 파이어베이스에서는 안의 내용을 검색하는 고급 조건문이 없기 때문에 indexOf로 구현했음.
        * Description : **********************************************************/
       final ListView listView = (ListView) view.findViewById(R.id.listview);
           if (checked[0] == 1) {
               k = "Emart";
           } else if (checked[1] == 1) {
               k = "CU";
           } else if (checked[2] == 1) {
               k = "Seven";
           } else if (checked[3] == 1) {
               k = "GS";
           } else if (checked[4] == 1) {
               k = "Mini";
           }
           Log.d("search", k);
           item2 = new ArrayList<Item>();
           FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
           DatabaseReference reference = mdatabase.getReference(k);
           reference.orderByChild("title").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   for (DataSnapshot datas : dataSnapshot.getChildren()) {
                       count++;
                       if(count >=7 && item2 != null){
                           adapter = new listadapter(getActivity(), 0, item2);
                           listView.setAdapter(adapter);
                           adapter.notifyDataSetChanged();
                           count= 0;
                       }
                       Item temp = datas.getValue(Item.class);
                       if(temp.getTitle().indexOf(value)>-1){
                           item2.add(temp);
                       }
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {
                   throw databaseError.toException();
               }
           });
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
    /*********************************************************
     * 각각의 리스트뷰의 image 설정과 text 설정을 담당
     * 상품이미지를 모두 drawable 파일에 넣어 이미지 받아오는 것은 파이어베이스를 쓰는 목적이 없기 때문에
     * Glide 메소드 사용하며 데이터베이스에 있는 URL를 다운 받아옴
     * Description : **********************************************************/
    public class listadapter extends ArrayAdapter<Item> {
        private ArrayList<Item> allitem;

        public listadapter(Context context,int k,ArrayList<Item> item2){
            super(context,k,item2);
            this.allitem= item2;
        }

        public void add(ArrayList<Item> data){
            this.allitem =data;
        }

        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customlist,null);

            final View finalConvertView = convertView;
            titletext = (TextView) convertView.findViewById(R.id.titletext); // 상품명
            pricetext = (TextView) convertView.findViewById(R.id.pricetext); // 가격명
            imageView2 = (ImageView) convertView.findViewById(R.id.image2); // 상품 이미지
            plustext = (TextView) convertView.findViewById(R.id.plustext); // 상품 증정 종류
            hideimg = (ImageView) convertView.findViewById(R.id.hideimg); // 숨김 이미지(증정)
            hidetitle = (TextView) convertView.findViewById(R.id.hidetitle); // 숨김 상품명(증정)
            starbutton = (ImageButton) finalConvertView.findViewById(R.id.starimage); // 즐겨찾기 버튼

            titletext.setText(allitem.get(position).getTitle());
            pricetext.setText(allitem.get(position).getPrice());

            hideimg.setVisibility(convertView.GONE);
            hidetitle.setVisibility(convertView.GONE);

            if(allitem.get(position).getPlus().equals("덤 증정")){
                plustext.setText("증정행사");
            }else{
                plustext.setText(allitem.get(position).getPlus());
            }
            if(allitem.get(position).getStar().equals("notcheck")){
                starbutton.setImageResource(android.R.drawable.btn_star_big_off);
            }else{
                starbutton.setImageResource(android.R.drawable.btn_star_big_on);
            }
            if(allitem.get(position).getSrc()!=null){
                Glide.with(convertView).load(allitem.get(position).getSrc()).into(imageView2);
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
                    if(check[position] ==false&&allitem.get(position).getTitle2()!=null){
                        anim = AnimationUtils.loadAnimation
                                (getContext(), // 현재화면의 제어권자
                                        R.anim.trans);   // 에니메이션 설정 파일
                        linearLayout.startAnimation(anim);// 레이아웃 이동
                        hidetitle.setVisibility(finalConvertView.VISIBLE);
                        hideimg.setVisibility(finalConvertView.VISIBLE);

                        String k = allitem.get(position).getTitle2();
                        if(k.indexOf(")")>-1){
                            String[] title2 = k.split("\\)");
                            hidetitle.setText(title2[0] + "\n"+title2[1]);
                        }else {
                            hidetitle.setText(k);
                        }
                        if(allitem.get(position).getSrc2() == null){
                            Glide.with(finalConvertView).load("https://image.shutterstock.com/image-vector/no-image-vector-isolated-on-260nw-1481369594.jpg").override(72,80).into(hideimg);
                        }else{
                            Glide.with(finalConvertView).load(allitem.get(position).getSrc2()).override(72,80).into(hideimg);
                        }
                        check[position] = true;
                        check2.add(position);

                    }else if(check[position] == true && allitem.get(position).getTitle2()!=null ){
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
             * 검색을 한 곳에서도 즐겨찾기 버튼을 누른다면 즐겨찾기의 값이 변해야하기 때문에 구현
             * 클릭한 부분의 상품명을 가지고 찾아가 해당 데이터베이스의 즐겨찾기 키의 값을 변환
             * Description : **********************************************************/
            starbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titletext = (TextView) finalConvertView.findViewById(R.id.titletext);
                    starbutton = (ImageButton) finalConvertView.findViewById(R.id.starimage);
                    FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference reference = mdatabase.getReference(k);
                    Log.d("Plase",k);
                    reference.orderByChild("title").equalTo(titletext.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot datas1 : dataSnapshot.getChildren()) {
                                Item temp = datas1.getValue(Item.class);
                                String[] pass =String.valueOf(datas1.getRef()).split("/");
                                if(temp.getStar().equals("notcheck")){
                                    reference.child(pass[4]).child("star").setValue("Yes");
                                    starbutton.setImageResource(android.R.drawable.btn_star_big_on);
                                    allitem.get(position).setStar("Yes");
                                }else if(temp.getStar().equals("Yes")){
                                    reference.child(pass[4]).child("star").setValue("notcheck");
                                    starbutton.setImageResource(android.R.drawable.btn_star_big_off);
                                    allitem.get(position).setStar("notcheck");
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