README

팀 과제 설명을 위한 파일
테스트는 Pixel 2 안드로이드 R(API 30)에서 진행했습니다.

저희가 만든 앱은 편의점 정보 제공 어플입니다.
크게 2가지 기능으로 나눠지는데, 하나는 편의점 할인 정보,
다른 하나는 편의점을 찾는 데에 특화되어있는 지도 기능입니다.
전자는 정재욱 학생이, 후자는 제가 맡아서 작업했습니다.
이 외의 작업은 아래쪽에서 다시 설명드리겠습니다.



- - 2020-11-22 추가 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

저희가 만든 앱을 제가 아닌 다른 사람이 실행시키려면, 제가 하나하나 그 사람의 디버그 키를

입력해넣지 않으면 안된다는 사실을 너무 뒤늦게 알았습니다.

교수님의 디버그 키를 받아서 넣으면 못할 일도 아니지만,

그렇게 하는 건 좀 아닌거같다고 의견이 모여서, 아예 릴리즈 파일을 만들기로 했습니다.

게다가 원래부터 해당 어플은 AVD에서는 완전하게 작동하지 않았습니다.

카카오에서는 AVD를 제대로 지원하지 않기 때문입니다.

따라서 저희의 코드는 그냥 코드 자체로만 봐주시고,

실제 실행 결과는 APK 파일을 참조해주시면 될 것 같습니다.



PPT 파일이나 추가적으로 필요한 파일들은 다 ADDITIONAL 폴더에 있습니다.

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



먼저 앱 설명입니다.

1. MapActivity

지도를 띄우는 액티비티이자 제가 맡은 기능의 유일한 액티비티입니다.

핵심적인 기능은 3가지입니다. 코드에 모든 함수에 관한 주석이 달려있으므로,

여기서는 중요한 변수들과 핵심 기능 관련 함수만 기능별로 모아 설명합니다.

1-1. 중요 변수
     private MapView mMapView - 카카오맵에서 지원하는 카카오맵 변수입니다.
                                맵 화면 처리는 이 변수가 담당합니다.

     private CoordinatorLayout fabLayout - 
	
	아래쪽의 FloatingActionButton을 담아두는 Layout입니다.

     private EditText editTextSearch - 
	검색창입니다.앱 내의 키보드 엔터키를 검색 기능으로 바꾸는 Listener가 있습니다.

     private ImageButton btnReturn - 검색창 왼쪽의, 뒤로 돌아가는 버튼입니다.
 
     private ImageButton btnSearch - 검색창 오른쪽의, 검색 버튼입니다.

     FloatingActionButton fabMyLocation - 내 위치 정보를 표시하는 버튼입니다.

     FloatingActionButton fabConvSearch - 편의점만을 검색하는 버튼입니다.

     FloatingActionButton fabClear - 모든 맵 상의 기록들을 초기화시키는 버튼입니다.

     private RecyclerView rvResults - 장소 검색 결과를 표시하는 RecyclerView입니다.      private LinearLayoutManager layoutManager - RecyclerView에 Adapter로 만든 View를 넣는 변수입니다.

     private PlaceListAdapter placeAdapter - RecyclerView에 데이터를 넣는 변수입니다.



1-2. 핵심 기능

1-2-1 내 위치 추적

어플 사용자의 위치를 GPS를 기반으로 추적하는 기능입니다.

해당 기능을 담당하는 함수들은 다음과 같습니다.


[public void onRequestPermissionsResult]

requestPermission 함수가 호출된 이후에 자동으로 호출되는 오버라이드 함수입니다.

requestPermission 함수의 뒷처리를 맡습니다.

적어둔 권한이 모두 승인되었고, 그것이 확인되었다면 바로 

카카오맵의 MapView.setCurrentLocationTrackingMode를 통해 현재 위치를 표시합니다.

그렇지 않은 경우에는 위치 권한을 허용하지 않았거나,

위치 권한 허용 안내를 아예 꺼버린 경우인데,

