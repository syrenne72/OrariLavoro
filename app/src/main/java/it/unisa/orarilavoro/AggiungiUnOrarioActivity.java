package it.unisa.orarilavoro;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AggiungiUnOrarioActivity extends AppCompatActivity {
    private LinearLayout llFormOrario, llAggiungi, llModifica;
    private TextView tvId;
    private EditText etAnno, etMese, etGiorno, etDaOra, etAOra, etTotale;
    private ListView lvPrimiDieciOrari;

    private DbManager dbManager;
    private CursorAdapter adapter;
    private Cursor cursorOrari;
    private Cursor cursorImpostazioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_orario);

        llFormOrario = findViewById(R.id.llFormOrario);
        llAggiungi = findViewById(R.id.llAggiungi);
        llModifica = findViewById(R.id.llModifica);
        tvId = findViewById(R.id.tvId);
        etAnno = findViewById(R.id.etAnno);
        etMese = findViewById(R.id.etMese);
        etGiorno = findViewById(R.id.etGiorno);
        etDaOra = findViewById(R.id.etDaOra);
        etAOra = findViewById(R.id.etAOra);
        etTotale = findViewById(R.id.etTotale);
        lvPrimiDieciOrari = findViewById(R.id.lvPrimiDieciOrari);

        /*Quando vengono inseriti gli orari di inizio e fine lavoro viene calcolato automaticamente il tempo di lavoro*/
        etDaOra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int da = 0, a = 0;

                try {
                    da = Integer.parseInt(etDaOra.getText().toString());
                    a = Integer.parseInt(etAOra.getText().toString());

                    etTotale.setText("" + (a - da - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        etAOra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int da = 0, a = 0;

                try {
                    da = Integer.parseInt(etDaOra.getText().toString());
                    a = Integer.parseInt(etAOra.getText().toString());

                    etTotale.setText("" + (a - da));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dbManager = new DbManager(this);
        cursorImpostazioni = dbManager.findImpostazioni();
        cursorOrari = dbManager.primiDieciOrari();

        if(cursorImpostazioni == null || !cursorImpostazioni.moveToNext())
            /*Inizializzo il form dell'orario con i dati di oggi preimpostati*/
            setEditText(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 8, 18, 9, -1);
        else {
            int totale = cursorImpostazioni.getInt(cursorImpostazioni.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE))
                    - cursorImpostazioni.getInt(cursorImpostazioni.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO))
                    - cursorImpostazioni.getInt(cursorImpostazioni.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA));

            setEditText(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1,
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    cursorImpostazioni.getInt(cursorImpostazioni.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO)),
                    cursorImpostazioni.getInt(cursorImpostazioni.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE)), totale, -1);
        }

        refreshListView();
    }

    public void confermaDati(View view) {
        int anno = 0, mese, giorno, daOra, aOra, totale = 0;

        try {
            anno = Integer.parseInt(etAnno.getText().toString());
            mese = Integer.parseInt(etMese.getText().toString());
            giorno = Integer.parseInt(etGiorno.getText().toString());
            daOra = Integer.parseInt(etDaOra.getText().toString());
            aOra = Integer.parseInt(etAOra.getText().toString());
            totale = Integer.parseInt(etTotale.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Inserire dati numerici", Toast.LENGTH_SHORT).show();
            return;
        }

        if(anno > 2100 || anno < 2000) {
            Toast.makeText(getApplicationContext(), "Inserire un anno valido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mese > 12 || mese < 1) {
            Toast.makeText(getApplicationContext(), "Inserire un mese valido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(giorno > 31 || giorno < 1) {
            Toast.makeText(getApplicationContext(), "Inserire un giorno valido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(daOra > 24 || daOra < 0 || aOra > 24 || aOra < 0 || daOra > aOra) {
            Toast.makeText(getApplicationContext(), "Inserire un orario valido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(totale > 24 || totale < 0) {
            Toast.makeText(getApplicationContext(), "Inserire un totale valido", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": elementi da salvare: " + anno + "/" + mese + "/" + giorno + " " + daOra + " - " + aOra + " tot. " + totale);

        dbManager.save(anno, mese, giorno, daOra, aOra, totale);

        reload();
    }

    public void confermaDatiModificati(View view) {
        int anno, mese, giorno, daOra, aOra, totale, _id = 0;

        anno = Integer.parseInt(etAnno.getText().toString());
        mese = Integer.parseInt(etMese.getText().toString());
        giorno = Integer.parseInt(etGiorno.getText().toString());
        daOra = Integer.parseInt(etDaOra.getText().toString());
        aOra = Integer.parseInt(etAOra.getText().toString());
        totale = Integer.parseInt(etTotale.getText().toString());
        _id = Integer.parseInt(tvId.getText().toString());

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": elementi da modificare: " + anno + "/" + mese + "/" + giorno + " " + daOra + " - " + aOra + " tot. " + totale);

        dbManager.modificaById(_id, anno, mese, giorno, daOra, aOra, totale);

        reload();
    }

    public void indietro(View view) {
        setEditText(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 7, Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2, Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2 - 7, 0);
        llAggiungi.setVisibility(View.VISIBLE);
        llModifica.setVisibility(View.INVISIBLE);
        llFormOrario.setBackgroundColor(getResources().getColor(R.color.vlGrigio));
    }

    private void setEditText(int anno, int mese, int giorno, int daOra, int aOra, int totale, int _id) {
        etAnno.setText(anno + "");
        etMese.setText(mese + "");
        etGiorno.setText(giorno + "");
        etDaOra.setText(daOra + "");
        etAOra.setText(aOra + "");
        etTotale.setText(totale + "");
        tvId.setText(_id + "");
    }

    /**
     * Aggiorna la listView con i nuovi dati
     */
    private void refreshListView() {
        adapter = new CursorAdapter(this, cursorOrari, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                View v = getLayoutInflater().inflate(R.layout.list_item, null);
                return v;
            }

            @Override
            public void bindView(View view, Context context, Cursor crs) {
                final TextView tvData, tvDaOra, tvAOra, tvTotale, tvId;
                ImageButton btnModifica, btnCancella;
                int anno, mese, giorno, daOra, aOra, totale, id = 0;

                tvData = view.findViewById(R.id.tvData);
                tvDaOra = view.findViewById(R.id.tvDaOra);
                tvAOra = view.findViewById(R.id.tvAOra);
                tvTotale = view.findViewById(R.id.tvTotale);
                tvId = view.findViewById(R.id.tvId);
                btnModifica = view.findViewById(R.id.btnModifica);
                btnCancella = view.findViewById(R.id.btnCancella);

                anno = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ANNO));
                mese = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE));
                giorno = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO));
                daOra = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_ORA));
                aOra = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                totale = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
                id = (int) crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID));

                /*Formattare una data*/
                tvData.setText(String.format("%02d/%02d/%04d", giorno, mese, anno));
                tvDaOra.setText(daOra + ":00");
                tvAOra.setText(aOra + ":00");
                tvTotale.setText(totale + "");
                tvId.setText(id + "");

                btnModifica.setTag(id);
                btnCancella.setTag(id);

                Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": id dei dati caricati: " + id);

                /**
                 * Chiama una nuova activity che permette la modifica dell'orario
                 */
                btnModifica.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = (int) view.getTag();

                        Log.i("KIWIBUNNY", this.getClass().getSimpleName()  + ": si vuole modificare il dato " + id);

                        Cursor cursor = dbManager.findById((int) id);
                        cursor.moveToNext();

                        int anno, mese, giorno, daOra, aOra, totale, _id = 0;

                        anno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ANNO));
                        mese = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_MESE));
                        giorno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_GIORNO));
                        daOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_DA_ORA));
                        aOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                        totale = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
                        _id = (int) cursor.getLong(cursor.getColumnIndex(DatabaseStrings.FIELD_ID));

                        setEditText(anno, mese, giorno, daOra, aOra, totale, _id);

                        llFormOrario.setBackgroundColor(getApplicationContext().getResources().getColor((R.color.vlRosso)));
                        llModifica.setVisibility(View.VISIBLE);
                        llAggiungi.setVisibility(View.INVISIBLE);
                    }
                });

                btnCancella.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = (int) view.getTag();

                        Log.i("KIWIBUNNY", this.getClass().getSimpleName()  + ": si vuole eliminare il dato " + id);

                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("");
                        alertDialog.setMessage("Sei sicuro di volerlo eliminare?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sì", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dbManager.delete((int) id);
                                reload();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });
            }
        };

        lvPrimiDieciOrari.setAdapter(adapter);
    }

    private void reload() {
        finish();
        startActivity(getIntent());
    }
}
