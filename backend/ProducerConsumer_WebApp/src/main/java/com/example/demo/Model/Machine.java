package com.example.demo.Model;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Machine {
    Producer p=null;
    Consumer c=null;
    private static int IDgenerator=0;
    private int ID;

    public int getID() {
        return ID;
    }

    public Machine(){
        this.p=new Producer();
        this.c=new Consumer();
        this.c.products=this.p.products;
        setID(IDgenerator++);
    }
    public void addProductToMachine(Product p){
        this.p.addproduct(p);
    }
    public void addQueueToMachine(Queue q){
        this.c.addQueue(q);
    }
    public boolean testEND(){
        if(this.p.WaitingProducts.isEmpty()&&this.p.products.isEmpty()){
            return true;
        }
        else return false;
    }
    public void RunMachine(){
        Thread t1=new Thread(this.p);
        Thread t2=new Thread(this.c);
        t1.start();
        t2.start();
    }
    public String deleteQueueFromMachine(int id){
        return this.c.removeQueue(id);
    }
    public void setMachineTime(int i){
        this.c.setTimeConsume(i);
    }
    public void setID(int id){
        this.p.setID(id);
        this.c.setID(id);
        this.ID=id;
    }
    public Color getMachineColor(){
        return this.c.getCurrentColor();
    }

    public ArrayList<Color> WaitingProducts(){
         ArrayList<Color>Waiting=new ArrayList<Color>();
         for (int i=0;i<this.c.products.size();i++){
            Waiting.add(this.c.products.get(i).getColor());
         }
            for (int i=0;i<this.p.WaitingProducts.size();i++){
                Waiting.add(this.p.WaitingProducts.get(i).getColor());
            }
        return Waiting;
    }

    public Memento saveColorToMemento(){
        return new Memento(getMachineColor(), WaitingProducts());
    }
}
