package com.ITxSolution.itx_plant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class AppConnection {
    WebSocket websocket;
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url("ws://192.168.0.103:9500/app").build();
    WebSocketListener websocket_listner = new WebSocketListener() {
        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
//            System.out.println("websocket close");

        }


        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
//            System.out.println("websocket failure");

            if(StaticClass.current_activity_id==1){
                StaticClass.set_plant_activity.backpressed();
            } else if(StaticClass.current_activity_id==2){
                StaticClass.add_plant_screen.backpressed();
            }else if(StaticClass.current_activity_id==3){
                StaticClass.camer_view_screen.backpressed();
            }
            if(StaticClass.app_connection != null){
                StaticClass.app_connection.start();
            }



        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
//            System.out.println(text);
            try {
                JSONObject jsonObject = new JSONObject(text);
                String action = jsonObject.getString(StaticClass._action);
                if(action.equals(StaticClass._plant_data)){
                    StaticClass.plant_info.setJson(jsonObject.getJSONObject(StaticClass._data));
                    StaticClass.main_activity.set_variables();
                }else if(action.equals(StaticClass._plant_names)){
//                    System.out.println(jsonObject.getJSONArray(StaticClass._data));
                    JSONArray jsonArray = jsonObject.getJSONArray(StaticClass._data);

                    StaticClass.PlantNamesArray.clear();
                    for(int i = 0 ; i<jsonArray.length();i++){
                        StaticClass.PlantNamesArray.add(jsonArray.getString(i));
                    }

                }
//                System.out.println(jsonObject.getJSONObject(StaticClass._data));


            } catch (JSONException e) {
//                throw new RuntimeException(e);
                System.out.println(e);
            }


        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            StaticClass.main_activity.close_progress();
        }

    };

    public void start(){
        StaticClass.main_activity.show_progress();
        this.websocket  = client.newWebSocket(this.request,this.websocket_listner);
    }
}
