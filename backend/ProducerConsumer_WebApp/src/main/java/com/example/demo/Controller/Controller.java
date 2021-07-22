package com.example.demo.Controller;

import com.example.demo.Model.Memento;
import com.example.demo.Services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("api")
@RestController
public class Controller {
    @Autowired
    private Services services;
    public Services getServices() {
        return services;
    }
    @GetMapping("/addQueue")
    public int addQueue(){
        return services.addQueue();
    }
    @GetMapping("/removeQueue")
    public String removeQueue(@RequestParam(value = "id")int id){
        return services.removeQueue(id);
    }

    @GetMapping("/addMachine")
    public int addMachine(){
        return services.addMachine();
    }

    @GetMapping("/removeMachine")
    public String removeMachine(@RequestParam(value = "id")int id){
        return services.removeMachine(id);
    }

    @GetMapping("/addMachineWithTime")
    public int addMachineWithTime(@RequestParam(value = "Time")int Time){
        return services.addMachineWithTime(Time);
    }

    @GetMapping("/ModifyMachineTime")
    public String ModifyMachineTime(@RequestParam(value = "Time")int Time,@RequestParam(value = "id")int id){
        return services.modifyMachineTime(id,Time);
    }

    @GetMapping("/addMachineToQueue")
    public String addMachineToQueue(@RequestParam(value = "MachineID")int MachineID,@RequestParam(value = "QueueID")int QueueID){
        return services.addMachineToQueue(QueueID,MachineID);
    }

    @GetMapping("/addQueueToMachine")
    public String addQueueToMachine(@RequestParam(value = "MachineID")int MachineID,@RequestParam(value = "QueueID")int QueueID){
        return services.addQueueToMachine(QueueID,MachineID);
    }

    @GetMapping("/RemoveQueueBond")
    public String RemoveQueueBond(@RequestParam(value = "MachineID")int MachineID,@RequestParam(value = "QueueID")int QueueID){
        return services.deleteQueueBond(QueueID,MachineID);
    }

    @GetMapping("/RemoveMachineBond")
        public String RemoveMachineBond(@RequestParam(value = "MachineID")int MachineID,@RequestParam(value = "QueueID")int QueueID){
        return services.deleteMachineBond(QueueID,MachineID);
    }

    @GetMapping("/GetMachineColor")
    public Color GetMachineColor(@RequestParam(value = "id")int id){
        return services.getMachineColor(id);
    }
    @GetMapping("/SetIntialProducts")
    public void SetIntialProducts (@RequestParam(value = "ProductsNumber")int ProductsNumber,@RequestParam(value = "QueueID")int QueueID){
         services.SetIntialProducts(QueueID,ProductsNumber);
    }
    @GetMapping("/GetMachinesColors")
    public ArrayList<Color>GetMachinesColors(){
        return services.getMachinesColors();
    }
    @GetMapping("/GetMachinesIDs")
    public ArrayList<Integer>GetMachinesIDs(){
        return services.getMachinesIDs();
    }

    @GetMapping("/isEnd")
    public boolean isEnd(){
        return services.testEnd();
    }

    @GetMapping("/inti")
    public void inti (){
        services.inti();
    }

    @GetMapping("/Waiting")
    public ArrayList<ArrayList<Color>> Waiting (){
        return services.GetWitingProducts();
    }

    @GetMapping("/fullReplay")
    public ArrayList<ArrayList<Memento>> getFullReplay(){
        return services.getfullReplay();
    }

    @GetMapping("/clearReplay")
    public void clearReplay(){
        services.clearReplay();
    }
}
