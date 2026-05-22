import java.util.ArrayList;

public class Rummikub implements NormesBasiques {
    public static ArrayList<ArrayList<Carta>> taulaComuna = new ArrayList<>();
    public static void jugarRummiKub(){
        Rummikub jocActual = new Rummikub();

        boolean hiHaGuanyador = false;
        boolean esDarreraRonda = false;
        int tornsDarreraRonda = Joc.arrayJugadors.length;

        while (!hiHaGuanyador) {
            Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
            Consola.tornDe(jugadorActual.nom);
            Consola.espais();
            Consola.missatgeCartes();
            Consola.espais();
            Consola.mostrarBaralla(jugadorActual.maCartes);

            if (esDarreraRonda) { Consola.missatgeDarreraRonda(); }

            Consola.demanarAccio(jugadorActual);

            hiHaGuanyador = jocActual.haGuanyat(jugadorActual);

            if (!hiHaGuanyador && Joc.barallaPartida.baralla.isEmpty() && !esDarreraRonda) {
                esDarreraRonda = true;
            }

            if (esDarreraRonda & !hiHaGuanyador) {
                tornsDarreraRonda --;

                if (tornsDarreraRonda == 0) {
                    hiHaGuanyador = determinarGuanyador();
                }
            }

            if (!hiHaGuanyador) {
                Torn.calcularTorn(Joc.arrayJugadors.length);
            }
        }
    }

    public static boolean determinarGuanyador() {
        return true; // falta implementar
    }

    @Override
    public boolean haGuanyat(Jugador jugador){
        if (jugador.maCartes.isEmpty()){
            return true;
        }
        return false;
    }

    public boolean esCombinacioValida(){
        return true;
    }
}
