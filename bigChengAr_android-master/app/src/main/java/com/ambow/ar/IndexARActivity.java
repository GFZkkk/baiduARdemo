package com.ambow.ar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ambow.ar.utils.DensityUtil;
import com.baidu.ar.util.ARLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ambow.ar.ARApp.indexUrl;
import static com.ambow.ar.ARApp.userUrl;

/**
 * Created by gaofengze on 2019/3/18
 */
public class IndexARActivity extends BaseARActivity implements View.OnClickListener {

    @BindView(R.id.web_view)
    WebView index;
    @BindView(R.id.bottom)
    FrameLayout bottom;
    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.cl_error)
    LinearLayout clError;
//    String indexUrl = "http://10.10.60.170:8080/index.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        if (DensityUtil.isNavigationBarShow(this)) {
            bottom.setPadding(0, 100, 0, 0);
        }
        ARLog.setDebugEnable(true);
        setWebView();

        index.loadUrl(indexUrl);
    }

    @Override
    public WebView getWebview() {
        return index;
    }

    @Override
    public void toAr() {
        if (!isLoading()) {
            loadAr();
        }
    }

    @Override
    public void startActivity(WebView webView, String url) {
        webView.loadUrl(url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        index.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        index.onResume();
    }

    @Override
    public ViewGroup getErrorCL() {
        return clError;
    }

    @OnClick({R.id.error})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.error:
                getWebview().loadUrl(indexUrl);
                break;
            default:
        }
    }
}
