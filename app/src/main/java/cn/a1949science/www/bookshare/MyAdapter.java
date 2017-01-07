package cn.a1949science.www.bookshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 高子忠 on 2017/1/2.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Book_Info> list;

    public MyAdapter(Context context, List<Book_Info> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        //新增一个内部类ViewHolder,用于对控件的实例进行缓存
        final ViewHolder viewHolder;
        if (view == null) {
            String bookName;
            String bookWriter;
            bookName = list.get(i).getBookName();
            bookWriter = list.get(i).getBookWriter();
            LayoutInflater inflater = LayoutInflater.from(context);
            //实例化一个布局文件
            view = inflater.inflate(R.layout.booklist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            viewHolder.book = (TextView) view.findViewById(R.id.book);
            viewHolder.writer = (TextView) view.findViewById(R.id.writer);
            view.setTag(viewHolder);//将viewHolder存储在view中
            //不能直接在主线程中进行从网络端获取图片，而需要单独开一个子线程完成从网络端获取图片
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //根据表中图片的url地址来得到图片（Bitmap类型）
                    final Bitmap bitmap = getPicture(list.get(i).getBookPicture().getFileUrl());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    viewHolder.image.post(new Runnable() {
                        @Override
                        public void run() {
                            //将图片放到视图中
                            viewHolder.image.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
            viewHolder.book.setText(bookName);
            viewHolder.writer.setText(bookWriter);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }
    class ViewHolder{
        ImageView image;
        TextView book;
        TextView writer;
    }

    public Bitmap getPicture(String path) {
        Bitmap bm = null;
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bm = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
}

