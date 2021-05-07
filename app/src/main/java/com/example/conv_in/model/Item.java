/*********************************************************
 * Name : 정재욱
 * Student ID : 20163155
 * Program ID : Item.java
 * Description : **********************************************************/


package com.example.conv_in.model;

import android.os.Parcel;
import android.os.Parcelable;

/*********************************************************
 * 파이어베이스의 값들을 class로 받아와 사용할 수 있게끔 하기 위해 구현.
 * Description : **********************************************************/

public class Item implements Parcelable {
    public String title;
    public String title2;
    public String price;
    public String src;
    public String src2;
    public String plus;
    public String cro;
    public String star;
    public Item(String title,String title2, String price, String src,String src2,String plus,String cro, String Star){
        this.title = title;
        this.title2 = title2;
        this.price=price;
        if(src.indexOf("http") >-1 || src.indexOf("http") >-1 ){
            this.src = src;
        }else{
            this.src = null;
        }
        if(src2.indexOf("http") >-1 || src2.indexOf("http") >-1 ){
            this.src2 = src2;
        }else{
            this.src2 = null;
        }
        this.plus = plus;
       this.star = Star;
    }
    public Item(){}
    public Item(Parcel in) {
        this.title = in.readString();
        this.title2 = in.readString();
        this.price = in.readString();
        this.src = in.readString();
        this.src2 = in.readString();
        this.plus = in.readString();
        this.cro = in.readString();
        this.star = in.readString();
    }
    public String getStar(){
        return this.star;
    }
    public String getTitle(){
        return this.title;
    }
    public String getTitle2(){
        return this.title2;
    }
    public String getPrice(){
        return this.price;
    }
    public String getSrc(){
        return this.src;
    }
    public String getSrc2(){
        return this.src2;
    }
    public String getPlus(){
        return this.plus;
    }
    public String getCro(){
        return this.cro;
    }

    public void setStar(String star){
        this.star = star;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.price);
        dest.writeString(this.src);
        dest.writeString(this.src2);
        dest.writeString(this.plus);
        dest.writeString(this.cro);
        dest.writeString(this.star);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Item[size];
        }
    };

}
