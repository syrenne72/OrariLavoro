package it.unisa.orarilavoro;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class Impostazioni extends AppCompatActivity {
    private TextView tvInizio, tvFine, tvPausa, tvNotifica;
    private EditText etNome;
    private Switch sNotifica;

    private DbManager dbManager;
    private Cursor crs;
    private TimePickerDialog pickerDaOra, pickerAOra, pickerPausa, pickerNotifica;
    private Intent intent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    private int oraInizio, oraFine, minutoInizio, minutoFine, oraPausa, minutoPausa, oraNotifica, minutoNotifica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impostazioni);

        tvInizio = findViewById(R.id.tvInizio);
        tvFine = findViewById(R.id.tvFine);
        etNome = findViewById(R.id.etNome);
        tvPausa = findViewById(R.id.tvPausa);
        tvNotifica = findViewById(R.id.tvNotifica);
        sNotifica = findViewById(R.id.sNotifica);

        dbManager = new DbManager(this);
        crs = dbManager.findImpostazioni();

        setView();

        intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    /**
     * Inserisco i dati presenti nel database negli edittext e mostro l'orologio quando ci si clicca su
     */
    private void setView() {
        if(crs != null && crs.moveToNext()) {
            etNome.setText(crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_NOME)));
            oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO)) / 60;
            oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE)) / 60;
            oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA)) / 60;
            oraNotifica = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_NOTIFICA)) / 60;
            minutoInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO)) % 60;
            minutoFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE)) % 60;
            minutoPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA)) % 60;
            minutoNotifica = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_NOTIFICA)) % 60;
            tvInizio.setText(String.format("%02d:%02d", oraInizio, minutoInizio));
            tvFine.setText(String.format("%02d:%02d", oraFine, minutoFine));
            tvPausa.setText(String.format("%02d:%02d", oraPausa, minutoPausa));

            boolean sValue = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_RICHIESTA_NOTIFICA)) == 1;
            sNotifica.setChecked(sValue);

            if(sValue) {
                tvNotifica.setVisibility(View.VISIBLE);
                tvNotifica.setText(String.format("%02d:%02d", oraNotifica, minutoNotifica));
            }

        } else
            Log.i("KIWIBUNNY", getClass().getSimpleName() + ": non ci sono dati");

        /*Setto l'orologio per la data di inizio del lavoro*/
        tvInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDaOra = new TimePickerDialog(Impostazioni.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvInizio.setText(String.format("%02d:%02d", i, i1));
                    }
                }, oraInizio, minutoInizio, true);
                pickerDaOra.show();
            }
        });

        /*Setto l'orologio per la data di fine del lavoro*/
        tvFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerAOra = new TimePickerDialog(Impostazioni.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvFine.setText(String.format("%02d:%02d", i, i1));
                    }
                }, oraFine, minutoFine, true);
                pickerAOra.show();
            }
        });

        /*Setto l'orologio per la pausa*/
        tvPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerPausa = new TimePickerDialog(Impostazioni.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvPausa.setText(String.format("%02d:%02d", i, i1));
                    }
                }, oraPausa, minutoPausa, true);
                pickerPausa.show();
            }
        });

        /*Setto l'orologio per l'orario di notifica*/
        tvNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerNotifica = new TimePickerDialog(Impostazioni.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvNotifica.setText(String.format("%02d:%02d", i, i1));
                    }
                }, oraNotifica, minutoNotifica, true);
                pickerNotifica.show();
            }
        });

        //Metodo per verificare se lo switch è attivo
        sNotifica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    tvNotifica.setVisibility(View.VISIBLE);
                } else {
                    tvNotifica.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void confermaDati(View view) {
        String n = etNome.getText().toString();

        if(n.length() == 0) {
            Toast.makeText(getApplicationContext(), "Inserire tutti i dati", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String inizio = tvInizio.getText().toString();
            String fine = tvFine.getText().toString();
            String pausa = tvPausa.getText().toString();

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

        //Se è stato inserito l'orario per la notifica, questo va salvato
        int ricNotifica = 0;

        if(tvNotifica.getVisibility() == View.VISIBLE)
            ricNotifica = 1;
        else {
            oraNotifica = 20;
            minutoNotifica = 0;
        }

        if(ricNotifica == 1) {
            try {
                String notifica = tvNotifica.getText().toString();

                String strNotifica[] = notifica.split(":");
                oraNotifica = Integer.parseInt(strNotifica[0]);
                minutoNotifica = Integer.parseInt(strNotifica[1]);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Inserire l'orario per la notifica", Toast.LENGTH_SHORT).show();
            }
        }
        //////////////////////////////////////////////////////////////

        if(dbManager.saveImpostazioni(n, oraInizio, oraFine, minutoInizio, minutoFine, oraPausa, minutoPausa, ricNotifica, oraNotifica, minutoNotifica))
            if(ricNotifica == 0)
                Toast.makeText(getApplicationContext(), "Impostazioni salvate", Toast.LENGTH_SHORT).show();
            else if(ricNotifica == 1) {
                //Verifico se settare la notifica oppure cancellarla
                if(crs != null && crs.moveToNext()) {
                    if (crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_RICHIESTA_NOTIFICA)) == 1)
                        Log.d("kiwi", "Creating alarm");
                    myAlarm();
                } else
                    alarmManager.cancel(pendingIntent);

                AlertDialog alertDialog = new AlertDialog.Builder(Impostazioni.this).create();
                alertDialog.setTitle("Notifica impostata");
                alertDialog.setMessage("La notifica è stata impostata correttamente\nL'orario selezionato potrebbe variare leggermente");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        else
            Toast.makeText(getApplicationContext(), "Si è verificato un errore", Toast.LENGTH_SHORT).show();
    }

    /**
     * Crea una notifica che ricorda all'utente di inserire l'orario giornaliero
     */
    public void myAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_NOTIFICA)) / 60);
        calendar.set(Calendar.MINUTE, crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_NOTIFICA)) % 60);

        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("kiwi", "Created Alarm");
        }
    }
}
