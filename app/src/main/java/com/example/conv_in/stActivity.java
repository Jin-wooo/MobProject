/*********************************************************
 * Name : 정재욱
 * Student ID : 20163155
 * Program ID : stActivity.java
 * Description :
 * **********************************************************/

package com.example.conv_in;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.conv_in.Fragments.stSearchFragement;
import com.example.conv_in.Fragments.starFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class stActivity extends AppCompatActivity {
    private starFragment f6;
    private stSearchFragement f7;
    private    int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_activity);

        f6 = new starFragment();
        /*********************************************************
         * 즐겨찾기 버튼을 눌렀을 시 해당 Activity가 뜨며 starFragment를 불러와 리스트를 보여주는 부분
         * 하단 메뉴바를 통해 홈과 지도로 갈 수 있게끔 구현.
         * Description : **********************************************************/
        getSupportFragmentManager().beginTransaction().replace(R.id.store,f6).addToBackStack(null).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomactionbarst);
        Menu menu = bottomNavigationView.getMenu();
        bottomNavigationView.setSelectedItemId(R.id.action_store);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.action_home:
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.action_maps:
                        Intent intent2 = new Intent(getApplicationContext(),MapActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return true;
            }
        });
        final EditText editText = (EditText) findViewById(R.id.editTextSearch);
        /*********************************************************
         * EditText로 만들어진 검색 창에 2글자 이상 검색을 하였을 경우
         * 즐겨찾기 메뉴에서만 검색을 실행 검색 결과를 보여줌.
         * Description : **********************************************************/
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final boolean isEnterEvent = event != null
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;

                final boolean isEnterUpEvent = isEnterEvent && event.getAction() == KeyEvent.ACTION_UP;
                final boolean isEnterDownEvent = isEnterEvent && event.getAction() == KeyEvent.ACTION_DOWN;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    {
                        Log.d("oneditor",String.valueOf(actionId));
                        if (editText.getText().toString().length() >= 2) {
                            f7 = new stSearchFragement();
                            Bundle bundle = new Bundle();
                            bundle.putString("value2", editText.getText().toString());
                            f7.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.store, f7).commit();

                            editText.clearFocus();
                            editText.setText("");
                            editText.setFocusable(false);
                            editText.setFocusableInTouchMode(true);
                            editText.setFocusable(true);
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.store, f6).commit();
    }
}

