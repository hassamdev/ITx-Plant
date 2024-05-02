from .models import Footage
import base64
from django.db import transaction


class FrameHandler():
    limit = 100
    last_image_index = 0

    def getFrames(self):
        # Get all images from the database                                   # start point                                                                  #endpoint             #order 
        all_images = Footage.objects.order_by('-timestamp').values("image")[(self.last_image_index - self.limit) if self.last_image_index>self.limit else 0 :self.last_image_index:-1]
        # setting index to appropriate position
        if self.last_image_index>self.limit:
            self.last_image_index -= self.limit
        else:
            self.last_image_index = 0

        all_images = list(all_images)
        
        temp_list = []
        for frame in all_images: # converting from LIst of dict items to list of frames
            temp_list.append(frame["image"])
        # print(len(temp_list))
        return {'frames': temp_list}
        


    def addFrame(self,limit,_frame):
        frame = Footage()
        frame.image = base64.b64encode(_frame).decode('utf-8')
        frame.save()

        if self.last_image_index<limit:
            self.last_image_index += 1
        # print("index :",self.last_image_index)

        frame_count = Footage.objects.count()
        if frame_count>limit:
            # print("frame_count :",frame_count)
            first_obj = Footage.objects.first()
            first_obj.delete()
            transaction.atomic()
            
            return
        
        


frame_handler = FrameHandler()