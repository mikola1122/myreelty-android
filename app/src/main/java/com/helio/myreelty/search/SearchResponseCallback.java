package com.helio.myreelty.search;

import com.helio.myreelty.network.models.NetworkHouseModel;

/**
 * Created by Fedir on 09.03.2016.
 */
public interface SearchResponseCallback {
    void onResponse(NetworkHouseModel houses);
}
