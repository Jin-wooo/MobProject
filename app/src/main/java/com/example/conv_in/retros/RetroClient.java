/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : RetroClient.java
 * Description : 카카오 서버와 통신하기 위한 Retrofit 객체입니다.
 **********************************************************/
package com.example.conv_in.retros;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**********************************************************
 * class : public class RetroClient
 * description : Retrofit2로 카카오맵과 통신하고 객체 정보를 반환할 Retrofit 객체입니다.
 * variable :
 *      private static final String BASE_URL : 통신을 위한 URL 중 기본 URL입니다.
 *                                             이 URL에 주소를 덧붙여 완전한 주소로 만들어 통신합니다.
 *      private static Retrofit retrofit : retrofit 통신의 주체입니다.
 **********************************************************/
public class RetroClient {
    private static final String BASE_URL = "https://dapi.kakao.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetroClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
