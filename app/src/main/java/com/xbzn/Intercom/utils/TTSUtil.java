package com.xbzn.Intercom.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Enzo Cotter on 2020/6/4.
 */
public class TTSUtil {
    TextToSpeech textToSpeech;
    public TTSUtil (Context context){
        textToSpeech =new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.CHINESE);
                }
            }

        });

    }
    public void speak(String text){
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }
}
