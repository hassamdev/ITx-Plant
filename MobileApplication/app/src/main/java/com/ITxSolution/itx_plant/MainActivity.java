package com.ITxSolution.itx_plant;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import java.lang.NullPointerException;
import android.widget.Button;
import android.widget.ImageButton;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import pl.droidsonroids.gif.GifImageView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView _plant_name,_period,_temperature,_soil_temperature,_soil_moisture,_water_ph,_humidity,_water_level,_light_intensity,_light_schedule;
    Button add_plant_btn,set_plant_btn;
    ImageButton camera_view_btn,light_schedule_btn;
    Dialog light_schedule_dialog,progress_dialog;
    Handler handler = new Handler(Looper.getMainLooper());
    TimePicker StartTimePicker,StopTimePicker;
    Animation scale_up_animation,arrow_animation;
    View light_schedule_view,custom_toast_layout;
    Button light_schedlue_save_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set own id and screen object
        StaticClass.current_activity_id =0;
        StaticClass.main_activity = this;

        // custom layout views
        LayoutInflater inflater = getLayoutInflater();
        custom_toast_layout = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.custom_layout));
        light_schedule_view = inflater.inflate(R.layout.light_schedule_dialog, findViewById(R.id.custom_layout));


        //animation
        arrow_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.arrow_anim);
        scale_up_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_up);

        // Textviews
        _plant_name = findViewById(R.id._plant_name_var);
        _period = findViewById(R.id._period_var);
        _temperature = findViewById(R.id._temperature_var);
        _soil_temperature = findViewById(R.id._soil_temperature_var);
        _soil_moisture = findViewById(R.id._soil_moisture_var);
        _water_ph = findViewById(R.id._water_ph_var);
        _water_level = findViewById(R.id._water_level_var);
        _humidity = findViewById(R.id._humidity_var);
        _light_intensity= findViewById(R.id._light_intensity_var);
        _light_schedule = findViewById(R.id._light_schedule_var);

        // set Textviews
        set_variables();
        // buttons
        add_plant_btn  = findViewById(R.id.add_plant_btn);
        camera_view_btn = findViewById(R.id.camera_view_btn);
        set_plant_btn = findViewById(R.id.set_plant_btn);
        light_schedule_btn = findViewById(R.id.light_schedule_btn);

        progress_dialog = new Dialog(MainActivity.this);
        progress_dialog.setContentView(R.layout.progress_dialog);
        progress_dialog.setCancelable(false);




        light_schedule_dialog = new Dialog(MainActivity.this);
        light_schedule_dialog.setContentView(light_schedule_view);

        //Timepicker
        StartTimePicker = light_schedule_view.findViewById(R.id.starttime);
        StopTimePicker = light_schedule_view.findViewById(R.id.stoptime);

        // light shedule save button
        light_schedlue_save_btn = light_schedule_view.findViewById(R.id.light_schedule_save_btn);

        light_schedlue_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_schedlue_save_btn.startAnimation(scale_up_animation);

                int SH = StartTimePicker.getHour();
                int SM = StartTimePicker.getMinute();

                int StopH = StopTimePicker.getHour();
                int StopM = StopTimePicker.getMinute();

                String time = String.valueOf(SH)+":"+String.valueOf(SM)+"-"+String.valueOf(StopH)+":"+String.valueOf(StopM);
                update_light_schedule(time);


            }
        });


        light_schedule_btn.startAnimation(arrow_animation);
        light_schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_schedule_dialog.show();
            }
        });

        set_plant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            set_plant_btn.startAnimation(scale_up_animation);
            get_plant_names();

            Intent intent = new Intent(MainActivity.this,SetPlant.class);
            startActivity(intent);
            }
        });
        camera_view_btn.startAnimation(arrow_animation);
        camera_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraViewScreen.class);
                startActivity(intent);
            }
        });
        add_plant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_plant_names();
                add_plant_btn.startAnimation(scale_up_animation);
                Intent intent = new Intent(MainActivity.this, AddPlantScreen.class);
                startActivity(intent);
            }
        });

        // creating app connections
//        this.show_progress();
        StaticClass.main_activity = this;
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                System.out.println( "The default network is now: " + network);
                StaticClass.app_connection = new AppConnection();
                StaticClass.app_connection.start();
            }

            @Override
            public void onLost(Network network) {
                System.out.println( "The application no longer has a default network. The last default network was " + network);
                StaticClass.app_connection = null;
                show_progress();
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
//                System.out.println( "The default network changed capabilities: " + networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
//                System.out.println( "The default network changed link properties: " + linkProperties);
            }
        });
    }

    public void set_variables(){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                _plant_name.setText(StaticClass.plant_info.getName());
                _period.setText(StaticClass.plant_info.getPeriod());
                _temperature.setText(StaticClass.plant_info.getTemperature());
                _soil_moisture.setText(StaticClass.plant_info.getSoil_moisture());
                _soil_temperature.setText(StaticClass.plant_info.getSoil_temperature());
                _water_ph.setText(StaticClass.plant_info.getWater_ph());
                _water_level.setText(StaticClass.plant_info.getWater_level());
                _humidity.setText(StaticClass.plant_info.getHumidity());
                _light_intensity.setText(StaticClass.plant_info.getLight_intensity());
                _light_schedule.setText(StaticClass.plant_info.getLight_schedule());
            }
        });
    }
    public void get_plant_names()  {
        JSONObject json = new JSONObject();
        try{
            json.put(StaticClass._action,2);
            StaticClass.app_connection.websocket.send(json.toString());
        }catch (Exception e){
            System.out.print("get_plant_names()->>");
            System.out.println(e);
        }


    }
    public void show_progress(){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                progress_dialog.show();
            }
        });

    }
    public void close_progress(){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                progress_dialog.dismiss();
            }
        });

    }

    public void update_light_schedule(String time){
        JSONObject json = new JSONObject();
        try{
            json.put("Action",5);
            json.put("light_schedule",time);// no need unique name ,because it is understood that current plant is set in current table.
            StaticClass.app_connection.websocket.send(json.toString());
        }catch (NullPointerException NE){
            light_schedule_dialog.dismiss();
//            Toast.makeText(this, "you are not connected with internet",Toast.LENGTH_LONG).show();
            show_msg(R.drawable.cross_gif,"No Internet");
            return;
        }catch (Exception e){
            light_schedule_dialog.dismiss();
//            Toast.makeText(this, "Try Again",Toast.LENGTH_LONG).show();
            show_msg(R.drawable.asclamation_gif,"Try Again");
            return;
        }
        light_schedule_dialog.dismiss();
//        Toast.makeText(this, "Updated",Toast.LENGTH_LONG).show();
        this.show_msg(R.drawable.check,"Updated");

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