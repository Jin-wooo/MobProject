/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : ConvListAdapter.java
 * Description : RecyclerView 적용을 위한 Adapter 클래스입니다.
 *               홰당 클래스를 통해 RecyclerView에 Item을 집어넣습니다.
 **********************************************************/

/**********************************************************
 * interface :
 * description :
 * parameter :
 **********************************************************/

package com.example.conv_in.PlaceList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conv_in.R;

import java.util.ArrayList;

/**********************************************************
 * class : public class PlaceListAdapter
 * description : RecyclerView에 Item을 넣기 위한 Adapter를 상속받은 클래스입니다.
 * variable :
 *      private ArrayList<PlaceListProduct> mList - Adapter에서 RecyclerView로 보낼 데이터를 담아두는 곳입니다.
 *      private OnItemClickListener mListener - Listener로 전달받은 객체를 저장하는 변수입니다.
 **********************************************************/
public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private ArrayList<PlaceListProduct> mList;
    private OnItemClickListener mListener = null;

    // Constructor
    public PlaceListAdapter() { mList = new ArrayList<PlaceListProduct>(); }
    public PlaceListAdapter(ArrayList<PlaceListProduct> mList) {
        this.mList = mList;
    }

    // 아래의 세 함수는 RecyclerView.Adapter를 상속하면 반드시 구현해야 하는 함수들입니다.
    /**********************************************************
     * function : public PlaceViewHolder onCreateViewHolder
     * description : View로 보낼 데이터를 저장하는 리스트를 생성하는 함수입니다.
     * parameter : ViewGroup parent - 데이터를 넣을 RecyclerView
     *             int viewType - 이 매개변수의 타입에 맞게 RecyclerView에 넣습니다.
     **********************************************************/
    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list, parent, false);

        // inline으로 줄인 거 같음. 안돌아가면 의심해봐,
        return new PlaceViewHolder(view);
    }


    /**********************************************************
     * function : public void onBindViewHolder
     * description : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시합ㄴ디ㅏ.
     * variable :
     *      @NonNull PlaceViewHolder holder - 아이템을 넣을 뷰홀더입니다.
     *      int position - 표시할 위치를 나타냅니다.
     **********************************************************/
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.tvPlaceName.setText(mList.get(position).getStrPlaceName());
        holder.tvPlaceAddr.setText(mList.get(position).getStrPlaceAddress());
        holder.tvPlaceType.setText(mList.get(position).getStrPlaceType());
    }

    // 전체 아이템 갯수를 리턴하는 함수입니다.
    @Override
    public int getItemCount() { return (null != mList ? mList.size() : 0); }

    // 여기서부터는 이 Adapter를 편하게 쓰기 위해 만들어진 함수입니다.
    // 이 함수들은 간단히 설명합니다.

    // idx에 해당하는 PlaceListProduct를 리턴합니다.
    public PlaceListProduct getItem(int idx) { return mList.get(idx); };

    // 해당 데이터를 Adapter의 리스트에 추가합니다.
    public void addItem(PlaceListProduct listProduct) { mList.add(listProduct); }

    // idx에 해당하는 데이터를 삭제합니다.
    public void removeItem(int idx) { mList.remove(idx); }

    // 리스트를 초기화합니다.
    public void clearItem() { mList.clear(); }

    /**********************************************************
     * class : public class PlaceViewHolder
     * description : RecyclerView 내의 ItemView와 metadata를 담아두는 클래스입니다.
     * variable : tvPlaceName - 장소 이름을 저장하는 TextView
     *            tvPlaceAddr - 장소 주소를 저장하는 TextView
     *            tvPlaceType - 장소 타입을 저장하는 TextView
     **********************************************************/
    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvPlaceName;
        protected TextView tvPlaceAddr;
        protected TextView tvPlaceType;

        // Constructor
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvPlaceName = (TextView) itemView.findViewById(R.id.tvPlaceName);
            this.tvPlaceAddr = (TextView) itemView.findViewById(R.id.tvPlaceAddr);
            this.tvPlaceType = (TextView) itemView.findViewById(R.id.tvPlaceType);

            // 이번에 구현한 RecyclerView에서는, Item을 클릭하면 구현되어야 할 이벤트가 존재하므로
            // Custom Listener를 통해 Click Event를 구현합니다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }


    /**********************************************************
     * interface : public interface OnItemClickListener
     * description : Adapter OnClick를 커스텀으로 구현하기 위한 리스너 인터페이스입니다.
     **********************************************************/
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    // 리스너 객체를 전달하는 메서드입니다.
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
