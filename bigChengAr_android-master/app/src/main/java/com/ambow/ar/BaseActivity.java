package com.ambow.ar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.LinkedList;

import static com.ambow.ar.ARApp.activityList;


/**
 * Created by gfz on 2018/10/12
 */
public abstract class BaseActivity extends AppCompatActivity {
    private long clickTime = 0;

    /**
     * a1.维护全局activity队列
     * 2.创建自己的fragment队列
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(activityList == null){
            activityList = new LinkedList<>();
        }
        activityList.add(this);
    }

    /**
     * 再按一次退出程序
     */
    private void exit(){
        if (activityList.size() == 1 &&(System.currentTimeMillis() - clickTime) > 2100) {
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {

            this.finish();
        }
    }
    /**
     * 监听回退健
     */
    @Override
    public void onBackPressed() {
        exit();
    }


    /**
     * 防止内存泄漏
     * 维护总的activity队列
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }


}
