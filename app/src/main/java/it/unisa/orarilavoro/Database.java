package it.unisa.orarilavoro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME = "ORARIDILAVORO";

    public DBhelper(Context context) {
        super(context, DBNAME, null, 11);
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

    /*Aggiungo le colonne indicanti gli orari settimanali di inizio, fine e pausa*/
    private static final String DATABASE_ALTER_TEAM_4 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_INIZIO_L + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_5 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_INIZIO_M + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_6 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_INIZIO_ME + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_7 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_INIZIO_G + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_8 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_INIZIO_V + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_9 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_INIZIO_S + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_10 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_INIZIO_D + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_11 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_FINE_L + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_12 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_FINE_M + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_13 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_FINE_ME + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_14 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_FINE_G + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_15 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_FINE_V + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_16 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_FINE_S + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_17 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORA_FINE_D + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_18 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORE_PAUSA_L + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_19 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORE_PAUSA_M + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_20 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORE_PAUSA_ME + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_21 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORE_PAUSA_G + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_22 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORE_PAUSA_V + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_23 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORE_PAUSA_S + " INTEGER DEFAULT 0";

    private static final String DATABASE_ALTER_TEAM_24 = "ALTER TABLE "
            + DatabaseStrings.TBL_NAME_IMPOSTAZIONI + " ADD COLUMN "
            + DatabaseStrings.FIELD_ORE_PAUSA_D + " INTEGER DEFAULT 0";
    /**/

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
        if (oldVersion < 11) {
            db.execSQL(DATABASE_ALTER_TEAM_4);
            db.execSQL(DATABASE_ALTER_TEAM_5);
            db.execSQL(DATABASE_ALTER_TEAM_6);
            db.execSQL(DATABASE_ALTER_TEAM_7);
            db.execSQL(DATABASE_ALTER_TEAM_8);
            db.execSQL(DATABASE_ALTER_TEAM_9);
            db.execSQL(DATABASE_ALTER_TEAM_10);
            db.execSQL(DATABASE_ALTER_TEAM_11);
            db.execSQL(DATABASE_ALTER_TEAM_12);
            db.execSQL(DATABASE_ALTER_TEAM_13);
            db.execSQL(DATABASE_ALTER_TEAM_14);
            db.execSQL(DATABASE_ALTER_TEAM_15);
            db.execSQL(DATABASE_ALTER_TEAM_16);
            db.execSQL(DATABASE_ALTER_TEAM_17);
            db.execSQL(DATABASE_ALTER_TEAM_18);
            db.execSQL(DATABASE_ALTER_TEAM_19);
            db.execSQL(DATABASE_ALTER_TEAM_20);
            db.execSQL(DATABASE_ALTER_TEAM_21);
            db.execSQL(DATABASE_ALTER_TEAM_22);
            db.execSQL(DATABASE_ALTER_TEAM_23);
            db.execSQL(DATABASE_ALTER_TEAM_24);
        }
    }
}
