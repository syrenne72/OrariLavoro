package it.unisa.orarilavoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        Intent i = new Intent(getApplicationContext(), AggiungiUnOrarioActivity.class);
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