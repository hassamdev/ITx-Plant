package com.ITxSolution.itx_plant;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class CustomArrayList {
    private int ImageLimit = 300;
    public int CurrIndex = 0;
    private ArrayList<byte []> arrayList;
    private ArrayListener arrayListener;

    public CustomArrayList(){
        this.arrayList = new ArrayList<>();
    }

    public int size(){
        return this.arrayList.size();
    }

    public void setOnArrayListSizeChangedListener(ArrayListener listener) {
        this.arrayListener = listener;
    }

    public void addItems(JSONArray Array)  {
        try{
            for(int i =0;i<Array.length();i++){
                byte[] imageData = Base64.decode(Array.getString(i), Base64.DEFAULT);
                arrayList.add(imageData);
                if(arrayList.size()>ImageLimit){
                    arrayList.remove(0);
                    decreaseIndex();
                }
            }
        }
        catch (JSONException js){
            System.out.println();
            System.out.print("Error in add Items");
            System.out.println(js);
            return;
        }


        notifyOnItemsAdd();
    }

    public byte[] getItem(){
        if(this.arrayList.size() >= this.CurrIndex + 1){
            byte [] temp;
            temp = this.arrayList.get(this.CurrIndex);
            increaseIndex();
            return temp;
        }
        return null;
    }

    private void decreaseIndex(){
        if(this.CurrIndex>0) {
            this.CurrIndex--;
            notifyIndexChanged();
        }
    }
    private void increaseIndex(){
        this.CurrIndex ++;
        notifyIndexChanged();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < arrayList.size()) {
            arrayList.remove(index);
            if(CurrIndex>0){
                CurrIndex--;
            }
            notifySizeChanged();
        }
    }

    private void notifySizeChanged() {
        if (arrayListener != null) {
            arrayListener.onSizeChanged(arrayList.size());
        }
    }

    private void notifyIndexChanged(){
        if (arrayListener != null) {
            arrayListener.OnIndexChanged(this.CurrIndex);
        }
    }
    private void notifyOnItemsAdd(){
        if (arrayListener != null) {
            arrayListener.onItemsAdd();
        }
    }
}


