from django.db import models

# Create your models here.

class Plant(models.Model):
    name = models.CharField(max_length=100,unique=True)
    max_temp = models.IntegerField()
    min_temp = models.IntegerField()
    max_soil_temp = models.IntegerField()
    min_soil_temp = models.IntegerField()
    max_moisture=models.IntegerField()
    min_moisture=models.IntegerField()
    max_water_ph = models.DecimalField(decimal_places=2,max_digits=5)
    min_water_ph = models.DecimalField(decimal_places=2,max_digits=5)
    max_light_intensity = models.IntegerField()
    min_light_intensity = models.IntegerField()
    max_humidity = models.IntegerField()
    min_humidity = models.IntegerField()
    light_schedule = models.CharField(max_length=25, default="00:00-13:00")
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.name

class CurrentStatus(models.Model):
    plant = models.OneToOneField(Plant,on_delete=models.CASCADE,related_name='plantname')
    iot_set_flag = models.BooleanField(default=False)
    water_ph = models.DecimalField(default=0,decimal_places=2,max_digits=5)
    light_intensity = models.IntegerField(default=0)
    humidity = models.IntegerField(default=0)
    soil_temp = models.IntegerField(default=0)
    moisture = models.IntegerField(default=0)
    temperature = models.IntegerField(default=0)
    water_level = models.IntegerField(default=0)
    timestamp = models.DateTimeField(auto_now_add=True)


class Footage(models.Model):
    image = models.TextField(null=False, blank=False)
    timestamp = models.DateTimeField(auto_now_add=True)

    
