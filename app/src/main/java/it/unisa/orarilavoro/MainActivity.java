package it.unisa.orarilavoro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        myAlarm();
    }

    /**
     * Crea la nuova activity per l'inserimento di un orario di lavoro
      * @param view bottone cliccato
     */
    public void aggiungiUnOrario(View view) {
        Log.i("KIWIBUNNY", "Chiamato aggiungiUnOrario");

        //Avvio l'activity per l'inserimento di un nuovo orario
        Intent i = new Intent(getApplicationContext(), NuovoOrarioActivity.class);
        startActivity(i);
    }

    public void visualizzaOrari(View view) {
        Log.i("KIWIBUNNY", "Chiamato visualizzaOrari");

        //Avvio l'activity per l'inserimento di un nuovo orario
        Intent i = new Intent(getApplicationContext(), VisualizzaOrariActivity.class);
        startActivity(i);
    }

    public void impostazioni(View view) {
        Log.i("KIWIBUNNY", "Chiamato impostazioni");

        Intent i = new Intent(getApplicationContext(), Impostazioni.class);
        startActivity(i);
    }

    /**
     * Crea una notifica che ricorda all'utente di inserire l'orario giornaliero
     */
    public void myAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);

        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("kiwi", "Created Alarm");
        }
    }
}