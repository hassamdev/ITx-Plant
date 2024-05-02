package com.ITxSolution.itx_plant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

public class AddPlantScreen extends AppCompatActivity {
    Button AddButton;
    EditText PlantName,MaxTemp,MinTemp;
    EditText MaxMoist,MinMoist,
    MaxHumidity,MinHumidity,
    MaxIntensity,MinIntensity,
    MaxSoilTemp,MinSoilTemp,
    MaxWaterPh,MinWaterPh;

    int max_temp ,min_temp,
    max_moist,min_moist,
    max_humidity,min_humidity,
    max_intensity,min_intensity,
    max_soil_tem,min_soil_temp;
    float max_water_ph,min_water_ph;
    View custom_toast_layout;
    String plant_name;
    Animation scale_up_animation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant_screen);

        // set own id and screen object
        StaticClass.current_activity_id =2;
        StaticClass.add_plant_screen = this;

        LayoutInflater inflater = getLayoutInflater();
        custom_toast_layout = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.custom_layout));

        scale_up_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_up);


        PlantName = findViewById(R.id.PlantNameVar);
        MaxTemp = findViewById(R.id.MaxTempVar);
        MinTemp = findViewById(R.id.MinTempVar);
        MaxMoist = findViewById(R.id.MaxMoistVar);
        MinMoist = findViewById(R.id.MinMoistVar);
        MaxHumidity = findViewById(R.id.MaxHumidityVar);
        MinHumidity = findViewById(R.id.MinHumidityVar);
        MaxIntensity = findViewById(R.id.MaxIntensityVar);
        MinIntensity = findViewById(R.id.MinIntensityVar);
        MaxSoilTemp = findViewById(R.id.MaxSoilTempVar);
        MinSoilTemp = findViewById(R.id.MinSoilTempVar);
        MaxWaterPh = findViewById(R.id.MaxWaterPhVar);
        MinWaterPh = findViewById(R.id.MinWaterPhVar);


        AddButton = findViewById(R.id.AddPlantBtn);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddButton.startAnimation(scale_up_animation);
                try{
                    getName();
                    getTemp();
                    getMoist();
                    getHumidity();
                    getIntensity();
                    getSoilTemp();
                    getWaterPh();
                    CollectDataAndSend();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void backpressed(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Handle the back button press

                onBackPressed();
            }
        });
    }

    public void getName()throws Exception{
        String name = this.PlantName.getText().toString().toLowerCase();
        if(StaticClass.PlantNamesArray.contains(name)){
//            Toast.makeText(this,"PlantName already exists" ,Toast.LENGTH_SHORT).show();
            show_msg(R.drawable.asclamation_gif,"PlantName already exists");
            throw new Exception("PlantName already exists");
        } else if (name.length()==0) {
            show_msg(R.drawable.asclamation_gif,"PlantName empty");
            throw new Exception("PlantName empty");
        }
        this.plant_name = name;
    }

    public void getTemp() throws Exception {
        String maxtemp = String.valueOf(this.MaxTemp.getText());
        String mintemp = String.valueOf(this.MinTemp.getText());

        this.CheckValidation(maxtemp,mintemp,"Temperature");
        this.max_temp = Integer.parseInt(maxtemp);
        this.min_temp = Integer.parseInt(mintemp);

    }

    public void getMoist() throws Exception {
        String maxmoist = String.valueOf(this.MaxMoist.getText());
        String minmoist = String.valueOf(this.MinMoist.getText());

        this.CheckValidation(maxmoist,minmoist,"SoilMoisture");

        this.max_moist = Integer.parseInt(maxmoist);
        this.min_moist = Integer.parseInt(minmoist);

    }

    public void getHumidity() throws Exception {
        String max = String.valueOf(this.MaxHumidity.getText());
        String min = String.valueOf(this.MinHumidity.getText());

        this.CheckValidation(max,min,"Humidity");

        this.max_humidity = Integer.parseInt(max);
        this.min_humidity = Integer.parseInt(min);
    }

    public void getIntensity() throws Exception {
        String max = String.valueOf(this.MaxIntensity.getText());
        String min = String.valueOf(this.MinIntensity.getText());

        this.CheckValidation(max,min,"Intensity");

        this.max_intensity = Integer.parseInt(max);
        this.min_intensity = Integer.parseInt(min);
    }

    public void getSoilTemp() throws Exception {
        String max = String.valueOf(this.MaxSoilTemp.getText());
        String min = String.valueOf(this.MinSoilTemp.getText());

        this.CheckValidation(max,min,"SoilTemperature");

        this.max_soil_tem = Integer.parseInt(max);
        this.min_soil_temp = Integer.parseInt(min);
    }

    public void getWaterPh() throws Exception {
        String max = String.valueOf(this.MaxWaterPh.getText());
        String min = String.valueOf(this.MinWaterPh.getText());

        this.CheckValidation(max,min,"WaterPh");
        if(Integer.parseInt(max)>14 || Integer.parseInt(min)<1){
//            Toast.makeText(this,"please enter WaterPH between 1 to 14 " ,Toast.LENGTH_SHORT).show();
            show_msg(R.drawable.asclamation_gif,"please enter WaterPH between 1 to 14 ");
            throw new Exception(" WaterPh exceeded limit");

        }


        this.max_water_ph = Float.parseFloat(max);
        this.min_water_ph = Float.parseFloat(min);
    }

    public void CheckValidation(String MaxValue,String MinValue,String VariableName) throws Exception {
        if(MaxValue.length()==0 || MinValue.length()==0){

//            Toast.makeText(this,"please enter the "+VariableName+" Range." ,Toast.LENGTH_SHORT).show();
            show_msg(R.drawable.asclamation_gif,"please enter the "+VariableName+" Range.");
            throw new Exception(VariableName+" is empty");

        } else if (MaxValue.equals(MinValue)) {

//            Toast.makeText(this,VariableName+" is same." ,Toast.LENGTH_SHORT).show();
            show_msg(R.drawable.asclamation_gif,VariableName+" is same.");
            throw new Exception(VariableName+" Range is Same");

        } else if (Integer.parseInt(MinValue)> Integer.parseInt(MaxValue)) {
//            Toast.makeText(this,"Min "+VariableName+" is Greater." ,Toast.LENGTH_SHORT).show();
            show_msg(R.drawable.asclamation_gif,"Min "+VariableName+" is Greater.");
            throw new Exception("Min "+VariableName+" is greater");

        }

    }

    public void CollectDataAndSend(){
//        {
//            "action":3,
//                "name": "Rose", "max_temp": 40, "min_temp": 35, "max_soil_temp": 20, "min_soil_temp": 10, "max_moisture": 50, "min_moisture": 30,
//                "max_water_ph": 9.00, "min_water_ph": 6.50, "max_light_intensity": 7000, "min_light_intensity": 5000, "max_humidity": 80, "min_humidity": 70
//        }
        JSONObject json = new JSONObject();
        try {
            json.put("Action",3);
            json.put("name",this.plant_name);
            json.put("max_temp",this.max_temp);
            json.put("min_temp",this.min_temp);
            json.put("max_soil_temp",this.max_soil_tem);
            json.put("min_soil_temp",this.min_soil_temp);
            json.put("max_moisture",this.max_moist);
            json.put("min_moisture",this.min_moist);
            json.put("max_water_ph",this.max_water_ph);
            json.put("min_water_ph",this.min_water_ph);
            json.put("max_light_intensity",this.max_intensity);
            json.put("min_light_intensity",this.min_intensity);
            json.put("max_humidity",this.max_humidity);
            json.put("min_humidity",this.min_humidity);
        }catch (Exception e){
            System.out.println(e);
            this.show_msg(R.drawable.asclamation_gif,"Try Again");
            return;
        }

        try{
            StaticClass.app_connection.websocket.send(json.toString());
            StaticClass.main_activity.get_plant_names();// to get the updated list of plantNames
            this.EmptyCall();
        }catch (NullPointerException NE){
            this.show_msg(R.drawable.cross_gif,"No Internet");
            return;
        }



    }

    public void EmptyCall(){
        PlantName.setText("");
        MaxTemp.setText("");
        MinTemp.setText("");
        MaxMoist.setText("");
        MinMoist.setText("");
        MaxHumidity.setText("");
        MinHumidity.setText("");
        MaxIntensity.setText("");
        MinIntensity.setText("");
        MaxSoilTemp.setText("");
        MinSoilTemp.setText("");
        MaxWaterPh.setText("");
        MinWaterPh.setText("");
    }
    public void show_msg(int resource,String msg){
        TextView msg_text = this.custom_toast_layout.findViewById(R.id.message_text);
        msg_text.setText(msg);
        GifImageView gif = this.custom_toast_layout.findViewById(R.id.gif_file);
        gif.setImageResource(resource);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(this.custom_toast_layout);
        toast.show();
    }

}