package it.unisa.orarilavoro;

public class Orario {
    private int anno;
    private int mese;
    private int giorno;
    private int daOra;
    private int daMinuto;
    private int aOra;
    private int aMinuto;

    public Orario() {
    }

    public Orario(int anno, int mese, int giorno, int daOra, int daMinuto, int aOra, int aMinuto) {
        this.anno = anno;
        this.mese = mese;
        this.giorno = giorno;
        this.daOra = daOra;
        this.daMinuto = daMinuto;
        this.aOra = aOra;
        this.aMinuto = aMinuto;
    }

    public void setData(int a, int m, int g) {
        anno = a;
        mese = m;
        giorno = g;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getMese() {
        return mese;
    }

    public void setMese(int mese) {
        this.mese = mese;
    }

    public int getGiorno() {
        return giorno;
    }

    public void setGiorno(int giorno) {
        this.giorno = giorno;
    }

    public int getDaOra() {
        return daOra;
    }

    public void setDaOra(int daOra) {
        this.daOra = daOra;
    }

    public int getDaMinuto() {
        return daMinuto;
    }

    public void setDaMinuto(int daMinuto) {
        this.daMinuto = daMinuto;
    }

    public int getaOra() {
        return aOra;
    }

    public void setaOra(int aOra) {
        this.aOra = aOra;
    }

    public int getaMinuto() {
        return aMinuto;
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
                aMinuto == orario.aMinuto;
    }

    @Override
    public String toString() {
        return "Orario{" +
                "anno=" + anno +
                ", mese=" + mese +
                ", giorno=" + giorno +
                ", daOra=" + daOra +
                ", daMinuto=" + daMinuto +
                ", aOra=" + aOra +
                ", aMinuto=" + aMinuto +
                '}';
    }
}
