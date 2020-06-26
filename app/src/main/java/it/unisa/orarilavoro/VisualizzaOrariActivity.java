package it.unisa.orarilavoro;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class VisualizzaOrariActivity extends AppCompatActivity {
    private ListView lvOrariTotali;
    private TextView tvTotaleOre;
    private EditText etDaGiorno, etAGiorno;
    private DatePickerDialog picker;

    private DbManager dbManager;
    private CursorAdapter adapter;
    private Cursor cursorOrari;
    private DbManager.MyResult myResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizza_orari);

        /*Richiedo i permessi di scrittura*/
        ActivityCompat.requestPermissions(VisualizzaOrariActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        lvOrariTotali = findViewById(R.id.lvOrariTotali);
        tvTotaleOre = findViewById(R.id.tvTotaleOre);
        etDaGiorno = findViewById(R.id.etDaGiorno);
        etAGiorno = findViewById(R.id.etAGiorno);

        dbManager = new DbManager(this);

        /*Inizializzo gli EditText per la ricerca dei dati*/
        inizializzaRicerca();

        //Prendo tutti gli orari memorizzati
        myResult = dbManager.findAll();
        cursorOrari = myResult.getCursor();

        refreshListView();

        /*Mostro le ore totali di tutti i risultati disponibili*/
        tvTotaleOre.setText("" + myResult.getTot());
    }

    /**
     * Stampa un pdf delle informazioni mostrate sulla pagina
     * @param view
     */
    public void stampa(View view) {
        String stampa = "";
        String da = etDaGiorno.getText().toString();
        String a = etAGiorno.getText().toString();
        boolean fineStampa = false;

        //Numero di pagine
        int pagine = myResult.getDatiTotali() / 30 + 1;

        //Spostamento x e y dell'inizio della frase
        int x = 20, y = 30;

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(400,647, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Paint myPaint = new Paint();

        if(da.length() == 0 || a.length() == 0)
            stampa = "ORARI DI LAVORO FINO AL " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
        else
            stampa = "ORARI DI LAVORO DAL " + da + " AL " + a;

        cursorOrari.moveToFirst();

        myPage.getCanvas().drawText(stampa, x, y, myPaint);
        y += myPaint.descent() - myPaint.ascent() + 10;

        /*Stampa delle pagine*/
        for(int i = 0; i < pagine; i++) {
            for (int j = 0; j < 30; j++) {
                int anno, mese, giorno, daOra, aOra, totale;

                anno = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_ANNO));
                mese = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_MESE));
                giorno = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_GIORNO));
                daOra = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_DA_ORA));
                aOra = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                totale = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));

                stampa = "Data: " + String.format("%02d/%02d/%04d", giorno, mese, anno) + "   Dalle ore: " + daOra + ":00    Alle ore: " + aOra + ":00    Totale: " + totale + "\n";

                myPage.getCanvas().drawText(stampa, x, y, myPaint);

                //Calcolo lo spostamento verticale da cui partire per la prossima scritta
                y += myPaint.descent() - myPaint.ascent();

                //Passo al prossimo dato
                if (!cursorOrari.moveToNext()) {
                    fineStampa = !fineStampa;
                    break;
                }
            }

            if (!fineStampa) {
                myPdfDocument.finishPage(myPage);
                myPage = myPdfDocument.startPage(myPageInfo);
                myPaint = new Paint();
                x = 20;
                y = 30;
            }
        }

        //Stampo le ore totali
        myPage.getCanvas().drawText(("Ore totali: " + myResult.getTot()), x, y + 10, myPaint);
        myPdfDocument.finishPage(myPage);

        /*******/

        String myFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/Orari_di_lavoro.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Si è verificato un errore durante la creazione del PDF", Toast.LENGTH_SHORT).show();
            return;
        }

        myPdfDocument.close();
        Toast.makeText(getApplicationContext(), "PDF creato in " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Cerca i dati inclusi tra le due date
     * @param view
     */
    public void cercaDati(View view) {
        String dataDa = etDaGiorno.getText().toString();
        String dataA = etAGiorno.getText().toString();

        if(dataDa == null || dataA == null) {
            Toast.makeText(getApplicationContext(), "Inserire le date per la ricerca", Toast.LENGTH_SHORT).show();
            return;
        }

        /*Ottengo gli interi rappresentanti la data dall'input*/
        int daG, aG, daM, aM, daA, aA;

        String strDa[] = dataDa.split("/");
        daG = Integer.parseInt(strDa[0]);
        daM = Integer.parseInt(strDa[1]);
        daA = Integer.parseInt(strDa[2]);

        String strA[] = dataA.split("/");
        aG = Integer.parseInt(strA[0]);
        aM = Integer.parseInt(strA[1]);
        aA = Integer.parseInt(strA[2]);

        /*Controllo che l'intervallo di date sia valido*/
        if(aA < daA) {
            Toast.makeText(getApplicationContext(), "Inserire intervallo di date valido", Toast.LENGTH_SHORT).show();
            return;
        } else if(aM < daM) {
            Toast.makeText(getApplicationContext(), "Inserire intervallo di date valido", Toast.LENGTH_SHORT).show();
            return;
        } else if(aG < daG) {
            Toast.makeText(getApplicationContext(), "Inserire intervallo di date valido", Toast.LENGTH_SHORT).show();
            return;
        }

        DbManager.MyResult myResult = dbManager.findBetweenTwoDates(daG, daM, daA, aG, aM, aA);
        cursorOrari = myResult.getCursor();

        tvTotaleOre.setText(myResult.getTot() + "");
        refreshListView();
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
                /*****/

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

                        Intent i = new Intent(getApplicationContext(), ModificaOrarioActivity.class);
                        i.putExtra("anno", anno);
                        i.putExtra("mese", mese);
                        i.putExtra("giorno", giorno);
                        i.putExtra("daOra", daOra);
                        i.putExtra("aOra", aOra);
                        i.putExtra("totale", totale);
                        i.putExtra("id", _id);

                        startActivityForResult(i, 0);
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

        lvOrariTotali.setAdapter(adapter);
    }

    /**
     * Si occupa di inizializzare gli EditText per la ricerca dei dati
     */
    private void inizializzaRicerca() {
        etDaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(VisualizzaOrariActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDaGiorno.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        etAGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(VisualizzaOrariActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etAGiorno.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    private void reload() {
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reload();
    }
}
