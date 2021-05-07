/*********************************************************
 * Name :  장진우
 * Student ID : 20171694
 * Program ID : MapActivity.java
 * Description : Conv_in의 두 번째 기능인 지도 기능을 담고 있는 액티비티입니다.
 **********************************************************/

package com.example.conv_in;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.conv_in.PlaceList.PlaceListAdapter;
import com.example.conv_in.PlaceList.PlaceListProduct;
import com.example.conv_in.model.RespDocument;
import com.example.conv_in.model.RespResult;
import com.example.conv_in.retros.RetroClient;
import com.example.conv_in.retros.RetroInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;


/**********************************************************
 * class : public class MapActivity
 * description : 지도 기능을 담고 있는 액티비티입니다.
 *               지도 관련 기능들은 모두 이 곳에서 해결합니다.
 * variable :
 *     private MapView mMapView - 카카오맵에서 지원하는 카카오맵 변수입니다.
 *                                맵 화면 처리는 이 변수가 담당합니다.
 *     private CoordinatorLayout fabLayout - 아래쪽의 FloatingActionButton을 담아두는 Layout입니다.
 *     private EditText editTextSearch - 검색창입니다.
 *                                       앱 내의 키보드 엔터키를 검색 기능으로 바꾸는 Listener가 붙어있습니다.
 *     private ImageButton btnReturn - 검색창 왼쪽의, 뒤로 돌아가는 버튼입니다.
 *     private ImageButton btnSearch - 검색창 오른쪽의, 검색 버튼입니다.
 *     FloatingActionButton fabMyLocation - 내 위치 정보를 표시하는 버튼입니다.
 *     FloatingActionButton fabConvSearch - 편의점만을 검색하는 버튼입니다.
 *     FloatingActionButton fabClear - 모든 맵 상의 기록들을 초기화시키는 버튼입니다.
 *     private RecyclerView rvResults - 장소 검색 결과를 표시하는 RecyclerView입니다.
 *     private LinearLayoutManager layoutManager - RecyclerView에 Adapter로 만든 View를 넣는 변수입니다.
 *     private PlaceListAdapter placeAdapter - RecyclerView에 데이터를 넣는 변수입니다.
 *     InputMethodManager inputMethodManager - 키보드를 관리하는 매니저 변수입니다.
 *                                             여기서는 검색 후 키보드를 내리는 용도로 씁니다.
 *     private static final int GPS_ENABLE_REQUEST_CODE - GPS 요청 코드입니다.
 *     private static final int PERMISSIONS_REQUEST_CODE - 권한 요청 코드입니다.
 *
 *     private boolean isMyLocationActivated - 현재 내 위치를 표시중인지 알려주는 변수입니다.
 *     private boolean isMapScreenMoved - 지도가 움직였는지를 알려주는 변수입니다.
 *     private boolean isSearchResultShow - 장소 검색 결과가 표시 중인지를 알려주는 변수입니다.
 *     private boolean isSearchResultSelected - 장소 검색 결과를 클릭해서 장소로 이동했는지를 알려주는 변수입니다.
 *
 *     private int numPageCount = 0 - 편의점 검색을 몇 페이지까지 했는지 표시하는 변수입니다.
 *     String[] REQUIRED_PERMISSIONS - 필요 권한을 적어두는 리스트입니다. 현재는 위치 권한만 적혀있습니다.
 **********************************************************/
