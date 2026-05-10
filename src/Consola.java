import java.util.ArrayList;

public class Consola {
    public static void mostrarBaralla(ArrayList<Carta> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            Carta c = cartes.get(i);
            System.out.println((i + 1)+ ". " + c.toString());
        }
    }
}
