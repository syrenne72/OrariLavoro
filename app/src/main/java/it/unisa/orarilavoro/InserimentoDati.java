package it.unisa.orarilavoro;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class InserimentoDati extends Fragment {
    public interface InvioDatiListener {
        int SALVA_DATI = 0;
        int MODIFICA_DATI = 1;
        int RIMUOVI_FRAMMENTO = 2;

        void onInvioDati(int codice, int dati[]);
    }

    /**
     * Classe che mi permette di ricevere dati dall'activity
     */
    public BroadcastReceiver receiverUpdateDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("kiwi", "BroadcastReceiver: ricevuti dei dati");

            int anno, mese, giorno, daOra, aOra, totale, _id = 0;
            boolean temp;

            anno = intent.getIntExtra("anno", 0);
            mese = intent.getIntExtra("mese", 0);
            giorno = intent.getIntExtra("giorno", 0);
            daOra = intent.getIntExtra("daOra", 0);
            aOra = intent.getIntExtra("aOra", 0);
            totale = intent.getIntExtra("totale", 0);
            _id = intent.getIntExtra("_id", -1);
            temp = intent.getBooleanExtra("temp", false);

            setEditText(anno, mese, giorno, daOra, aOra, totale, _id);

            btnIndietro.setVisibility(View.VISIBLE);
            if(!temp) {
                btnIndietro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        setEditText(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 7,
                                calendar.get(Calendar.HOUR_OF_DAY) + 2, calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2 - 7, -1);

                        btnIndietro.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                btnIndietro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onInvioDati(InvioDatiListener.RIMUOVI_FRAMMENTO, null);
                    }
                });
            }
        }
    };

    private TextView tvId;
    private EditText etAnno, etMese, etGiorno, etDaOra, etAOra, etTotale;
    private ImageButton btnIndietro;

    private InvioDatiListener listener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter("STRING_ID_FOR_BRODCAST");
        getActivity().registerReceiver(receiverUpdateDownload, filter);

        return inflater.inflate(R.layout.form_orario, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvId = getActivity().findViewById(R.id.tvId);
        etAnno = getActivity().findViewById(R.id.etAnno);
        etMese = getActivity().findViewById(R.id.etMese);
        etGiorno = getActivity().findViewById(R.id.etGiorno);
        etDaOra = getActivity().findViewById(R.id.etDaOra);
        etAOra = getActivity().findViewById(R.id.etAOra);
        etTotale = getActivity().findViewById(R.id.etTotale);
        btnIndietro = getActivity().findViewById(R.id.btnIndietro);

        /*Setto il bottone salva*/
        getActivity().findViewById(R.id.btnConferma).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int anno, mese, giorno, daOra, aOra, totale, _id;

                try {
                    anno = Integer.parseInt(etAnno.getText().toString());
                    mese = Integer.parseInt(etMese.getText().toString());
                    giorno = Integer.parseInt(etGiorno.getText().toString());
                    daOra = Integer.parseInt(etDaOra.getText().toString());
                    aOra = Integer.parseInt(etAOra.getText().toString());
                    totale = Integer.parseInt(etTotale.getText().toString());
                    _id = Integer.parseInt(tvId.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Inserire dati numerici", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(anno > 2100 || anno < 2000) {
                    Toast.makeText(getActivity().getApplicationContext(), "Inserire un anno valido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mese > 12 || mese < 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Inserire un mese valido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(giorno > 31 || giorno < 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Inserire un giorno valido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(daOra > 24 || daOra < 0 || aOra > 24 || aOra < 0 || daOra > aOra) {
                    Toast.makeText(getActivity().getApplicationContext(), "Inserire un orario valido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(totale > 24 || totale < 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Inserire un totale valido", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": elementi da salvare: " + anno + "/" + mese + "/" + giorno + " " + daOra + " - " + aOra + " tot. " + totale);

                int dati[] = {anno, mese, giorno, daOra, aOra, totale, _id};

                /*****************
                Verifico se devo inserire o modificare controllando se l'id è fittizio (-1) oppure è reale
                *****************/
                if(_id == -1)
                    listener.onInvioDati(InvioDatiListener.SALVA_DATI, dati);
                else
                    listener.onInvioDati(InvioDatiListener.MODIFICA_DATI, dati);
            }
        });

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

        Calendar calendar = Calendar.getInstance();
        setEditText(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 7,
                calendar.get(Calendar.HOUR_OF_DAY) + 2, calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2 - 7, -1);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (InvioDatiListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " c'è un problema col listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();

        /*Cancello il servizio di broadcast per ricevere i dati*/
        if (receiverUpdateDownload != null) {
            try {
                getActivity().unregisterReceiver(receiverUpdateDownload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /***/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
