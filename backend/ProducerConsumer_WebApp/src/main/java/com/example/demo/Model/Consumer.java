package com.example.demo.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Consumer implements Runnable{
    private List<Queue>OutPut=new ArrayList<Queue>();
    private int TimeConsume=2000;
    private Color CurrentColor=new Color(229,229,229);
    private int ID;
    final int LIMIT=10;
    private int productNO=0;
    List<Product>products=null;




    public Color getCurrentColor() {
        return CurrentColor;
    }
    public int getTimeConsume() {
        return TimeConsume;
    }
    public void setTimeConsume(int timeConsume) {
        TimeConsume = timeConsume;
    }

    Random random=new Random();
    public Consumer(){
    }
    public void addQueue(Queue q){
        this.OutPut.add(q);
    }
    public String removeQueue(int id){
        for(int i=0;i<OutPut.size();i++){
            if(id==OutPut.get(i).getID()){
                OutPut.remove(i);
                return "Queue Deleted Successfully";
            }
        }
        return "Queue Not Found";
    }
    public void removeQueue(){
        if(!OutPut.isEmpty()){
        this.OutPut.remove(0);}
    }
    public void setProducts(List<Product>l){
        this.products=l;
    }
    public void setID(int id){
        this.ID=id;
    }
    public void MakeProducts() throws InterruptedException {

        synchronized (products) {
            while (products.isEmpty()){
                System.out.println("From Consumer: "+this.ID+": No products to Make");
                products.wait();
            }
        }
        synchronized (products){
            Product p=products.get(0);
            if(p!=null){
                int c=p.getColor().getRGB();
                this.CurrentColor=new Color(c);
                System.out.println("From Consumer: "+this.ID+" "+this.CurrentColor+"  productNumber= "+productNO++);
            }
            Thread.sleep(TimeConsume);
            p=products.remove(0);
            if(!OutPut.isEmpty()){
                int i=random.nextInt(OutPut.size());
                OutPut.get(i).addProductToRandomMachine(p);
            }
            products.notify();
            this.CurrentColor=new Color(229,229,229);

        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                MakeProducts();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
