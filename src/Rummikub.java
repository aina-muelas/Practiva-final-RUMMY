import java.util.ArrayList;

public class Rummikub implements NormesBasiques {
    public static ArrayList<ArrayList<Carta>> taulaComuna = new ArrayList<>();

    public static void jugarRummiKub() {
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

            if (esDarreraRonda) {
                Consola.missatgeDarreraRonda();
            }

            Consola.demanarAccio(jugadorActual);

            hiHaGuanyador = jocActual.haGuanyat(jugadorActual);

            if (!hiHaGuanyador && Joc.barallaPartida.baralla.isEmpty() && !esDarreraRonda) {
                esDarreraRonda = true;
            }

            if (esDarreraRonda & !hiHaGuanyador) {
                tornsDarreraRonda--;

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
        int fitxesMinimes = Joc.arrayJugadors[0].getMaCartes().size();
        Jugador jugadorGuanyador = Joc.arrayJugadors[0];
        int puntsMinims = comptarPunts(Joc.arrayJugadors[0]);

        for (int i = 1; i < Joc.arrayJugadors.length; i++) {
            if (Joc.arrayJugadors[i].getMaCartes().size() < fitxesMinimes) {
                fitxesMinimes = Joc.arrayJugadors[i].getMaCartes().size();
                jugadorGuanyador = Joc.arrayJugadors[i];
                puntsMinims = comptarPunts(Joc.arrayJugadors[i]);
            } else if (Joc.arrayJugadors[i].getMaCartes().size() == fitxesMinimes) {
                int puntsJugadorActual = comptarPunts(Joc.arrayJugadors[i]);
                if (puntsJugadorActual < puntsMinims) {
                    jugadorGuanyador = Joc.arrayJugadors[i];
                    puntsMinims = puntsJugadorActual;
                }
            }
        }
        Consola.missatgeGuanyador(jugadorGuanyador);
        return true;
    }

    public static int comptarPunts(Jugador jugador) {
        int numPunts = 0;
        Carta cartaActual;
        for (int i = 0; i < jugador.maCartes.size(); i++) {
            cartaActual = jugador.getMaCartes().get(i);
            if (cartaActual.esJoker()) {
                numPunts += 30;
            } else {
                numPunts += cartaActual.getValor();
            }
        }
        return numPunts;
    }

    @Override
    public boolean haGuanyat(Jugador jugador) {
        if (jugador.maCartes.isEmpty()) {
            Consola.missatgeGuanyador(jugador);
            return true;
        }
        return false;
    }

    public boolean esCombinacioValida() {
        return true;
    }
}
