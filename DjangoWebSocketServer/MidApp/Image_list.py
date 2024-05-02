import base64

class TestClass():
    Quantity = 200
    currentIndex = 0
    Limit = 300
    Image_list = list()

    def get_bulk_images(self):
        temp_list = []
        
        for i in range(0,self.Quantity):
            try:
                base64_data = base64.b64encode(self.Image_list[self.currentIndex]).decode('utf-8')
                temp_list.append(base64_data)
                self.currentIndex += 1
            except IndexError:
                break
        print(len(temp_list))
        data = {
            "frames": temp_list,
        }
        return data


    def add_image_data(self,img_data):
        

        if len(self.Image_list)>self.Limit:
            self.Image_list.pop(0)
            if self.currentIndex>0:
                self.currentIndex -= 1
        # print(len(self.Image_list))
        self.Image_list.append(img_data)



    def get_image_data(self):
        return self.Image_list.pop(0) if self.Image_list else False
    

test_object = TestClass()