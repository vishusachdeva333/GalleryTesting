package com.enanek.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ankur.jha on 25/03/18.
 */

public abstract class RecyclerPaginationListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 3; // The minimum amount of items to have below your current scroll position before loading more.

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public RecyclerPaginationListener(LinearLayoutManager linearLayoutManager, int threshold) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.visibleThreshold = threshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        onFirstItemPosition(firstVisibleItem);
        onLastItemPosition(lastVisibleItem);

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;
            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
    public abstract void onFirstItemPosition(int firstVisibleItem);
    public abstract void onLastItemPosition(int lastVisibleItem);

    public void resetPaginatingRecycler() {
        previousTotal = 0;
        loading = true;
        current_page = 0;
    }
}