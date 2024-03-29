//package it.unisa.orarilavoro;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.Fragment;
//import android.app.TimePickerDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import java.util.Calendar;
//
//public class InserimentoDati extends Fragment {
//    public interface InvioDatiListener {
//        int SALVA_DATI = 0;
//        int MODIFICA_DATI = 1;
//        int RIMUOVI_FRAMMENTO = 2;
//
//        void onInvioDati(int codice, Orario dati);
//    }
//
//    /**
//     * Classe che mi permette di ricevere dati dall'activity
//     */
//    public BroadcastReceiver receiverUpdateDownload = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d("kiwi", "BroadcastReceiver: ricevuti dei dati");
//
//            Orario orario;
//            boolean temp;
//
//            orario = (Orario) intent.getSerializableExtra("Orario");
//            temp = intent.getBooleanExtra("temp", false);
//
//            setEditText(orario);
//
//            btnIndietro.setVisibility(View.VISIBLE);
//            if(!temp) {
//                btnIndietro.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Calendar calendar = Calendar.getInstance();
//                        setEditText(new Orario(-1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 7, 0,
//                                calendar.get(Calendar.HOUR_OF_DAY) + 2, 0, calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2 - 7, 0));
//
//                        btnIndietro.setVisibility(View.INVISIBLE);
//                    }
//                });
//            } else {
//                btnIndietro.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        listener.onInvioDati(InvioDatiListener.RIMUOVI_FRAMMENTO, null);
//                    }
//                });
//            }
//        }
//    };
//
//    private TextView tvId;
//    private EditText etData, etDaOra, etAOra, etTotale;
//    private ImageButton btnIndietro;
//
//    //Widget per l'inserimento della data e degli orari
//    private DatePickerDialog picker;
//    private TimePickerDialog pickerDaOra, pickerAOra;
//
//    private InvioDatiListener listener = null;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        IntentFilter filter = new IntentFilter("STRING_ID_FOR_BRODCAST");
//        getActivity().registerReceiver(receiverUpdateDownload, filter);
//
//        return inflater.inflate(R.layout.form_orario, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        tvId = getActivity().findViewById(R.id.tvId);
//        etData = getActivity().findViewById(R.id.etData);
//        etDaOra = getActivity().findViewById(R.id.etDaOra);
//        etAOra = getActivity().findViewById(R.id.etAOra);
//        etTotale = getActivity().findViewById(R.id.etTotale);
//        btnIndietro = getActivity().findViewById(R.id.btnIndietro);
//
//        /*Setto il calendario per selezionare la data*/
//        etData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar cldr = Calendar.getInstance();
//                int day = cldr.get(Calendar.DAY_OF_MONTH);
//                int month = cldr.get(Calendar.MONTH);
//                int year = cldr.get(Calendar.YEAR);
//
//                picker = new DatePickerDialog(InserimentoDati.this.getContext(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        etData.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    }
//                }, year, month, day);
//                picker.show();
//            }
//        });
//
//        /*Setto l'orologio per la data di inizio del lavoro*/
//        etDaOra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickerDaOra = new TimePickerDialog(InserimentoDati.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        etDaOra.setText(String.format("%02d:%02d", i, i1));
//                    }
//                }, 7, 0, true);
//                pickerDaOra.show();
//            }
//        });
//
//        /*Setto l'orologio per la data di fine del lavoro*/
//        etAOra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickerAOra = new TimePickerDialog(InserimentoDati.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        etAOra.setText(String.format("%02d:%02d", i, i1));
//                    }
//                }, 19, 0, true);
//                pickerAOra.show();
//            }
//        });
//
//        /*Quando vengono inseriti gli orari di inizio e fine lavoro viene calcolato automaticamente il tempo di lavoro*/
//        etDaOra.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String da, a;
//
//                try {
//                    da = etDaOra.getText().toString();
//                    a = etAOra.getText().toString();
//                    etTotale.setText(Orario.calcoloOreTotali(da, a));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        etAOra.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String da, a;
//
//                try {
//                    da = etDaOra.getText().toString();
//                    a = etAOra.getText().toString();
//                    etTotale.setText(Orario.calcoloOreTotali(da, a));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        /*Fine settaggio automatico delle ore lavorative*/
//
//        /*Setto il bottone salva*/
//        getActivity().findViewById(R.id.btnConferma).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Orario orario = new Orario();
//
//                try {
//                    orario.setData(etData.getText().toString());
//                    orario.setDaOra(etDaOra.getText().toString());
//                    orario.setAOra(etAOra.getText().toString());
//                    orario.setTotale(etTotale.getText().toString());
//                    orario.setId(Integer.parseInt(tvId.getText().toString()));
//                } catch (NumberFormatException e) {
//                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                //Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": elementi da salvare: " + anno + "/" + mese + "/" + giorno + " " + daOra + " - " + aOra + " tot. " + totale);
//
//                /*Verifico se devo inserire o modificare controllando se l'id è fittizio (-1) oppure è reale*/
//                if(orario.id == -1)
//                    listener.onInvioDati(InvioDatiListener.SALVA_DATI, orario);
//                else
//                    listener.onInvioDati(InvioDatiListener.MODIFICA_DATI, orario);
//                /********************************************************************************************/
//            }
//        });
//        /*Fine settaggio bottone salva*/
//
//        Calendar calendar = Calendar.getInstance();
//        setEditText(new Orario(-1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 7, 0,
//                calendar.get(Calendar.HOUR_OF_DAY) + 2, 0, calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2 - 7, 0));
//    }
//
//    private void setEditText(Orario orario) {
//        etData.setText(orario.getData());
//        etDaOra.setText(orario.getDaOra());
//        etAOra.setText(orario.getAOra());
//        etTotale.setText(orario.getTotale());
//        tvId.setText(orario.id + "");
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        try {
//            listener = (InvioDatiListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " c'è un problema col listener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        /*Cancello il servizio di broadcast per ricevere i dati*/
//        if (receiverUpdateDownload != null) {
//            try {
//                getActivity().unregisterReceiver(receiverUpdateDownload);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        /***/
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
//}
package it.unisa.orarilavoro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class InserimentoDati extends Fragment {
    public interface InvioDatiListener {
        int SALVA_DATI = 0;
        int MODIFICA_DATI = 1;
        int RIMUOVI_FRAMMENTO = 2;

        void onInvioDati(int codice, Orario dati);
    }

    /**
     * Classe che mi permette di ricevere dati dall'activity
     */
    public BroadcastReceiver receiverUpdateDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("kiwi", "BroadcastReceiver: ricevuti dei dati");

            Orario orario;
            boolean temp;

            orario = (Orario) intent.getSerializableExtra("Orario");
            temp = intent.getBooleanExtra("temp", false);

            setEditText(orario);

            btnIndietro.setVisibility(View.VISIBLE);

            Log.d("kiwi", "BroadcastReceiver: valore di temp: " + temp);

            if(!temp) {
                btnIndietro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();

                        setEditText(new Orario(-1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 7, 0,
                                calendar.get(Calendar.HOUR_OF_DAY) + 2, 0, calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2 - 7, 0));

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

    private TextView tvId, tvData, tvDaOra, tvAOra, tvTotale;
    private ImageButton btnIndietro;

    //Widget per l'inserimento della data e degli orari
    private DatePickerDialog picker;
    private TimePickerDialog pickerDaOra, pickerAOra, pickerTotale;

    private InvioDatiListener listener = null;

    private DbManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter("STRING_ID_FOR_BRODCAST");
        getActivity().registerReceiver(receiverUpdateDownload, filter);

        return inflater.inflate(R.layout.form_orario_res, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbManager = new DbManager(this.getContext());

        tvId = getActivity().findViewById(R.id.tvId);
        tvData = getActivity().findViewById(R.id.etData);
        tvDaOra = getActivity().findViewById(R.id.etDaOra);
        tvAOra = getActivity().findViewById(R.id.etAOra);
        tvTotale = getActivity().findViewById(R.id.etTotale);
        btnIndietro = getActivity().findViewById(R.id.btnIndietro);

        /*Setto il calendario per selezionare la data*/
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(InserimentoDati.this.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvData.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        /*Carico le impostazioni predefinite per l'orario*/
        Cursor crs = dbManager.findImpostazioni();

        //Seleziono il giorno corrente
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        Log.d("kiwi", getClass().getSimpleName() + ": oggi è il giorno della settimana n°: " + dayOfWeek);

        int oraInizio = 7 * 60, oraFine = 18 * 60, oraPausa = 1 * 60;
        if(crs.moveToFirst()) {
            oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO));
            oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE));
            oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA));
        }
        //Recupero l'orario preimpostato relativo al giorno corrente
        /*if(crs.moveToFirst()) {
            switch (dayOfWeek) {
                case 1:
                    oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO_L));
                    oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE_L));
                    oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA_L));
                    break;
                case 2:
                    oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO_M));
                    oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE_M));
                    oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA_M));
                    break;
                case 3:
                    oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO_ME));
                    oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE_ME));
                    oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA_ME));
                    break;
                case 4:
                    oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO_G));
                    oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE_G));
                    oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA_G));
                    break;
                case 5:
                    oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO_V));
                    oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE_V));
                    oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA_V));
                    break;
                case 6:
                    oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO_S));
                    oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE_S));
                    oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA_S));
                    break;
                case 7:
                    oraInizio = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_INIZIO_D));
                    oraFine = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORA_FINE_D));
                    oraPausa = crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_PAUSA_D));
                    break;
            }
        }*/
        /************************************************/

        /*Setto l'orologio per la data di inizio del lavoro*/
        final int finalOraInizio = oraInizio;

        tvDaOra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDaOra = new TimePickerDialog(InserimentoDati.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvDaOra.setText(String.format("%02d:%02d", i, i1));
                    }
                }, finalOraInizio / 60, finalOraInizio % 60, true);
                pickerDaOra.show();
            }
        });

        /*Setto l'orologio per la data di fine del lavoro*/
        final int finalOraFine = oraFine;
        tvAOra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerAOra = new TimePickerDialog(InserimentoDati.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvAOra.setText(String.format("%02d:%02d", i, i1));
                    }
                }, finalOraFine / 60, finalOraFine % 60, true);
                pickerAOra.show();
            }
        });

        /*Setto l'orologio per le ore totali*/
        tvTotale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerTotale = new TimePickerDialog(InserimentoDati.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvTotale.setText(String.format("%02d:%02d", i, i1));
                    }
                }, 9, 0, true);
                pickerTotale.show();
            }
        });

        /*Quando vengono inseriti gli orari di inizio e fine lavoro viene calcolato automaticamente il tempo di lavoro*/
        final int finalOraPausa = oraPausa;
        tvDaOra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String da, a;

                try {
                    da = tvDaOra.getText().toString();
                    a = tvAOra.getText().toString();
                    tvTotale.setText(Orario.calcoloOreTotali(da, a, finalOraPausa));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvAOra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String da, a;

                try {
                    da = tvDaOra.getText().toString();
                    a = tvAOra.getText().toString();
                    tvTotale.setText(Orario.calcoloOreTotali(da, a, finalOraPausa));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /*Fine settaggio automatico delle ore lavorative*/

        /*Setto il bottone salva*/
        getActivity().findViewById(R.id.btnConferma).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Orario orario = new Orario();

                try {
                    orario.setData(tvData.getText().toString());
                    orario.setDaOra(tvDaOra.getText().toString());
                    orario.setAOra(tvAOra.getText().toString());
                    orario.setTotale(tvTotale.getText().toString());
                    orario.setId(Integer.parseInt(tvId.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": elementi da salvare: " + anno + "/" + mese + "/" + giorno + " " + daOra + " - " + aOra + " tot. " + totale);

                /*Verifico se devo inserire o modificare controllando se l'id è fittizio (-1) oppure è reale*/
                if(orario.id == -1)
                    listener.onInvioDati(InvioDatiListener.SALVA_DATI, orario);
                else
                    listener.onInvioDati(InvioDatiListener.MODIFICA_DATI, orario);
                /********************************************************************************************/
            }
        });
        /*Fine settaggio bottone salva*/

        Calendar calendar = Calendar.getInstance();
        setEditText(new Orario(-1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), oraInizio / 60, oraInizio % 60,
                oraFine / 60, oraFine % 60, (oraFine - oraInizio - oraPausa) / 60, (oraFine - oraInizio - oraPausa) % 60));
    }

    private void setEditText(Orario orario) {
        tvData.setText(orario.getData() + "");
        tvDaOra.setText(orario.getDaOra() + "");
        tvAOra.setText(orario.getAOra() + "");
        tvTotale.setText(orario.getTotale() + "");
        tvId.setText(orario.id + "");
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
