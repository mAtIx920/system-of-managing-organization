import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Zlecenie implements Runnable, FilesOperator, Serializable {
    private static long ID;
    public long identyfikator;

    public enum RodzajZlecenia {PLANOWANE, NIEPLANOWANE};
    public enum StanZlecenia {UTWORZONE, ROZPOCZETE, ZAKONCZONE};
    private List<Praca> pracaList = new ArrayList<>();
    public static final HashMap<Long, Zlecenie> allOrders = new HashMap<>();

    private RodzajZlecenia rodzajZlecenia;
    private StanZlecenia stanZlecenia;
    private Brygada brygada;
    private LocalDateTime dataUtworzenia;
    private LocalDateTime dataRealizacji;
    private LocalDateTime dataZakonczenia;

    Zlecenie(boolean czyPlanowane) throws IOException {
        this.setOrderType(czyPlanowane);
        this.stanZlecenia = StanZlecenia.UTWORZONE;
        this.brygada = null;
        this.dataUtworzenia = LocalDateTime.now();
        this.dataRealizacji = null;
        this.dataZakonczenia = null;
        this.identyfikator = ID++;
        allOrders.put(this.identyfikator, this);
    }

    public List<Praca> getPraca() {
        return this.pracaList;
    }

    Zlecenie(boolean czyPlanowane, Brygada brygada) throws IOException {
        this(czyPlanowane);
        this.brygada = brygada;
    }

    Zlecenie(boolean czyPlanowane, List<Praca> pracaList) throws IOException {
        this(czyPlanowane);
        this.pracaList = pracaList;
    }

    Zlecenie(boolean czyPlanowane, List<Praca> pracaList, Brygada brygada) throws IOException {
        this(czyPlanowane, pracaList);
        this.brygada = brygada;
    }

    private void setOrderType(boolean czyPlanowane) {
        this.rodzajZlecenia = czyPlanowane ? RodzajZlecenia.PLANOWANE : RodzajZlecenia.NIEPLANOWANE;
    }

    public boolean addBrygada(Brygada brygada) throws IOException {
        if(this.brygada != null) return false;

        this.brygada = brygada;
        this.variableUpdate();
        return true;
    }

    public void addPraca(Praca praca) throws IOException {
        this.pracaList.add(praca);
        this.variableUpdate();
    }

    public void rozpocznijZlecenie() {
        if(this.getOrderState() != StanZlecenia.UTWORZONE) {
            System.out.println("Zlecenie o identyfikatorze " + this.identyfikator +
                    " nie moze zostac uruchomione ponownie, poniewaz zostało ukonczone badz jest w trakcie realizacji");
            return;
        }

        Thread thread = new Thread(this);
        if(!thread.isAlive()) {
            thread.start();
        }
    }

    private boolean checkAvailabilityWorkers() {
        return this.brygada.getWorkers().stream().anyMatch(Pracownik::getAvailabilityWorker);
    }

    boolean isOrderWorksAvailable() {
        return this.pracaList.stream().anyMatch(praca -> praca.getState() != Thread.State.NEW);
    }

    void setIfWorkerFromBrigadeWorkNow(boolean state) {
        for(Pracownik worker: this.brygada.getWorkers()) {
            worker.workerDoingWork(state);
        }
    }

    StanZlecenia getOrderState() {
        return this.stanZlecenia;
    }

    @Override
    public void variableUpdate() throws IOException {
        saveDataToFile(this.getClass().getName(), allOrders);
        loadDataFromFile(this.getClass().getName());
    }

    @Override
    public void run() {
        if(this.brygada == null || this.brygada.getWorkers().size() == 0 || this.pracaList.size() == 0) {
            System.out.println("Przed rozpoczeciem zlecenia dodaj brygade i liste prac do wykonania");
            return;
        }

        if(this.checkAvailabilityWorkers()) {
            System.out.println("Pracownicy z brygady w zleceniu o identydikatorze (" +
                    this.identyfikator + ") są zajeci, rozpoczecie zlecenia nie moze byc wykonane");
            return;
        }


        if(this.isOrderWorksAvailable()) {
            System.out.println("Praca znajdujaca sie w zleceniu o identyfikatorze (" +
                    this.identyfikator + ") zostala juz wykonana badz jest w trakcie realizacji");

            return;
        }

        int i = 0;
        for(Praca praca : this.pracaList) {
            praca.addWorksToQueue(this.pracaList.subList(0, i));
            praca.setOrderId(this.identyfikator);

            if(i < this.pracaList.size() - 1) {
                praca.setNextWork(this.pracaList.get(i + 1));
            }
            i++;
        }

        System.out.println("Rozpoczeto zlecenie o identyfikatorze - " + "(" + this.identyfikator + ")");
        this.stanZlecenia = StanZlecenia.ROZPOCZETE;
        this.dataRealizacji = LocalDateTime.now();

        for (Praca praca : this.pracaList) {
            praca.start();
        }

        this.setIfWorkerFromBrigadeWorkNow(true);

        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Zakończenie zlecenia o identyfikatorze - " + "(" + this.identyfikator + ")");
        this.dataZakonczenia = LocalDateTime.now();
        this.stanZlecenia = StanZlecenia.ZAKONCZONE;

        this.setIfWorkerFromBrigadeWorkNow(false);
    }

    @Override
    public String toString() {
        return "{Klasa Zlecenie o identyfiktorze - " + "(" + this.identyfikator + ")," +
                " Rodzaj zlecenia: " + this.rodzajZlecenia + "," +
                " Stan zlecenia: " + this.stanZlecenia + "." +
                " Data utworzenia zlecenia: " + this.dataUtworzenia + "," +
                " Data realizacji zelcenia: " + this.dataRealizacji + "," +
                " Data ukonczenia zelcenia: " + this.dataUtworzenia + "}";
    }
}
