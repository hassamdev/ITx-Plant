package com.ITxSolution.itx_plant;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class CameraViewScreen extends AppCompatActivity {

    ProgressBar progressBar;
    ImageButton PausePlayBtn,RewindBtn,ForwardBtn;
    ImageView camera_view;
    boolean StopCondition=true;
    Handler handler = new Handler(Looper.getMainLooper());
    CustomArrayList ImageList = new CustomArrayList();
    WebSocket websocket;
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url("ws://192.168.0.103:9500/cameraview").build();
    WebSocketListener websocket_listner;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view_screen);
        // set own id and screen object
        StaticClass.current_activity_id =3;
        StaticClass.camer_view_screen = this;
        camera_view = findViewById(R.id.Camera_view);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        //Button
        PausePlayBtn = findViewById(R.id.pause_play_btn);
        ForwardBtn = findViewById(R.id.forward_btn);
        RewindBtn = findViewById(R.id.rewind_btn);

        PausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PausePlay();
            }
        });

        ForwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forward();
            }
        });

        RewindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backward();
            }
        });


        ImageList.setOnArrayListSizeChangedListener(new ArrayListener() {
            @Override
            public void onSizeChanged(int newSize) {}

            @Override
            public void onItemsAdd() {}

            @Override
            public void OnIndexChanged(int index) { /// this event to get images in advanced.
//                System.out.println("index change");
                if( (float) ((index+1) / ImageList.size()) >=0.5) {
                    System.out.println(((index+1) / ImageList.size()));
                    GetImages(1);
                }
            }
        });



        websocket_listner = new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                System.out.println("websocket close");
            }
            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                startConnx();
                System.out.println("Connx fail");
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text)   {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    JSONArray jsonarray = (JSONArray) jsonObject.get("frames");
                    ImageList.addItems(jsonarray);
                    StartStopProgress(0);// to stop the progress
//                    System.out.println(jsonarray.length());

                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                GetImages(1);
            }

        };

        startConnx();
        PausePlay();


    }


    public void startConnx(){
        websocket = client.newWebSocket(request, websocket_listner);
    }

    public void updateImage(byte[] imageData){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//                camera_view.setRotation(90.0f);
                camera_view.setImageBitmap(bitmap);
            }
        });
    }

    public void StartVideo(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                byte [] temp;
                while(true){
                    temp = ImageList.getItem();
                    if(StopCondition){
                        break;
                    }
                    if(temp!=null){
                        updateImage(temp);
                    }else{
                        System.out.println(temp);
                        GetImages(1);
                        try {
                            Thread.sleep(4000); // convert to milliseconds
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(100); // convert to milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("ThreadStop");

            }
        });
        thread.start();


    }
    public void StartStopProgress(int flag){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                if(flag==1){
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    public void GetImages(int flag){
        // 1 for forward images and 2 for backward images.
        StartStopProgress(1);
        JSONObject json = new JSONObject();
        try {
            if (flag == 1) {
                json.put("Action", 1);
            } else if (flag == 2) {
                json.put("Action", 2);
            }
        }
        catch (JSONException je){
         return;
        }
        this.websocket.send(json.toString());
    }

    public void PausePlay(){
        if(StopCondition){
            StopCondition = false;
            PausePlayBtn.setImageResource(R.drawable.pause);
            StartVideo();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            StopCondition = true;
            PausePlayBtn.setImageResource(R.drawable.play_arrow);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
    }

    public void Forward(){
        if(this.ImageList.CurrIndex +30+1 <= this.ImageList.size() ){

            this.ImageList.CurrIndex = this.ImageList.CurrIndex + 30 ;
        }else{
            this.ImageList.CurrIndex = this.ImageList.size() -1 -10 ;
        }
    }

    public void Backward(){
        if(this.ImageList.CurrIndex - 30 < 0 ){
            this.ImageList.CurrIndex = 0 ;

        }else{
            this.ImageList.CurrIndex = this.ImageList.CurrIndex - 30 ;
        }
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
                StopCondition = true;
                onBackPressed();
            }
        });
    }
}