import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Uzytkownik extends Pracownik implements FilesOperator, Serializable {
    private String login;
    private String haslo;
    private String inicjal;
    private final List<Uzytkownik> allUsers = new ArrayList<>();

    Uzytkownik(String imie, String nazwisko, String login, String haslo, LocalDateTime dataUrodzenia, DzialPracownikow dzialPracownikow) throws IOException {
        super(imie, nazwisko, dataUrodzenia, dzialPracownikow);
        this.login = login;
        this.haslo = haslo;
        this.inicjal = this.createInitial(imie, nazwisko);
        this.allUsers.add(this);
        this.variableUpdate();
    }

    public void setName(String name) throws IOException {
        this.imie = name;
        this.inicjal = this.createInitial(name, this.nazwisko);
        this.variableUpdate();
    }

    public void setLastName(String lastName) throws IOException {
        this.nazwisko = lastName;
        this.inicjal = this.createInitial(this.inicjal, lastName);
        this.variableUpdate();
    }

    private String createInitial(String name, String lastName) {
        return (Character.toString(name.charAt(0)) + Character.toString(lastName.charAt(0))).toUpperCase();
    }

    @Override
    public void variableUpdate() throws IOException {
        saveDataToFile(this.getClass().getName(), this.allUsers);
        loadDataFromFile(this.getClass().getName());
    }

    @Override
    public String toString() {
        return "Klasa Użytkownik o identyfikatorze - " + "(" + this.identyfikator + ")," +
                " Login użytkownika: " + this.login + "," +
                " Haslo użytkownika: " + this.haslo + "," +
                " Inicjały użytkownika: " + this.inicjal + "," +
                super.toString();
    }
}
