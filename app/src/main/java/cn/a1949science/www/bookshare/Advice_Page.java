package cn.a1949science.www.bookshare;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Advice_Page extends AppCompatActivity {

    SQLiteDatabase db;
    ImageView before;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice__page);
        findView();
        onClick();

       /* db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/advice.db3", null);
        Button btn = (Button) findViewById(R.id.advBtn);
        assert btn != null;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String advcontent = ((EditText) findViewById(R.id.editText)).getText().toString();
                try
                {
                    insertData(db,advcontent);

                }
            }
        });*/
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
