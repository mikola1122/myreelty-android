package com.helio.myreelty.common.pagination;

import rx.Observable;

/**
 * Created by Fedir on 30.03.2016.
 */
public interface PagingListener<T> {
    Observable<T> onNextPage(int offset);
}