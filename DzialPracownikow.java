import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DzialPracownikow implements FilesOperator, Serializable {
    private static long ID;
    public long identyfikator;

    private String nazwa;
    private final static List<DzialPracownikow> allDepartments = new ArrayList<>();
    private final static List<String> departmentNames = new ArrayList<>();
    private final List<Pracownik> workers = new ArrayList<>();

    private DzialPracownikow(String nazwa) {
        try {
            this.identyfikator = ID++;
            this.checkUniqueName(nazwa);
            this.nazwa = nazwa;
            departmentNames.add(nazwa);
            allDepartments.add(this);
            variableUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DzialPracownikow createDzial(String nazwa) {
        return new DzialPracownikow(nazwa);
    }

    public void addWorker(Pracownik pracownik) throws IOException {
        this.workers.add(pracownik);
        this.variableUpdate();
    }

    public List<Pracownik> getListPracownik() {
        return this.workers;
    }

    private void checkUniqueName(String departmentName) throws NotUniqueNameException {
        for(String name : departmentNames) {
            if(departmentName.equals(name)) throw new NotUniqueNameException(departmentName);
        }
    }

    @Override
    public void variableUpdate() throws IOException {
        saveDataToFile(this.getClass().getName(), allDepartments);
        loadDataFromFile(this.getClass().getName());
    }

    @Override
    public String toString() {
        return "{Klasa DziałPracowników o identyfikatorze - " + "(" + this.identyfikator + ")," +
                " Nazwa: " + this.nazwa + "," +
                " Lista pracowników w dziale: " + this.getListPracownik().toString() + "}";
    }
}
