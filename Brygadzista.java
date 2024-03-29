import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Brygadzista extends Uzytkownik implements FilesOperator, Serializable {
    private final List<Brygadzista> allForemen = new ArrayList<>();
    private final List<Brygada> brigadeNamesList = new ArrayList<>();
    private final List<Zlecenie> ordersNamesList = new ArrayList<>();

    Brygadzista(String imie, String nazwisko, String login, String haslo, LocalDateTime dataUrodzenia, DzialPracownikow dzialPracownikow) throws IOException {
        super(imie, nazwisko, login, haslo, dataUrodzenia, dzialPracownikow);
        this.allForemen.add(this);
        this.variableUpdate();
    }

    public void addBrigade(Brygada brigade) throws IOException {
        this.brigadeNamesList.add(brigade);
        this.variableUpdate();
    }

    public void addOrder(Zlecenie order) throws IOException {
        this.ordersNamesList.add(order);
        this.variableUpdate();
    }

    public List<Brygada> getBrigadeList() {
        return this.brigadeNamesList;
    }

    public List<Zlecenie> getOrdersList() {
        return this.ordersNamesList;
    }

    @Override
    public void variableUpdate() throws IOException {
        saveDataToFile(this.getClass().getName(), this.allForemen);
        loadDataFromFile(this.getClass().getName());
    }

    @Override
    public String toString() {
        return "{Klasa Brygadzista o identyfikatorze - " + "(" + this.identyfikator + ")," +
                " Lista brygad w jakich brał udział: " + "," +
                " Lista zleceń w jakich brał udział: " + this.getOrdersList().toString();
    }
}
