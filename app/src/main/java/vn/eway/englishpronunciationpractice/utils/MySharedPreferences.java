package vn.eway.englishpronunciationpractice.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.net.ssl.SSLContextSpi;

import vn.eway.englishpronunciationpractice.AppConfigs;

/**
 * Created by nguye_000 on 1/27/2016.
 */
public class MySharedPreferences {
    private static MySharedPreferences instance;
    private final Context context;
    private SharedPreferences sharedPreferences;

    public MySharedPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(AppConfigs.SHARED_PREFERENCES_NAME, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
    }

    public static MySharedPreferences getInstance(Context context){
        if(instance == null){
            instance = new MySharedPreferences(context);
        }
        return instance;
    }

    public void countNumberOfRecordAction(){
        int numberOfRecordAction = sharedPreferences.getInt(Consts.Shared.NUMBER_OF_RECORD_ACTION, 0) + 1;
        sharedPreferences.edit().putInt(Consts.Shared.NUMBER_OF_RECORD_ACTION, numberOfRecordAction).apply();
    }

    public int getNumberOfRecordAction(){
        return sharedPreferences.getInt(Consts.Shared.NUMBER_OF_RECORD_ACTION, 0);
    }

    public void resetNumberOfRecordAction() {
        sharedPreferences.edit().remove(Consts.Shared.NUMBER_OF_RECORD_ACTION).apply();
    }
}
