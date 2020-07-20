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

                        int anno, mese, giorno, daOra, aOra, totale, _id = 0;

                        anno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ANNO));
                        mese = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_MESE));
                        giorno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_GIORNO));
                        daOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_DA_ORA));
                        aOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                        totale = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
                        _id = (int) cursor.getLong(cursor.getColumnIndex(DatabaseStrings.FIELD_ID));

                        intent.putExtra("anno",anno);
                        intent.putExtra("mese", mese);
                        intent.putExtra("giorno", giorno);
                        intent.putExtra("daOra", daOra);
                        intent.putExtra("aOra", aOra);
                        intent.putExtra("totale", totale);
                        intent.putExtra("_id", id);

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
    public void onInvioDati(int codice, int[] dati) {
        if(codice == SALVA_DATI) {
            int anno = dati[0], mese = dati[1], giorno = dati[2], daOra = dati[3], aOra = dati[4], totale = dati[5];

            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + " Salvo i dati confermati");
            dbManager.save(anno, mese, giorno, daOra, aOra, totale);

            reload();
        } else if(codice == MODIFICA_DATI) {
            int anno = dati[0], mese = dati[1], giorno = dati[2], daOra = dati[3], aOra = dati[4], totale = dati[5], _id = dati[6];

            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + " Modifico i dati confermati");
            dbManager.modificaById(_id, anno, mese, giorno, daOra, aOra, totale);

            reload();
        }
    }
}
