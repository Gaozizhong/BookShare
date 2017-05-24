package cn.a1949science.www.bookshare.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.a1949science.www.bookshare.R;

public class ScanISBN extends AppCompatActivity implements View.OnClickListener{
    Context mContext = ScanISBN.this;
    Toolbar toolbar;
    public static boolean isOpen = false;
    ImageView light;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_isbn);
        //在Activity中执行Fragment的初始化操作
        //执行扫面Fragment的初始化操作
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_scan);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        light = (ImageView) findViewById(R.id.light);
        light.setOnClickListener(this);
    }


    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScanISBN.this.setResult(RESULT_OK, resultIntent);
            ScanISBN.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScanISBN.this.setResult(RESULT_OK, resultIntent);
            ScanISBN.this.finish();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.light:
                if (!isOpen) {
                    light.setImageResource(R.mipmap.open_light);
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    light.setImageResource(R.mipmap.close_light);
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;

        }
    }
}
