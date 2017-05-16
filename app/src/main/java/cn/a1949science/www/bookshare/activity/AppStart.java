package cn.a1949science.www.bookshare.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class AppStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动页面
        final View view = View.inflate(this, R.layout.activity_app_start, null);
        setContentView(view);
        /*沉浸式标题栏*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");

        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        //设置持续时间
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            //动画页面结束后要干嘛
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}

        });
    }

    private void redirectTo(){
            //BmobUser bmobUser = BmobUser.getCurrentUser();
            final _User bmobUser = BmobUser.getCurrentUser(_User.class);
            if (bmobUser!=null&&bmobUser.getCertificationOk()) {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, Login_Page.class);
                startActivity(intent);
                finish();
            }
    }


}
