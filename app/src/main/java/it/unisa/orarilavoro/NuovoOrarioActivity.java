package it.unisa.orarilavoro;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NuovoOrarioActivity extends AppCompatActivity implements InserimentoDati.InvioDatiListener {
    private ListView lvPrimiDieciOrari;

    private DbManager dbManager;
    private FragmentManager fm;
    private InserimentoDati inserimentoDatiFragment;
    private CursorAdapter adapter;
    private Cursor cursorOrari;
    private Cursor cursorImpostazioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuovo_orario);

        lvPrimiDieciOrari = findViewById(R.id.lvPrimiDieciOrari);

        dbManager = new DbManager(this);
        cursorOrari = dbManager.primiDieciOrari();

       /*Carico il frammento per l'inserimento dati*/
        inserimentoDatiFragment = new InserimentoDati();
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flInserimento, inserimentoDatiFragment);
        //ft.addToBackStack(null);
        ft.commit();
        /******************************************/

        refreshListView();
    }

    /**
     * Aggiorna la listView con i nuovi dati
     */
    private void refreshListView() {
        Log.d("KIWI", getClass().getSimpleName() + ": Chiamato refreshListView");

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
                int anno, mese, giorno, daOra, daMinuto, aOra, aMinuto, oreTotali, minutiTotali, id = 0;

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
                daMinuto = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_MINUTO));
                aOra = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                aMinuto = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_MINUTO));
                oreTotali = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
                minutiTotali = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI));
                id = (int) crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID));

                /*Formattare una data*/
                tvData.setText(String.format("%02d/%02d/%04d", giorno, mese, anno));
                tvDaOra.setText(String.format("%02d:%02d", daOra, daMinuto));
                tvAOra.setText(String.format("%02d:%02d", aOra, aMinuto));
                tvTotale.setText(String.format("%02d:%02d", oreTotali, minutiTotali));
                tvId.setText(id + "");

                btnModifica.setTag(id);
                btnCancella.setTag(id);

                //Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": id dei dati caricati: " + id);

                /**
                 * Chiama una nuova activity che permette la modifica dell'orario
                 */
                btnModifica.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("STRING_ID_FOR_BRODCAST");

                        int id = (int) view.getTag();

                        Log.i("KIWIBUNNY", this.getClass().getSimpleName()  + ": si vuole modificare il dato " + id);

                        Cursor cursor = dbManager.findById((int) id);
                        cursor.moveToNext();

                        Orario orario = new Orario();

                        orario.id = (int) cursor.getLong(cursor.getColumnIndex(DatabaseStrings.FIELD_ID));
                        orario.anno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ANNO));
                        orario.mese = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_MESE));
                        orario.giorno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_GIORNO));
                        orario.daOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_DA_ORA));
                        orario.daMinuto = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_DA_MINUTO));
                        orario.aOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                        orario.aMinuto = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_A_MINUTO));
                        orario.oreTotali = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
                        orario.minutiTotali = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI));

                        intent.putExtra("Orario", orario);
                        sendBroadcast(intent);
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

    @Override
    public void onInvioDati(int codice, Orario dati) {
        if(codice == SALVA_DATI) {
            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + " Salvo i dati confermati");
            dbManager.save(dati);

            reload();
        } else if(codice == MODIFICA_DATI) {
            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + " Modifico i dati confermati");
            dbManager.modificaById(dati);

            reload();
        }
    }
}
