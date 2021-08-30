package it.unisa.orarilavoro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME = "ORARIDILAVORO";

    public DBhelper(Context context) {
        super(context, DBNAME, null, 10);
    }

    /**
     * Creo la tabella degli orari
     * @param db il database che conterrà la tabella
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String q= "CREATE TABLE " + DatabaseStrings.TBL_NAME +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_ANNO + " INT," +
                DatabaseStrings.FIELD_MESE+ " INT," +
                DatabaseStrings.FIELD_GIORNO + " INT," +
                DatabaseStrings.FIELD_DA_ORA + " INT," +
                DatabaseStrings.FIELD_DA_MINUTO + " INT," +
                DatabaseStrings.FIELD_A_ORA + " INT," +
                DatabaseStrings.FIELD_A_MINUTO + " INT," +
                DatabaseStrings.FIELD_ORE_TOTALI + " INT," +
                DatabaseStrings.FIELD_MINUTI_TOTALI + " INT)";

        db.execSQL(q);

        q= "CREATE TABLE " + DatabaseStrings.TBL_NAME_IMPOSTAZIONI +
                " (" + DatabaseStrings.FIELD_NOME + " STRING PRIMARY KEY," +
                DatabaseStrings.FIELD_ORA_INIZIO + " INT," +
                DatabaseStrings.FIELD_ORA_FINE+ " INT," +
                DatabaseStrings.FIELD_ORE_PAUSA + " INT," +
                DatabaseStrings.FIELD_RICHIESTA_NOTIFICA + " INT," +
                DatabaseStrings.FIELD_ORA_NOTIFICA + " INT," +
                DatabaseStrings.FIELD_MINUTI_NOTIFICA + " INT)";

        db.execSQL(q);

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": creato database");
    }

    //Aggiunge la colonna per la richiesta della notifica
    //Il valore 0 indica che la notifica non è richiesta, il valore 1 indica che la notifica è richiesta
    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_RICHIESTA_NOTIFICA + " INTEGER DEFAULT 0";

    //Aggiunge la colonna per l'ora di invio della notifica
    private static final String DATABASE_ALTER_TEAM_2 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_NOTIFICA + " INTEGER DEFAULT 20";

    //Aggiunge la colonna per i minuti per l'invio della notifica
    private static final String DATABASE_ALTER_TEAM_3 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_MINUTI_NOTIFICA + " INTEGER DEFAULT 0";

    /**
     * Viene eseguito quando c'è un aggiornamento del database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 9) {
            db.execSQL(DATABASE_ALTER_TEAM_1);
            db.execSQL(DATABASE_ALTER_TEAM_2);
        }
        if (oldVersion < 10) {
            db.execSQL(DATABASE_ALTER_TEAM_3);
        }
    }
}
