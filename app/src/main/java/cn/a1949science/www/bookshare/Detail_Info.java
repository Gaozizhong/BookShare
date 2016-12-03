package cn.a1949science.www.bookshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Detail_Info extends AppCompatActivity {

    ImageView before;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__info);

        findView();
        onClick();

    }
    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
    }

    //点击事件
    protected void onClick(){
        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
