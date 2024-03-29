import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Brygada implements FilesOperator, Serializable {
    private static long ID;
    public long identyfikator;

    private String nazwa;
    private Brygadzista brygadzista;
    private final List<Brygada> allBrigade = new ArrayList<>();
    private final List<Pracownik> allWorkers = new ArrayList<>();

    Brygada(String nazwa, Brygadzista brygadzista) throws IOException, ClassNotFoundException {
        this.identyfikator = ID++;
        this.nazwa = nazwa;
        this.brygadzista = brygadzista;
        this.brygadzista.addBrigade(this);
        this.allBrigade.add(this);
        this.variableUpdate();
    }

    public void addWorker(Pracownik worker) throws IOException {
        if(worker instanceof Uzytkownik && !(worker instanceof Brygadzista)) {
            System.out.println("Klasa Użytkownik nie może zostać dodana do brygady");
            return;
        }
        this.allWorkers.add(worker);
        this.variableUpdate();
    }

    public void addWorkers(List<Pracownik> workers) throws IOException {
        for(Pracownik worker : workers) {
            this.addWorker(worker);
        }
    }

    public List<Pracownik> getWorkers() {
        return this.allWorkers;
    }

    @Override
    public void variableUpdate() throws IOException {
        saveDataToFile(this.getClass().getName(), this.allBrigade);
        loadDataFromFile(this.getClass().getName());
    }

    @Override
    public String toString() {
        return "{Klasa Brygada o  identyfikatorze - " + "(" + this.identyfikator + ")," +
                " Nazwa brygady: " + this.nazwa + "," +
                " Brygadzista: " + this.brygadzista + "," +
                " Lista pracowników w brygadzie: " + this.getWorkers().toString() +"}";

    }
}
