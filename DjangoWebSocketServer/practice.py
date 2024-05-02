# import cv2,os

# img = cv2.imread('image.jpg')
# height, width, _ = img.shape

# cv2.imshow("image",img)


# fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # Be sure to use lower case
# out = cv2.VideoWriter("video.mp4", fourcc, 30, (width, height))

# while True:  
#     out.write(img)
#     if cv2.waitKey(0) & 0xFF == ord('q'):
#         break


# cv2.destroyAllWindows()
# print(os.getcwd())


# list_ = [2,2,3,4,5,6,7,8,9,10,11,12]

# print()
byte_data = b'hassam'
str_data = str(byte_data)  # This converts bytes to string using default encoding (usually UTF-8)

# Convert the string back to byte data using the same encoding
byte_data_again = str_data.encode()  # This uses UTF-8 encoding by default

print(byte_data_again)  # b'hassam'