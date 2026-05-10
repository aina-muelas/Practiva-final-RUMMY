import java.util.ArrayList;

public class Baralla {
    ArrayList<Carta> baralla = new ArrayList<Carta>();
    final String[] numerosFrancesa = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "AS"};
    final String joker = "Joker";
    final String[] palsFrancesa = {"Pica", "Trevol", "Cor", "Diamant"};

    final String[] numerosRummiKub = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    final String[] colorsRummiKub = {"Vermell", "Blau", "Groc", "Negre"};


    public void crearCartesRummy() {
        for (int pal = 0; pal < palsFrancesa.length; pal++) {
            for (int num = 0; num < numerosFrancesa.length; num++) {
                baralla.add(new Carta(numerosFrancesa[num], palsFrancesa[pal]));
            }
        }

        baralla.add(new Carta(joker));
        baralla.add(new Carta(joker));
    }

    public void crearCartesRummiKub() {
        for (int i = 0; i < 2; i++) {
            for (int color = 0; color < colorsRummiKub.length; color++) {
                for (int num = 0; num < numerosRummiKub.length; num++) {
                    baralla.add(new Carta(numerosRummiKub[num], colorsRummiKub[color]));
                }
            }
        }
        baralla.add(new Carta(joker));
        baralla.add(new Carta(joker));
    }
}