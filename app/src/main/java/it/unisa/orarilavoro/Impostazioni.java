package it.unisa.orarilavoro;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//public class Impostazioni extends AppCompatActivity {
//    private EditText etInizio, etFine, etPausa, etNome;
//
//    private DbManager dbManager;
//    private Cursor crs;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.impostazioni);
//
//        etInizio = findViewById(R.id.etInizio);
//        etFine = findViewById(R.id.etFine);
//        etNome = findViewById(R.id.etNome);
//        etPausa = findViewById(R.id.etPausa);
//
//        dbManager = new DbManager(this);
//        crs = dbManager.findImpostazioni();
//
//        if(crs != null && crs.moveToNext()) {
//            etInizio.setText("" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO)));
//            etFine.setText("" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE)));
//            etPausa.setText("" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA)));
//            etNome.setText(crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_NOME)));
//        } else
//            Log.i("KIWIBUNNY", getClass().getSimpleName() + ": non ci sono dati");
//    }
//
//    public void confermaDati(View view) {
//        String n = etNome.getText().toString();
//        int i = 0, f = 0, p = 0;
//
//        if(n.length() == 0) {
//            Toast.makeText(getApplicationContext(), "Inserire tutti i dati", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            i = Integer.parseInt(etInizio.getText().toString());
//            f = Integer.parseInt(etFine.getText().toString());
//            p = Integer.parseInt(etPausa.getText().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Inserire tutti i dati", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(dbManager.saveImpostazioni(n, i, f, p))
//            Toast.makeText(getApplicationContext(), "Impostazioni salvate", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(getApplicationContext(), "Si è verificato un errore", Toast.LENGTH_SHORT).show();
//    }
//}

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Impostazioni extends AppCompatActivity {
    private EditText etInizio, etFine, etPausa, etNome;

    private DbManager dbManager;
    private Cursor crs;
    private TimePickerDialog pickerDaOra, pickerAOra, pickerPausa;

    private int oraInizio, oraFine, minutoInizio, minutoFine, oraPausa, minutoPausa;

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

        setEdittext();
    }

    /**
     * Inserisco i dati presenti nel database negli edittext e mostro l'orologio quando ci si clicca su
     */
    private void setEdittext() {
        if(crs != null && crs.moveToNext()) {
            oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO)) / 60;
            oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE)) / 60;
            oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA)) / 60;
            minutoInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO)) % 60;
            minutoFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE)) % 60;
            minutoPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA)) % 60;
            etInizio.setText(String.format("%02d:%02d", oraInizio, minutoInizio));
            etFine.setText(String.format("%02d:%02d", oraFine, minutoFine));
            etPausa.setText(String.format("%02d:%02d", oraPausa, minutoPausa));
            etNome.setText(crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_NOME)));
        } else
            Log.i("KIWIBUNNY", getClass().getSimpleName() + ": non ci sono dati");

        /*Setto l'orologio per la data di inizio del lavoro*/
        etInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDaOra = new TimePickerDialog(Impostazioni.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        etInizio.setText(String.format("%02d:%02d", i, i1));
                    }
                }, oraInizio, minutoInizio, true);
                pickerDaOra.show();
            }
        });

        /*Setto l'orologio per la data di fine del lavoro*/
        etFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerAOra = new TimePickerDialog(Impostazioni.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        etFine.setText(String.format("%02d:%02d", i, i1));
                    }
                }, oraFine, minutoFine, true);
                pickerAOra.show();
            }
        });

        /*Setto l'orologio per la pausa*/
        etPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerPausa = new TimePickerDialog(Impostazioni.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        etPausa.setText(String.format("%02d:%02d", i, i1));
                    }
                }, oraPausa, minutoPausa, true);
                pickerPausa.show();
            }
        });
    }

    public void confermaDati(View view) {
        String n = etNome.getText().toString();
        int i = 0, f = 0, p = 0;

        if(n.length() == 0) {
            Toast.makeText(getApplicationContext(), "Inserire tutti i dati", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String inizio = etInizio.getText().toString();
            String fine = etFine.getText().toString();
            String pausa = etPausa.getText().toString();

            String strOre[] = inizio.split(":");
            oraInizio = Integer.parseInt(strOre[0]);
            minutoInizio = Integer.parseInt(strOre[1]);

            strOre = fine.split(":");
            oraFine = Integer.parseInt(strOre[0]);
            minutoFine = Integer.parseInt(strOre[1]);

            strOre = pausa.split(":");
            oraPausa = Integer.parseInt(strOre[0]);
            minutoPausa = Integer.parseInt(strOre[1]);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Inserire tutti i dati", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dbManager.saveImpostazioni(n, oraInizio, oraFine, minutoInizio, minutoFine, oraPausa, minutoPausa))
            Toast.makeText(getApplicationContext(), "Impostazioni salvate", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Si è verificato un errore", Toast.LENGTH_SHORT).show();
    }
}
