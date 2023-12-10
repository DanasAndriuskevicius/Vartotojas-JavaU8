import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vartotojas implements Serializable {

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static int vartototojuKiekis = 0;
    private int id;
    private String vardas;
    private String slaptazodis;
    private String email;
    private Lytis lytis;
    private final LocalDateTime regData;
    private final LocalDate gimimoData;
    private boolean aktyvus = true;

    public Vartotojas(String vardas, String slaptazodis, String email, Lytis lytis, LocalDate gimimoData) {
        this.id = vartototojuKiekis;
        this.vardas = vardas;
        this.slaptazodis = slaptazodis;
        this.email = email;
        this.lytis = lytis;
        this.regData = LocalDateTime.now();
        this.gimimoData = gimimoData;
        vartototojuKiekis++;
    }

    /* ALL ARGS CONSTRUCTOR */
    private Vartotojas(int id, String vardas, String slaptazodis, String email, Lytis lytis, LocalDateTime regData, LocalDate gimimoData, boolean aktyvus) {
        this.id = id;
        this.vardas = vardas;
        this.slaptazodis = slaptazodis;
        this.email = email;
        this.lytis = lytis;
        this.regData = regData;
        this.gimimoData = gimimoData;
        this.aktyvus = aktyvus;
    }

    public static Vartotojas fromCSV(String text) {
        String[] tokens = text.split(",");
        int id = Integer.parseInt(tokens[0]);
        String vardas = tokens[1];
        String slaptazodis = tokens[2];
        String email = tokens[3];
        Lytis lytis = Lytis.valueOf(tokens[4]);
        LocalDateTime regData = LocalDateTime.parse(tokens[5]);
        LocalDate gimimoData = LocalDate.parse(tokens[6]);
        boolean aktyvus = Boolean.parseBoolean(tokens[7]);
        return new Vartotojas(id, vardas, slaptazodis, email, lytis, regData, gimimoData, aktyvus);
    }

    public String toCSV() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%b",
                id, vardas, slaptazodis, email, lytis, regData, gimimoData, aktyvus);
    }

    //<editor-fold desc="Setters/Getters/Etc">

    public static int getVartototojuKiekis() {
        return vartototojuKiekis;
    }

    public static void setVartototojuKiekis(int vartototojuKiekis) {
        Vartotojas.vartototojuKiekis = vartototojuKiekis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVardas() {
        return vardas;
    }

    public void setVardas(String vardas) {
        this.vardas = vardas;
    }

    public String getSlaptazodis() {
        return slaptazodis;
    }

    public void setSlaptazodis(String slaptazodis) {
        this.slaptazodis = slaptazodis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Lytis getLytis() {
        return lytis;
    }

    public void setLytis(Lytis lytis) {
        this.lytis = lytis;
    }

    public LocalDateTime getRegData() {
        return regData;
    }

    public LocalDate getGimimoData() {
        return gimimoData;
    }

    public boolean isAktyvus() {
        return aktyvus;
    }

    public void setAktyvus(boolean aktyvus) {
        this.aktyvus = aktyvus;
    }

    @Override
    public String toString() {
        return String.format("[%s] Id: %d | Vardas: %s | Slaptazodis: %s | Email: %s | Lytis: %s | Reg.Data: %s | Gimimo data: %s",
                aktyvus ? "🟢" : "🔴", id, vardas, slaptazodis, email, lytis, regData.format(dtf), gimimoData.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    //</editor-fold>
}
