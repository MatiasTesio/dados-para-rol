package matiastesio.dices.dicesscreen;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import java.util.List;
import matiastesio.dices.MainHelpers.GMailSender;
import matiastesio.dices.MainHelpers.SharedPreferencesHelper;
import matiastesio.dices.constants.Constants;
import matiastesio.dices.model.Master;

public class DicesPresenter {
    private DicesView view;
    private Master model;

    public DicesPresenter(DicesView view, Master model){
        this.view = view;
        this.model = model;
    }

    public void saveContact(String jugador, String msisdn, String sender_mail, Context context) {
        SharedPreferencesHelper.getInstance().saveContactData(jugador, msisdn, sender_mail, context);
        model.setMail(sender_mail);
    }

    public void saveSenderMail(String sender_mail, Context context) {
        SharedPreferencesHelper.getInstance().saveSenderMail(sender_mail, context);
        model.setMail(sender_mail);
    }

    public void rollDices(String cantDados, String spinnerItem){
        String msj="";
        for (int i = 0; i < Integer.parseInt(cantDados); i++) {
            msj += String.valueOf(((int) (Math.random() * Integer.parseInt(spinnerItem)) + 1)) + "; ";
        }
        view.setDicesValue(msj);
    }

    public void sendMail(final String jugador,
                         final String habilidad,
                         final Editable cantDados,
                         final String spinner,
                         final String msj, final String sender_mail) {

        model.setMail(sender_mail);
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(Constants.SENDER_MAIL_POKE, Constants.SENDER_PASSWORD);
                    sender.sendMail(jugador + " - " +habilidad,
                            "El jugador "+ jugador
                                    + " para la habilidad "+habilidad +" Tiro " + cantDados
                                    + " dados de " + spinner + " caras, el resultado es: "
                                    + msj,
                            Constants.SENDER_MAIL_POKE,
                            sender_mail);
                    view.dismissDialog();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    public void getDataFromSharedPref(Context context) {
        List<String> data = SharedPreferencesHelper.getInstance().getData(context);
        view.populateFromSharedPref(data);
    }
}
