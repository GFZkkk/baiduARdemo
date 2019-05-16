package com.ambow.ar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ambow.ar.utils.DensityUtil;
import com.ambow.ar.utils.SoftKeyBoardListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaofengze on 2019/3/18
 */
public class ContentActivity extends BaseARActivity implements View.OnClickListener {
    @BindView(R.id.web_view)
    WebView webView;
    String url;
    @BindView(R.id.bottom)
    FrameLayout bottom;
    int bottomHeight;
    @BindView(R.id.cl_error)
    LinearLayout clError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        bottomHeight = 0;
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                bottom.setPadding(0, height + bottomHeight, 0, 0);
            }

            @Override
            public void keyBoardHide(int height) {
                bottom.setPadding(0, bottomHeight, 0, 0);
            }
        });
        if (DensityUtil.isNavigationBarShow(this)) {
            bottomHeight = 100;
            bottom.setPadding(0, 100, 0, 0);
        }
        setWebView();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) finish();
        url = bundle.getString("url", "");
        Log.d("gfz", url);
        webView.loadUrl(url);
    }

    @Override
    public void toAr() {
        if (!isLoading()) {
            loadAr();
            finish();
        }
    }

    @Override
    public void toIndex() {
        startActivity(new Intent(this, IndexARActivity.class));
        finish();
    }

    @Override
    public ViewGroup getErrorCL() {
        return clError;
    }

    @Override
    public WebView getWebview() {
        return webView;
    }

    @Override
    public void startActivity(WebView view, String url) {
        view.loadUrl(url);
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @OnClick({R.id.error})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error:
                getWebview().loadUrl(url);
                break;
            default:
        }
    }
}
