package com.project.unithon.unithon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends FragmentActivity {


    private Intent FirstSurveyIntent;
    private Intent HomeIntent;
    private String token;

    SharedMemory sharedMemory;
    Boolean isDownloadFirst = false;
    UserInfoVO userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedMemory = SharedMemory.getinstance();
        userinfo = new UserInfoVO();

        FirstSurveyIntent= new Intent(this, FirstSurveyActivity.class);
        HomeIntent = new Intent(this,HomeActivity.class);


        //토큰값을 받기위해 브로드캐스트 리시버 설
        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver,
                new IntentFilter("tokenReceiver"));

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                token = FirebaseInstanceId.getInstance().getToken();
                if (token != null && isDownloadFirst == false) {  //이미 어플이 설치된적이 있는 상태에서 토큰값을 가져온 경우
                    //http커넥션으로 토큰값을 데이터베이스에서 검색
                    //answer 에 응답을 받는다
//                    if(answer.equals("not found token")){ // 토큰과 매치되는 아이디가 없을 경우
//                        startActivity(loginIntent);
//                        finish();
//                    }else{ //가지고 있는 토큰으로 아이디를 찾았을 경우
//                        Log.d(">>>>>>>>>",answer + "aaaaaaaaa");
//                        userinfo = gson.fromJson(answer,UserInfo.class);
                            userinfo.setToken(token);
                            sharedMemory.setUserinfo(userinfo);

                        startActivity(HomeIntent);
                        finish();}
            }
        }, 3000);


    }

    Button.OnClickListener bListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

            }
        }
    };

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            token = intent.getStringExtra("token");
            if (token != null) {
                //send token to your server or what you want to do
                //토큰이 새로 만들어졌을 경우
                    isDownloadFirst = true;
                    userinfo.setToken(token);
                    sharedMemory.setUserinfo(userinfo);;
                    startActivity(FirstSurveyIntent);
                    finish();
                }
        }
    };

}