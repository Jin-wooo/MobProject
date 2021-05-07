/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : RespDocument.java
 * Description : Retrofit으로 통신할 때, 통신의 결과값을 받는 RespResult의 Document 부분입니다.
 **********************************************************/

package com.example.conv_in.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**********************************************************
 * class : public class RespDocument implements Parcelable
 * description : Retrofit2와 REST API로 통신할 때, 통신 결과값 중 Document를 저장하는 곳입니다.
 *               해당 클래스는 주로 받아 온 지역의 이름이나 위치, 주소 정보 등을 저장합니다.
 *               보통 이 결과물들은 JSON으로 오는데,
 *               SerializedName을 통해 원하는 이름의 자료를 원하는 변수에 매칭시킬 수 있습니다.
 * variable :
 *     private String strPlaceName : place_name 값을 저장하는 변수입니다.
 *     private String strAddressName : address_name 값을 저장하는 변수입니다.
 *     private String strRoadAddressName : read_address_name 값을 저장하는 변수입니다.
 *     private String distance : distance값을 저장하는 변수입니다.
 *     private String strX : x값(경도)을 저장하는 변수입니다.
 *     private String strY : y값(위도)을 저장하는 변수입니다.
 **********************************************************/
public class RespDocument{
    @SerializedName("place_name")
    @Expose
    private String strPlaceName;
    @SerializedName("address_name")
    @Expose
    private String strAddressName;
    @SerializedName("read_address_name")
    @Expose
    private String strRoadAddressName;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("category_group_name")
    @Expose
    private String strCategoryGroupName;
    @SerializedName("x")
    @Expose
    private String strX;
    @SerializedName("y")
    @Expose
    private String strY;

    public RespDocument() {
        strPlaceName = "";
        strAddressName = "";
        strRoadAddressName = "";
        distance = "";
        strX = "";
        strY = "";
    }

    // Getters and Setters
    public String getStrPlaceName() { return strPlaceName; }
    public void setStrPlaceName(String strPlaceName) {
        this.strPlaceName = strPlaceName;
    }

    public String getStrAddressName() { return strAddressName; }
    public void setStrAddressName(String strAddressName) {
        this.strAddressName = strAddressName;
    }

    public String getStrRoadAddressName() { return strRoadAddressName; }
    public void setStrRoadAddressName(String strRoadAddressName) {
        this.strRoadAddressName = strRoadAddressName;
    }

    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }

    public String getStrCategoryGroupName() { return strCategoryGroupName; }
    public void setStrCategoryGroupName(String strCategoryGroupName) {
        this.strCategoryGroupName = strCategoryGroupName;
    }

    public String getStrX() { return strX; }
    public void setStrX(String strX) { this.strX = strX; }

    public String getStrY() { return strY; }
    public void setStrY(String strY) { this.strY = strY; }
}
