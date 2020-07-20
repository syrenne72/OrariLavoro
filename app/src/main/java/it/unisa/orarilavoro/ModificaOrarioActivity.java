package it.unisa.orarilavoro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModificaOrarioActivity extends AppCompatActivity {
    private TextView tvId;
    private EditText etAnno, etMese, etGiorno, etDaOra, etAOra, etTotale;

    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_orario);

        /*tvId = findViewById(R.id.tvId);
        etAnno = findViewById(R.id.etAnno);
        etMese = findViewById(R.id.etMese);
        etGiorno = findViewById(R.id.etGiorno);
        etDaOra = findViewById(R.id.etDaOra);
        etAOra = findViewById(R.id.etAOra);
        etTotale = findViewById(R.id.etTotale);

        dbManager = new DbManager(this);

        Intent i = getIntent();
        tvId.setText(i.getIntExtra("id", 0) + "");
        etAnno.setText(i.getIntExtra("anno", 0) + "");
        etMese.setText(i.getIntExtra("mese", 0) + "");
        etGiorno.setText(i.getIntExtra("giorno", 0) + "");
        etDaOra.setText(i.getIntExtra("daOra", 0) + "");
        etAOra.setText(i.getIntExtra("aOra", 0) + "");
        etTotale.setText(i.getIntExtra("totale", 0) + "");*/
    }

    /*public void confermaDatiModificati(View view) {
        int anno = 0, mese, giorno, daOra, aOra, totale = 0, _id;

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

        _id = Integer.parseInt(tvId.getText().toString());

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": elementi da modificare: " + anno + "/" + mese + "/" + giorno + " " + daOra + " - " + aOra + " tot. " + totale);

        dbManager.modificaById(_id, anno, mese, giorno, daOra, aOra, totale);

        super.onBackPressed();
    }*/

    public void indietro(View view) {
        super.onBackPressed();
    }

    private void reload() {
        finish();
        startActivity(getIntent());
    }
}
