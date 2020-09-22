package it.unisa.orarilavoro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DbManager {
    private DBhelper dbhelper;

    public class MyResult {
        private Cursor cursor;
        public int ore;
        public int minuti;
        public int datiTotali;

        public MyResult(Cursor c, int t, int m, int dT) {
            cursor = c;
            ore = t;
            minuti = m;
            datiTotali = dT;
        }

        public Cursor getCursor() {
            return cursor;
        }

        public String getTotale() {
            return String.format("%d:%02d", ore, minuti);
        }

        public int getDatiTotali() {
            return datiTotali;
        }
    }

    public DbManager(Context ctx) {
        dbhelper = new DBhelper(ctx);
    }

    public void save(Orario orario) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_ANNO, orario.anno);
        cv.put(DatabaseStrings.FIELD_MESE, orario.mese);
        cv.put(DatabaseStrings.FIELD_GIORNO, orario.giorno);
        cv.put(DatabaseStrings.FIELD_DA_ORA, orario.daOra);
        cv.put(DatabaseStrings.FIELD_DA_MINUTO, orario.daMinuto);
        cv.put(DatabaseStrings.FIELD_A_ORA, orario.aOra);
        cv.put(DatabaseStrings.FIELD_A_MINUTO, orario.aMinuto);
        cv.put(DatabaseStrings.FIELD_ORE_TOTALI, orario.oreTotali);
        cv.put(DatabaseStrings.FIELD_MINUTI_TOTALI, orario.minutiTotali);

        try {
            long id = db.insert(DatabaseStrings.TBL_NAME, null, cv);
            Log.i("KIWIBUNNY", this.getClass().getSimpleName() + " inserimento completato di " + id);
        }
        catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }
    }

