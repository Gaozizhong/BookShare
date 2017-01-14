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
import java.net.URL;
import java.net.URLConnection;

import cn.a1949science.www.bookshare.bean.Book_Info;

/**
 * Created by 高子忠 on 2017/1/7.
 */

class BookAdapter extends BaseAdapter {
    private Context context;
    private Book_Info list;

    BookAdapter(Context context, Book_Info list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return list;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        //新增一个内部类ViewHolder,用于对控件的实例进行缓存
        final BookAdapter.ViewHolder viewHolder;
        if (view == null) {
            String bookName,introduce,writername;
            int time,OwnerNum;
            bookName = list.getBookName();
            introduce = list.getBookDescribe();
            writername = list.getBookWriter();
            time = list.getkeepTime();
            OwnerNum = list.getOwnerNum();
            LayoutInflater inflater = LayoutInflater.from(context);
            //实例化一个布局文件
            view = inflater.inflate(R.layout.bookinfo, null);
            viewHolder = new BookAdapter.ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            viewHolder.introduce = (TextView)view.findViewById(R.id.introduce);
            viewHolder.bookName = (TextView) view.findViewById(R.id.book);
            viewHolder.writername = (TextView) view.findViewById(R.id.writer);
            viewHolder.time = (TextView)view.findViewById(R.id.time);
            viewHolder.bookowner = (TextView)view.findViewById(R.id.bookowner);
            view.setTag(viewHolder);//将viewHolder存储在view中
            //不能直接在主线程中进行从网络端获取图片，而需要单独开一个子线程完成从网络端获取图片
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //根据表中图片的url地址来得到图片（Bitmap类型）
                    final Bitmap bitmap = getPicture(list.getBookPicture().getFileUrl());
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
            viewHolder.introduce.setText(introduce);
            viewHolder.bookName.setText(bookName);
            viewHolder.writername.setText(writername);
            viewHolder.time.setText(time);
            viewHolder.bookowner.setText(OwnerNum);
        } else {
            viewHolder = (BookAdapter.ViewHolder) view.getTag();
        }
        return view;
    }
    private class ViewHolder{
        ImageView image;
        TextView introduce;
        TextView bookName;
        TextView writername;
        TextView time;
        TextView bookowner;
    }

    private Bitmap getPicture(String path) {
        Bitmap bm = null;
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bm = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
}
