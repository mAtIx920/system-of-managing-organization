public class NotUniqueNameException extends Exception {
    NotUniqueNameException(String departmentName) {
        super("Dział o nazwie" + " '" + departmentName + "' " + "już istnieje");
    }
}
