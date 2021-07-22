package com.example.demo.Services;

import com.example.demo.Model.CareTaker;
import com.example.demo.Model.Machine;
import com.example.demo.Model.Memento;
import com.example.demo.Model.Queue;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
@Service
public class Services {
    private ArrayList<Queue> queues = new ArrayList<>();
    private ArrayList<Machine> machines = new ArrayList<>();
    private CareTaker careTaker = new CareTaker();

    public Services() {
    }

    public int addQueue() {
        Queue q = new Queue();
        this.queues.add(q);
        return q.getID();
    }

    public String removeQueue(int ID){
        for(int i=0;i<this.queues.size();i++){
            if(this.queues.get(i).getID()==ID){
                this.queues.remove(i);
                for(int j=0;j<machines.size();j++){
                    machines.get(j).deleteQueueFromMachine(ID);
                }
                return "Successfully removed";
            }
        }
        return "This Queue Doesn't exist";
    }

    public int addMachine(){
        Machine m=new Machine();
        this.machines.add(m);
        return m.getID();
    }

    public int addMachineWithTime(int Time){
        Machine m=new Machine();
        if(Time>0){
            m.setMachineTime(Time);
        }
        this.machines.add(m);
        return m.getID();
    }

    public String modifyMachineTime(int ID,int Time){
        if(Time<0){return
                "Time must be bigger than or equal 2000(ms)";}
        for(int i=0;i<this.machines.size();i++){
            if(this.machines.get(i).getID()==ID){
                this.machines.get(i).setMachineTime(Time);
                return "Successfully Modified";
            }
        }
        return "Machine Not found";
    }

    public String removeMachine(int ID){
        for(int i=0;i<this.machines.size();i++){
            if(this.machines.get(i).getID()==ID){
                this.machines.remove(i);
                for(int j=0;j<queues.size();j++){
                    queues.get(j).removeMachineFromQueue(ID);
                }
                return "Successfully removed";
            }
        }
        return "This Machine Doesn't exist";
    }

    public String deleteQueueBond(int QueueID,int MachineID){
        for(int i=0;i<queues.size();i++){
            if(queues.get(i).getID()==QueueID){
                return queues.get(i).removeMachineFromQueue(MachineID);
            }
        }
        return "Can't find that Queue";
    }

    public String deleteMachineBond(int QueueID,int MachineID){
        for(int i=0;i<machines.size();i++){
            if(machines.get(i).getID()==MachineID){
                return  machines.get(i).deleteQueueFromMachine(QueueID);
            }
        }
        return "Can't find that Machine";
    }

    public String addQueueToMachine(int queueID,int machineID){
        for(int i=0;i<machines.size();i++){
            if(this.machines.get(i).getID()==machineID){
                for(int j=0;j<queues.size();j++) {
                    if(this.queues.get(j).getID()==queueID){
                        this.machines.get(i).addQueueToMachine(this.queues.get(j));
                        return "Successfully added Q number : "+this.queues.get(j).getID()+"to Machine Number "+this.machines.get(i).getID();
                    }
                }
                return "Can't find that Queue";
            }
        }
        return "Can't find that machine";
    }

    public String addMachineToQueue(int queueID,int machineID){
        for(int i=0;i<queues.size();i++){
            if(this.queues.get(i).getID()==queueID){
                for(int j=0;j<machines.size();j++)
                {
                    if(this.machines.get(j).getID()==machineID){
                        this.queues.get(i).addMachine(this.machines.get(j));
                        return "Successfully added Machine number : "+this.machines.get(j).getID()+"to Q Number "+this.queues.get(i).getID();
                    }
                }
                return "Can't find that machine";
            }
        }
        return "Can't find that queue";
    }

    public Color getMachineColor(int machineID){
        for(int j=0;j<machines.size();j++) {
            if(this.machines.get(j).getID()==machineID) {
                return this.machines.get(j).getMachineColor();
            }
        }
        return null;
    }

    public ArrayList<Integer>getMachinesIDs(){
        ArrayList<Integer>MachinesIDs=new ArrayList<Integer>();
        for(int i=0; i<machines.size() ;i++){
            MachinesIDs.add(machines.get(i).getID());
        }
        return MachinesIDs;
    }

    public ArrayList<Color>getMachinesColors(){
        ArrayList<Color>MachinesIDs=new ArrayList<Color>();
        for(int i=0;i<machines.size();i++){
            MachinesIDs.add(machines.get(i).getMachineColor());
        }
        saveMementoColors();
        return MachinesIDs;
    }



    public void saveMementoColors(){
        ArrayList<Memento> MachinesMementos =new ArrayList<Memento>();
        for(int i=0;i<machines.size();i++){
            MachinesMementos.add(machines.get(i).saveColorToMemento());
        }
        careTaker.addShot(MachinesMementos);
    }

    public ArrayList<ArrayList<Memento>> getfullReplay(){
        return careTaker.getAllShots();
    }


    public void SetIntialProducts(int id,int productsNumber) {
        for (int i = 0; i < queues.size(); i++) {
            if (queues.get(i).getID() == id) {
                queues.get(i).SetInetialProducts(productsNumber);
                break;
            }
        }
    }

    public void inti(){
        this.machines.clear();
        this.queues.clear();
        this.careTaker = new CareTaker();
    }

    public boolean testEnd(){
        for(int i=0;i<machines.size();i++){
            if(!machines.get(i).testEND())return false;
        }
        return true;
    }

    public ArrayList<ArrayList<Color>>GetWitingProducts(){
        ArrayList<ArrayList<Color>>Waiting=new ArrayList<ArrayList<Color>>();
        for(int i=0;i<machines.size();i++){
            Waiting.add(machines.get(i).WaitingProducts());
        }
        return Waiting;
    }

    public void clearReplay(){
        this.careTaker = new CareTaker();
    }

    public static void main(String[] args) {
        Services s=new Services();
        s.addQueue();
        s.addMachine();
        s.addQueue();
        s.addMachine();
        s.modifyMachineTime(0,1);
        s.modifyMachineTime(1,1);
        s.addMachineToQueue(0,0);
        s.addQueueToMachine(1,0);
        s.addMachineToQueue(1,1);
        s.modifyMachineTime(0,10);
        s.SetIntialProducts(0,1000);



    }


}
