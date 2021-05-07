/*********************************************************
 * Name : 정재욱
 * Student ID : 20163155
 * Program ID : MainActivity.java
 * Description : **********************************************************/


package com.example.conv_in;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.conv_in.Fragments.CUFragment;
import com.example.conv_in.Fragments.EmartFragment;
import com.example.conv_in.Fragments.GSFragment;
import com.example.conv_in.Fragments.MiniFragment;
import com.example.conv_in.Fragments.SearchFragment;
import com.example.conv_in.Fragments.SevenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    public com.example.conv_in.Fragments.EmartFragment f1 ;
    public com.example.conv_in.Fragments.CUFragment f2;
    public com.example.conv_in.Fragments.SevenFragment f3;
    public com.example.conv_in.Fragments.GSFragment f4 ;
    public com.example.conv_in.Fragments.MiniFragment f5;
    public DatabaseReference mDatabase;
    public BottomNavigationView bottomNavigationView;
    public Menu menu;
    public int[] checked = new int[5];
    public com.example.conv_in.Fragments.SearchFragment searchFragment;

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.conv_in", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        f1 = new EmartFragment();
        f2 = new CUFragment();
        f3 = new SevenFragment();
        f4 = new GSFragment();
        f5 = new MiniFragment();
        final ImageButton emart = (ImageButton) findViewById(R.id.emart);
        final ImageButton seven = (ImageButton) findViewById(R.id.seven);
        final ImageButton cu = (ImageButton) findViewById(R.id.CU);
        final ImageButton gs = (ImageButton) findViewById(R.id.gs);
        final ImageButton mini = (ImageButton) findViewById(R.id.mini);

        /*********************************************************
         * 홈에 있는 각 메뉴들의 버튼을 클릭 했을 때 배경을 바꾸어주어
         * 각각의 메뉴가 선택했음을 보여주는 부분
         * 각 프래그먼트의 백스택의 값을 초기화 해주고 검색을 했을 시 뒤로가기로 처음 리스트의 값을 볼 수 있게끔 구현
         * Description : **********************************************************/
        emart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container1,f1).addToBackStack(null).commit();
                emart.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selectbutton));
                checked[0]=1;
                if(checked[1] == 1){
                    cu.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[1] = 0;
                }else if(checked[2] == 1){
                    seven.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[2] = 0;
                }else if(checked[3] == 1){
                    gs.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[3] = 0;
                }else if(checked[4] == 1){
                    mini.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[4] = 0;
                }
            }
        });
        cu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container1,f2).addToBackStack(null).commit();
                cu.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selectbutton));
                checked[1]=1;
                if(checked[0] == 1){
                    emart.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[0] = 0;
                }else if(checked[2] == 1){
                    seven.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[2] = 0;
                }else if(checked[3] == 1){
                    gs.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[3] = 0;
                }else if(checked[4] == 1){
                    mini.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[4] = 0;
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container1,f3).addToBackStack(null).commit();
                seven.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selectbutton));
                checked[2]=1;
                if(checked[0] == 1){
                    emart.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[0] = 0;
                }else if(checked[1] == 1){
                    cu.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[1] = 0;
                }else if(checked[3] == 1){
                    gs.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[3] = 0;
                }else if(checked[4] == 1){
                    mini.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[4] = 0;
                }
            }
        });
        gs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container1,f4).addToBackStack(null).commit();
                gs.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selectbutton));
                checked[3]=1;
                if(checked[0] == 1){
                    emart.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[0] = 0;
                }else if(checked[1] == 1){
                    cu.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[1] = 0;
                }else if(checked[2] == 1){
                    seven.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[2] = 0;
                }else if(checked[4] == 1){
                    mini.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[4] = 0;
                }
            }
        });
        mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container1,f5).addToBackStack(null).commit();
                mini.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selectbutton));
                checked[4]=1;
                if(checked[0] == 1){
                    emart.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[0] = 0;
                }else if(checked[1] == 1){
                    cu.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[1] = 0;
                }else if(checked[2] == 1){
                    seven.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[2] = 0;
                }else if(checked[3] == 1){
                    gs.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nomalbutton));
                    checked[3] = 0;
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottomactionbar);
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        /*********************************************************
         * 하단 메뉴의 각각이 클릭 됐을 경우
         * 새로운 액티비티로 전환 자기 자신의 액티비티는 띄우지 않음.
         * Description : **********************************************************/
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.action_maps:
                        Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.action_store:
                        Intent intent2 = new Intent(getApplicationContext(),stActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
        final EditText editText = (EditText) findViewById(R.id.editTextSearch);
        /*********************************************************
         * EditText로 만들어진 검색 창에 2글자 이상 검색을 하였을 경우
         * SearchFragment가 실행되면서 검색 결과를 보여줌.
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
                            searchFragment = new SearchFragment();
                            Bundle bundle = new Bundle();
                            bundle.putIntArray("checked", checked); //fragment로 데이터전달
                            bundle.putString("value", editText.getText().toString());
                            searchFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container1, searchFragment).addToBackStack(null).commit();
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}