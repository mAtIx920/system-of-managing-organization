import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Pracownik implements Comparable<Pracownik>, Serializable {
    private static long ID;
    long identyfikator;

    private static List<Pracownik> allWorkers = new ArrayList<>();
    String imie;
    String nazwisko;
    public boolean doingOrderNow = false;
    private LocalDateTime dataUrodzenia;
    private DzialPracownikow dzialPracownikow;

    Pracownik(String imie, String nazwisko, LocalDateTime dataUrodzenia, DzialPracownikow dzialPracownikow) throws IOException{
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.dataUrodzenia = dataUrodzenia;
        this.dzialPracownikow = dzialPracownikow;
        this.identyfikator = ID++;
        allWorkers.add(this);
    }

    public void setAvailabilityWorker(boolean state) {
        this.doingOrderNow = state;
    }

    public boolean getAvailabilityWorker() {
        return this.doingOrderNow;
    }

    void workerDoingWork(boolean state) {
        this.doingOrderNow = state;
    }

    boolean getWorkingState() {
        return this.doingOrderNow;
    }

    @Override
    public int compareTo(Pracownik o) {
        int resultNameComparison = this.imie.compareTo(o.imie);
        int resultLastNameComparison = this.nazwisko.compareTo(o.nazwisko);
        int resultBirthDateComparison = this.dataUrodzenia.compareTo(o.dataUrodzenia);

        if(resultNameComparison != 0) {
            return resultNameComparison;
        }

        if(resultLastNameComparison != 0) {
            return resultLastNameComparison;
        }

        return resultBirthDateComparison;
    }

    @Override
    public String toString() {
        return "Imie pracownika: " + this.imie + "," +
                " Nazwisko pracownika: " + this.nazwisko + "," +
                " Data urodzenia: " + this.dataUrodzenia + "}";
    }
}
