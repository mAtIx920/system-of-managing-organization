import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Praca extends Thread implements FilesOperator, Serializable {
    private static long ID;
    public final long identyfikator;

    public enum RodzajPracy {Ogolna, Montaz, Demontaz, Wymiana};
    public final List<Praca> worksQueue = new ArrayList<>();
    public static final HashMap<Long, Praca> allWorks = new HashMap<>();

    private final RodzajPracy rodzajPracy;
    public Praca nextWorkInQueue;
    private final int czasPracy;
    public boolean czyZrealizowane = false;
    private final String opis;
    private long orderId;

    Praca(RodzajPracy rodzajPracy, int czasPracy, String opis) throws IOException {
        this.rodzajPracy = rodzajPracy;
        this.czasPracy = czasPracy;
        this.opis = opis;
        this.orderId = -1; // -1 means that this work is available and not busy by some order
        this.identyfikator = ID++;
        allWorks.put(this.identyfikator, this);
        this.variableUpdate();
    }

    void setNextWork(Praca work) {
        this.nextWorkInQueue = work;
    }

    void setOrderId(long id) {
        this.orderId = id;
    }

    boolean isQueueWorksDone() {
        return this.worksQueue.stream().anyMatch(praca -> !praca.czyZrealizowane);
    }

    boolean isWorkDone() {
        return this.czyZrealizowane;
    }

    void addWorksToQueue(List<Praca> works) {
        this.worksQueue.addAll(works);
    }

    void addWorkToQueue(Praca work) {
        this.worksQueue.add(work);
    }

    String getWorksQueue() {
       return this.worksQueue.toArray().toString();
    }

    long getWorkOrderAssign() {
        return this.orderId;
    }

    @Override
    public void run() {
        synchronized (this) {
            while(this.isQueueWorksDone()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Odpalenie pracy o identyfikatorze - " + "(" + this.identyfikator + ")");

        try {
            synchronized (this) {
                System.out.println(this);
                System.out.println("Przetwarzanie danych...");
                Thread.sleep(this.czasPracy * 1000);
                System.out.println("Zakonczenie pracy o identyfikatorze - " + "(" + this.identyfikator + ")");
                this.czyZrealizowane = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(this.nextWorkInQueue != null) {
            synchronized (this.nextWorkInQueue) {
                this.nextWorkInQueue.notify();
            }
        } else {
            synchronized (Zlecenie.allOrders.get(orderId)) {
                Zlecenie.allOrders.get(orderId).notify();
            }
        }

        this.setOrderId(-1); // -1 means that this work is available and not busy by some order
    }

    @Override
    public void variableUpdate() throws IOException {
        saveDataToFile(this.getClass().getName(), allWorks);
        loadDataFromFile(this.getClass().getName());
    }

    @Override
    public String toString() {
        return "{Klasa Praca o identyfikatorze - " + "(" + this.identyfikator + ")," +
                " Rodzaj pracy: " + this.rodzajPracy + "," +
                " Czas na wykonanie pracy: " + this.czasPracy + "s," +
                " Opis pracy: " + this.opis + "}";
    }

}
