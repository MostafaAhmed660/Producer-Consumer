package com.example.demo.Model;

import java.awt.*;
import java.util.ArrayList;

public class CareTaker {
    ArrayList<ArrayList<Memento>> machine = new ArrayList<>();

    public CareTaker(){
    }


    public void addShot(ArrayList<Memento> list){
        machine.add(list);
    }

    public ArrayList<ArrayList<Memento>> getAllShots(){
        return machine;
    }
}