public class MapActivity extends AppCompatActivity
        implements MapView.CurrentLocationEventListener, MapView.POIItemEventListener, MapView.MapViewEventListener{

    private static final String LOG_TAG = "MapActivity";

    // Widgets
    private MapView mMapView;
    private CoordinatorLayout fabLayout;
    private EditText editTextSearch;
    private ImageButton btnReturn;
    FloatingActionButton fabMyLocation;
    FloatingActionButton fabConvSearch;
    FloatingActionButton fabClear;

    private RecyclerView rvResults;
    private PlaceListAdapter placeAdapter;
    InputMethodManager inputMethodManager;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private boolean isMyLocationActivated = false;
    private boolean isMapScreenMoved = false;
    private boolean isSearchResultShow = false;
    private boolean isSearchResultSelected = false;

    private int numPageCount = 0;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    //
    /**********************************************************
     * function : public void onBackPressed()
     * description : 뒤로가기 키를 눌렀을 경우 작동하는 함수입니다.
     **********************************************************/
    @Override
    public void onBackPressed() {
        if (isSearchResultSelected) {
            isSearchResultSelected = false;
            isSearchResultShow = true;
            mMapView.removeAllPOIItems();
            rvResults.setVisibility(View.VISIBLE);
            fabLayout.setVisibility(View.GONE);
        } else if (isSearchResultShow) {
            isSearchResultShow = false;
            rvResults.setVisibility(View.GONE);
            fabLayout.setVisibility(View.VISIBLE);
            rvResults.removeAllViewsInLayout();
            btnReturn.setVisibility(View.GONE);
            editTextSearch.setText("");
        } else {
            finish();
        }
    }


    /**********************************************************
     * function : protected void onCreate
     * description : MapActivity가 처음 실행될 때 작동하는 함수입니다.
     *               이 곳에서 앞에 선언한 변수들의 대부분을 초기화하고 사용합니다.
     **********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//         맵 세팅
        mMapView = (MapView) findViewById(R.id.map_view);
        fabLayout = (CoordinatorLayout) findViewById(R.id.fabLayout);

        // 키보드 입력을 제어하는 매니저입니다.
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch();
                }
                return handled;
            }
        });

        // RecyclerView 세팅
        rvResults = (RecyclerView) findViewById(R.id.rvResult);
        // RecyclverView에는 데이터를 세팅할 Adapter와 그 데이터를 View에 넣을 LayoutManager가 필요합니다.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvResults.setLayoutManager(layoutManager);
        placeAdapter = new PlaceListAdapter();
        rvResults.setAdapter(placeAdapter);


        /**********************************************************
         * function : public void onItemClick
         * description : Adapter로 넣은 각 항목들을 클릭했을 때 실행되는 메서드입니다.
         *               이 클릭 리스너는 Custom Listener로, PlaceListAdapter에 정의되어 있습니다.
         * parameter :
         *      View v - 이 이벤트를 호출한 뷰입니다.
         *      int pos - 이 이벤트를 호출한 뷰의 인덱스입니다.
         **********************************************************/
        placeAdapter.setOnItemClickListener(new PlaceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                rvResults.setVisibility(View.GONE);
                PlaceListProduct resultProduct = placeAdapter.getItem(pos);
                isSearchResultSelected = true;
                isSearchResultShow = false;
                fabLayout.setVisibility(View.VISIBLE);

                // 받아온 위치
                MapPoint mapPointResult = MapPoint.mapPointWithGeoCoord(
                        Double.parseDouble(resultProduct.getStrPlaceY()),
                        Double.parseDouble(resultProduct.getStrPlaceX()));

                // 해당 위치로 이동해서 마커 생성
                mMapView.setMapCenterPoint(mapPointResult, false);
                createMarker(resultProduct.getStrPlaceName(),
                        resultProduct.getStrPlaceX(), resultProduct.getStrPlaceY());
            }
        });

        // 검색 영역을 설정해주는 뷰입니다.
        // 카카오맵은 원하는 사각형 지점을 선택하게 해 주는 함수가 없어서 임시방편으로 쓴 방법입니다.
        // 이 영역의 끝 픽셀 좌표를 계산해서, 그걸 맵 화면의 좌표로 바꿔 계산하는 방식입니다.
        View searchArea = (View) findViewById(R.id.searchArea);

        // 카카오맵 전용 리스너 세팅
        mMapView.setCurrentLocationEventListener(this); // 현재 위치 관련 리스너
        mMapView.setPOIItemEventListener(this); // 위치 표시 아이템 리스너
        mMapView.setMapViewEventListener(this); // 맵 뷰 리스너너


        // 여기서부터는 버튼과 버튼의 OnClickListener를 설정합니다.

        /**********************************************************
         * * function : public void onClick
         * description : btnReturn을 클릭했을 때 실행되는 메서드입니다.
         *              앞에서 정의한 onBackPressed와 거의 같은 기능을 수행합니다.
         * parameter : View v - 클릭한 객체입니다.
         **********************************************************/
        btnReturn = (ImageButton) findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchResultSelected) {
                    isSearchResultSelected = false;
                    isSearchResultShow = true;
                    mMapView.removeAllPOIItems();
                    rvResults.setVisibility(View.VISIBLE);
                    fabLayout.setVisibility(View.GONE);
                } else if (isSearchResultShow) {
                    isSearchResultShow = false;
                    rvResults.setVisibility(View.GONE);
                    rvResults.removeAllViewsInLayout();
                    fabLayout.setVisibility(View.VISIBLE);
                    btnReturn.setVisibility(View.GONE);
                    editTextSearch.setText("");
                }
            }
        });

        // 검색 버튼
        /**********************************************************
         * function : public void onClick
         * description : btnSearch 클릭했을 때 실행되는 메서드입니다.
         *               안드로이드 키보드의 엔터를 대체한 검색 버튼도 같은 기능을 수행합니다.
         * parameter : View v - 클릭한 객체입니다.
         **********************************************************/
        ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });

        // 내 위치를 추적하는 버튼과 Onclick
        /**********************************************************
         * function : public void onClick
         * description : fabMyLocation 클릭했을 때 실행되는 메서드입니다.
         *               위치 권한 상태와 위치 기능 활성화 상태를 파악하고,
         *               필요한 것들을 요청하는 함수를 사용한 다음,
         *               필요한 권한과 기능이 모두 모였으면 현재 위치를 요청합니다.
         * parameter : View v - 클릭한 객체입니다.
         **********************************************************/
        fabMyLocation = (FloatingActionButton) findViewById(R.id.fabMyLocation);
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkLocationPermissionStatus()) {
                    Log.d("Location", "런타임 퍼미션 체크");
                    checkRunTimePermission();
                } else if (!checkLocationServicesStatus()){
                    // 바로 위치 권한을 새로 물어보는 건 이상해.
                    showDialogForLocationServiceSetting();
                    Log.d("Location", "위치체크");
                } else {
                    if (isMyLocationActivated) {
                        Log.d("Location", "끕니다");
                        isMyLocationActivated = false;
                        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                        mMapView.setShowCurrentLocationMarker(false);
                    } else {
                        Log.d("Location", "켭니다");
                        isMyLocationActivated = true;
                        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                        mMapView.setShowCurrentLocationMarker(true);
                    }
                }
            }
        });

        /**********************************************************
         * function : public void onClick
         * description : fabMyLocation 클릭했을 때 실행되는 메서드입니다.
         *               위치 권한 상태와 위치 기능 활성화 상태를 파악하고,
         *               필요한 것들을 요청하는 함수를 사용한 다음,
         *               필요한 권한과 기능이 모두 모였으면 현재 위치를 요청합니다.
         * parameter : View v - 클릭한 객체입니다.
         **********************************************************/
        fabConvSearch = (FloatingActionButton) findViewById(R.id.fabConvSearch);
        fabConvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabConvSearch.isClickable()) {
                    if (isMapScreenMoved) {
                        mMapView.removeAllPOIItems();
                        isMapScreenMoved = false;
                    }
                    numPageCount++;
                    getSinglePageConvList(searchArea, numPageCount);

                    fabClear.setClickable(true);
                }
            }
        });

        /**********************************************************
         * function : public void onClick
         * description : fabClear 클릭했을 때 실행되는 메서드입니다.
         *               전체 초기화 버튼입니다. 그냥 초기 지도 화면으로 되돌린다고 보시면 됩니다.
         * parameter : View v - 클릭한 객체입니다.
         **********************************************************/
        fabClear = (FloatingActionButton) findViewById(R.id.fabClear);
        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeAllPOIItems();
                if (isMyLocationActivated) {
                    isMyLocationActivated = false;
                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    mMapView.setShowCurrentLocationMarker(false);
                }
                editTextSearch.setText("");
                rvResults.removeAllViewsInLayout();
                isMyLocationActivated = false;
                isSearchResultSelected = false;
                isSearchResultShow = false;
                fabClear.setClickable(false);
            }
        });

        // 하단 네비게이션 바 세팅
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomactionbarMap);
        Menu menu = bottomNavigationView.getMenu();
        bottomNavigationView.setSelectedItemId(R.id.action_maps);

        /**********************************************************
         * function : public boolean onNavigationItemSelected
         * description : 하단 네비게이션 바의 아이템이 선택되었을 때의 처리를 담당하는 함수입니다.
         *               지도 화면 외의 즐겨찾기 화면, 메인 화면으로 이동합니다.
         * parameter : MenuItem menuItem - 함수를 발생시킨 메뉴 객체
         **********************************************************/
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.action_home:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.action_store:
                        Intent intent2 = new Intent(getApplicationContext(), stActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    /**********************************************************
     * function : protected void onDestroy
     * description : 앱이 종료될 때 호출되는 함수입니다.
     *               카카오맵의 현재 위치 기능 후처리를 위해 쓰였습니다
     **********************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 현재 위치 기능 완전히 종료
        if (checkLocationServicesStatus())
        {
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
            mMapView.setShowCurrentLocationMarker(false);
        }

    }

    /**********************************************************
     * function : void checkRunTimePermission()
     * description : 실제로 Runtime Permission을 처리하는 함수입니다.
     *               요청할 퍼미션의 상태가 어떤지, 거절한 적이 있는지 등을 파악하여
     *               각 상황에 맞게 위치 권한을 요청합니다.
     **********************************************************/
    void checkRunTimePermission(){
        // 런타임 퍼미션 처리
        // 위치 퍼미션을 가지고 있는지 체크합니다.

        if (checkLocationPermissionStatus()) {
            // 이미 퍼미션을 가지고 있다면 위치 값을 가져올 수 있음
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            isMyLocationActivated = true;
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        } else {  // 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우가 있습니다.
            // 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            Log.d("Location", "Check");
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.

                // 스낵바를 통해 권한을 요청할 수 있게 선택지를 줍니다.
                Snackbar.make(fabLayout, "해당 기능을 이용하려면 위치 권한이 필요합니다.", Snackbar.LENGTH_LONG)
                        .setAction("권한 요청", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
                                        PERMISSIONS_REQUEST_CODE);
                            }
                        }).show();
            } else {
                // 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    /**********************************************************
     * function : public void onClick
     * description : ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     *               허가가 나면 현재 위치 기능 실행 여부에 따라 현재 위치를 요청하거나 기능 설정을 실행하고,
     *               허가가 나지 않았으면 실행할 수 없는 이유를 설명하고 함수를 종료합니다.
     * parameter : int permsRequestCode - 위에서 작성했던 요청 코드입니다.
     *             String[] permissions - 퍼미션 목록입니다.
     *             int[] grandResults - 허가된 권한 결과 목록입니다.
     **********************************************************/
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                // 이 반복문 자체는 여러 권한 승인에 쓰이는데, 이번엔 그냥 위치만 넣었음.
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                Log.d("@@@", "start");
                // 퍼미션이 허용된 경우
                //위치 값을 가져올 수 있음
                if (checkLocationServicesStatus()) {
                    isMyLocationActivated = true;
                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                } else {
                    showDialogForLocationServiceSetting();
                }
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                // 그냥 거부한 경우
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(MapActivity.this, "퍼미션이 거부되었습니다. 해당 기능을 이용하려면 위치 권한이 필요합니다.",
                            Toast.LENGTH_LONG).show();
                }else {
                    // 다시 묻지 않음 옵션을 켜고 거부한 경우.
                    Snackbar.make(fabLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해주세요.", Snackbar.LENGTH_LONG)
                            .setAction("설정 이동", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);

                                }
                            }).show();
                }
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들입니다.

    /**********************************************************
     * function : private void showDialogForLocationServiceSetting
     * description : 위치 '기능'을 요청하는 함수입니다.
     *               위치 기능 활성화 상태는 아래 함수가 파악하므로,
     *               이 함수는 위치 기능이 없음을 전제하고 작동합니다.
     **********************************************************/
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MapActivity.this, "위치 서비스가 활성화되어야 해당 기능을 사용할 수 있습니다.", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    /**********************************************************
     * function : protected void onActivityResult
     * description : onActivityResult의 Override 함수입니다.
     *               위치 기능 활성화를 위해 showDialogForLocationServiceSetting에서 보낸 위치 설정에서
     *               다시 해당 앱으로 되돌아오면 작동하는 기능이 주입니다.
     * parameter : int requestCode - 요청 코드입니다. 요청이 제대로 돌아왔는지 확인할 때 쓰입니다.
     *             int resultCode - 결과값 코드입니다. 이 앱에서는 쓰이지 않습니다.
     *             Intent data - 해당 Result로 받아 온 데이터입니다. 이 앱에서는 쓰이지 않습니다.
     **********************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    /**********************************************************
     * function : public boolean checkLocationServicesStatus
     * description : 현재 위치 기능이 활성화되어있는지를 파악하는 함수입니다.
     **********************************************************/
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**********************************************************
     * function : public boolean checkLocationPermissionStatus
     * description : 현재 위치 권한이 활성화되어있는지를 파악하는 함수입니다.
     **********************************************************/
    public boolean checkLocationPermissionStatus() {
        return ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // 여기까지 위치 관련 함수

    // 여기서부터는 검색 관련 함수입니다.
    /**********************************************************
     * function : public void setPlaceList
     * description : 장소를 키워드 기반으로 검색하는 함수입니다.
     *               이 함수에서는 RetroInterface.getSearchByName을 사용하여 결과물을 받아옵니다.
     *               받아 온 결과물은 PlaceAdapter에 등록하고,
     *               그 Adapter를 RecyclerView에 등록해서 리스트를 표시합니다.
     * parameter : String keyword - 검색을 원하는 장소 키워드입니다.
     **********************************************************/
    public void setPlaceList(String keyword) {
        // Initialize
        RetroInterface retroInterface = RetroClient.getRetroClient().create(RetroInterface.class);

        // 키워드 기반 검색 통신 시작
        retroInterface.getSearchByName(
                "KakaoAK 31ab3132b75bf77e792e4ae15ed89658",
                keyword, 15, 1)
                .enqueue(new Callback<RespResult>() {
                    @Override
                    public void onResponse(Call<RespResult> call, Response<RespResult> response) {
                        if (response.isSuccessful()) {
                            RespResult data = response.body(); // 받은 값을 저장하는 변수
                            String code = response.code() + "";
                            Log.d("setPlaceList", code);

                            RespDocument listPlaceDoc = new RespDocument(); // 그 중에서도 장소 목록만 받아오는 변수
                            Log.d("setPlaceList", "SUCCESS");

                            // 받아 온 변수들을 RecyclerVie의 Adapter에 저장하는 부분
                            for (int iterPlace = 0; iterPlace < data.getListDocuments().size(); iterPlace++) {
                                listPlaceDoc = data.getListDocuments().get(iterPlace);
                                PlaceListProduct listProduct = new PlaceListProduct();
                                listProduct.setStrPlaceName(listPlaceDoc.getStrPlaceName());
                                listProduct.setStrPlaceAddress(listPlaceDoc.getStrAddressName());
                                listProduct.setStrPlaceType(listPlaceDoc.getStrCategoryGroupName());
                                listProduct.setStrPlaceX(listPlaceDoc.getStrX());
                                listProduct.setStrPlaceY(listPlaceDoc.getStrY());

                                placeAdapter.addItem(listProduct);
                            }
                            placeAdapter.notifyDataSetChanged();
                            Log.d("setPlaceList", placeAdapter.getItem(placeAdapter.getItemCount() - 1).getStrPlaceName());
                        } else {
                            Log.d("setPlaceList", "Failed");
                        }
                    }
                    @Override
                    public void onFailure(Call<RespResult> call, Throwable t) {
                        t.printStackTrace();
                        Log.d("setPlaceList", "FAIL");
                    }
                });

    }

    /**********************************************************
     * function : public void getSinglePageConvList
     * description : 편의점을 1페이지씩 검색하는 함수입니다.
     *               Retrofit과 카카오맵 API의 한계로, 이 함수는 결과물을 최대 15개까지만 리턴합니다.
     *               버튼을 여러 번 누르면, 추가로 최대 15개씩 결과물을 불러옵니다.
     * parameter : View searchArea - 검색하려는 영역입니다.
     *                               이 영역의 픽셀 값을 기준으로 MapView의 좌표값을 역산합니다.
     *             int numPage - 검색 페이지 변수입니다. 페이지가 늘어나면 다음 페이지를 검색하는 방식입니다.
     **********************************************************/
    public void getSinglePageConvList(View searchArea, int numPage) {
        // Initialize
        // 검색할 Rect 영역을 계산할 변수들
        float searchAreaX = searchArea.getX();
        float searchAreaY = searchArea.getY();
        int searchAreaPxWidth = searchArea.getWidth();
        int searchAreaPxHeight = searchArea.getHeight();
        Toast CA = Toast.makeText(this.getApplicationContext(), "이미 모든 편의점이 검색되었습니다.", Toast.LENGTH_LONG);

        // 여기서 검색할 영역을 계산합니다.
        MapPoint.GeoCoordinate centerPoint = mMapView.getMapCenterPoint().getMapPointGeoCoord();
        MapPoint.GeoCoordinate AreaTopLeft = MapPoint.mapPointWithScreenLocation(searchAreaX, searchAreaY).getMapPointGeoCoord();
        MapPoint.GeoCoordinate AreaBottomRight = MapPoint.mapPointWithScreenLocation
                (searchAreaX + searchAreaPxWidth, searchAreaY + searchAreaPxHeight).getMapPointGeoCoord();

        String AreaRect = Double.toString(AreaTopLeft.longitude) + ","
                + Double.toString(AreaTopLeft.latitude) + ","
                + Double.toString(AreaBottomRight.longitude) + ","
                + Double.toString(AreaBottomRight.latitude);

        // 실제 통신 부분
        RetroInterface retroInterface = RetroClient.getRetroClient().create(RetroInterface.class);
        retroInterface.getConvSearchByLocation(
                "KakaoAK 31ab3132b75bf77e792e4ae15ed89658",
                "CS2", Double.toString(centerPoint.longitude), Double.toString(centerPoint.latitude),
                AreaRect, numPage)
                .enqueue(new Callback<RespResult>() {
                    @Override
                    public void onResponse(Call<RespResult> call, Response<RespResult> response) {
                        Log.d("getSinglePageConvList", "RESPONSE ACTIVATED");
                        String code = response.code() + "";
                        Log.d("getSinglePageConvList", code);
                        if (response.isSuccessful()) {
                            RespResult data = response.body();
                            if (data.getMeta().getEnd()) {
                                // 리스트를 최대로 불러왔다면, 더 이상 이 기능을 쓰지 못하도록 해당 버튼을 막습니다.
                                CA.show();
                                Log.d("getSinglePageConvList", "OnResponse Ends");
                                fabConvSearch.setClickable(false);
                                return;
                            }
                            for (RespDocument document : data.getListDocuments()) {
                                createMarker(document);
                            }
                            for (int iter = 0; iter < data.getListDocuments().size(); iter++) {
                                Log.d("getSinglePageConvList",
                                        "Response Body : " + data.getListDocuments().get(iter).getStrPlaceName());
                            }
                        } else {
                            Log.d("getSinglePageConvList", "Failed");
                        }
                    }
                    @Override
                    public void onFailure(Call<RespResult> call, Throwable t) { }
                });
    }

    // 마커 생성 함수
    /**********************************************************
     * function : private void createMarker
     * description : 아래의 private void createMarker의 Overload 함수입니다.
     *               아래의 함수는 getSinglePageConvList를 위해 RespDocument로 만들어서,
     *               더 쉽게 쓸 수 있는 함수를 만들어두었습니다.
     * parameter : strPlaceName - 마커를 클릭하면 나오는 말풍선에 넣을 장소 이름입니다.
     *             x - 마커를 찍을 x좌표입니다.
     *             y - 마커를 찍을 y좌표입니다.
     **********************************************************/
    private void createMarker(String strPlaceName, String x, String y) {
        RespDocument tmp = new RespDocument();
        tmp.setStrPlaceName(strPlaceName);
        tmp.setStrX(x);
        tmp.setStrY(y);
        createMarker(tmp);
    }

    /**********************************************************
     * function : private void createMarker
     * description : 실제로 마커를 찍는 함수입니다. 마커는 편의점에 따라 색을 구분해두었습니다.
     *               편의점이 아닌 지역에는 기본 노란색 마커를 표시하도록 만들었습니다.
     * parameter : document - 마커를 표시할 위치의 정보를 담은 매개변수입니다.
     **********************************************************/
    private void createMarker(RespDocument document) {
        Log.d("createMarker", "Called");
        MapPOIItem mConvMarker = new MapPOIItem();
        MapPoint tmpPoint = MapPoint.mapPointWithGeoCoord
                (Double.parseDouble(document.getStrY()), Double.parseDouble(document.getStrX()));
        String strIdentify = identifyConvName(document.getStrPlaceName());
        mConvMarker.setItemName(document.getStrPlaceName());
        mConvMarker.setMapPoint(tmpPoint);

        // 일반 마커를 포함해서 마커 이미지 세팅
        if (strIdentify.equals("OTHER")) {
            mConvMarker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
            mConvMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        } else {
            mConvMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            switch (strIdentify) {
                case "GS25":
                    mConvMarker.setCustomImageResourceId(R.drawable.gs25_64clicked);
                    break;
                case "세븐일레븐":
                    mConvMarker.setCustomImageResourceId(R.drawable.seveneleven_64clicked);
                    break;
                case "미니스톱":
                    mConvMarker.setCustomImageResourceId(R.drawable.ministop_64clicked);
                    break;
                case "이마트24":
                    mConvMarker.setCustomImageResourceId(R.drawable.emart_64clicked);
                    break;
                case "CU":
                    mConvMarker.setCustomImageResourceId(R.drawable.cu_64clicked);
                    break;
                default:
                    Log.d(LOG_TAG, "On createConvMarker. Marker is Wrong....");
                    mConvMarker.setCustomImageResourceId(R.drawable.wronged);
                    break;
            }
            mConvMarker.setCustomImageAutoscale(true);
            mConvMarker.setCustomImageAnchor(0.5f, 1.0f);
        }
        // 마커찍기
        mMapView.addPOIItem(mConvMarker);
        mMapView.selectPOIItem(mConvMarker, true);
    }


    /**********************************************************
     * function : public String identifyConvName
     * description : 실제 편의점의 지점명을 받아서 편의점 이름을 리턴하는 함수입니다.
     * parameter : strFullConvName - 해당 편의점의 지점명을 포함한 이름입니다.
     **********************************************************/
    public String identifyConvName(String strFullConvName) {
        String tmp = strFullConvName.substring(0, 2);
        switch (tmp) {
            case "GS":
                return "GS25";
            case "세븐":
                return "세븐일레븐";
            case "미니":
                return "미니스톱";
            case "이마":
                return "이마트24";
            case "CU":
                return "CU";
            default:
                return "OTHER";
        }
    }


    /**********************************************************
     * function : public void startSearch()
     * description : 장소 검색 버튼을 눌렀을 때 작동하는 함수입니다.
     *               장소 검색 뿐만 아니라, 그 전이나 도중에 처리해야 할 이벤트나 에러들을 처리합니다.
     **********************************************************/
    public void startSearch() {
        if (editTextSearch.getText() == null || editTextSearch.getText().toString().length() == 0) {
            // 검색어를 입력 안했거나 오류가 나서 없으면
            Toast.makeText(getApplicationContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();

        } else {
            // 검색어가 잘 있는 경우
            String SearchText = editTextSearch.getText().toString();
            Log.d("btnSearch.Onclick", SearchText);
            isSearchResultShow = true;

            // 검색 후에는 키보드 숨기기기
            inputMethodManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);

            // 검색화면으로 UI 변경
            btnReturn.setVisibility(View.VISIBLE);
            fabLayout.setVisibility(View.GONE);
            rvResults.setVisibility(View.VISIBLE);
            placeAdapter.clearItem();
            setPlaceList(editTextSearch.getText().toString()); // 검색 시작
        }
    }

    // 아래의 함수는 카카오맵 리스너들의 오버라이드 함수입니다.
    // 특정 기능을 제외하고는 쓰지 않으나, 이 리스너들은 인터페이스라서 적어만 두었습니다.
    // 실제로 사용하는 기능에만 함수 설명을 추가했습니다.

    // CurrentLocationEventListener의 Override Method
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
    // Override Method Ends.

    // POIItemEventListener의 Override Method.
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
    // Override Method Ends.

    // MapviewEventListener의 Override Method
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }
    /**********************************************************
     * function : public void onMapViewCenterPointMoved
     * description : 지도 중앙점이 움직였을 경우 호출되는 함수입니다.
     *               이 앱에서는 편의점 검색 버튼 활성화를 감지하는 데 쓰였습니다.
     * parameter : MapView mapView - 현재 쓰고 있는 MapView
     *             MapPoint mapPoint - 중심 좌표
     **********************************************************/
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        isMapScreenMoved = true;
        numPageCount = 0;
        if (!fabConvSearch .isClickable())
            fabConvSearch.setClickable(true);
    }
    /**********************************************************
     * function : public void onMapViewZoomLevelChanged
     * description : 지도 축척 레벨이 바뀌었을 경우 호출됩니다.
     *               이 앱에서는 편의점 검색 버튼 활성화를 감지하는 데 쓰였습니다.
     * parameter : MapView mapView - 현재 쓰고 있는 MapView
     *             int i - 지도 축척 레벨
     **********************************************************/
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        isMapScreenMoved = true;
        numPageCount = 0;
        if (!fabConvSearch .isClickable())
            fabConvSearch.setClickable(true);    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
    // Override Method Ends.
}