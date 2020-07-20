package it.unisa.orarilavoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    /**
     * Crea la nuova activity per l'inserimento di un orario di lavoro
      * @param view bottone cliccato
     */
    public void aggiungiUnOrario(View view) {
        Log.i("KIWIBUNNY", "Chiamato aggiungiUnOrario");

        //Avvio l'activity per l'inserimento di un nuovo orario
        Intent i = new Intent(getApplicationContext(), NuovoOrarioActivity.class);
        startActivity(i);
    }

    public void visualizzaOrari(View view) {
        Log.i("KIWIBUNNY", "Chiamato visualizzaOrari");

        //Avvio l'activity per l'inserimento di un nuovo orario
        Intent i = new Intent(getApplicationContext(), VisualizzaOrariActivity.class);
        startActivity(i);
    }

    public void impostazioni(View view) {
        Log.i("KIWIBUNNY", "Chiamato impostazioni");

        Intent i = new Intent(getApplicationContext(), Impostazioni.class);
        startActivity(i);
    }
}