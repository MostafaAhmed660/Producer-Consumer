<template>
  <v-app>
    <v-app-bar
      app
      color="balck"
      dark
      src="..\src\assets\1.jpg"
    >
      <v-spacer></v-spacer>
        <h2>Producer/Consumer Simulation Program</h2>
      <v-spacer></v-spacer>
    </v-app-bar>


    <v-main>
      <v-col cols="12">
        <v-row class="my-2 mx-1">
          <!-- run and stop button -->

          <v-menu
            v-model="runMenu"
            :close-on-content-click="false"
            :nudge-width="100"
            persistent
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn 
                color="green"
                :dark= !clicked
                v-bind="attrs" 
                v-on="on"
                :disabled= clicked
              >
                  RUN<v-icon right dark>mdi-arrow-left-drop-circle</v-icon>
              </v-btn>
            </template>

            <v-card>
              <v-list>
                <v-list-item>
                  <v-col>
                    <h2 style="padding : 5px">Intial Number Of Product</h2>
                        <v-text-field
                            class="my-4 mx-1"
                            v-model.number="noOfProducts"
                            type="number"
                            label="Enter intial num.of products"
                            min="1"
                            required
                        ></v-text-field>
                    </v-col>
                </v-list-item>
              </v-list>

              <v-divider></v-divider>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn text @click="runMenu = false">Cancel</v-btn>
                <v-btn color="green" dark @click="runstop">
                  RUN
                  <v-icon right dark>{{stateicon}}</v-icon>  
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-menu>

          <v-btn @click="replay" :disabled= !canRepaly||loading :loading = "loading" >Replay</v-btn>
          <v-btn @click="clear" :disabled= clicked>clear</v-btn>
          <v-btn  @click="Delete" :disabled= clicked>Delete</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="#602A1A" :dark= !clicked @click="createBond" :disabled= clicked>Create bond</v-btn>

          <!-- create machine -->
          <v-menu
            v-model="timeMenu"
            :close-on-content-click="false"
            :nudge-width="100"
            persistent
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="#FCDDCA" :disabled= clicked
                v-bind="attrs" 
                v-on="on"
              >Create machine</v-btn>
            </template>

            <v-card>
              <v-list>
                <v-list-item>
                  <v-col>
                      <h2>Machine Time in Seconds</h2>
                      <v-text-field
                          class="my-4 mx-1"
                          v-model.number="machineTime"
                          type="number"
                          label="Enter Machine time"
                          min="1"
                          required
                      ></v-text-field>
                  </v-col>
                </v-list-item>
              </v-list>
            

              <v-divider></v-divider>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn text @click="timeMenu = false">Cancel</v-btn>
                <v-btn color="#FCDDCA" @click="createMachine">Create</v-btn>
              </v-card-actions>
            </v-card>
          </v-menu>

          <v-btn color="#FCDDCA" @click="createQueue" :disabled= clicked>Create queue</v-btn>

        </v-row>
        
        

        <v-row class="my-2 mx-2">
            <v-card elevation="5">
                <canvas ref="my-canvas" width="1480" height="580"></canvas>
            </v-card>
        </v-row>




      </v-col>
    </v-main>
  </v-app>
</template>

<script>


import axios from 'axios'


