from channels.generic.websocket import WebsocketConsumer
from .Image_list import test_object
from .frame_handler import frame_handler
import threading   

class Esp32CamConsumer(WebsocketConsumer):
    def connect(self):
        self.accept()

    def disconnect(self, close_code):
        pass

    def receive(self, **kwargs):
        # thread = threading.Thread(target=self._thread, args=(kwargs,))
        # thread.start()
        self._thread(kwargs)
    
    def _thread(self,_dict):
        image_data = _dict["bytes_data"]
        # test_object.add_image_data(image_data)# storing image in jpeg format
        frame_handler.addFrame(2000,image_data)




# data = {"image": kwargs["bytes_data"]}
# serializer = FootageSerializer(data = data)
# if serializer.is_valid():
#     serializer.save()
#     return
# print(serializer.errors)