전자의 경우 토스트를 띄워서 왜 이 기능을 쓸 수 없는지 설명하고,

후자의 경우 기능 수행을 위해서는 설정에서 직접 권한을 허용해야 한다는 Snackbar를 띄우고

사용자는 Snackbar 옆의 버튼을 통해 바로 설정의 해당 항목으로 이동할 수 있게 만들었습니다.


[void checkRunTimePermission]

실제로 Permission 요청을 수행하는 부분입니다.

현재 앱이 퍼미션을 가지고 있는지를 체크하고, 퍼미션을 요청합니다.

이 함수도 상황에 따라 분기가 나눠지는데,

퍼미션 요청이 필요 없는 6.0 이하의 버전인 경우, 요청을 거부한 적이 있는 경우,

그리고 요청을 거부한 적이 없는 경우로 나눠집니다.

첫 번째는 그냥 넘기고, 두 번째는 Snackbar를 통해 해당 기능이 왜 이 퍼미션이 필요한지를

설명하고, 세 번째는 즉시 퍼미션 요청을 수행합니다.



[private void showDialogForLocationServiceSetting]

위치 기능을 요청하는 함수입니다.

위치 기능을 작동 상태를 파악하는 것은 아래 함수가 하고 있으므로,

해당 함수는 위치 기능이 없는 것을 전제하고 작동하는 함수입니다.

AlertDialog를 통해 위치 기능 작동을 요청하고, 승인하면 해당 설정으로,

거부하면 현재 위치 기능을 사용할 수 없는 이유를 알려주는 Toast를 띄웁니다.



[public boolean checkLocationServicesStatus]

현재 위치 기능 활성화 상태를 체크하는 함수입니다.

이 함수를 통해 지금 없는 게 위치 기능인지, 위치 권한인지를 파악합니다.



1-2. 장소 검색 기능

편의점을 검색하는 것이 아니라, 편의점을 검색하고 싶은 지역을 검색하는 기능입니다.

카카오맵의 REST API와 Retrofit을 이용하여 카카오 서버와 통신을 시도하고,

그 결과로 나온 값을 RecyclerView인 rvResults에 표시합니다.

해당 기능을 수행하는 함수는 다음과 같습니다.


[public void setPlaceList]

Retrofit과 REST API를 통해 장소를 키워드 기반으로 검색하는 함수입니다.

키워드를 retroInterface를 통해 전달해주면, 이를 getSearchByName 함수로 받아서 통신합니다.

그렇게 해서 OnResponse의 값이 정상적으로 들어오면, 해당 데이터를 파싱합니다.

파싱한 데이터는 하나씩 PlaceAdapter에 넣고,

notifyDataSetChanged 함수를 통해 RecyclerView에게 데이터 변경을 통보합니다.

이 함수를 통해 RecyclerView는 데이터를 갱신합니다.



1-3. 편의점 검색 기능

장소 검색에 다 붙이지 않은 이유는, 편의점만을 따로 검색하는 기능이 필요해서였습니다.

장소는 이름을 붙여서 검색하지만, 편의점은 버튼 하나만 누르면 근처의 편의점을 검색할 수

있게 만들어두었습니다.

물론 장소 검색에서도 편의점을 검색할 수 있습니다.

해당 기능을 수행하는 함수는 다음과 같습니다.

[public void getSinglePageConvList]

setPlaceList와 마찬가지로 Retrofit과 REST API를 통해 검색하는 함수입니다.

다만 여기서는 카테고리 기반으로 검색을 수행합니다.

편의점 검색이다보니, 카테고리는 편의점 딱 하나로 고정됩니다.

이 함수는 실제 통신 부분 위쪽이 좀 많은데,

이는 검색을 원하는 영역만큼만 검색을 수행하기 위해 필요한 작업입니다.

우선 검색을 원하는 영역의 왼쪽 위 픽셀과 그 영역의 넓이 픽셀을 구합니다

(저는 아무 기능 없는 뷰를 하나 더 추가하여 인식시켰습니다.)

