package com.wozainali.manho.myapplication.bus;

import com.squareup.otto.Bus;

public class ZaiNaliBus {

    private static final Bus bus = new Bus();

    public static Bus getBus() {
        return bus;
    }

}
