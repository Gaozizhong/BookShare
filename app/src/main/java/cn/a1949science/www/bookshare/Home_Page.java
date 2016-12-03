package cn.a1949science.www.bookshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.a1949science.www.bookshare.R.layout.activity_home__page;


public class Home_Page extends AppCompatActivity {
    Context mContext = Home_Page.this;
    FrameLayout next_layout;
    Button shareBtn,returnBtn;
    Animation animation = null;
    ImageButton shortCut;
    ImageView searchImg;
    ImageView menuImg;
    ListView list;
    boolean clicked  = false;
    private String[] books = new String[]
            {"偷影子的人", "岛上书店", "摆渡人", "大唐李白·少年游",
                    "大唐李白·凤凰台", "大唐李白·将进酒"};
    private String[] writers = new String[]
            {"[法]马克·李维", "[美]加布瑞埃拉·泽文", "[英]克莱尔·麦克福尔",
                    "[中]张大春", "[中]张大春", "[中]张大春"};
    private int[] imageIds = new int[]
            {R.drawable.touyingzideren, R.drawable.daoshangshudian, R.drawable.baiduren,
                    R.drawable.datanglibai1, R.drawable.datanglibai2, R.drawable.datanglibai3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home__page);

        findView();
        onClick();


        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < books.length; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("imageName", imageIds[i]);
            listItem.put("bookName", books[i]);
            listItem.put("writerName", writers[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.booklist_item,
                new String[]{"imageName", "bookName", "writerName"},
                new int[]{R.id.image, R.id.book, R.id.writer});

        //为ListView设置Adapter
        assert list != null;
        list.setAdapter(simpleAdapter);
    }
    //查找地址
    private void findView() {
        next_layout = (FrameLayout) findViewById(R.id.home__page);
        shortCut = (ImageButton) findViewById(R.id.shortcut);
        searchImg = (ImageView) findViewById(R.id.searchImg);
        menuImg = (ImageView) findViewById(R.id.menuImg);
        list = (ListView) findViewById(R.id.booklist);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        returnBtn = (Button) findViewById(R.id.returnBtn);
    }

    //点击事件
    private void onClick() {

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出信息框
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.sharing, (ViewGroup) findViewById(R.id.sharing_Dialog));

                new AlertDialog.Builder(mContext)
                        .setTitle("图书共享")
                        .setPositiveButton("确认共享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setView(layout)
                        .show();
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出信息框
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.raturning_yes, (ViewGroup) findViewById(R.id.returning_Yes_Dialog));

                new AlertDialog.Builder(mContext)
                        .setPositiveButton("确认还书", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setView(layout)
                        .setTitle("图书归还")
                        .show();

            }
        });

        assert shortCut != null;
        shortCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
                returnBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
                if (!clicked ) {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_add);
                } else {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_return);
                }
                animation.setFillAfter(true);
                shortCut.startAnimation(animation);
                clicked = !clicked;

                next_layout.setClickable(!clicked);
                next_layout.setBackgroundColor(clicked ? Color.parseColor("#ddffffff") : Color.parseColor("#ebecf0"));

            }
        });

        assert searchImg != null;
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "过两天就能用了！！！", Toast.LENGTH_SHORT).show();
            }
        });

        assert menuImg != null;
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Menu_page.class);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle data = new Bundle();
                /**
                 *利用Intent传递数据
                 */
                data.putInt("imageid", imageIds[i]);
                data.putString("bookname", books[i]);
                data.putString("writername", writers[i]);
                Intent intent = new Intent(mContext, Book_Info.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }


}
