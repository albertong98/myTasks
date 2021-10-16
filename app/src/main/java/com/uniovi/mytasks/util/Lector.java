package com.uniovi.mytasks.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class Lector {
    public static String leerDeJson(Context context, String fileName){
        String jsonString = "";
        try {
            InputStream file = context.getAssets().open(fileName);

            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
