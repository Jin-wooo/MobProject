/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : RetroInterface.java
 * Description : 카카오 서버와 통신할 통신 인터페이스입니다.
 **********************************************************/
package com.example.conv_in.retros;

import com.example.conv_in.model.RespResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**********************************************************
 * interface : public interface RetroInterface
 * description : Retrofit2와 REST API로 통신할 때, 통신을 요청하는 인터페이스입니다.
 **********************************************************/
public interface RetroInterface {
    /**********************************************************
     * function: Call<RespResult> getSearchByName
     * description : 장소 이름을 기반으로 검색하는 함수입니다.
     * parameter : token - REST API 키를 담는 부분입니다.
     *             strQuery - 검색할 장소 이름입니다.
     *             numSize - 한 페이지에 보여질 문서의 갯수입니다.
     *             numPage - 몇 번째 페이지인지를 지정합니다.
     **********************************************************/
    @GET("v2/local/search/keyword.json")
    Call<RespResult> getSearchByName(
           @Header("Authorization") String token,
           @Query("query") String strQuery,
           @Query("size") int numSize,
           @Query("page") int numPage
    );

    // 특정 위치 기준으로 편의점 검색
    // 이 위치는 내 현재 위치도 되고, 그냥 아무데나 집어도 된다.
    /**********************************************************
     * function: Call<RespResult> getConvSearchByLocation
     * description : 특정 위치와 장소의 카테고리를 기준으로 검색하는 함수입니다.
     *               저는 편의점 위치를 검색할 때만 썼습니다.
     * parameter : token - REST API 키를 담는 부분입니다.
     *             strCategoryCode - 검색할 장소의 타입을 지정합니다.
     *             strX - 검색할 중심 지점의 X좌표입니다.
     *             strY - 검색할 기준 지점의 Y좌표입니다.
     *             strRectVal - 검색할 영역입니다.
     *             numPage - 몇 번째 페이지인지를 지정합니다.
     **********************************************************/
    @GET("v2/local/search/category.json")
    Call<RespResult> getConvSearchByLocation(
            @Header("Authorization") String token,
            @Query("category_group_code") String strCategoryCode,
            @Query("x") String strX,
            @Query("y") String strY,
            @Query("rect") String strRectVal,
            @Query("page") int numPage
    );
}
