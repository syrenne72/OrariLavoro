package it.unisa.orarilavoro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

public class DbManager {
    private DBhelper dbhelper;
    private String[] monthName = {"", "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};

    public static class MyResult {
        public String type;
        public Cursor cursor;
        public int ore;
        public int minuti;
        public int datiTotali;

        public MyResult(String type, Cursor c, int t, int m, int dT) {
            this.type = type;
            this.cursor = c;
            this.ore = t;
            this.minuti = m;
            this.datiTotali = dT;
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

    public boolean saveImpostazioni(String n, int oraInizio, int oraFine, int minutoInizio, int minutoFine,
                                    int oraPausa, int minutoPausa, int notifica, int oraNotifica, int minutoNotifica) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_NOME, n);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO, oraInizio * 60 + minutoInizio);
        cv.put(DatabaseStrings.FIELD_ORA_FINE, oraFine * 60 + minutoFine);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA, oraPausa * 60 + minutoPausa);
        cv.put(DatabaseStrings.FIELD_ORA_NOTIFICA, oraNotifica * 60 + minutoNotifica);
        cv.put(DatabaseStrings.FIELD_RICHIESTA_NOTIFICA, notifica);

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

    public boolean saveImpostazioni(String n, int oraInizio, int oraFine, int minutoInizio, int minutoFine,
                                    int oraPausa, int minutoPausa, int notifica, int oraNotifica, int minutoNotifica,
                                    int[] orariAvanzati) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_NOME, n);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO, oraInizio * 60 + minutoInizio);
        cv.put(DatabaseStrings.FIELD_ORA_FINE, oraFine * 60 + minutoFine);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA, oraPausa * 60 + minutoPausa);
        cv.put(DatabaseStrings.FIELD_ORA_NOTIFICA, oraNotifica * 60 + minutoNotifica);
        cv.put(DatabaseStrings.FIELD_RICHIESTA_NOTIFICA, notifica);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO_L, orariAvanzati[0] * 60 + orariAvanzati[1]);
        cv.put(DatabaseStrings.FIELD_ORA_FINE_L, orariAvanzati[2] * 60 + orariAvanzati[3]);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA_L, orariAvanzati[4] * 60 + orariAvanzati[5]);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO_M, orariAvanzati[6] * 60 + orariAvanzati[7]);
        cv.put(DatabaseStrings.FIELD_ORA_FINE_M, orariAvanzati[8] * 60 + orariAvanzati[9]);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA_M, orariAvanzati[10] * 60 + orariAvanzati[11]);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO_ME, orariAvanzati[12] * 60 + orariAvanzati[13]);
        cv.put(DatabaseStrings.FIELD_ORA_FINE_ME, orariAvanzati[14] * 60 + orariAvanzati[15]);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA_ME, orariAvanzati[16] * 60 + orariAvanzati[17]);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO_G, orariAvanzati[18] * 60 + orariAvanzati[19]);
        cv.put(DatabaseStrings.FIELD_ORA_FINE_G, orariAvanzati[20] * 60 + orariAvanzati[21]);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA_G, orariAvanzati[22] * 60 + orariAvanzati[23]);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO_V, orariAvanzati[24] * 60 + orariAvanzati[25]);
        cv.put(DatabaseStrings.FIELD_ORA_FINE_V, orariAvanzati[26] * 60 + orariAvanzati[27]);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA_V, orariAvanzati[28] * 60 + orariAvanzati[29]);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO_S, orariAvanzati[30] * 60 + orariAvanzati[31]);
        cv.put(DatabaseStrings.FIELD_ORA_FINE_S, orariAvanzati[32] * 60 + orariAvanzati[33]);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA_S, orariAvanzati[34] * 60 + orariAvanzati[35]);
        cv.put(DatabaseStrings.FIELD_ORA_INIZIO_D, orariAvanzati[36] * 60 + orariAvanzati[37]);
        cv.put(DatabaseStrings.FIELD_ORA_FINE_D, orariAvanzati[38] * 60 + orariAvanzati[39]);
        cv.put(DatabaseStrings.FIELD_ORE_PAUSA_D, orariAvanzati[40] * 60 + orariAvanzati[41]);

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

        Log.i("kiwi", this.getClass().getSimpleName() + ": findAll");

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, null, null, null, null, orderBy, String.valueOf(num));
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        MatrixCursor matrixCursor = new MatrixCursor(new String[] {DatabaseStrings.FIELD_ID, DatabaseStrings.FIELD_ANNO, DatabaseStrings.FIELD_MESE, DatabaseStrings.FIELD_GIORNO, DatabaseStrings.FIELD_DA_ORA, DatabaseStrings.FIELD_DA_MINUTO,
        DatabaseStrings.FIELD_A_ORA, DatabaseStrings.FIELD_A_MINUTO, DatabaseStrings.FIELD_ORE_TOTALI, DatabaseStrings.FIELD_MINUTI_TOTALI});

        int ore = 0, minuti = 0, x = 0;

        //Setting the string from last time to stamp
        String toType = "";
        if(crs.moveToNext()) {
            toType = " fino a ";

            toType += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)) + "/" +
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE)) + "/" +
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ANNO));
        }

        crs.moveToFirst();

        do {
            matrixCursor.addRow(new Object[]{
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

            Log.i("kiwi", this.getClass().getSimpleName() + ": findAll" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)) + "/" + crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE)));

            ore += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ORE_TOTALI));
            minuti += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MINUTI_TOTALI));

            if (minuti >= 60) {
                ore++;
                minuti -= 60;
            }

            x++;
        } while(crs.moveToNext());

        //Setting the string from first time to stamp
        String fromType = "";
        if(crs.moveToPrevious()) {
            fromType = "Orari di lavoro dal ";

            fromType += crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_GIORNO)) + "/" +
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_MESE)) + "/" +
                    crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ANNO));
        }

        Log.i("kiwi", this.getClass().getSimpleName() + ": findAll: " + fromType + toType);

        MyResult myResult = new MyResult(fromType + toType, matrixCursor, ore, minuti, x);

        return myResult;
    }

    public Cursor findById(int id) {
        Cursor crs = null;

        //Log.i("kiwi", this.getClass().getSimpleName() + ": " + id);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, DatabaseStrings.FIELD_ID+"=?", new String[]{String.valueOf(id)}, null, null, null, null);

            //Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": " + crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID)));
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return crs;
    }

    public MyResult findByMonthAndYear(int month, int year) {
        Cursor crs = null;
        String orderBy = DatabaseStrings.FIELD_ANNO + " DESC, " + DatabaseStrings.FIELD_MESE + " DESC, " + DatabaseStrings.FIELD_GIORNO + " DESC";

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": findByMonthAndYear: received month " + month + " and year " + year);

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, DatabaseStrings.FIELD_ANNO+"=? AND " + DatabaseStrings.FIELD_MESE+"=?", new String[]{String.valueOf(year), String.valueOf(month)}, null, null, orderBy, null);
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        MatrixCursor matrixCursor = new MatrixCursor(new String[] {DatabaseStrings.FIELD_ID, DatabaseStrings.FIELD_ANNO, DatabaseStrings.FIELD_MESE, DatabaseStrings.FIELD_GIORNO, DatabaseStrings.FIELD_DA_ORA, DatabaseStrings.FIELD_DA_MINUTO,
                DatabaseStrings.FIELD_A_ORA, DatabaseStrings.FIELD_A_MINUTO, DatabaseStrings.FIELD_ORE_TOTALI, DatabaseStrings.FIELD_MINUTI_TOTALI});

        int ore = 0, minuti = 0, x = 0;

        /*Verifico se sono stati trovati dati nel database
        * Se non sono presenti dati, restituisco null*/
        if(crs.getCount() == 0)
            return null;

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

        String type = "Orari di lavoro di " + monthName[month] + " " + year;

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": findByMonthAndYear: " + type);

        MyResult myResult = new MyResult(type, matrixCursor, ore, minuti, x);

        return myResult;
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

        /*Verifico se sono stati trovati dati nel database
         * Se non sono presenti dati, restituisco null*/
        if(crs.getCount() == 0)
            return null;

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

        String type = "Orari di lavoro dal " + daG + "/" + daM + "/" + daA + " al " +
                        aG + "/" + aM + "/" + aA;

        MyResult myResult = new MyResult(type, matrixCursor, ore, minuti, x);

        Log.i("KIWIBUNNY", this.getClass().getSimpleName() + ": trovati " + x + " elementi");

        return myResult;
    }

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

    public int[] findOrariAvanzati() {
        Cursor crs = null;
        int[] orariAvanzati = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME_IMPOSTAZIONI, new String[] {DatabaseStrings.FIELD_ORA_INIZIO_L, DatabaseStrings.FIELD_ORA_FINE_L,
                    DatabaseStrings.FIELD_ORE_PAUSA_L, DatabaseStrings.FIELD_ORA_INIZIO_M, DatabaseStrings.FIELD_ORA_FINE_M,
                            DatabaseStrings.FIELD_ORE_PAUSA_M, DatabaseStrings.FIELD_ORA_INIZIO_ME, DatabaseStrings.FIELD_ORA_FINE_ME,
                            DatabaseStrings.FIELD_ORE_PAUSA_ME, DatabaseStrings.FIELD_ORA_INIZIO_G, DatabaseStrings.FIELD_ORA_FINE_G,
                            DatabaseStrings.FIELD_ORE_PAUSA_G, DatabaseStrings.FIELD_ORA_INIZIO_V, DatabaseStrings.FIELD_ORA_FINE_V,
                            DatabaseStrings.FIELD_ORE_PAUSA_V, DatabaseStrings.FIELD_ORA_INIZIO_S, DatabaseStrings.FIELD_ORA_FINE_S,
                            DatabaseStrings.FIELD_ORE_PAUSA_S, DatabaseStrings.FIELD_ORA_INIZIO_D, DatabaseStrings.FIELD_ORA_FINE_D,
                            DatabaseStrings.FIELD_ORE_PAUSA_D},
                    null, null, null, null, null, null);

            int j = 0;

            if (crs != null && crs.moveToFirst()) {
                for(int i = 0; i < 21; i++) {
                    orariAvanzati[j] = crs.getInt(i) / 60;
                    j += 1;
                    orariAvanzati[j] = crs.getInt(i) % 60;

                    Log.d("kiwi", "Orari avanzati trovati: " + orariAvanzati[j - 1] + " - " + orariAvanzati[j]);

                    j += 1;
                }

                return orariAvanzati;
            }

        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
        }

        return null;
    }
}