package com.wozainali.manho.myapplication.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class ZaiNaliBus {

    // I'm not sure if this is ok...threadEnforcer.ANY
    private static final Bus bus = new Bus(ThreadEnforcer.ANY);

    public static Bus getBus() {
        return bus;
    }

}
