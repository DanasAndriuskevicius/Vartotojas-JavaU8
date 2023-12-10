import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public record Busena(ArrayList<Vartotojas> vartotojai,
                         int sekantisId) implements Serializable {
    }

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss");
    static Scanner in = new Scanner(System.in);
    static ArrayList<Vartotojas> vartotojai = new ArrayList<>();

    public static void main(String[] args) {

        //busenosUzkrovimas();
        busenosUzkrovimasText();

        // Test data
        /*vartotojai.add(
                new Vartotojas("Jonas", "asd", "jonas@gmail.com", Lytis.VYRAS, LocalDate.of(2000, 1, 2))
        );
        vartotojai.add(
                new Vartotojas("Ana", "qwe", "ana@gmail.com", Lytis.MOTERIS, LocalDate.of(2002, 5, 3))
        );*/
        // End of test data


        menu:
        while (true) {
            System.out.print("""
                                        
                    ┌───────────────────────────────────┐
                    │           MENIU                   │
                    ├───────────────────────────────────┤
                    │ 1 - Ivesti vartotoja              │
                    │ 2 - Pakeisti esama                │
                    │ 3 - Trinti/deaktivuoti            │
                    │ 4 - Spaudinti vartotojus i ekrana │
                    │ 5 - Spaudinti vartotojus i faila  │
                    │ 6 - Baigti programa               │
                    │ Ka norite daryti:\s""");
            int pasirinkimas = 0;
            try {
                pasirinkimas = in.nextInt();
            } catch (InputMismatchException e) {
                in.next();
            }
            System.out.println("└───────────────────────────────────┘");
            switch (pasirinkimas) {
                case 1 -> vartotojoIvedimas();
                case 2 -> vartotojoKoregavimas();
                case 3 -> vartotojoTrinimas();
                case 4 -> vartotojuSpausdinimas();
                case 5 -> vartotojuSpausdinimasIFaila();
                case 6 -> {
                    System.out.println("Programa baigia darba");
                    break menu;
                }
                default -> System.out.println("Blogas pasirinkimas");
            }
        }
        in.close();

        //busenosIrasimas();
        busenosIrasimasText();
    }

    private static void busenosUzkrovimas() {
        try {
            FileInputStream fis = new FileInputStream("busena.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Busena busena = (Busena) ois.readObject();
            vartotojai = busena.vartotojai();
            Vartotojas.setVartototojuKiekis(busena.sekantisId());

            ois.close();
            System.out.println("Busena uzkrauta is failo");
        } catch (Exception e) {
            System.out.println("Busenos uzkrauti is failo nepavyko!");
        }
    }

    public static void busenosIrasimas() {
        try {
            FileOutputStream fs = new FileOutputStream("busena.dat");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            Busena busena = new Busena(vartotojai, Vartotojas.getVartototojuKiekis());
            os.writeObject(busena);
            System.out.println("Busena irasyta i faila.");
            os.close();
            fs.flush();
            fs.close();
        } catch (IOException e) {
            System.out.println("Nepavyko issaugoti busenos!");
        }

    }

    private static void busenosUzkrovimasText() {
        try {
            FileInputStream fis = new FileInputStream("busena.sav");
            String content = new String(fis.readAllBytes());
            String[] vartotojaiStr = content.split("\n");
            for (String vartStr : vartotojaiStr) {
                Vartotojas vart = Vartotojas.fromCSV(vartStr);
                vartotojai.add(vart);
            }
            fis.close();
            System.out.println("Busena uzkrauta is failo");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Busenos uzkrauti is failo nepavyko!");
        }
    }

    public static void busenosIrasimasText() {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("busena.sav"));
            for (Vartotojas vartotojas : vartotojai) {
                bos.write(vartotojas.toCSV().getBytes());
                bos.write('\n');
            }
            System.out.println("Busena irasyta i faila.");
            bos.flush();
            bos.close();
        } catch (IOException e) {
            System.out.println("Nepavyko issaugoti busenos!");
        }

    }

    // Trinama pagal eiles numeri ArrayList
    public static void vartotojoTrinimasPagalIndeksa() {
        vartotojuSpausdinimas();
        System.out.print("\tKuri vartotoja norite trinti: ");
        int i = -1;
        try {
            i = in.nextInt();
        } catch (InputMismatchException e) {
            in.next();
        }
        if (i >= 0 && i < vartotojai.size()) {
            vartotojai.remove(i);
            System.out.println("\tVartotojas istrintas");
        } else {
            System.out.println("Tokio vartotojo nera!");
        }
    }


    // Trinama pagal Vartotojas objekto id lauka
    public static void vartotojoTrinimas() {
        vartotojuSpausdinimas();
        System.out.print("\tIveskite vartotojo id: ");
        int id = -1;
        try {
            id = in.nextInt();
        } catch (InputMismatchException e) {
            in.next();
        }
        for (Vartotojas vartotojas : vartotojai) {
            if (vartotojas.getId() == id) {
                System.out.print("""
                        1 - Trinti
                        2 - Deaktivuoti
                        3 - Atsaukti
                        Jusu pasirinkimas:\s""");
                int pasirinkimas = 3;
                try {
                    pasirinkimas = in.nextInt();
                } catch (InputMismatchException e) {
                    in.next();
                    System.out.println("Ivestas ne skaicius!");
                }
                switch (pasirinkimas) {
                    case 1 -> {
                        vartotojai.remove(vartotojas);
                        System.out.println("\tVartotojas istrintas!");
                    }
                    case 2 -> {
                        vartotojas.setAktyvus(false);
                        System.out.println("\tVartotojas deaktivuotas!");
                    }
                }
                break;
            }
        }

        /*for (int i = 0; i < vartotojai.size(); i++) {
            if (vartotojai.get(i).getId() == id) {
                vartotojai.remove(i);
                break;
            }
        }*/

        System.out.println("Tokio vartotojo nera!");
    }

    public static void vartotojoKoregavimas() {
        vartotojuSpausdinimas();
        System.out.print("Kuri vartotoja norite keisti: ");
        int id = -1;

        try {
            id = in.nextInt();
        } catch (InputMismatchException e) {
            in.next();
        }

        for (Vartotojas vart : vartotojai) {
            if (vart.getId() == id) {
                if (vart.isAktyvus())
                    vartotojoKeitimasMeniu(vart);
                else {
                    System.out.println("\tIsjungta vartotoja draudziama modifikuoti!");
                    vartotojoReaktivacija(vart);
                }
                return;
            }
        }
        System.out.println("Tokio vartotojo nera!");

    }

    public static void vartotojoReaktivacija(Vartotojas vart) {
        System.out.print("\tAr norite vartotoja reaktivuoti(t/n): ");
        String pasirinkimas = in.next();
        if (pasirinkimas.equalsIgnoreCase("t")) {
            vart.setAktyvus(true);
            System.out.println("\tVartotojas reaktivuotas!");
        } else {
            System.out.println("\tAtsaukiama..");
        }
    }

    public static void vartotojuSpausdinimas() {
        // Zemiau esantis enhanced for ciklas is esmes atstoja sitoki for cikla
        /*for (int i = 0; i < vartotojai.length; i++) {
            Vartotojas v = vartotojai[i];
            // CIKLO VIDUS
        }*/

        for (int i = 0; i < vartotojai.size(); i++) {
            Vartotojas vart = vartotojai.get(i);
            System.out.println(i + " | " + vart);
        }
    }

    public static void vartotojuSpausdinimasIFaila() {
        try {
            String fileName = String.format("vartotojai_%s.log", LocalDateTime.now().format(dtf));
            BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream(fileName));
            for (int i = 0; i < vartotojai.size(); i++) {
                Vartotojas vart = vartotojai.get(i);
                String line = i + " | " + vart + "\n";
                bs.write(line.getBytes());
            }
            bs.flush();
            bs.close();
            System.out.println("Failas irasytas!");
        } catch (IOException e) {
            System.out.println("Nepavyko irasyti duomenu i faila!");
        }
    }

    public static void vartotojoIvedimas() {
        System.out.println("Iveskite " + (vartotojai.size() + 1) + " vartotoja:");

        String vardas = vardoIvestis();
        String slaptazodis = slaptazodzioIvestis();
        String email = emailIvestis();
        Lytis lytis = lytiesIvestis();
        LocalDate gimimoData = gimimoDatosIvestis();

        vartotojai.add(new Vartotojas(vardas, slaptazodis, email, lytis, gimimoData));
    }

    @SuppressWarnings("unused")
    public static void vartotojoKeitimas(Vartotojas vart) {
        String ats;
        System.out.println("Iveskite vartotoja:");

        System.out.print("Dabartinis vardas: " + vart.getVardas() + ". Ar norite keisti varda(t/n): ");
        ats = in.next();
        if (ats.equalsIgnoreCase("t"))
            vart.setVardas(vardoIvestis());

        System.out.print("Ar norite keisti slaptazodi(t/n): ");
        ats = in.next();
        if (ats.equalsIgnoreCase("t"))
            vart.setSlaptazodis(slaptazodzioIvestis());

        System.out.print("Dabartinis email: " + vart.getEmail() + ". Ar norite keisti email(t/n): ");
        ats = in.next();
        if (ats.equalsIgnoreCase("t"))
            vart.setEmail(emailIvestis());

        System.out.print("Dabartine lytis: " + vart.getLytis() + ". Ar norite keisti lyti(t/n): ");
        ats = in.next();
        if (ats.equalsIgnoreCase("t"))
            vart.setLytis(lytiesIvestis());
    }

    public static void vartotojoKeitimasMeniu(Vartotojas vart) {
        menu:
        while (true) {
            System.out.print("""
                                        
                    ┌──────────────────────────┐
                    │           MENIU          │
                    ├──────────────────────────┤
                    │ Ka norite keisti?        │
                    │ 1 - varda                │
                    │ 2 - slaptazodi           │
                    │ 3 - emaila               │
                    │ 4 - lyti                 │
                    │ 5 - gryzti i pradzia     │
                    │ Jusu pasirinkimas:\s""");
            int laukas = 0;
            try {
                laukas = in.nextInt();
            } catch (InputMismatchException e) {
                in.next();
            }
            System.out.println("└──────────────────────────┘");
            switch (laukas) {
                case 1 -> vart.setVardas(vardoIvestis());
                case 2 -> vart.setSlaptazodis(slaptazodzioIvestis());
                case 3 -> vart.setEmail(emailIvestis());
                case 4 -> vart.setLytis(lytiesIvestis());
                case 5 -> {
                    break menu;
                }
                default -> System.out.println("Blogas pasirinkimas!");
            }
        }
    }

    public static int idIvestis() {
        int id;

        while (true) {
            System.out.print("\tIveskite id: ");
            try {
                id = in.nextInt();
                break;
            } catch (InputMismatchException e) {
                in.next();
                System.out.println("\tBlogas id formatas!");
            }
        }
        return id;
    }

    public static String vardoIvestis() {
        String vardas;
        while (true) {
            System.out.print("\tIveskite varda: ");
            vardas = in.next();
            if (vardas.length() < 3)
                System.out.println("\tVardas per trumpas!");
            else if (vardas.length() > 15)
                System.out.println("\tVardas per ilgas!");
            else break;
        }
        return vardas;
    }

    public static String slaptazodzioIvestis() {
        String slaptazodis;
        String slaptazodis2;

        while (true) {
            System.out.print("\tIveskite slaptazodi: ");
            slaptazodis = in.next();

            System.out.print("\tIveskite slaptazodi(dar karta): ");
            slaptazodis2 = in.next();
            if (!slaptazodis.equals(slaptazodis2))
                System.out.println("\tSlaptazodziai nesutampa!");
            else break;
        }
        return slaptazodis;
    }

    public static String emailIvestis() {
        String email;
        while (true) {
            System.out.print("\tIveskite email: ");
            email = in.next();
            if (email.indexOf('@') < 1 || email.indexOf('@') != email.lastIndexOf('@'))
                System.out.println("\tNeteisingas email formatas!");
            else break;
        }
        return email;
    }

    public static Lytis lytiesIvestis() {
        System.out.print("\tIveskite lyti: ");
        String lytisStr = in.next().toUpperCase();
        Lytis lytis = switch (lytisStr) {
            case "VYRAS" -> Lytis.VYRAS;
            case "MOTERIS" -> Lytis.MOTERIS;
            default -> Lytis.NEZINOMA;
        };

        if (lytis.equals(Lytis.NEZINOMA))
            System.out.println("\tNeteisinga lytis");

        return lytis;
    }

    public static LocalDate gimimoDatosIvestis() {
        LocalDate gimimoData;
        while (true) {
            System.out.print("\tIveskite savo gimimo data: ");
            String gimimoDataStr = in.next();

            try {
                gimimoData = LocalDate.parse(gimimoDataStr);
                if (gimimoData.isAfter(LocalDate.now())) {
                    System.out.println("\tGimimo data velesne nei siandiena!");
                } else break;
            } catch (DateTimeParseException e) {
                System.out.println("\tNeteisingas datos formatas");
            }
        }
        return gimimoData;
    }
}