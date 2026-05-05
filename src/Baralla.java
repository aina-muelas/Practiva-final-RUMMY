import java.util.ArrayList;

public class Baralla {
    ArrayList <Carta> baralla = new ArrayList<Carta>();
    final String[] numeros = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "AS"};
    final String joker = "Joker";
    final String[] palsNegres = {"Pica", "Trevol"};
    final String[] palsVermells = {"Cor", "Diamant"};

    public void crearCartes() {
        for (int pal = 0; pal < palsNegres.length; pal++) {
            for ( int num = 0; num < numeros.length; num++){
                new Carta(numeros[num], palsNegres[pal]);
                baralla.add(new Carta(numeros[num], palsNegres[pal], "negre"));
            }
        }

        for (int pal = 0; pal < palsVermells.length; pal++) {
            for ( int num = 0; num < numeros.length; num++){
                new Carta(numeros[num], palsVermells[pal]);
                baralla.add(new Carta(numeros[num], palsVermells[pal], "vermell"));
            }
        }
        baralla.add(new Carta(joker, "negre"));
        baralla.add(new Carta(joker, "vermell"));

    }
}