//    public boolean saveImpostazioni(String n, int i, int f, int p) {
//        SQLiteDatabase db = dbhelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(DatabaseStrings.FIELD_NOME, n);
//        cv.put(DatabaseStrings.FIELD_ORA_INIZIO, i);
//        cv.put(DatabaseStrings.FIELD_ORA_FINE, f);
//        cv.put(DatabaseStrings.FIELD_ORE_PAUSA, p);
//
//        try {
//            db.delete(DatabaseStrings.TBL_NAME_IMPOSTAZIONI, null, null);
//            if(db.insert(DatabaseStrings.TBL_NAME_IMPOSTAZIONI, null, cv) > 0)
//                return true;
//        }
//        catch (SQLiteException sqle) {
//            sqle.printStackTrace();
//        }
//
//        return false;
//    }
    public boolean saveImpostazioni(String n, int oraInizio, int oraFine, int minutoInizio, int minutoFine, int oraPausa, int minutoPausa) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_NOME, n);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO, oraInizio * 60 + minutoInizio);
        cv.put(DatabaseStrings.FIELD_ORA_FINE, oraFine * 60 + minutoFine);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA, oraPausa * 60 + minutoPausa);

        try {
            db.delete(DatabaseStrings.TBL_NAME_IMPOSTAZIONI, null, null);
            if(db.insert(DatabaseStrings.TBL_NAME_IMPOSTAZIONI, null, cv) > 0)
                return true;
        }
        catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return false;
    }

    public boolean modificaById(Orario orario) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_ANNO, orario.anno);
        cv.put(DatabaseStrings.FIELD_MESE, orario.mese);
        cv.put(DatabaseStrings.FIELD_GIORNO, orario.giorno);
        cv.put(DatabaseStrings.FIELD_DA_ORA, orario.daOra);
        cv.put(DatabaseStrings.FIELD_DA_MINUTO, orario.daMinuto);
        cv.put(DatabaseStrings.FIELD_A_ORA, orario.aOra);
        cv.put(DatabaseStrings.FIELD_A_MINUTO, orario.aMinuto);
        cv.put(DatabaseStrings.FIELD_ORE_TOTALI, orario.oreTotali);
        cv.put(DatabaseStrings.FIELD_MINUTI_TOTALI, orario.minutiTotali);

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": " + orario.id);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            if(db.update(DatabaseStrings.TBL_NAME, cv, DatabaseStrings.FIELD_ID+"=" + orario.id, null) == 1)
                return true;
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return false;
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

    public MyResult findAll(int num) {
        Cursor crs = null;
        String orderBy = DatabaseStrings.FIELD_ANNO + " DESC, " + DatabaseStrings.FIELD_MESE + " DESC, " + DatabaseStrings.FIELD_GIORNO + " DESC";

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": findAll");

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, null, null, null, null, orderBy, String.valueOf(num));
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        MatrixCursor matrixCursor = new MatrixCursor(new String[] {DatabaseStrings.FIELD_ID, DatabaseStrings.FIELD_ANNO, DatabaseStrings.FIELD_MESE, DatabaseStrings.FIELD_GIORNO, DatabaseStrings.FIELD_DA_ORA, DatabaseStrings.FIELD_DA_MINUTO,
        DatabaseStrings.FIELD_A_ORA, DatabaseStrings.FIELD_A_MINUTO, DatabaseStrings.FIELD_ORE_TOTALI, DatabaseStrings.FIELD_MINUTI_TOTALI});

        int ore = 0, minuti = 0, x = 0;

        /*Ordino gli orari in ordine crescente e calcolo il totale delle ore*/
        crs.moveToLast();

        do {
            matrixCursor.addRow(new Object[] {
                (int) crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ANNO)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_ORA)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_MINUTO)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_ORA)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_MINUTO)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI)),
                crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI))
            });

            ore += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
            minuti += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI));

            if(minuti >= 60) {
                ore++;
                minuti -= 60;
            }

            x++;
        } while (crs.moveToPrevious());
        /************************************/

        MyResult myResult = new MyResult(matrixCursor, ore, minuti, x);

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
        String orderBy = DatabaseStrings.FIELD_ANNO + " DESC, " + DatabaseStrings.FIELD_MESE + " DESC, " + DatabaseStrings.FIELD_GIORNO + " DESC";

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": chiamato findBetweenTwoDates tra " + daG + "-" + daM + "-" + daA + " e " + aG + "-" + aM + "-" + aA);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null,
                    DatabaseStrings.FIELD_ANNO+"<=? AND " + DatabaseStrings.FIELD_ANNO +">=? AND " +
                            DatabaseStrings.FIELD_MESE+"<=? AND "+ DatabaseStrings.FIELD_MESE+">=? AND " +
                    DatabaseStrings.FIELD_GIORNO+"<=? AND " + DatabaseStrings.FIELD_GIORNO+">=?",
                    new String[]{String.valueOf(aA), String.valueOf(daA), String.valueOf(aM),
                    String.valueOf(daM), String.valueOf(aG), String.valueOf(daG)}, null, null, orderBy, null);
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        int ore = 0, minuti = 0, x = 0;

        MatrixCursor matrixCursor = new MatrixCursor(new String[] {DatabaseStrings.FIELD_ID, DatabaseStrings.FIELD_ANNO, DatabaseStrings.FIELD_MESE, DatabaseStrings.FIELD_GIORNO, DatabaseStrings.FIELD_DA_ORA, DatabaseStrings.FIELD_DA_MINUTO,
                DatabaseStrings.FIELD_A_ORA, DatabaseStrings.FIELD_A_MINUTO, DatabaseStrings.FIELD_ORE_TOTALI, DatabaseStrings.FIELD_MINUTI_TOTALI});

        /*Ordino gli orari in ordine crescente e calcolo il totale delle ore*/
        crs.moveToLast();

        do {
            matrixCursor.addRow(new Object[] {
                    (int) crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ANNO)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_ORA)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_MINUTO)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_ORA)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_MINUTO)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI)),
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI))
            });

            ore += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
            minuti += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI));

            if(minuti >= 60) {
                ore++;
                minuti -= 60;
                Log.i("KIWIBUNNY", "FindBetweenTwoDates: Ho messo un'ora: " + ore + ":" + minuti);
            }

            //Log.i("KIWIBUNNY", "FindBetweenTwoDates: Giorno: " + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)) + " Ore: " + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI)));

            x++;
        } while (crs.moveToPrevious());
        /************************************/

        //Log.i("KIWIBUNNY", "FindBetweenTwoDates: Ore finali: " + ore + ":" + minuti);

        MyResult myResult = new MyResult(matrixCursor, ore, minuti, x);

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": trovati " + x + " elementi");

        return myResult;
    }
