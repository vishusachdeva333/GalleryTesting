package com.enanek.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ankur.jha on 21/02/18.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    /**
     * The total number of items in the dataset after the last load
     */
    private int mPreviousTotal = 0;
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mLoading = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

        onFirstItemPosition(firstVisibleItem);
        onLastItemPosition(lastVisibleItem);
        //CricLog.e("EndlessRecycler", "visibleItemCount = " + visibleItemCount+" , totalItemCount = "+totalItemCount+" , firstVisibleItem = "+firstVisibleItem);
        //CricLog.e("EndlessRecycler","mLoading = "+mLoading);
        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }
        int visibleThreshold = 20;
        if (!mLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            //CricLog.v("EndlessRecycler","onLoadMore calling");
            onLoadMore();

            mLoading = true;
        }
    }

    public abstract void onLoadMore();

    public abstract void onFirstItemPosition(int firstVisibleItem);

    public abstract void onLastItemPosition(int lastVisibleItem);

    public void resetEndlessRecycler() {
        mPreviousTotal = 0;
        mLoading = true;
    }
}
