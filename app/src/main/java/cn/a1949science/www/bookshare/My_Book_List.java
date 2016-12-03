package cn.a1949science.www.bookshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_Book_List extends AppCompatActivity {

    ImageView before;
    ListView list;
    private String[] books = new String[]
            {"大唐李白·少年游", "大唐李白·凤凰台","大唐李白·将进酒"};
    private String[] writers = new String[]
            {"[中]张大春","[中]张大春","[中]张大春"};
    private int[] imageIds = new int[]
            {R.drawable.datanglibai1, R.drawable.datanglibai2,R.drawable.datanglibai3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__book__list);

        findView();
        onClick();

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i=0;i<books.length;i++){
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("imageName",imageIds[i]);
            listItem.put("bookName", books[i]);
            listItem.put("writerName", writers[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.booklist_item,
                new String[] {"imageName","bookName","writerName"},
                new int[] {R.id.image,R.id.book,R.id.writer});

        //为ListView设置Adapter
        assert list != null;
        list.setAdapter(simpleAdapter);
    }

    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        list = (ListView) findViewById(R.id.myBookList);
    }

    //点击事件
    private void onClick(){
        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
