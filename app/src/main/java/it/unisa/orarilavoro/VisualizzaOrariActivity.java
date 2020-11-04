package it.unisa.orarilavoro;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VisualizzaOrariActivity extends AppCompatActivity implements InserimentoDati.InvioDatiListener {
    private LinearLayout llRicerca, llAdvancedSearch;
    private ListView lvOrariTotali;
    private FrameLayout flInserimento;
    private TextView tvTotaleOre;
    private EditText etDaGiorno, etAGiorno, etNomePdf;
    private DatePickerDialog picker;
    private Spinner sOptions, sMonth;
    private ImageButton ibSearch;
    private CheckBox cAdvancedSearch;

    private DbManager dbManager;
    private CursorAdapter adapter;
   // private Cursor cursorOrari;
    private DbManager.MyResult myResult;
    private FragmentManager fm;
    private InserimentoDati inserimentoDatiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizza_orari);

        /*Richiedo i permessi di scrittura*/
        ActivityCompat.requestPermissions(VisualizzaOrariActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        llRicerca = findViewById(R.id.llRicerca);
        llAdvancedSearch = findViewById(R.id.llAdvancedSearch);
        lvOrariTotali = findViewById(R.id.lvOrariTotali);
        flInserimento = findViewById(R.id.flInserimento);
        tvTotaleOre = findViewById(R.id.tvTotaleOre);
        etDaGiorno = findViewById(R.id.etDaGiorno);
        etAGiorno = findViewById(R.id.etAGiorno);
        etNomePdf = findViewById(R.id.etNomePdf);
        sOptions = findViewById(R.id.sOptions);
        sMonth = findViewById(R.id.sMonth);
        ibSearch = findViewById(R.id.ibSearch);
        cAdvancedSearch = findViewById(R.id.cAdvancedSearch);

        dbManager = new DbManager(this);

        /*Inizializzo gli EditText per la ricerca dei dati*/
        inizializzaRicerca();

        /*Carico il frammento per l'inserimento dati*/
        inserimentoDatiFragment = new InserimentoDati();
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flInserimento, inserimentoDatiFragment);
        ft.hide(inserimentoDatiFragment);
        ft.commit();
        /***********/

        /*Preparing spinner for research*/
        final String timeStamp = new SimpleDateFormat("yyyyMM").format(Calendar.getInstance().getTime());

        ArrayAdapter<CharSequence> adapterMese = ArrayAdapter.createFromResource(this, R.array.mese_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMese.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sMonth.setAdapter(adapterMese);
        sMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("kiwi", "VisualizzaOrariActivity - OnCreate: " + " Select spinner's item: " + parentView.getItemAtPosition(position).toString());
                if(parentView.getItemAtPosition(position).toString().equalsIgnoreCase("Mostra tutto")) {
                    ArrayAdapter<CharSequence> adapterAnno = ArrayAdapter.createFromResource(getApplicationContext(), R.array.quantity_array, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapterAnno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    sOptions.setAdapter(adapterAnno);
                    sOptions.setSelection(2);
                } else {
                    ArrayAdapter<CharSequence> adapterAnno = ArrayAdapter.createFromResource(getApplicationContext(), R.array.anno_array, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapterAnno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    sOptions.setAdapter(adapterAnno);
                    sOptions.setSelection(getIndex(sOptions, timeStamp.substring(0, 4)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        sMonth.setSelection(Integer.parseInt(timeStamp.substring(4)));
        /******************/

        //Prendo tutti gli orari memorizzati
        myResult = dbManager.findByMonthAndYear(Integer.parseInt(timeStamp.substring(4)), Integer.parseInt(timeStamp.substring(0, 4)));
//        cursorOrari = myResult.getCursor();

        refreshListView();

        /*Mostro le ore totali di tutti i risultati disponibili*/
        tvTotaleOre.setText(myResult.getTotale());
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

    /**
     * Stampa un pdf delle informazioni mostrate sulla pagina
     * @param view
     */
    public void stampa(View view) {
        String stampa = "", cartella = "/Orari lavoro", nomePdf = "/Orari di lavoro";
        String da = etDaGiorno.getText().toString();
        String a = etAGiorno.getText().toString();
        boolean fineStampa = false;

        /*Se la directory non esiste, la creo*/
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + cartella);

        if(!directory.exists()) {
            directory.mkdirs();
        }
        /************************************/

        if(etNomePdf.getText().toString().length() != 0)
            nomePdf = "/" + etNomePdf.getText().toString();

        //Numero di pagine
        int pagine = myResult.getDatiTotali() / 30 + 1;

        //Spostamento x e y dell'inizio della frase
        int x = 20, y = 30;

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(400,647, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Paint myPaint = new Paint();

        Cursor cursorOrari = myResult.cursor;
        cursorOrari.moveToFirst();

        if(cursorOrari.isNull(0))
            return;

        myPage.getCanvas().drawText(myResult.type, x, y, myPaint);
        y += myPaint.descent() - myPaint.ascent() + 10;

        Log.i("kiwi", this.getClass().getSimpleName() + ": stampa: title: " + myResult.type);

        /*Stampa delle pagine*/
        for(int i = 0; i < pagine && !fineStampa; i++) {
            for (int j = 0; j < 30; j++) {
                int anno, mese, giorno, daOra, aOra, totale;

                anno = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_ANNO));
                mese = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_MESE));
                giorno = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_GIORNO));
                daOra = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_DA_ORA));
                aOra = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                totale = cursorOrari.getInt(cursorOrari.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));

                Log.d("KIWI", "VisualizzaOrariActivity: " + i + 1 + "° pagina " + giorno + "/" + mese + "/" + anno);

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
        myPage.getCanvas().drawText(("Ore totali: " + tvTotaleOre.getText()), x, y + 10, myPaint);
        myPdfDocument.finishPage(myPage);

        /*******/

        String myFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + cartella + nomePdf + ".pdf";
        File myFile = new File(myFilePath);
        int i = 0;

        while(i >= 0) {
            if (myFile.exists()) {
                i++;
                myFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + cartella + nomePdf + " (" + i + ").pdf";
                myFile = new File(myFilePath);
            } else
                break;
        }

        /*Salvo il pdf*/
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Si è verificato un errore durante la creazione del PDF", Toast.LENGTH_SHORT).show();
            return;
        }
        /************/

        myPdfDocument.close();
        Toast.makeText(getApplicationContext(), "PDF creato in " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/Orari lavoro", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mostra il pannello per la ricerca avanzata
     * @param view la checkbox cliccata
     */
    public void onClickAdvancedSearch(View view) {
        CheckBox c = (CheckBox) view;

        if(c.isChecked()) {
            llAdvancedSearch.setVisibility(View.VISIBLE);
            ibSearch.setVisibility(View.INVISIBLE);
            llRicerca.setBackgroundColor(getResources().getColor(R.color.vlGrigio));

            //Create space for new linear layout about advanced search
            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.llAdvancedSearch);
            params.addRule(RelativeLayout.ABOVE, R.id.llTotaleOre);
            lvOrariTotali.setLayoutParams(params);
        } else {
            llAdvancedSearch.setVisibility(View.INVISIBLE);
            ibSearch.setVisibility(View.VISIBLE);
            llRicerca.setBackgroundColor(getResources().getColor(R.color.lRosso));

            //Remove space for new linear layout about advanced search
            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.cAdvancedSearch);
            params.addRule(RelativeLayout.ABOVE, R.id.llTotaleOre);
            lvOrariTotali.setLayoutParams(params);
        }
    }

    /**
     * Cerca i dati inclusi tra le due date
     * @param view
     */
    public void cercaDati(View view) {
        String dataDa = etDaGiorno.getText().toString();
        String dataA = etAGiorno.getText().toString();

        if(dataDa.length() == 0 || dataA.length() == 0) {
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

        myResult = dbManager.findBetweenTwoDates(daG, daM, daA, aG, aM, aA);
        tvTotaleOre.setText(myResult.getTotale());
        refreshListView();
    }

    /**
     * Cerca i dati del mese e dell'anno selezionato con gli spinner
     * @param view
     */
    public void searchSpinnerData(View view) {
        int month;
        try {
            month = sMonth.getSelectedItemPosition();

            if(month == 0 && !sOptions.getSelectedItem().toString().equalsIgnoreCase("tutti")) {
                Log.d("KIWI", "VisualizzaOrariActivity - searchSpinnerData: " + " searching last: " + sOptions.getSelectedItem().toString().substring(7));

                myResult = dbManager.findAll(Integer.parseInt(sOptions.getSelectedItem().toString().substring(7)));
            } else if(sOptions.getSelectedItem().toString().equalsIgnoreCase("tutti"))
                myResult = dbManager.findAll(1000);
            else
                myResult = dbManager.findByMonthAndYear(month, Integer.parseInt(sOptions.getSelectedItem().toString()));

            tvTotaleOre.setText(myResult.getTotale());
            refreshListView();
        } catch (IllegalStateException e) {
            Log.d("KIWI", "VisualizzaOrariActivity - searchSpinnerData: " + " problem with get month and year: " + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Inserire il mese e l'anno per la ricerca", Toast.LENGTH_SHORT).show();
        } catch (CursorIndexOutOfBoundsException c) {
            Log.d("KIWI", "VisualizzaOrariActivity - searchSpinnerData: no data found");
            Toast.makeText(getApplicationContext(), "Nessun dato trovato", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Refresh listview with new data
     */
    private void refreshListView() {
        adapter = new CursorAdapter(this, myResult.cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                View v = getLayoutInflater().inflate(R.layout.list_item, null);
                return v;
            }

            @Override
            public void bindView(View view, Context context, Cursor crs) {
                final TextView tvData, tvDaOra, tvAOra, tvTotale, tvId;
                ImageButton btnModifica, btnCancella;
                int anno, mese, giorno, daOra, daMinuto, aOra, aMinuto, oreTotali, minutiTotali, id;

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

                Log.i("kiwi", this.getClass().getSimpleName() + ": id dei dati caricati: " + id);

                /**
                 * Chiama una nuova activity che permette la modifica dell'orario
                 */
                btnModifica.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.show(inserimentoDatiFragment);
                        ft.commit();

                        llRicerca.setVisibility(View.INVISIBLE);

                        Intent intent = new Intent("STRING_ID_FOR_BRODCAST");
                        int id = (int) view.getTag();

                        Log.i("kiwi", "VisualizzaOrari - btnModifica - onClick: si vuole modificare il dato " + id);

                        Cursor cursor = dbManager.findById((int) id);
                        cursor.moveToNext();

                        Orario orario = new Orario();

                        orario.id = (int) cursor.getLong(cursor.getColumnIndex(DatabaseStrings.FIELD_ID));
                        orario.anno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ANNO));
                        orario.mese = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_MESE));
                        orario.giorno = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_GIORNO));
                        orario.daOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_DA_ORA));
                        orario.aOra = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_A_ORA));
                        orario.oreTotali = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));

                        intent.putExtra("temp", true);
                        intent.putExtra("Orario", orario);

                        sendBroadcast(intent);
                    }
                });

                btnCancella.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = (int) view.getTag();

                        Log.i("kiwi", this.getClass().getSimpleName()  + ": si vuole eliminare il dato " + id);

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

    private void reload() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onInvioDati(int codice, Orario dati) {
        if(codice == MODIFICA_DATI) {
            dbManager.modificaById(dati);

            reload();
        } else if(codice == RIMUOVI_FRAMMENTO) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(inserimentoDatiFragment);
            ft.commit();
        }

        llRicerca.setVisibility(View.VISIBLE);
    }

    /**
     * Define what happens if back button is pressed
     */
    @Override
    public void onBackPressed() {
        if(cAdvancedSearch.isChecked()) {
            cAdvancedSearch.setChecked(false);
            cAdvancedSearch.callOnClick();
        } else if(llRicerca.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else {
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(inserimentoDatiFragment);
            ft.commit();

            llRicerca.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Utility for spinner
     * @param spinner spinner for setting default item selected
     * @param myString string to set by default
     * @return default value, 0
     */
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}
