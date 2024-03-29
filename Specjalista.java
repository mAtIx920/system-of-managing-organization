import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Specjalista extends Pracownik implements FilesOperator, Serializable {
    private final String specjalizacja;
    private final List<Specjalista> allSpecjalist = new ArrayList<>();

    Specjalista(String imie, String specjalizacja, String nazwisko, LocalDateTime dataUrodzenia, DzialPracownikow dzialPracownikow) throws IOException {
        super(imie, nazwisko, dataUrodzenia, dzialPracownikow);
        this.specjalizacja = specjalizacja;
        this.allSpecjalist.add(this);
        this.variableUpdate();
    }

    @Override
    public void variableUpdate() throws IOException {
        saveDataToFile(this.getClass().getName(), this.allSpecjalist);
        loadDataFromFile(this.getClass().getName());
    }

    @Override
    public String toString() {
        return "Klasa Specjalista o identyfikatorze - " + "(" + this.identyfikator + ")," +
                " Specjalizacja: " + this.specjalizacja + "," +
                super.toString();
    }
}
