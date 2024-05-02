package com.ITxSolution.itx_plant;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import pl.droidsonroids.gif.GifImageView;

public class SetPlant extends AppCompatActivity {
    Spinner plant_name_spinner;
    Button save_btn;
    ArrayAdapter<String> spinner_adapter;
    Animation scale_up_animation;
    View custom_toast_layout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_plant);

        // set own id and screen object
        StaticClass.current_activity_id =1;
        StaticClass.set_plant_activity = this;

        LayoutInflater inflater = getLayoutInflater();
        custom_toast_layout = inflater.inflate(R.layout.custom_toast_layout,findViewById(R.id.custom_layout));


        scale_up_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_up);
        plant_name_spinner = findViewById(R.id._plant_names_spinner);
        spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,StaticClass.PlantNamesArray);
        plant_name_spinner.setAdapter(spinner_adapter);

        save_btn = findViewById(R.id._set_plant_save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_btn.startAnimation(scale_up_animation);
                if(StaticClass.PlantNamesArray.size()>0) {
                    String item = StaticClass.PlantNamesArray.get(plant_name_spinner.getSelectedItemPosition());
                    Set_Plant(item);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        // Handle the back button press
        // For example, you can close the current activity and return to the previous one
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

    public void Set_Plant(String plant_name) {
        JSONObject json = new JSONObject();
        try {
            json.put(StaticClass._action,4);
            json.put("name",plant_name);
        } catch (Exception e){
            System.out.println(e);
            show_msg(R.drawable.asclamation_gif,"Try Again");
            return;
        }

        try{
            StaticClass.app_connection.websocket.send(json.toString());
        }catch (NullPointerException NE){
            show_msg(R.drawable.cross_gif,"No Internet");
            return;
        }
//        Toast.makeText(this,"Plant Set",Toast.LENGTH_SHORT).show();
        show_msg(R.drawable.check,"Plant Set");
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