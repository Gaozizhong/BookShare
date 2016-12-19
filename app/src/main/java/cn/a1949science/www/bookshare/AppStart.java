package cn.a1949science.www.bookshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AppStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_app_start);*/
        final View view = View.inflate(this, R.layout.activity_app_start, null);
        setContentView(view);

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
            Intent intent = new Intent(this, Login_Page.class);
            startActivity(intent);
            finish();
        }
}
