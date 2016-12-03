package cn.a1949science.www.bookshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book_Info extends AppCompatActivity {

    ImageView before;
    ListView list;
    ImageButton likeBtn;
    private String[] introduce = new String[]
            {"    一个老是受班上同学欺负的瘦弱小男孩，因为拥有一种特殊能力而强大：他能偷别人的影子。"};
    private String[] time=new String[]
            {"7天"};
    private String[] bookowner=new String[]
            {" 高子中"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);

        findView();
        onClick();

        Bundle bundle = this.getIntent().getExtras();
        int imageid = bundle.getInt("imageid");
        String bookname = bundle.getString("bookname");
        String writername = bundle.getString("writername");

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i=0;i<time.length;i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("imageid", String.valueOf(imageid));
            listItem.put("introduce", introduce[i]);
            listItem.put("bookname", bookname);
            listItem.put("writername", writername);
            listItem.put("time", time[i]);
            listItem.put("bookowner", bookowner[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.bookinfo,
                new String[] {"imageid","introduce","bookname","writername","time","bookowner"},
                new int[] {R.id.image,R.id.introduce,R.id.bookName,R.id.writename,R.id.time,R.id.bookOwner});

        //为ListView设置Adapter
        list.setAdapter(simpleAdapter);


    }
    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        list = (ListView) findViewById(R.id.bookInfo);
        likeBtn = (ImageButton)findViewById(R.id.likeBtn);
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
