/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : PlaceListProduct.java
 * Description : Adapter가 저장할 데이터 클래스입니다.
 **********************************************************/

package com.example.conv_in.PlaceList;


/**********************************************************
 * class : public class PlaceListProduct
 * description : Adapter에 저장될 List의 Data 클래스입니다.
 *               데이터만 저장하는 클래스라, 기본적인 생성자와 Setter, Getter정도만 가지고 있습니다.
 * variable :
 *     private String strPlaceName - 장소 이름 저장 변수
 *     private String strPlaceAddress - 장소 주소 저장 변수
 *     private String strPlaceType - 장소 유형 저장 변수
 *     private String strPlaceX - 장소 X좌표 저장 변수
 *     private String strPlaceY - 장소 Y좌표 저장 변수
 **********************************************************/
public class PlaceListProduct {
    private String strPlaceName;
    private String strPlaceAddress;
    private String strPlaceType;
    private String strPlaceX;
    private String strPlaceY;

    // Constructor
    public PlaceListProduct(String strPlaceName, String strPlaceAddress, String strPlaceType) {
        this.strPlaceName = strPlaceName;
        this.strPlaceAddress = strPlaceAddress;
        this.strPlaceType = strPlaceType;
    }

    public PlaceListProduct() {
        this.strPlaceName = "";
        this.strPlaceAddress = "";
        this.strPlaceType = "";
    }

    // strPlaceName의 Getter and Setter
    public String getStrPlaceName() {
        return strPlaceName;
    }
    public void setStrPlaceName(String strPlaceName) {
        this.strPlaceName = strPlaceName;
    }

    // strPlaceAddress의 Getter and Setter
    public String getStrPlaceAddress() {
        return strPlaceAddress;
    }
    public void setStrPlaceAddress(String strPlaceAddress) {
        this.strPlaceAddress = strPlaceAddress;
    }

    // strPlaceType의 Getter and Setter
    public String getStrPlaceType() {
        return strPlaceType;
    }
    public void setStrPlaceType(String strPlaceType) {
        this.strPlaceType = strPlaceType;
    }

    // strPlaceX의 Getter and Setter
    public String getStrPlaceX() { return strPlaceX; }
    public void setStrPlaceX(String strPlaceX) { this.strPlaceX = strPlaceX; }

    // strPlaceY의 Getter and Setter
    public String getStrPlaceY() { return strPlaceY; }
    public void setStrPlaceY(String strPlaceY) { this.strPlaceY = strPlaceY; }
}