export default {
  name: 'App',
  data: () => ({
    state : "RUN",
    stateicon : "mdi-arrow-left-drop-circle",
    clicked : false ,
    canvas: null,
    ctx: null,
    dragging: false,
    snapshot: "",
    object:{
        name:"",
        type:"",
        BorderColor:"",
        FillColor:"",
        dragStartLocation:{x:0,y:0},
        position:{x:0,y:0}
      },
    machines : [],
    queues : [],
    bonds : [],
    createdShapes : [],
    selectedMachineIndex: "",
    selectedQueueIndex: "",
    selectedShape: "",
    isCreateBond: false,
    firstShape: "",
    firstShapeType: "",
    isDelete: false,
    waitingProducts: [],
    noOfProducts:15,
    machineTime : 1 ,
    timeMenu : false ,
    runMenu : false ,
    canRepaly : false ,
    loading: false
  }),
  mounted() {
    var vm = this;
    vm.canvas = vm.$refs["my-canvas"];
    vm.ctx = vm.canvas.getContext("2d");
    window.addEventListener('load', this.init, false);
    this.createdShapes.push(this.machines);
    this.createdShapes.push(this.queues);
    this.createdShapes.push(this.bonds);
  },
  methods : {
    sleep(ms) {
      return new Promise(
        resolve => setTimeout(resolve, ms)
      );
    },
    // async
    async runstop(){
      if (this.state == "RUN"){
        this.runMenu = false ;
        this.state = "STOP";
        this.stateicon = "mdi-stop";
        this.clicked = true ;
        if (this.queues.length != 0 && this.machines.length !=0){
            //start run
            axios.get('http://localhost:8080/api/SetIntialProducts',{
                    params: {
                        ProductsNumber : this.noOfProducts,
                        QueueID : this.queues[0].ID,
                    }
            }).then();

            //clear replay
            axios.get('http://localhost:8080/api/clearReplay',{
            }).then();

            
            for (let index = 0; this.clicked == true ; index++) {
              console.log("hello");
              
              //get colors 
              axios.get('http://localhost:8080/api/GetMachinesColors',{
                }).then(Response=>{
                  const Data = Response.data;
                  var data1 = Data;
                  console.log(Data);
                  console.log(this.machines);

                  for (let index = 0; index < this.machines.length; index++) {
                    const color = data1[index] ;
                        this.machines[index].fillColor = "#"+(color.red).toString(16)+(color.green).toString(16)+(color.blue).toString(16);
                  }
                  axios.get('http://localhost:8080/api/Waiting',{}
                  ).then(Response=>{
                    const Data = Response.data;
                    this.waitingProducts = Data;
                  });
                  this.drawAll();
                });

                  

                  axios.get('http://localhost:8080/api/isEnd',{}
                  ).then(Response=>{
                    const Data = Response.data;
                    if (Data == true){
                      this.state = "RUN" ;
                      this.stateicon = "mdi-arrow-left-drop-circle";
                      this.clicked = false ;
                      this.waitingProducts.length = 0;
                      this.canRepaly = true ;
                      this.drawAll();
                      return;
                    }
              });

              await this.sleep(100);
            }
        }
      }
      else{
        //stop case
        this.state = "RUN" ;
        this.stateicon = "mdi-arrow-left-drop-circle";
        this.clicked = false ;
        this.waitingProducts.length = 0;
      }
    },
    replay(){
      this.loading = true;
      axios.get('http://localhost:8080/api/fullReplay',{
        }).then(Response=>{
            const Data = Response.data;
            this.getReplay(Data);           
            
      });
    },
    async getReplay(Data){
        for (let loop = 0; loop < Data.length; loop++) {
            var loopData = Data[loop];
            var waitinglist = [];
            console.log("this is loop data");         
            for (let index = 0; index < this.machines.length; index++) {
                const color = loopData[index].c ;
                this.machines[index].fillColor = "#"+(color.red).toString(16)+(color.green).toString(16)+(color.blue).toString(16);
                waitinglist.push(loopData[index].waiting);
            }             
            this.waitingProducts = waitinglist ;
            console.log(this.waitingProducts);
            this.drawAll();
            await this.sleep(100);
        }
        this.waitingProducts.length = 0;
        this.loading = false;
    },
    init: function() {
      var vm = this;
      vm.ctx.strokeStyle = "red";
      vm.ctx.lineCap = "round";
      vm.ctx.fillStyle = "blue";
      vm.ctx.font= "16px Arial";
      vm.ctx.textAlign = 'center';
      vm.ctx.textBaseline = 'middle';
      vm.ctx.lineWidth = 3;
      vm.canvas.addEventListener("mousedown", this.dragStart, false);
      vm.canvas.addEventListener("mousemove", this.drag, false);
      vm.canvas.addEventListener("mouseup", this.dragStop, false);
      //this.drawAll();
      axios.get('http://localhost:8080/api/inti',{}
      ).then();
    },
    createMachine() {
      this.canRepaly = false;
      this.timeMenu = false ;
      this.isCreateBond = false ;
      this.isDelete = false; 
      this.dragging = true;
      this.ctx.fillStyle = "rgb(229, 229, 229)";
      this.drawCircle(1224, 25);
      var newObject ={ID:"",name:"M"+(this.machines.length+1),borderColor:"rgb(229, 229, 229)" ,fillColor:"rgb(229, 229, 229)",
                    dragStartLocation : {x: 1224,y: 25}};
      this.machines.push(newObject);
      this.selectedShape=this.machines[this.machines.length-1];
      this.ctx.fillStyle = "black";
      this.ctx.fillText(newObject.name,newObject.dragStartLocation.x, newObject.dragStartLocation.y);
      
      //request to back-end
      axios.get('http://localhost:8080/api/addMachineWithTime',{
        params: {
            Time : this.machineTime*1000,
        }
      }).then(Response=>{
          const Data = Response.data;
          this.machines[this.machines.length-1].ID = Data ;
      });
    },
    createQueue() {
      this.isCreateBond = false ;
      this.canRepaly = false;
      this.isDelete = false; 
      this.dragging = true;
      this.ctx.fillStyle = "#EBCEBD";
      this.drawRectangle(1400, 24);
      var newObject ={ID:"",name:"Q"+(this.queues.length+1),borderColor:"#EBCEBD",fillColor:"#EBCEBD",
                    dragStartLocation : {x: 1400,y: 24}};
      this.queues.push(newObject);
      this.selectedShape=this.queues[this.queues.length-1];
      this.ctx.fillStyle = "black";
      this.ctx.fillText(newObject.name,newObject.dragStartLocation.x, newObject.dragStartLocation.y);

      //request to back-end
      axios.get('http://localhost:8080/api/addQueue',{}
      ).then(Response=>{
                const Data = Response.data;
                this.queues[this.queues.length-1].ID = Data ;
      });
    },
    createBond() {
      this.isDelete = false ;
      if (this.isCreateBond){
        this.dragging = false;
        this.drawAll();
      }
      this.isCreateBond = !this.isCreateBond ;
      this.ctx.strokeStyle = "black";
    },
    Delete() {
      this.isCreateBond  = false ;
      this.isDelete = !this.isDelete;
    },
    clear(){
      this.machines.length = 0;
      this.queues.length = 0;
      this.bonds.length = 0;
      this.canRepaly = false ;
      this.drawAll();
      axios.get('http://localhost:8080/api/inti',{}
      ).then();
    },
    dragStart: function(event) {
      if (this.isCreateBond === true || this.isDelete === true){
        var location = this.getCanvasCoordinates(event);
        for(var i=1 ; i>=0 ;i--){
          for(var j= this.createdShapes[i].length-1 ; j>=0 ;j--){
            if(this.isMouseInShape(location.x,location.y,this.createdShapes[i][j])){
                //this.found = true ;
                //this.selectedMachineIndex=i;
                if (this.isCreateBond === true && this.dragging === false){
                  this.takeSnapshot();
                  this.dragging = true;
                  this.object.dragStartLocation = location;
                  this.firstShape = this.createdShapes[i][j]
                  this.firstShapeType = i;
                }
                else if (this.isCreateBond === true && this.firstShapeType != i ) {
                  var enable = true;
                  for (var k = this.bonds.length-1; k >=0 ; k--){
                    if ( (this.bonds[k].from === this.firstShape.name && this.bonds[k].to === this.createdShapes[i][j].name) || (this.bonds[k].to === this.firstShape.name && this.bonds[k].from === this.createdShapes[i][j].name))
                      {
                        enable = false;
                        return;
                      }
                  }
                  if (enable){
                    this.dragging = false;
                    this.isCreateBond = false;
                    this.canRepaly = false;
                    var newObject ={name:"B",from: this.firstShape.ID ,to: this.createdShapes[i][j].ID ,borderColor:"black",fillColor:"black",
                    dragStartLocation : this.object.dragStartLocation , end : location};
                    this.bonds.push(newObject);
                    this.drawAll();

                    if(this.firstShape.name.includes("M")){
                      //create bond from machine
                      axios.get('http://localhost:8080/api/addQueueToMachine',{
                          params: {
                              MachineID : this.firstShape.ID,
                              QueueID : this.createdShapes[i][j].ID,
                          }
                      }).then();
                    }
                    else{
                      //form queue
                      axios.get('http://localhost:8080/api/addMachineToQueue',{
                          params: {
                              MachineID : this.createdShapes[i][j].ID,
                              QueueID : this.firstShape.ID,
                          }
                      }).then();
                    }
                  }   
                }
                else if (this.isDelete === true){
                  var ID = this.createdShapes[i][j].ID;
                  this.canRepaly = false;
                  if(this.createdShapes[i][j].name.includes("M")){
                    //create bond from machine
                    axios.get('http://localhost:8080/api/removeMachine',{
                        params: {
                            id : this.createdShapes[i][j].ID
                        }
                    }).then();
                  }
                  else{
                    //form queue
                    axios.get('http://localhost:8080/api/removeQueue',{
                        params: {
                            id : this.createdShapes[i][j].ID
                        }
                    }).then();
                  }

                  this.createdShapes[i].splice(j, 1);
                  for (k = 0; k < this.bonds.length ; k++){
                    if (this.bonds[k].from == ID || this.bonds[k].to == ID){
                      this.bonds.splice(k, 1);
                      k--;
                    }
                  }
                  this.drawAll();
                  this.isDelete = false;


                  if(this.machines.length == 0 && this.queues.length == 0){
                    axios.get('http://localhost:8080/api/inti',{}
                    ).then();
                  }
                }
                break;
            }
          }
        }
      }
    },
    drag :function(event) {
            var position;
            if (this.dragging === true ){
              if( this.isCreateBond === false) {
                position = this.getCanvasCoordinates(event);
                // move the selected shape by the drag distance
                //selectedShape.points[1].x += dx;
                //selectedShape.points[1].y += dy;
                var dx=position.x - this.selectedShape.dragStartLocation.x;
                var dy=position.y - this.selectedShape.dragStartLocation.y;
                this.selectedShape.dragStartLocation.x +=dx;
                this.selectedShape.dragStartLocation.x +=dy;
                // clear the canvas and redraw all shapes
                this.drawAll();
                // update the starting drag position (== the current mouse position)
                this.selectedShape.dragStartLocation.x=position.x;
                this.selectedShape.dragStartLocation.y=position.y;
              }
              else if (this.isCreateBond === true) {
                this.restoreSnapshot();
                position = this.getCanvasCoordinates(event);
                this.drawLine(this.object.dragStartLocation.x, this.object.dragStartLocation.y , position.x  , position.y);
              }
            }
    },
    dragStop :function () {
        if (this.isCreateBond === false)
            this.dragging = false;
    },
    drawAll : function(){
      this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
      for (var i = this.createdShapes.length-1 ; i >= 0 ; i--){
        for (var j = this.createdShapes[i].length-1 ; j >= 0 ; j--){
          var shape = this.createdShapes[i][j];
          this.ctx.fillStyle = shape.fillColor;
          this.ctx.strokeStyle = shape.borderColor;
          if (shape.name.includes("B")) {
            this.drawLine(shape.dragStartLocation.x, shape.dragStartLocation.y, shape.end.x, shape.end.y);
          }
          else if (shape.name.includes("M")) {
            this.drawCircle(shape.dragStartLocation.x, shape.dragStartLocation.y);
            this.ctx.fillStyle = 'black';
            shape.name = "M"+(j+1);
            this.ctx.fillText(shape.name,shape.dragStartLocation.x, shape.dragStartLocation.y);

            if (this.waitingProducts.length != 0){
              let location= JSON.parse(JSON.stringify(this.machines[j].dragStartLocation));
                location.y -= 35;
                location.x -= 22; 
              for (var k = 1; k < this.waitingProducts[j].length ; k++){
                var color = this.waitingProducts[j][k];
                this.ctx.fillStyle = "#"+(color.red).toString(16)+(color.green).toString(16)+(color.blue).toString(16);
                this.drawProduct(location.x,location.y);
                location.x += 11;
              }
            }
          }
          else if (shape.name.includes("Q")) {
            this.drawRectangle(shape.dragStartLocation.x, shape.dragStartLocation.y);
            this.ctx.fillStyle = 'black';
            shape.name = "Q"+(j+1);
            this.ctx.fillText(shape.name,shape.dragStartLocation.x, shape.dragStartLocation.y);
          }
        }
      }
    },
    drawLine: function (x1, y1 ,x2  , y2) {
        var vm = this;
        vm.ctx.beginPath();
        vm.ctx.moveTo(x1, y1);
        vm.ctx.lineTo(x2, y2);
        vm.ctx.stroke();
    },
    drawProduct :function(x1, y1) {
        var vm = this;
        var radius = 5;
        vm.ctx.beginPath();
        vm.ctx.arc(x1,y1, radius, 0, 2 * Math.PI, false);
        vm.ctx.fill();
    },
    drawCircle :function(x1, y1) {
        var vm = this;
        var radius = 25;
        vm.ctx.beginPath();
        vm.ctx.arc(x1,y1, radius, 0, 2 * Math.PI, false);
        vm.ctx.fill();
    },

    drawRectangle : function (x1, y1){
      this.ctx.beginPath();
      this.ctx.fillRect(x1-30, y1-18 , 60,36);
      this.ctx.fill();
    },
    getCanvasCoordinates: function(event) {
      var vm = this;
      var x = event.clientX - vm.canvas.getBoundingClientRect().left,
        y = event.clientY - vm.canvas.getBoundingClientRect().top;

      return { x: x, y: y };
    },

    takeSnapshot: function() {
      var vm = this;
      this.snapshot = vm.ctx.getImageData(0,0,vm.canvas.width,vm.canvas.height);
    },

    restoreSnapshot: function() {
      var vm = this;
      vm.ctx.putImageData(this.snapshot, 0, 0);
    },
    isMouseInShape :function(mx,my,shape){
        if(shape.name.includes("M")){
            var dx=mx-shape.dragStartLocation.x;
            var dy=my-shape.dragStartLocation.y;
            var radius = 25;
            // math test to see if mouse is inside circle
            if(dx*dx+dy*dy<radius*radius){
                return(true);
            }
        }
        else if(shape.name.includes("Q")){
            var rLeft = shape.dragStartLocation.x-30;
            var rRight = rLeft + 60;
            var rTop = shape.dragStartLocation.y-18;
            var rBott = rTop + 36;
            // math test to see if mouse is inside rectangle
            if( mx>rLeft && mx<rRight && my>rTop && my<rBott){
                return(true);
            }
        }

        // the mouse isn't in any of the shapes
        return(false);
    },

  }

};
</script>




<style>
h2{
  font-family: "JetBrains Mono";
}
button{
  margin: 10px;
  font-family: "JetBrains Mono";
}
/* #my-canvas {
  border: 1px solid grey;
} */
</style>
