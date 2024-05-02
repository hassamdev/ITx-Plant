from channels.generic.websocket import WebsocketConsumer
from .models import CurrentStatus
from .serializer import PlantSerializerForIot,CurrentStatusSerializer
import threading,json
from .Common import WifiModule
from decimal import *


_action = "Action"
_update = 1
_acknowledge = 2
_data = "Data"



class WifiModuleConsumer(WebsocketConsumer):

    def connect(self):
        self.accept()
        WifiModule.append(self)
        if not self.IsCurrentPlantFlagSet():
            thread = threading.Thread(target=self.SetPlantInIOT)
            thread.start()
        



    def disconnect(self, close_code):
        WifiModule.clear()
        self.close()
        pass
    
    
    def receive(self, text_data):
        print(text_data)
        
        data = self.StrToDict(text_data)

        if data.get(_action) == _acknowledge:
            self.UpdateIotSetFlag()
            print("plant Set in IOT device:")
        
        elif data.get(_action) == _update:
            data.pop(_action)
            self.UpdateCurrentPlantData(data) 
            

    def UpdateCurrentPlantData(self,data):
        if self.IsCurrentPlantPresent():
            current_plant =CurrentStatus.objects.all()[0]
            # print(data)
            serializer = CurrentStatusSerializer(current_plant,data = data)
            # print(serializer.is_valid())
            if serializer.is_valid():
                serializer.save()


    def StrToDict(self,str_data:str):
        data_dict = json.loads(str_data)
        return data_dict
    
    

    def IsCurrentPlantPresent(self):
        current_plant = CurrentStatus.objects.all()
        if len(current_plant) == 0:
            return False
        return True


    def IsCurrentPlantFlagSet(self):
        current_plant = CurrentStatus.objects.all()
        if len(current_plant) == 0:
            return True# True means current plant is not set yet by application
        return current_plant[0].iot_set_flag
    


    def SetPlantInIOT(self):
        current_plant =  CurrentStatus.objects.all()
        serializer = PlantSerializerForIot(current_plant[0].plant)
        # print(serializer.data)
        self.send(str(serializer.data))



    def UpdateIotSetFlag(self):
        current_plant = CurrentStatus.objects.all()[0]
        current_plant.iot_set_flag = True
        current_plant.save()
        