import java.util.ArrayList;
import java.util.Collections;

public class Baralla {
    ArrayList<Carta> baralla = new ArrayList<Carta>();
    final String[] numerosFrancesa = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "AS"};
    final String joker = "Joker";
    final String[] palsFrancesa = {"Pica", "Trevol", "Cor", "Diamant"};

    final String[] numerosRummiKub = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    final String[] colorsRummiKub = {"Vermell", "Blau", "Groc", "Negre"};


    public void inicialitzarBaralla(int modalitat, int numBaralles) {
        baralla.clear(); // 1. Netegem sempre primer
        if (modalitat == 2) {
            crearCartesRummiKub();
        }
        else {
            for (int i = 0; i < numBaralles; i++){
                crearBarallaFrancesa(modalitat);
            }
        }
    }

    public void crearBarallaFrancesa( int modalitat) {
        for (int pal = 0; pal < palsFrancesa.length; pal++) {
            for (int num = 0; num < numerosFrancesa.length; num++) {
                baralla.add(new Carta(numerosFrancesa[num], palsFrancesa[pal]));
            }
        }
        if (modalitat != 3) {
            baralla.add(new Carta(joker, "Comodí"));
            baralla.add(new Carta(joker, "Comodí"));
        }
    }

    public void crearCartesRummiKub() {
        for (int i = 0; i < 2; i++) {
            for (int color = 0; color < colorsRummiKub.length; color++) {
                for (int num = 0; num < numerosRummiKub.length; num++) {
                    baralla.add(new Carta(numerosRummiKub[num], colorsRummiKub[color]));
                }
            }
        }
        baralla.add(new Carta(joker, "Comodí"));
        baralla.add(new Carta(joker, "Comodí"));
    }

    static int determinarQuantitatBaralles(int modalitat, int numJugadors){
        if (modalitat == 1) {
            return 2;
        } else if (modalitat == 2) {
            return 2;
        } else if (modalitat == 3){
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

    public void mesclarCartes(){
        Collections.shuffle(this.baralla);
    }

}