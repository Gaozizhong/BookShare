package cn.a1949science.www.bookshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.activity.Book_detail;
import cn.a1949science.www.bookshare.bean.BookInfo;
import cn.a1949science.www.bookshare.bean.Book_Info;

/**
 * Created by 高子忠 on 2017/4/22.
 */

public class myAdapterRecyclerView extends RecyclerView.Adapter<myAdapterRecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<BookInfo> list;
    private Integer[] bookNum;

    @Override
    public void onClick(View view) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View bookView;
        ImageView image;
        TextView book;
        TextView writer;
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            bookView = itemView;
            image = (ImageView) itemView.findViewById(R.id.image);
            book = (TextView) itemView.findViewById(R.id.book);
            writer = (TextView) itemView.findViewById(R.id.writer);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }

    public myAdapterRecyclerView(Context context,List<BookInfo> list,Integer[] bookNum) {
        this.context = context;
        this.list = list;
        this.bookNum = bookNum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booklist_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                BookInfo book_info = list.get(position);
                Bundle data = new Bundle();
                //利用Intent传递数据
                data.putInt("shareNum",bookNum[position]);
                data.putInt("booknum",book_info.getBookNum());
                data.putString("objectId",book_info.getObjectId());
                Intent intent = new Intent(context, Book_detail.class);
                intent.putExtras(data);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookInfo book_info = list.get(position);
        String bookName = book_info.getBookName();
        String bookWriter = book_info.getBookWriter();
        Glide.with(context)
                .load(book_info.getBookImage().getFileUrl())
                .thumbnail(0.5f)
                .override((int)(context.getResources().getDisplayMetrics().density*120+0.5f),(int)(context.getResources().getDisplayMetrics().density*120+0.5f))
                .into(holder.image);
        holder.book.setText(bookName);
        holder.writer.setText(bookWriter);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
