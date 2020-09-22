package it.unisa.orarilavoro;

import android.util.Log;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Orario implements Serializable {
    public int id;
    public int anno;
    public int mese;
    public int giorno;
    public int daOra;
    public int daMinuto;
    public int aOra;
    public int aMinuto;
    public int oreTotali;
    public int minutiTotali;

    public Orario() {
        this.id = -1;
        this.anno = 0;
        this.mese = 0;
        this.giorno = 0;
        this.daOra = 0;
        this.daMinuto = 0;
        this.aOra = 0;
        this.aMinuto = 0;
        this.oreTotali = 0;
        this.minutiTotali = 0;
    }

    public Orario(int id, int anno, int mese, int giorno, int daOra, int daMinuto, int aOra, int aMinuto, int oreTotali, int minutiTotali) {
        this.id = id;
        this.anno = anno;
        this.mese = mese;
        this.giorno = giorno;
        this.daOra = daOra;
        this.daMinuto = daMinuto;
        this.aOra = aOra;
        this.aMinuto = aMinuto;
        this.oreTotali = oreTotali;
        this.minutiTotali = minutiTotali;
    }

    public void setTotale(String time) {
        int totOre, totMinuti;

        String strTime[] = time.split(":");
        totOre = Integer.parseInt(strTime[0]);
        totMinuti = Integer.parseInt(strTime[1]);

        if (totOre > 24 || totOre < 0) {
            throw new NumberFormatException("Numero di ore giornaliere non valido");
        }

        if (totMinuti > 60 || totMinuti < 0) {
            throw new NumberFormatException("Numero di ore giornaliere non valido");
        }

        this.oreTotali = totOre;
        this.minutiTotali = totMinuti;
    }

    public void setTotale(int oreTotali, int minutiTotali) {
        if(oreTotali > 24 || oreTotali < 0) {
            throw new NumberFormatException("Numero di ore giornaliere non valido");
        }

        if(minutiTotali > 60 || minutiTotali < 0) {
            throw new NumberFormatException("Numero di ore giornaliere non valido");
        }

        this.oreTotali = oreTotali;
        this.minutiTotali = minutiTotali;
    }

    public void setOreTotali(int oreTotali) {
        if(oreTotali > 24 || oreTotali < 0) {
            throw new NumberFormatException("Numero di ore giornaliere non valido");
        }

        this.oreTotali = oreTotali;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return String.format("%02d/%02d/%04d", this.giorno, this.mese, this.anno);
    }

    public void setData(int a, int m, int g) {
        anno = a;
        mese = m;
        giorno = g;
    }

    public void setData(String data) {
        String strData[] = data.split("/");
        giorno = Integer.parseInt(strData[0]);
        mese = Integer.parseInt(strData[1]);
        anno = Integer.parseInt(strData[2]);
    }

    public String getDaOra() {
        return String.format("%02d:%02d", this.daOra, this.daMinuto);
    }

    public String getAOra() {
        return String.format("%02d:%02d", this.aOra, this.aMinuto);
    }

    public void setDaOra(String data) {
        if(data == null)
            throw new NumberFormatException("Inserire un orario");

        String strData[] = data.split(":");
        daOra = Integer.parseInt(strData[0]);
        daMinuto = Integer.parseInt(strData[1]);
    }

    public void setAOra(String data) {
        if(data == null)
            throw new NumberFormatException("Inserire un orario");

        String strData[] = data.split(":");
        aOra = Integer.parseInt(strData[0]);
        aMinuto = Integer.parseInt(strData[1]);
    }

    public String getTotale() {
        return String.format("%02d:%02d", this.oreTotali, this.minutiTotali);
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public void setMese(int mese) {
        this.mese = mese;
    }

    public void setGiorno(int giorno) {
        this.giorno = giorno;
    }

    public void setDaOra(int daOra) {
        this.daOra = daOra;
    }

    public void setDaMinuto(int daMinuto) {
        this.daMinuto = daMinuto;
    }

    public void setaOra(int aOra) {
        this.aOra = aOra;
    }

    public void setaMinuto(int aMinuto) {
        this.aMinuto = aMinuto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orario orario = (Orario) o;
        return anno == orario.anno &&
                mese == orario.mese &&
                giorno == orario.giorno &&
                daOra == orario.daOra &&
                daMinuto == orario.daMinuto &&
                aOra == orario.aOra &&
                aMinuto == orario.aMinuto &&
                oreTotali == orario.oreTotali &&
                minutiTotali == orario.minutiTotali;
    }

    @Override
    public String toString() {
        return "Orario{" +
                "id=" + id +
                "anno=" + anno +
                ", mese=" + mese +
                ", giorno=" + giorno +
                ", daOra=" + daOra +
                ", daMinuto=" + daMinuto +
                ", aOra=" + aOra +
                ", aMinuto=" + aMinuto +
                ", oreTotali=" + oreTotali +
                ", minutiTotali=" + minutiTotali +
                '}';
    }

    public static String calcoloOreTotali(String da, String a, int p) {
        int daOra, daMinuto, aOra, aMinuto, oraPausa, minutoPausa, totOre, totMinuti;

        String strData[] = da.split(":");
        daOra = Integer.parseInt(strData[0]);
        daMinuto = Integer.parseInt(strData[1]);

        strData = a.split(":");
        aOra = Integer.parseInt(strData[0]);
        aMinuto = Integer.parseInt(strData[1]);

        /*if(aMinuto >= daMinuto) {
            totMinuti = aMinuto;
            totOre = aOra - daOra;
        } else {
            aMinuto += 60;
            totMinuti = aMinuto - daMinuto;
            totOre = aOra - daOra - 1;
        }*/

        totOre = ((aOra * 60 + aMinuto) - (daOra * 60 + daMinuto) - p) / 60;
        totMinuti = ((aOra * 60 + aMinuto) - (daOra * 60 + daMinuto) - p) % 60;

        return String.format("%02d:%02d", totOre, totMinuti);
    }

    public static boolean isDateInRange(int year, int month, int day, int yearFrom, int monthFrom, int dayFrom, int yearUntil, int monthUntil, int dayUntil) {
        GregorianCalendar testDate = new GregorianCalendar(year, month, day);
        GregorianCalendar startDate = new GregorianCalendar(yearFrom, monthFrom, dayFrom);
        GregorianCalendar endDate = new GregorianCalendar(yearUntil, monthUntil, dayUntil);

//        GregorianCalendar gg = new GregorianCalendar(2020, 9, 2);
//        GregorianCalendar g = new GregorianCalendar(2020, 9, 1);
//
//        Log.i("KIWIBUNNY", "Prima>>>>>: " + g.before(new GregorianCalendar(2020, 8, 1)) + "Dopo: " + g.after(new GregorianCalendar(2020, 8, 31)));
//        Log.i("KIWIBUNNY", "Prima: " + gg.before(new GregorianCalendar(2020, 8, 1)) + "Dopo: " + gg.after(new GregorianCalendar(2020, 8, 31)));

        return !(testDate.before(startDate) || testDate.after(endDate));
    }
}
