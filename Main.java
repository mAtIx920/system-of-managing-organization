import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DzialPracownikow d1 = DzialPracownikow.createDzial("dzial1");
        Brygadzista b1 = new Brygadzista(
                "Mirek",
                "Bomba",
                "jazda",
                "start123",
                LocalDateTime.of(2000, 12, 10, 0, 0, 0),
                d1
        );

        Specjalista s1 = new Specjalista(
                "Mirek",
                "naprawa garniaków",
                "Bomba",
                LocalDateTime.of(2000, 12, 10, 0, 0, 0),
                d1
        );

        Uzytkownik u1 = new Uzytkownik(
                "Baśka",
                "jakas",
                "rura72634",
                "123456",
                LocalDateTime.of(2000, 12, 10, 0, 0, 0),
                d1
        );

        Brygada brygada = new Brygada("kopacze kryptowalut", b1);
        brygada.addWorker(s1);

        Praca p1 = new Praca(Praca.RodzajPracy.Montaz, 10, "praca1");
        Praca p2 = new Praca(Praca.RodzajPracy.Montaz, 7, "praca2");
        Praca p3 = new Praca(Praca.RodzajPracy.Montaz, 7, "praca3");
        Praca p4 = new Praca(Praca.RodzajPracy.Montaz, 10, "praca4");
        Praca p5 = new Praca(Praca.RodzajPracy.Montaz, 1, "praca5");
        Praca p6 = new Praca(Praca.RodzajPracy.Montaz, 7, "praca6");
        Praca p7 = new Praca(Praca.RodzajPracy.Montaz, 3, "praca7");


        Zlecenie zlecenie = new Zlecenie(true, brygada);
        zlecenie.addPraca(p1);
        zlecenie.addPraca(p2);
        zlecenie.addPraca(p3);
        zlecenie.addPraca(p4);
        zlecenie.addPraca(p5);
        zlecenie.addPraca(p6);
        zlecenie.addPraca(p7);
        
        zlecenie.rozpocznijZlecenie();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        zlecenie.rozpocznijZlecenie();
    }
}
