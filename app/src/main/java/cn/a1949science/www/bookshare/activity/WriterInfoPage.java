package cn.a1949science.www.bookshare.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.WriterInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WriterInfoPage extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    TextView writerName,introduce;
    String writer;
    View introduce_layout, expandView;
    boolean isExpand;
    WebView writer_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer_info_page);
        initView();
        detail();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        writerName = (TextView) findViewById(R.id.writername);
        introduce = (TextView) findViewById(R.id.introduce);
        introduce_layout = findViewById(R.id.introduce_layout);
        introduce_layout.setOnClickListener(this);
        expandView = findViewById(R.id.expand_view);
        writer_info = (WebView) findViewById(R.id.writer_info);
    }

    private void detail() {
        Bundle bundle = this.getIntent().getExtras();
        writer = bundle.getString("writername");
        //显示作者信息
        writerName.setText(writer);
        BmobQuery<WriterInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("writerName",writer );
        query.findObjects(new FindListener<WriterInfo>() {
            @Override
            public void done(List<WriterInfo> list, BmobException e) {
                if (e == null) {
                    introduce.setText(list.get(0).getIntroduction());
                    //descriptionView设置默认显示高度
                    introduce.setHeight(introduce.getLineHeight() * 3);
                    //根据高度来判断是否需要再点击展开
                    isExpand = introduce.getLineCount() <= 3;
                    introduce_layout.post(new Runnable() {
                        @Override
                        public void run() {
                            expandView.setVisibility(introduce.getLineCount() > 3 ? View.VISIBLE : View.GONE);
                        }
                    });
                } else {
                    //Toast.makeText(WriterInfoPage.this, "查询失败。"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        writer_info.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        writer_info.loadUrl("http://baike.baidu.com/item/"+writer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.introduce_layout:
                expand();
                break;
        }
    }

    //图书介绍的展开
    private void expand() {
        isExpand = !isExpand;
        introduce.clearAnimation();//清楚动画效果
        final int deltaValue;//默认高度，即前边由maxLine确定的高度
        final int startValue = introduce.getHeight();//起始高度
        int durationMillis = 350;//动画持续时间
        if (isExpand) {
            deltaValue = introduce.getLineHeight() * introduce.getLineCount() - startValue;
            RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(durationMillis);
            animation.setFillAfter(true);
            expandView.startAnimation(animation);
        } else {
            deltaValue = introduce.getLineHeight() * 3 - startValue;
            RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(durationMillis);
            animation.setFillAfter(true);
            expandView.startAnimation(animation);
        }
        Animation animation = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) { //根据ImageView旋转动画的百分比来显示textview高度，达到动画效果
                introduce.setHeight((int) (startValue + deltaValue * interpolatedTime));
            }
        };
        animation.setDuration(durationMillis);
        introduce.startAnimation(animation);
    }

}
