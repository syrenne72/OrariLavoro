package it.unisa.orarilavoro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Cursor crs;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_res);

        dbManager = new DbManager(this);
        crs = dbManager.findImpostazioni();
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