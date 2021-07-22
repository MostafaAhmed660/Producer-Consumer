package com.example.demo.Model;

public class Main {

    public static void main(String[] args) {
    Queue q=new Queue();
    Queue q2=new Queue();
    Machine m1=new Machine();
    m1.setMachineTime(10);
    m1.addQueueToMachine(q2);
    Machine m2=new Machine();
    m2.setMachineTime(10);
    q2.addMachine(m2);
    q.addMachine(m1);


    q.SetInetialProducts(100);


    }
}
