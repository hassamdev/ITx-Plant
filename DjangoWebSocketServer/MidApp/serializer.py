from rest_framework import serializers
from .models import CurrentStatus,Plant,Footage
import datetime


class CurrentStatusSerializer(serializers.ModelSerializer):
    plantname = serializers.SerializerMethodField()
    period = serializers.SerializerMethodField()
    lightschedule = serializers.SerializerMethodField()
    class Meta:
        model = CurrentStatus
        exclude = ['iot_set_flag','id','plant','timestamp']

    def get_plantname(self,obj):
        return obj.plant.name
    
    def get_period(self,obj):
        _datetime = datetime.datetime.now()
        period_obj =_datetime - obj.timestamp
        temp_str=str(period_obj.days) + "days" if period_obj.days > 1 else "day"
        temp_str +=" " + str(int(period_obj.seconds/3600)) +"hrs" if int(period_obj.seconds/3600) > 1 else " " +str(int(period_obj.seconds/3600))+" hr"
        return temp_str 
                   
    def get_lightschedule(self,obj):
        am_pm = None
        start_time,stop_time = obj.plant.light_schedule.split("-")
        SH,SM = start_time.split(":")
        stopH,stopM = stop_time.split(":")
        if(int(SH) > 12):
            am_pm = "PM"
            start_time = str(int(SH) - 12)+":"+str(SM) + am_pm
        else:
            am_pm = "AM"
            start_time = start_time + am_pm

        if(int(stopH) > 12):
            am_pm = "PM"
            stop_time = str(int(stopH) - 12)+":"+str(stopM) + am_pm
        else:
            am_pm = "AM"
            stop_time = stop_time + am_pm

        return start_time + "-"+ stop_time
    


class CreateCurrentStatusSerializer(serializers.ModelSerializer):
    class Meta:
        model = CurrentStatus
        fields = '__all__'
    


class PlantSerializer(serializers.ModelSerializer):
    class Meta:
        model = Plant
        # fields = ['name']
        fields = '__all__'



class PlantNameSerializer(serializers.ModelSerializer):
    name = serializers.SerializerMethodField()
    class Meta:
        model = Plant
        fields = ['name']

    def get_name(self,obj):
        return obj.name.lower()
    

class PlantSerializerForIot(serializers.ModelSerializer):
    max_temp = serializers.SerializerMethodField()
    min_temp = serializers.SerializerMethodField()
    max_soil_temp = serializers.SerializerMethodField()
    min_soil_temp = serializers.SerializerMethodField()
    max_moisture = serializers.SerializerMethodField()
    min_moisture = serializers.SerializerMethodField()
    max_light_intensity = serializers.SerializerMethodField()
    min_light_intensity = serializers.SerializerMethodField()
    max_humidity = serializers.SerializerMethodField()
    min_humidity = serializers.SerializerMethodField()
    light_start_time = serializers.SerializerMethodField()
    light_stop_time = serializers.SerializerMethodField()

    class Meta:
        model = Plant
        exclude = ['name','timestamp','id' ,'light_schedule']

    
    def get_max_temp(self,obj):
        return str(obj.max_temp)
    def get_min_temp(self,obj):
        return str(obj.min_temp)
    def get_max_soil_temp(self,obj):
        return str(obj.max_soil_temp)
    def get_min_soil_temp(self,obj):
        return str(obj.min_soil_temp)
    def get_max_moisture(self,obj):
        return str(obj.max_moisture)
    def get_min_moisture(self,obj):
        return str(obj.min_moisture)
    def get_max_light_intensity(self,obj):
        return str(obj.max_light_intensity)
    def get_min_light_intensity(self,obj):
        return str(obj.min_light_intensity)
    def get_max_humidity(self,obj):
        return str(obj.max_humidity)
    def get_min_humidity(self,obj):
        return str(obj.min_humidity)
    def get_light_start_time(self,obj):
        return obj.light_schedule.split("-")[0]
    def get_light_stop_time(self,obj):
        return obj.light_schedule.split("-")[1]


class FootageSerializer(serializers.ModelSerializer):
    class Meta:
        model = Footage
        fields = ["image"]

