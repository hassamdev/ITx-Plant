package com.ITxSolution.itx_plant;
import org.json.JSONException;
import org.json.JSONObject;
public class PlantInfo {
    private String name,period,temperature,soil_temperature,soil_moisture,water_ph,humidity,water_level,light_intensity,light_schedule;

    public PlantInfo(){
        this.name = "default";
        this.period = "default";
        this.temperature = "default";
        this.soil_temperature = "default";
        this.soil_moisture = "default";
        this.water_ph = "default";
        this.humidity = "default";
        this.water_level = "default";
        this.light_intensity = "default";
        this.light_schedule = "default";
    }
    public void setAll(String _name,String _period,String _temperature,String _soil_temperature,String _soil_moisture
                        ,String _water_ph,String _humidity,String _water_level,String _light_intensity,String _light_schedule){
        this.name = _name;
        this.period = _period;
        this.temperature = _temperature;
        this.soil_temperature = _soil_temperature;
        this.soil_moisture = _soil_moisture;
        this.water_ph = _water_ph;
        this.humidity = _humidity;
        this.water_level = _water_level;
        this.light_intensity = _light_intensity;
        this.light_schedule = _light_schedule;
    }
    public void setJson(JSONObject _json) throws JSONException {
        this.setAll(_json.getString(StaticClass._plant_name),_json.getString(StaticClass._period),_json.getString(StaticClass._temperature),
                _json.getString(StaticClass._soil_temp),_json.getString(StaticClass._moisture),_json.getString(StaticClass._water_ph),
                _json.getString(StaticClass._humidity),_json.getString(StaticClass._water_level),_json.getString(StaticClass._light_intensity),
                _json.getString(StaticClass._light_schedule));

    }

    public String getName() {
        return name;
    }

    public String getPeriod() {
        return period;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getSoil_moisture() {
        return soil_moisture;
    }

    public String getLight_intensity() {
        return light_intensity;
    }

    public String getSoil_temperature() {
        return soil_temperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWater_level() {
        return water_level;
    }

    public String getWater_ph() {
        return water_ph;
    }

    public String getLight_schedule() {
        return light_schedule;
    }
}
