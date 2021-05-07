/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : RespMeta.java
 * Description : Retrofit으로 통신할 때, 통신의 결과값을 받는 RespResult의 Meta 부분입니다.
 **********************************************************/

package com.example.conv_in.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**********************************************************
 * class : public class RespMeta
 * description : Retrofit2와 REST API로 통신할 때, 통신 결과값 중 Meta를 저장하는 곳입니다.
 *               이 데이터는 주로 해당 데이터 리스트의 상태나 분석 정보, 그리고 페이지 관련 데이터를 저장합니다.
 * variable :
 *      private Boolean isEnd - 현재 페이지가 끝인지 아닌지를 알려주는 변수입니다.
 *      private Integer numPageableCount - 불러올 수 있는 페이지의 수를 알려주는 변수입니다.
 *      private Integer numTotalCount - 총 페이지 수를 알려주는 변수입니다.
 **********************************************************/
public class RespMeta{
    // total count 중 노출 가능 문서 수. 최대 45
    @SerializedName("pageable_count")
    @Expose
    private Integer numPageableCount;

    // 검색된 문서 수 표시
    @SerializedName("total_count")
    @Expose
    private Integer numTotalCount;

    // 현제 페이지가 마지막인지를 표시해줌
    @SerializedName("is_end")
    @Expose
    private Boolean isEnd;

    // Setter and Getter
    public Integer getNumPageableCount() { return numPageableCount; }
    public void setNumPageableCount(Integer numPageableCount) {
        this.numPageableCount = numPageableCount;
    }

    public Integer getNumTotalCount() { return numTotalCount; }
    public void setNumTotalCount(Integer numTotalCount) {
        this.numTotalCount = numTotalCount;
    }

    public Boolean getEnd() { return isEnd; }
    public void setEnd(Boolean end) { isEnd = end; }
}
