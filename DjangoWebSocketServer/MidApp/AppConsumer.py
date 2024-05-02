from channels.generic.websocket import WebsocketConsumer
from .models import CurrentStatus,Plant
from .serializer import CurrentStatusSerializer,PlantSerializer,PlantNameSerializer,CreateCurrentStatusSerializer
import threading,time,json
from .Common import WifiModule

DEFAULT_VALUE =0
_action = "Action"
_data ="Data"
_get_plants =2
_add_plant = 3
_set_plant = 4
_update_schedule = 5

class AppConsumer(WebsocketConsumer):
    connx_state = False
    def connect(self):
        self.accept()
        self.connx_state = True
        thread = threading.Thread(target=self.CustomThread)
        thread.start()

    def disconnect(self, close_code):
        self.connx_state = False

    def receive(self, text_data):
        data_dict = self.StrToDict(text_data)
        # print(data_dict)
        if data_dict.get(_action) == _add_plant:
            data_dict.pop(_action)
            self.AddPlant(data_dict)
        elif data_dict.get(_action) == _get_plants:
            self.GetPlants()
        elif data_dict.get(_action) == _set_plant:
            data_dict.pop(_action)
            self.SetPlant(data_dict)
        elif data_dict.get(_action) == _update_schedule:
            data_dict.pop(_action)
            self.UpdateSchedule(data_dict)
        
    def CustomThread(self):
        while self.connx_state:
            data = CurrentStatus.objects.all()
            if not data:
                self.CreateDefaultPlant()
                continue
            serializer = CurrentStatusSerializer(data[0])
            temp_dict = {_action:"PlantData",
                         _data:serializer.data
                         }
            self.send(str(temp_dict))
            time.sleep(5) 


    def AddPlant(self, data):
        print("AddPlant")
        serializer = PlantSerializer(data = data)
        if serializer.is_valid():
            serializer.save()
            # self.send(str(serializer.data))
            return
        self.send(str(serializer.errors))


    
    
    def GetPlants(self):
        data = Plant.objects.all()
        serializer = PlantNameSerializer(data,many=True)
        temp_list = [x["name"] for x in serializer.data]
        temp_dict ={_action:"PlantNames",_data:temp_list}
        self.send(str(temp_dict))


    
    
    
    def SetPlant(self,data):
        current_plant = CurrentStatus.objects.all()[0]
        # if plant already set
        if current_plant.plant.name == data["name"]:
            return
        plant_obj = Plant.objects.get(name = data["name"])
        data = {"plant":plant_obj.id}
        serializer = CreateCurrentStatusSerializer(data = data)
        if serializer.is_valid():
            serializer.save()
            last_current_obj= CurrentStatus.objects.all()[0]
            last_current_obj.delete()
            print(WifiModule)
            if WifiModule:
                WifiModule[0].disconnect(123)
            return
        self.send(str(serializer.errors))
        
    def UpdateSchedule(self,data):
        current_plant = CurrentStatus.objects.all()[0]
        serializer = PlantSerializer(current_plant.plant,data=data,partial = True)
        if serializer.is_valid():
            serializer.save()
            current_plant.iot_set_flag = 0
            current_plant.save()
            return

    
    
    
    def StrToDict(self,str_data:str):
        data_dict = json.loads(str_data)
        return data_dict
    



    def CreateDefaultPlant(self):
        plant_data ={
        "name": "default",
          "max_temp": DEFAULT_VALUE,
            "min_temp": DEFAULT_VALUE,
              "max_soil_temp": DEFAULT_VALUE,
                "min_soil_temp": DEFAULT_VALUE,
                  "max_moisture": DEFAULT_VALUE,
                    "min_moisture": DEFAULT_VALUE,
                      "max_water_ph": DEFAULT_VALUE,
                        "min_water_ph": DEFAULT_VALUE,
                          "max_light_intensity": DEFAULT_VALUE, 
                          "min_light_intensity": DEFAULT_VALUE,
                            "max_humidity": DEFAULT_VALUE,
                              "min_humidity": DEFAULT_VALUE
        }

        serializer = PlantSerializer(data = plant_data)
        if serializer.is_valid():
            serializer.save()
            serializer = CreateCurrentStatusSerializer(data = {"plant":serializer.data["id"]})
            if serializer.is_valid():
                serializer.save()
            return
        


        # STILL NOT USING THIS
    def DeleteDefaultPlant(self):

        try:
            plant_obj = Plant.objects.get(name = "Default")
        except Plant.DoesNotExist:
            return
        plant_obj.delete()



