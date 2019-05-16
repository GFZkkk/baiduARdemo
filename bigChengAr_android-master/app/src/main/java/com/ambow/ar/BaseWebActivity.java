package com.ambow.ar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;

/**
 * Created by gaofengze on 2019/3/18
 */
public abstract class BaseWebActivity extends BaseActivity{
    private boolean isSuccess = false;
    private boolean isError = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setWebView(){
        WebSettings settings = getWebview().getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(true);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM Storage
        settings.setBuiltInZoomControls(true);
        getWebview().setSaveEnabled(false);
        getWebview().addJavascriptInterface(new JsOPR(), "feiyi");
        getWebview().setBackgroundColor(0); // 设置背景色 0表示透明

        getWebview().setWebViewClient(getWebViewClient());
//        setZoomControlGone(getWebview());
    }

    final class JsOPR {
        @JavascriptInterface
        public void BgImage(String file, int color_r, int color_g, int color_b) {
        }

        @JavascriptInterface
        public void jsCMD(String url) {
            Log.d("gfz",url);
            if("openAR".equals(url)){
                toAr();
            }else if("openIndex".equals(url)){
                toIndex();
            }else if(url.contains("tel")){
                call(url);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getWebview().canGoBack()) {
            getWebview().goBack(); // goBack()表示返回WebView的上一页面
        }else{
            super.onBackPressed();
        }

    }

    public void toAr() {

    }

    public void toIndex(){

    }

    public WebViewClient getWebViewClient() {
        return new WebViewClient(){

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isError) {
                    isSuccess = true;
                    //回调成功后的相关操作
                    getWebview().setVisibility(View.VISIBLE);
                    getErrorCL().setVisibility(View.INVISIBLE);
                }
                isError = false;
            }

            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError = true;
                isSuccess = false;
                getWebview().setVisibility(View.INVISIBLE);
                getErrorCL().setVisibility(View.VISIBLE);
                //回调失败的相关操作

            }
        };
    }

    public abstract ViewGroup getErrorCL();

    private void call(String phone) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(phone)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 隐藏放大缩小控件
     */
    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
                    view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public abstract WebView getWebview();

    public abstract void startActivity(WebView view,String url);
}
