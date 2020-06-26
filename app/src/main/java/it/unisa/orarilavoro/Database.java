package it.unisa.orarilavoro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME = "ORARIDILAVORO";

    public DBhelper(Context context) {
        super(context, DBNAME, null, 4);
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
                DatabaseStrings.FIELD_A_ORA + " INT," +
                DatabaseStrings.FIELD_ORE_TOTALI + " INT)";

        db.execSQL(q);

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": creato database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
