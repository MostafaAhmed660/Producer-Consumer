package com.example.demo.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Queue {
    private List<Machine>machines=new ArrayList<Machine>(0);
    private static int IDgenerator=0;
    private int ID=0;

    public int getID() {
        return ID;
    }

    public Queue() {
        this.ID=IDgenerator++;
    }

    public void addMachine(Machine m){
        this.machines.add(m);
        m.RunMachine();
    }
    public void removeMachine(){
        if(!machines.isEmpty()){
            machines.remove(0);
        }
    }
    public void addProductToRandomMachine(Product p){
        Random random=new Random();
        if(!machines.isEmpty()){
        int i=random.nextInt(machines.size());
        this.machines.get(i).addProductToMachine(p);}
    }
    public void SetInetialProducts(int numberOfProducts){
        Color c=new Color(229,229,229);
        List<Color> newColors = new ArrayList<Color>();
        for(int i=0;i<numberOfProducts;i++) {
            Random rand = new Random();
            float r = (float) (rand.nextFloat() / 2f + 0.5);
            float g = (float) (rand.nextFloat() / 2f + 0.5);
            float b = (float) (rand.nextFloat() / 2f + 0.5);
            Color randomColor = new Color(r, g, b);
            if(!newColors.contains(randomColor) && !(randomColor.equals(c))){
                newColors.add(randomColor);
            }
            else{i=i-1;}
        }
        for(int i=0;i<numberOfProducts;i++){
            Product p=new Product();
            p.setColor(newColors.get(i));
            addProductToRandomMachine(p);
        }
    }
    public String removeMachineFromQueue(int id){
        for(int i=0;i<this.machines.size();i++){
            if(machines.get(i).getID()==id){
                machines.remove(i);
                return "Machine Deleted Successfully from that machine";
            }
        }
        return "Cant Find That machine on that Queue";
    }
    public void RunMachines(){
        for(int i=0;i<machines.size();i++){
            machines.get(i).RunMachine();
        }
    }



}
