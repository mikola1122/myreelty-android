package com.helio.myreelty.common.pagination;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by Fedir on 30.03.2016.
 */
public class PaginationTool<T> {

    // for first start of items loading then on RecyclerView there are not items and no scrolling
    private static final int EMPTY_LIST_ITEMS_COUNT = 0;
    // default limit for requests
    private static final int DEFAULT_LIMIT = 50;
    // default max attempts to retry loading request
    private static final int MAX_ATTEMPTS_TO_RETRY_LOADING = 3;

    private RecyclerView recyclerView;
    private PagingListener<T> pagingListener;
    private int limit;
    private int emptyListCount;
    private int retryCount;
    private boolean emptyListCountPlusToOffset;
    private PaginationRecyclerAdapter adapter;


    private PaginationTool() {
    }

    public Observable<T> getPagingObservable() {
        int startNumberOfRetryAttempt = 0;
        return getScrollObservable(recyclerView, limit, emptyListCount)
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .observeOn(Schedulers.from(BackgroundExecutor.getSafeBackgroundExecutor()))
                .switchMap(offset -> getPagingObservable(pagingListener, pagingListener.onNextPage(offset), startNumberOfRetryAttempt, offset, retryCount));
    }

    private Observable<Integer> getScrollObservable(RecyclerView recyclerView, int limit, int emptyListCount) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                final RecyclerView.OnScrollListener sl = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (!subscriber.isUnsubscribed()) {
                            int position = getLastVisibleItemPosition(recyclerView);
                            int updatePosition = recyclerView.getAdapter().getItemCount() - 1 - (limit / 2);
                            if (position >= updatePosition) {
                                downloadNextPage(subscriber);
                            }
                        }
                    }
                };
                recyclerView.addOnScrollListener(sl);
                subscriber.add(Subscriptions.create(() -> recyclerView.removeOnScrollListener(sl)));
                if (recyclerView.getAdapter().getItemCount() == emptyListCount) {
                    PaginationTool.this.downloadNextPage(subscriber);
                }
            }
        });
    }

    private void downloadNextPage(Subscriber<? super Integer> subscriber) {
        int currentPage = adapter.getCurrentPage();
        if (adapter.getMaxPage() != adapter.getCurrentPage()) {
            adapter.setCurrentPage(currentPage + 1);
            subscriber.onNext(currentPage + 1);
        } else {
            subscriber.onCompleted();
        }
    }

    private int getLastVisibleItemPosition(RecyclerView recyclerView) {
        Class recyclerViewLMClass = recyclerView.getLayoutManager().getClass();
        if (recyclerViewLMClass == LinearLayoutManager.class || LinearLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            return linearLayoutManager.findLastVisibleItemPosition();
        } else if (recyclerViewLMClass == StaggeredGridLayoutManager.class || StaggeredGridLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            int[] into = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            List<Integer> intoList = new ArrayList<>();
            for (int i : into) {
                intoList.add(i);
            }
            return Collections.max(intoList);
        }
        throw new PagingException("Unknown LayoutManager class: " + recyclerViewLMClass.toString());
    }

    private Observable getPagingObservable(PagingListener<T> listener, Observable<T> observable, int numberOfAttemptToRetry, int offset, int retryCount) {
        return observable.onErrorResumeNext(throwable -> {
            // retry to load new data portion if error occurred
            if (numberOfAttemptToRetry < retryCount) {
                int attemptToRetryInc = numberOfAttemptToRetry + 1;
                return getPagingObservable(listener, listener.onNextPage(offset), attemptToRetryInc, offset, retryCount);
            } else {
                return Observable.empty();
            }
        });
    }

    public static <T> Builder<T> buildPagingObservable(RecyclerView recyclerView, PaginationRecyclerAdapter adapter, PagingListener<T> pagingListener) {
        return new Builder<>(recyclerView, adapter, pagingListener);
    }

    public static class Builder<T> {

        private RecyclerView recyclerView;
        private PagingListener<T> pagingListener;
        private int limit = DEFAULT_LIMIT;
        private int emptyListCount = EMPTY_LIST_ITEMS_COUNT;
        private int retryCount = MAX_ATTEMPTS_TO_RETRY_LOADING;
        private boolean emptyListCountPlusToOffset = false;
        private PaginationRecyclerAdapter adapter;

        private Builder(RecyclerView recyclerView, PaginationRecyclerAdapter adapter, PagingListener<T> pagingListener) {
            if (recyclerView == null) {
                throw new PagingException("null recyclerView");
            }
            if (recyclerView.getAdapter() == null) {
                throw new PagingException("null recyclerView adapter");
            }
            if (pagingListener == null) {
                throw new PagingException("null pagingListener");
            }
            this.recyclerView = recyclerView;
            this.pagingListener = pagingListener;
            this.adapter = adapter;
        }

        public Builder<T> setLimit(int limit) {
            if (limit <= 0) {
                throw new PagingException("limit must be greater then 0");
            }
            this.limit = limit;
            return this;
        }

        public Builder<T> setEmptyListCount(int emptyListCount) {
            if (emptyListCount < 0) {
                throw new PagingException("emptyListCount must be not less then 0");
            }
            this.emptyListCount = emptyListCount;
            return this;
        }

        public Builder<T> setRetryCount(int retryCount) {
            if (retryCount < 0) {
                throw new PagingException("retryCount must be not less then 0");
            }
            this.retryCount = retryCount;
            return this;
        }

        public Builder<T> setEmptyListCountPlusToOffset(boolean emptyListCountPlusToOffset) {
            this.emptyListCountPlusToOffset = emptyListCountPlusToOffset;
            return this;
        }

        public PaginationTool<T> build() {
            PaginationTool<T> paginationTool = new PaginationTool<>();
            paginationTool.recyclerView = this.recyclerView;
            paginationTool.pagingListener = pagingListener;
            paginationTool.limit = limit;
            paginationTool.emptyListCount = emptyListCount;
            paginationTool.retryCount = retryCount;
            paginationTool.emptyListCountPlusToOffset = emptyListCountPlusToOffset;
            paginationTool.adapter = adapter;
            return paginationTool;
        }

    }
}

