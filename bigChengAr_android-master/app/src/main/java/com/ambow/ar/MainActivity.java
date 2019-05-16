package com.ambow.ar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.ar.ARFragment;
import com.baidu.ar.constants.ARConfigKey;
import com.baidu.ar.external.ARCallbackClient;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ambow.ar.ARApp.userUrl;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.bdar_id_fragment_container)
    FrameLayout bdarIdFragmentContainer;
    @BindView(R.id.iv_zng)
    ImageView ivZng;
    @BindView(R.id.left)
    NestedScrollView left;
    @BindView(R.id.black)
    NestedScrollView black;
    @BindView(R.id.right)
    NestedScrollView right;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.show)
    ConstraintLayout show;
    @BindView(R.id.loading)
    LinearLayout loading;
    private ARFragment mARFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        ButterKnife.bind(this);

        if (findViewById(R.id.bdar_id_fragment_container) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // 准备调起AR的必要参数
            // AR_KEY:AR内容平台里申请的每个case的key
            // AR_TYPE:AR类型，目前0代表2D跟踪类型，5代表SLAM类型，后续会开放更多类型
            // AR_PATH:本地AR内容的路径
            String arkey = getIntent().getStringExtra(ARConfigKey.AR_KEY);
            int arType = getIntent().getIntExtra(ARConfigKey.AR_TYPE, 0);
            String arPath = getIntent().getStringExtra(ARConfigKey.AR_PATH);
            Bundle data = new Bundle();
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put(ARConfigKey.AR_KEY, arkey);
                jsonObj.put(ARConfigKey.AR_TYPE, arType);
                jsonObj.put(ARConfigKey.AR_PATH, arPath);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.putString(ARConfigKey.AR_VALUE, jsonObj.toString());
            mARFragment = new ARFragment();
            mARFragment.setArguments(data);
            mARFragment.setARCallbackClient(new ARCallbackClient() {
                // 分享接口
                @Override
                public void share(String title, String content, String shareUrl, String resUrl, int type) {
                    // type = 1 视频，type = 2 图片
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, resUrl);
                    shareIntent.putExtra(Intent.EXTRA_TITLE, title);
                    shareIntent.setType("text/plain");
                    // 设置分享列表的标题，并且每次都显示分享列表
                    try {
                        startActivity(Intent.createChooser(shareIntent, "分享到"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 透传url接口：当AR Case中需要传出url时通过该接口传出url
                @Override
                public void openUrl(String url) {
                    switch (url) {
                        case "black":
                        case "left":
                        case "right":
                        case "zng":
                            show(url);
                            break;
                        case "gift":
                            startActivity(new Intent(MainActivity.this, ContentActivity.class).putExtra("url", userUrl));
                            break;
                        case "loadFinish":
                            loading.setVisibility(View.INVISIBLE);
                            break;
                        default:
                    }
                }

                // AR黑名单回调接口：当手机不支持AR时，通过该接口传入退化H5页面的url
                @Override
                public void nonsupport(String url) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri contentUrl = Uri.parse(url);
                    intent.setData(contentUrl);
                    try {
                        MainActivity.this.finish();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
//            mARFragment.setARCaptureResultCallback(new ARCaptureResultCallback() {
//                @Override
//                public void onPictureTaken(String filePath) {
//                    Toast.makeText(ARActivity.this, "picture filepath=" + filePath, Toast.LENGTH_SHORT)
//                            .show();
//                }
//
//                @Override
//                public void onVideoTaken(String filePath) {
//                    Toast.makeText(ARActivity.this, "video filepath=" + filePath, Toast.LENGTH_SHORT)
//                            .show();
//                }
//            });
            // 将trackArFragment设置到布局上
            fragmentTransaction.replace(R.id.bdar_id_fragment_container, mARFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void show(String type) {
        left.scrollTo(0,0);
        right.scrollTo(0,0);
        black.scrollTo(0,0);
        show.setVisibility(View.VISIBLE);
        left.setVisibility(getVB("left".equals(type)));
        right.setVisibility(getVB("right".equals(type)));
        black.setVisibility(getVB("black".equals(type)));
        ivZng.setVisibility(getVB("zng".equals(type)));
    }

    public int getVB(boolean f) {
        return f ? View.VISIBLE : View.INVISIBLE;
    }


    @Override
    public void onBackPressed() {
        boolean backFlag = false;
        if (mARFragment != null) {
            backFlag = mARFragment.onFragmentBackPressed();
        }
        if (!backFlag) {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View v) {
        show.setVisibility(View.INVISIBLE);
    }
}