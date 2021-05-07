/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : RespResult.java
 * Description : Retrofit으로 통신할 때, 통신의 결과값을 받는 클래스입니다.
 **********************************************************/
package com.example.conv_in.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**********************************************************
 * class : public class RespResult
 * description : Retrofit2와 REST API로 통신할 때, 통신 결과값을 저장하는 변수입니다.
 * variable :
 *      private RespMeta meta - metaData를 저장하는 변수입니다.
 *      private ArrayList<RespDocument> listDocuments - 장소 정보를 저장하는 변수입니다.
 **********************************************************/
public class RespResult{
    @SerializedName("meta")
    @Expose
    private RespMeta meta;
    @SerializedName("documents")
    @Expose
    private ArrayList<RespDocument> listDocuments = null;

    // Setters and Getters
    public RespMeta getMeta() { return meta; }
    public void setMeta(RespMeta meta) {
        this.meta = meta;
    }

    public ArrayList<RespDocument> getListDocuments() { return listDocuments; }
    public void setListDocuments(ArrayList<RespDocument> listDocuments) {
        this.listDocuments = listDocuments;
    }
}
