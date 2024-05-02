from channels.generic.websocket import WebsocketConsumer
import threading,time,json
from .Image_list import test_object
from .frame_handler import frame_handler

_action = "Action"
_forward = 1
_backward = 2

class CameraViewConsumer(WebsocketConsumer):
    def connect(self):
        self.accept()
        self.connx = True
        # thread = threading.Thread(target = self.send_images)
        # thread.start()

    def disconnect(self, close_code):
        self.connx = False
    
    def receive(self, text_data):
        data_dict = self.StrToDict(text_data)
        if data_dict.get(_action) ==_forward:
            # image_data = test_object.get_bulk_images()
            image_data = frame_handler.getFrames()
            self.send(str(image_data))
            return
    
    def send_images(self):
        print("thread started")

        while self.connx:
            image_data = test_object.get_image_data()
            if not image_data:
                time.sleep(1)
                continue
            self.send(bytes_data=image_data)
            time.sleep(0.2)

        print("thread stop")


    def StrToDict(self,str_data:str)->dict:
        data_dict = json.loads(str_data)
        return data_dict





# base64_data = base64.b64encode(image_data).decode('utf-8')