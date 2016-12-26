package cn.a1949science.www.bookshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class User_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__info);

        ImageView before = (ImageView) findViewById(R.id.before);
        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void goToDetailInfo(View view) {
        Intent it = new Intent(this,Detail_Info.class);
        startActivity(it);
    }
    public void goToEditPassword(View view) {
        Intent it = new Intent(this,EditPassword.class);
        startActivity(it);
    }
}
