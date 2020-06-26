package it.unisa.orarilavoro;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Impostazioni extends AppCompatActivity {
    private EditText etInizio, etFine, etPausa, etNome;

    private DbManager dbManager;
    private Cursor crs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impostazioni);

        etInizio = findViewById(R.id.etInizio);
        etFine = findViewById(R.id.etFine);
        etNome = findViewById(R.id.etNome);
        etPausa = findViewById(R.id.etPausa);

        dbManager = new DbManager(this);
        crs = dbManager.findImpostazioni();

        if(crs != null && crs.moveToNext()) {
            etInizio.setText("" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO)));
            etFine.setText("" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE)));
            etPausa.setText("" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA)));
            etNome.setText(crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_NOME)));
        } else
            Log.i("KIWIBUNNY", getClass().getSimpleName() + ": non ci sono dati");
    }

    public void confermaDati(View view) {
        String n = etNome.getText().toString();
        int i = 0, f = 0, p = 0;

        if(n.length() == 0) {
            Toast.makeText(getApplicationContext(), "Inserire tutti i dati", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            i = Integer.parseInt(etInizio.getText().toString());
            f = Integer.parseInt(etFine.getText().toString());
            p = Integer.parseInt(etPausa.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Inserire tutti i dati", Toast.LENGTH_SHORT).show();
            return;
        }

        dbManager.saveImpostazioni(n, i, f, p);
    }
}