그리고 이 픽셀 영역을 좌표 영역으로 바꿔줍니다.

여기서는 카카오맵의 mapPointWithScreenLocation 함수를 사용해서.

뷰의 픽셀 영역을 그대로 카카오맵 지도에 대입시키는 방식으로 좌표를 얻어왔습니다.

이후 영역을 위한 MapPoint를 만들어두고, 

그것을 토대로 사각형 영역을 좌표기준으로 생성합니다.

이 Rect 영역을 REST API를 통해 서버로 보내어 검색을 합니다.

그리고 이렇게 받아온 편의점들은 createMarker 함수를 통해 마킹합니다.

그래서 해당 함수를 가진 fabConvSearch는, 클릭하면 바로 편의점의 갯수를 보여줍니다.


해당 함수의 이름이 SinglePage인 이유는, 그 지역 편의점이 엄청 많아도, 

실제로 한 번 통신에 결과값을 15개밖에 가져올 수 없기 때문입니다.

그래서 저는 버튼을 누르면 추가로 편의점 정보를 가져올 수 있게,

재검색하는 방식으로 해당 버튼을 구현했습니다.

재검색할 수 없으면, 해당 버튼은 Toast를 내보내고 Unclickable이 됩니다.

이 클릭 금지는 지도 줌 레벨을 바꾸거나 이동하면 풀리고, 새로 검색을 시작합니다.

카카오맵 API의 한계로, 저는 결과값이 얼마나 나오든 계속 검색할 수 있게 만들었으나,

최대 30개의 편의점까지만 검색이 가능한 것 같습니다...


추가로 이 검색방법은 Rect 영역 좌표만 있으면 가능하지만, 

가운데 좌표가 들어가는 이유는, 추가로 구현하고 싶었던 거리 측정을 위해 넣었었습니다.

이는 시간 부족으로 인해 우선순위가 밀렸고, 사용되지 않고 남았습니다.





2. PlaceList

처음으로는 RecyclerView에 쓰이는 Adapter는 PlaceListAdapter입니다.

이 어댑터를 extend하는 것은 RecyclerView를 구현할 때 반드시 필요한 작업입니다.

Adapter 자체는 View를 만들어서 RecyclerView 안에 넣는 일을 합니다.

사실 내부는 단순히 리스트이므로, 거의 대부분 리스트의 데이터 관리가 코드의 주입니다.

그리고 이 어댑터의 리스트의 자료형이 바로 PlaceListProduct입니다.

이쪽도 단순히 자료 저장용 클래스이므로, 복잡힌 기능은 없고,

이름이나 주소, 좌표 등을 저장합니다.



3. Retros

검색 기능 구현을 위해서는 REST API를 통한 통신이 필요하므로, 

이 앱에서는 Retrofit2를 사용했습니다

그 통신을 위한 파일들이 Models와 Retros 안에 있습니다.

Retrofit으로 통신할 때, 이 통신하고 난 결과값을 받아올 클래스도 필요합니다.

이 기능을 수행하는 클래스는 RespResult이고, 이게 Models 안에 있습니다.

Meta와 Document List로 나눠지는데, 각각 RespMeta와 RespDocument에 정의되어 있습니다.

Meta의 경우 지금 가져온 리스트의 외부적인 정보, 즉 어느 정도의 페이지이고,

다음에 더 페이지가 있는가? 같은 부가적인 정보를 제공합니다.

Document List의 경우 실제 장소의 정보를 담고 있습니다.

이 리스트의 하나하나에는 각각의 장소 이름, 주소, 좌표, 장소 유형 등이 저장되어 있습니다.

또한 Retros에는 직접적인 통신을 위한 파일이 있습니다.

RetroInterface는 Retrofit2와 REST API로 통신할 때, 통신을 요청하는 인터페이스이고,

RetroClient는 Retrofit2로 카카오맵과 통신하고 객체 정보를 반환할 Retrofit 객체입니다.

