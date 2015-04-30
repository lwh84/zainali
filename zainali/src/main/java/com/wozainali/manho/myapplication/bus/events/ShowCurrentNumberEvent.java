package com.wozainali.manho.myapplication.bus.events;

public class ShowCurrentNumberEvent {

    final public int number;

    public ShowCurrentNumberEvent(int number) {
        this.number = number;
    }
}
