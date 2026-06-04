import java.util.ArrayList;
import java.util.Collections;

public class Baralla {
    ArrayList<Carta> baralla = new ArrayList<Carta>();
    final String[] numerosFrancesa = {"AS", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    final int[] valorsFrancesa = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
    final String joker = "Joker";
    final String[] palsFrancesa = {"Pica", "Trevol", "Cor", "Diamant"};

    final String[] colorsRummiKub = {"Vermell", "Blau", "Groc", "Negre"};

    final int RUMMY_CLASSIC = 1;
    final int RUMMIKUB = 2;
    final int GIN_RUMMY = 3;
    final int RUMMY_ARGENTINO = 4;

    public void inicialitzarBaralla(int modalitat, int numBaralles) {
        baralla.clear();
        if (modalitat == RUMMIKUB) {
            crearBarallaRummiKub();
        } else if (modalitat == RUMMY_ARGENTINO) {
            for (int i = 0; i < numBaralles; i++) {
                crearBarallaRummyArgentino();
            }
        } else {
            for (int i = 0; i < numBaralles; i++) {
                crearBarallaFrancesaEstandard();
            }
        }
    }

    private void crearBarallaFrancesaEstandard() {
        for (int pal = 0; pal < palsFrancesa.length; pal++) {
            for (int num = 0; num < numerosFrancesa.length; num++) {
                baralla.add(new Carta(numerosFrancesa[num], palsFrancesa[pal], valorsFrancesa[num]));
            }
        }
    }

    private void crearBarallaRummyArgentino() {
        for (int pal = 0; pal < palsFrancesa.length; pal++) {
            for (int num = 0; num < numerosFrancesa.length; num++) {
                baralla.add(new Carta(numerosFrancesa[num], palsFrancesa[pal], valorsFrancesa[num]));
            }
        }
        baralla.add(new Carta(joker, "Comodí", 0));
        baralla.add(new Carta(joker, "Comodí", 0));
    }

    private void crearBarallaRummiKub() {
        for (int i = 0; i < 2; i++) {
            for (int color = 0; color < colorsRummiKub.length; color++) {
                for (int num = 1; num <= 13; num++) {
                    baralla.add(new Carta(String.valueOf(num), colorsRummiKub[color], num));
                }
            }
        }
        baralla.add(new Carta(joker, "Comodí", 0));
        baralla.add(new Carta(joker, "Comodí", 0));
    }

    static int determinarQuantitatBaralles(int modalitat, int numJugadors) {
        if (modalitat == 1) {
            return 2;
        } else if (modalitat == 2) {
            return 2;
        } else if (modalitat == 3) {
            return 1;
        } else if (modalitat == 4) {
            if (numJugadors > 4) {
                return 2;
            } else {
                return 1;
            }
        }
        return 1;
    }

    public void mesclarCartes() {
        Collections.shuffle(this.baralla);
    }

}