from django.urls import path
from .AppConsumer import AppConsumer
from .Esp32CamConsumer import Esp32CamConsumer
from .WifiModuleConsumer import WifiModuleConsumer
from .CameraViewConsumer import CameraViewConsumer

WebsocketUrlPattern = [
    path('app',AppConsumer.as_asgi()),
    path('wifimod',WifiModuleConsumer.as_asgi()),
    path('esp32cam',Esp32CamConsumer.as_asgi()),
    path('cameraview',CameraViewConsumer.as_asgi()),
]