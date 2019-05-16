package com.ambow.ar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.ambow.ar.utils.AssetsCopyToSdcard;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by gaofengze on 2019/3/18
 */
public abstract class BaseARActivity extends BaseWebActivity {
    private boolean isLoading = false;
    private boolean mIsDenyAllPermission = false;
    protected Intent intent;
    public static final String ASSETS_CASE_FOLDER = "ardebug";
    public static final String DEFAULT_PATH =
            Environment.getExternalStorageDirectory().toString() + "/" + ASSETS_CASE_FOLDER;

    // 权限请求相关
    protected static final String[] ALL_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    protected static final int REQUEST_CODE_ASK_ALL_PERMISSIONS = 154;

    public void loadAr() {
        isLoading = true;
        Bundle bundle = new Bundle();
        bundle.putString("ar_key", "");
        bundle.putInt("ar_type", 0);
        bundle.putString("ar_path", DEFAULT_PATH);
        intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        // 拷贝文件到SD卡
        if (!TextUtils.isEmpty(DEFAULT_PATH)) {
            requestAllPermissions(REQUEST_CODE_ASK_ALL_PERMISSIONS);
        } else {
            startActivity(intent);
        }
    }

    public class CopyFileTask extends AsyncTask {
        private final Intent intent;
        private final WeakReference<Context> contextRef;

        public CopyFileTask(Intent intent, Context context) {
            this.intent = intent;
            this.contextRef = new WeakReference<>(context);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Context context = contextRef.get();
            if (context != null) {
                AssetsCopyToSdcard assetsCopyTOSDcard = new AssetsCopyToSdcard(context);
                assetsCopyTOSDcard.assetToSD(ASSETS_CASE_FOLDER, DEFAULT_PATH);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (contextRef.get() != null) {
                Toast.makeText(contextRef.get(), "加载完成", Toast.LENGTH_SHORT).show();
                contextRef.get().startActivity(intent);
                isLoading = false;
            }
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    /**
     * 请求权限
     *
     * @param requestCode
     */
    protected void requestAllPermissions(int requestCode) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<String> permissionsList = getRequestPermissions(this);
                if (permissionsList.size() == 0) {
                    Toast.makeText(this, "正在加载资源", Toast.LENGTH_SHORT).show();
                    new CopyFileTask(intent, this).execute();
                    return;
                }
                if (!mIsDenyAllPermission) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            requestCode);
                }
            } else {
                Toast.makeText(this, "正在加载资源", Toast.LENGTH_SHORT).show();
                new CopyFileTask(intent, this).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_ALL_PERMISSIONS) {
            mIsDenyAllPermission = false;
            for (int i = 0; i < permissions.length; i++) {
                if (i >= grantResults.length || grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    mIsDenyAllPermission = true;
                    break;
                }
            }
            Toast.makeText(this, "正在加载资源", Toast.LENGTH_SHORT).show();
            new CopyFileTask(intent, this).execute();
            if (mIsDenyAllPermission) {
                finish();
            }
        }

    }

    private static List<String> getRequestPermissions(Activity activity) {
        List<String> permissionsList = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : ALL_PERMISSIONS) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
        }
        return permissionsList;
    }
}
