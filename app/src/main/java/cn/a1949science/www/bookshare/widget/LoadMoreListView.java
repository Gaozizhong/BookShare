package cn.a1949science.www.bookshare.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.a1949science.www.bookshare.R;

/**
 * Created by 高子忠 on 2017/4/28.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener{
    private Context mContent;
    private View mFootView;
    private int mTotalItemCount;//item总数
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoading=false;//是否正在加载

    public LoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContent = context;
        mFootView = LayoutInflater.from(context).inflate(R.layout.foot_view, null);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        // 滑到底部后，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        int lastVisibleIndex = absListView.getLastVisiblePosition();
        //停止滚动或滑动到最后一行
        if (!mIsLoading && i == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == mTotalItemCount - 1) {
            //滑动到最后一项
            mIsLoading = true;
            addFooterView(mFootView);
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onloadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        mTotalItemCount = i1;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener{
        void onloadMore();
    }

    public void setLoadCompleted() {
        mIsLoading = false;
        removeFooterView(mFootView);
    }

}
