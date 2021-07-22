package com.example.demo.Model;

import java.util.ArrayList;
import java.util.List;

public class Producer implements Runnable{
    List<Product>WaitingProducts=new ArrayList<Product>();
    private int ID;
    List<Product>products=new ArrayList<Product>() ;
    final int LIMIT=10;
    public void addproduct(Product p){
        this.WaitingProducts.add(p);
    }
    public void setID(int id){this.ID=id;}
    public void readProducts() throws InterruptedException {
        synchronized (products) {
            while (products.size() ==LIMIT){
                System.out.println("From Producer: "+this.ID+" : Products have pilled up...wait for machine");
                products.wait();
            }
        }
        synchronized (products){
            if(!WaitingProducts.isEmpty()){
                try {
                    Product p=WaitingProducts.remove(0);
                    if(p!=null){
                        products.add(p);
                        System.out.println("From Producer: "+this.ID+" : New product produced"+p.getColor());}
                    // Thread.sleep(10);
                    products.notify();
                } catch (Exception e) {

                }

            }
        }
    }

    @Override
    public void run() {
        while (true){
        try {
            readProducts();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    }
}
