package matiastesio.dices.MainHelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {

    private static SharedPreferencesHelper instance;


    private SharedPreferencesHelper(){
    }

    public static SharedPreferencesHelper getInstance(){
        if(instance == null){
            instance = new SharedPreferencesHelper();
        }
        return instance;
    }

    public void saveContactData(String jugador, String msisdn, String sender_mail, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("jugador",jugador);
        editor.putString("msisdn",msisdn);
        editor.putString("mail",sender_mail);
        editor.apply();
    }

    public void saveSenderMail(String sender_mail, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("mail",sender_mail);
        editor.apply();
    }

    public List<String> getData(Context context) {
        List<String> data = new ArrayList<String>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        data.add(preferences.getString("jugador", null));
        data.add(preferences.getString("msisdn", null));
        data.add(preferences.getString("mail", null));

        return data;
    }
}
