package com.qf.liuyong.lotto_android.presenter;

import com.squareup.otto.Bus;

/**
 * Created by Administrator on 2017/1/29 0029.
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
    }
}
