package it.unisa.orarilavoro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.lang.reflect.Method;

public class DbManager {
    private DBhelper dbhelper;

    public class MyResult {
        private Cursor cursor;
        private int tot;
        private int datiTotali;

        public MyResult(Cursor c, int t, int dT) {
            cursor = c;
            tot = t;
            datiTotali = dT;
        }

        public Cursor getCursor() {
            return cursor;
        }

        public int getTot() {
            return tot;
        }

        public int getDatiTotali() {
            return datiTotali;
        }
    }

    public DbManager(Context ctx) {
        dbhelper = new DBhelper(ctx);
    }

    public void save(int a, int m, int g, int daO, int aO, int tot) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_ANNO, a);
        cv.put(DatabaseStrings.FIELD_MESE, m);
        cv.put(DatabaseStrings.FIELD_GIORNO, g);
        cv.put(DatabaseStrings.FIELD_DA_ORA, daO);
        cv.put(DatabaseStrings.FIELD_A_ORA, aO);
        cv.put(DatabaseStrings.FIELD_ORE_TOTALI, tot);

        try {
            long id = db.insert(DatabaseStrings.TBL_NAME, null, cv);
            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + " inserimento completato di " + id);
        }
        catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }
    }

    public boolean delete(int id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            if (db.delete(DatabaseStrings.TBL_NAME, DatabaseStrings.FIELD_ID+"=?", new String[]{String.valueOf(id)}) > 0) {
                Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": eliminazione completata");
                return true;
            }
            return false;
        }
        catch (SQLiteException sqle) {
            return false;
        }
    }

    public Cursor query() {
        Cursor crs = null;
        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, null, null, null, null, null, null);
        } catch(SQLiteException sqle) {
            return null;
        }
        return crs;
    }

    /**
     * Ritorna i primi dieci orari della lista, in ordine descrescente per anno, mese e giorno
     * @return primi dieci orari (o meno se non ce ne sono abbastanza)
     */
    public Cursor primiDieciOrari() {
        Cursor crs = null;
        String orderBy = DatabaseStrings.FIELD_ANNO + " DESC, " + DatabaseStrings.FIELD_MESE + " DESC, " + DatabaseStrings.FIELD_GIORNO + " DESC";

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, null, null, null, null, orderBy, "10");

            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": " + crs.toString());
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return crs;
    }

    public MyResult findAll() {
        Cursor crs = null;
        String orderBy = DatabaseStrings.FIELD_ANNO + " DESC, " + DatabaseStrings.FIELD_MESE + " DESC, " + DatabaseStrings.FIELD_GIORNO + " DESC";

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": findAll");

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, null, null, null, null, orderBy, null);
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }
        int tot = 0, x = 0;

        while(crs.moveToNext()) {
            tot += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
            x++;
        }

        crs.moveToFirst();

        MyResult myResult = new MyResult(crs, tot, x);

        return myResult;
    }

    public Cursor findById(int id) {
        Cursor crs = null;

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": " + id);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, DatabaseStrings.FIELD_ID+"=?", new String[]{String.valueOf(id)}, null, null, null, null);

            //Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": " + crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID)));
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return crs;
    }

    public MyResult findBetweenTwoDates(int daG, int daM, int daA, int aG, int aM, int aA) {
        Cursor crs = null;

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": chiamato findBetweenTwoDates");

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null,
                    DatabaseStrings.FIELD_ANNO+"<=? AND " + DatabaseStrings.FIELD_ANNO +">=? AND " +
                            DatabaseStrings.FIELD_MESE+"<=? AND "+ DatabaseStrings.FIELD_MESE+">=? AND " +
                    DatabaseStrings.FIELD_GIORNO+"<=? AND " + DatabaseStrings.FIELD_GIORNO+">=?",
                    new String[]{String.valueOf(aA), String.valueOf(daA), String.valueOf(aM),
                    String.valueOf(daM), String.valueOf(aG), String.valueOf(daG)}, null, null, null, null);

            /*String sqlQry = SQLiteQueryBuilder.buildQueryString(false, DatabaseStrings.TBL_NAME, null,
                    DatabaseStrings.FIELD_ANNO+"<=" + daA + " AND " + DatabaseStrings.FIELD_ANNO +">=" +aA + " AND " +
                            DatabaseStrings.FIELD_MESE+"<=" + aM + " AND "+ DatabaseStrings.FIELD_MESE+">=" + daM + " AND " +
                            DatabaseStrings.FIELD_GIORNO+"<=" + aG + " AND " + DatabaseStrings.FIELD_GIORNO+">=" + aG,
                    null, null, null, null);
            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": " + sqlQry);*/

        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        int tot = 0, x = 0;

        while(crs.moveToNext()) {
            tot += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
            x++;
        }

        crs.moveToFirst();

        MyResult myResult = new MyResult(crs, tot, x);

        return myResult;
    }

    public boolean modificaById(int id, int anno, int mese, int giorno, int daOra, int aOra, int totale) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_ANNO, anno);
        cv.put(DatabaseStrings.FIELD_MESE, mese);
        cv.put(DatabaseStrings.FIELD_GIORNO, giorno);
        cv.put(DatabaseStrings.FIELD_DA_ORA, daOra);
        cv.put(DatabaseStrings.FIELD_A_ORA, aOra);
        cv.put(DatabaseStrings.FIELD_ORE_TOTALI, totale);

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": " + id);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            if(db.update(DatabaseStrings.TBL_NAME, cv, DatabaseStrings.FIELD_ID+"=" + id, null) == 1)
                return true;
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return false;
    }
}