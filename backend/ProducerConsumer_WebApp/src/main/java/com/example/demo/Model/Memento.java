package com.example.demo.Model;

import java.awt.*;
import java.util.ArrayList;

public class Memento {
    private Color c = null;
    private ArrayList<Color> waiting;

    public Memento(Color c, ArrayList<Color> w){
        this.c = c;
        this.waiting= w;
    }

    public Color getC() {
        return c;
    }

    public ArrayList<Color> getWaiting() {
        return waiting;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public void setWaiting(ArrayList<Color> waiting) {
        this.waiting = waiting;
    }
}