//    public MyResult findBetweenTwoDates(int daG, int daM, int daA, int aG, int aM, int aA) {
//        Cursor crs = null;
//        String orderBy = DatabaseStrings.FIELD_ANNO + " DESC, " + DatabaseStrings.FIELD_MESE + " DESC, " + DatabaseStrings.FIELD_GIORNO + " DESC";
//
//        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": chiamato findBetweenTwoDates tra " + daG + "-" + daM + "-" + daA + " e " + aG + "-" + aM + "-" + aA);
//
//        try {
//            SQLiteDatabase db = dbhelper.getReadableDatabase();
//                /*crs = db.query(DatabaseStrings.TBL_NAME, null,
//                        "(" + DatabaseStrings.FIELD_ANNO+"<=? AND " + DatabaseStrings.FIELD_ANNO +">=? AND " +
//                                DatabaseStrings.FIELD_MESE+"<=? AND "+ DatabaseStrings.FIELD_MESE+">=? AND " +
//                        DatabaseStrings.FIELD_GIORNO+"<=? AND " + DatabaseStrings.FIELD_GIORNO+">=?) OR (" + DatabaseStrings.FIELD_MESE+"==? AND " + DatabaseStrings.FIELD_GIORNO+">=?) OR ("
//                                + DatabaseStrings.FIELD_MESE +"==? AND " + DatabaseStrings.FIELD_GIORNO + "<=?)",
//                        new String[]{String.valueOf(aA), String.valueOf(daA), String.valueOf(aM),
//                        String.valueOf(daM), String.valueOf(aG), String.valueOf(daG), String.valueOf(daM), String.valueOf(daG), String.valueOf(aM), String.valueOf(aG)}, null, null, orderBy, null);*/
//            crs = db.query(DatabaseStrings.TBL_NAME, null, null, null, null, null, orderBy, null);
//        } catch(SQLiteException sqle) {
//            sqle.printStackTrace();
//        }
//
//        int ore = 0, minuti = 0, x = 0;
//
//        MatrixCursor matrixCursor = new MatrixCursor(new String[] {DatabaseStrings.FIELD_ID, DatabaseStrings.FIELD_ANNO, DatabaseStrings.FIELD_MESE, DatabaseStrings.FIELD_GIORNO, DatabaseStrings.FIELD_DA_ORA, DatabaseStrings.FIELD_DA_MINUTO,
//                DatabaseStrings.FIELD_A_ORA, DatabaseStrings.FIELD_A_MINUTO, DatabaseStrings.FIELD_ORE_TOTALI, DatabaseStrings.FIELD_MINUTI_TOTALI});
//
//        /*Ordino gli orari in ordine crescente e calcolo il totale delle ore*/
//        crs.moveToLast();
//
//        do {
//            //Verifico se la data è compresa tra le due selezionate per la ricerca
//            if(Orario.isDateInRange(crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ANNO)), crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE)), crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)),
//                    daA, daM, daG, aA, aM, aG)) {
//                matrixCursor.addRow(new Object[]{
//                        (int) crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ANNO)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_ORA)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_DA_MINUTO)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_ORA)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_A_MINUTO)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI)),
//                        crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI))
//                });
//
//                ore += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
//                minuti += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI));
//
//                if (minuti >= 60) {
//                    ore++;
//                    minuti -= 60;
//                }
//
//                x++;
//            }
//        } while (crs.moveToPrevious());
//        /************************************/
//
//        MyResult myResult = new MyResult(matrixCursor, ore, minuti, x);
//
//        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": trovati " + x + " elementi");
//
//        return myResult;
//    }

    public Cursor findImpostazioni() {
        Cursor crs = null;

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME_IMPOSTAZIONI, null, null, null, null, null, null, null);
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return crs;
    }
}