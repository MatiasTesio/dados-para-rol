package matiastesio.dices.dicesscreen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import matiastesio.dices.R;
import matiastesio.dices.constants.Constants;
import matiastesio.dices.model.Master;

public class MainActivity extends AppCompatActivity implements DicesView {

    private EditText cantDados;                 private Button enviar;
    private Spinner spinner;                    private EditText msisdn;
    private EditText habilidad;                 private String msj = "";
    private String SENDER_MAIL;                 private Button cambiarMail;
    private DicesPresenter presenter;           private EditText jugador;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new DicesPresenter(this, new Master());
        populateUI();

        if(SENDER_MAIL == null){
            alertDialogMailChanger();
        }

        cambiarMail.setOnClickListener(cambiarMailHandleClick);
        enviar.setOnClickListener(enviarHandleClick);
    }

    View.OnClickListener cambiarMailHandleClick = (view) -> {alertDialogMailChanger();};

    View.OnClickListener enviarHandleClick = (view) -> {
            msj = "";
            if (areInputFieldsOK()) {
                presenter.saveContact(jugador.getText().toString(),msisdn.getText().toString(),SENDER_MAIL, view.getContext());
                presenter.rollDices(cantDados.getText().toString(),spinner.getSelectedItem().toString());
                sendMessage();
                sendWhatsappMsj();
            }
        };

    private boolean habilidadTextIsOK(){
        if((!(habilidad.getText().toString().equals(null))
                && !(habilidad.getText().toString().equals("")))
                && !(habilidad.getText().toString().equals("Ingrese la habilidad a tirar"))){
            return true;
        }
        return false;
    }

    private boolean jugadorTextIsOK(){
        if((!(jugador.getText().toString().equals(null))
                && !(jugador.getText().toString().equals("")))
                && !(jugador.getText().toString().equals("Ingrese el nombre del personaje"))){
            return true;
        }
        return false;
    }

    private boolean cantidadDadosIsOK(){
        if (((!(cantDados.getText().toString().equals(null))
                && !(cantDados.getText().toString().equals(""))) && SENDER_MAIL !=null)
                && !(cantDados.getText().toString().equals("Cantidad de dados"))){
            return true;
        }
        return false;
    }

    private boolean spinnerIsOK(){
        if (!(spinner.getSelectedItem().toString().equals(null))
                && !(spinner.getSelectedItem().toString().equals(""))){
            return true;
        }
        return false;
    }

    private boolean msisdnIsOK(){
        if ((!(msisdn.getText().toString().equals(null))
                && !(msisdn.getText().toString().equals("")))
                && (!(msisdn.getText().toString().equals("Seleccionar la cantidad de caras del dado")))){
            return true;
        }
        return false;
    }

    private boolean areInputFieldsOK(){
        if(habilidadTextIsOK()
                && jugadorTextIsOK()
                && cantidadDadosIsOK()
                && spinnerIsOK()
                && msisdnIsOK())
            return true;
        return false;
    }

    private void sendMessage() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        presenter.sendMail( jugador.getText().toString().toUpperCase(), habilidad.getText().toString().toUpperCase(),
                            cantDados.getText(), spinner.getSelectedItem().toString(), msj, SENDER_MAIL);
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

    private void sendWhatsappMsj(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "El jugador " + jugador.getText().toString().toUpperCase()
                + " para la habilidad "+habilidad.getText().toString().toUpperCase() + " Tiro "
                + cantDados.getText()
                + " dados de " + spinner.getSelectedItem().toString() + " caras, el resultado es: " + msj);
        sendIntent.putExtra("jid", "549" + msisdn.getText() + "@s.whatsapp.net");
        sendIntent.setPackage(Constants.WHATSAPP_PACKAGE);
        sendIntent.setType("text/plain");
        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            showError(Constants.ERROR_WHATSAPP);
        }
    }

    public void showError(String msj){
        Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_LONG).show();
    }

    public void setDicesValue(String dices){
        this.msj = dices;
    }

    private void alertDialogMailChanger(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(Constants.DIALOG_TITLE);
        View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.text_inpu_password,
                            findViewById(android.R.id.content), false);

        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            presenter.saveSenderMail(SENDER_MAIL = input.getText().toString(), MainActivity.this);
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {dialog.cancel();});

        builder.show();
    }

    private void populateUI(){
        enviar = findViewById(R.id.enviar_datos);
        msisdn = findViewById(R.id.msisdn);
        habilidad = findViewById(R.id.habilidad);
        cambiarMail = findViewById(R.id.reset_mail);
        cantDados = findViewById(R.id.numero_dados);
        spinner = findViewById(R.id.caras_dados);
        jugador = findViewById(R.id.jugador);

        presenter.getDataFromSharedPref(getApplicationContext());
    }

    public void populateFromSharedPref(List<String> data){
        this.jugador.setText(data.get(0));
        this.msisdn.setText(data.get(1));
        SENDER_MAIL=data.get(2);
    }
}
