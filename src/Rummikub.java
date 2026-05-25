import java.util.ArrayList;

public class Rummikub extends Normes {
    public static ArrayList<ArrayList<Carta>> taulaComuna = new ArrayList<>();

    public static void jugarRummiKub() {
        Rummikub jocActual = new Rummikub();

        boolean hiHaGuanyador = false;
        boolean esDarreraRonda = false;
        int tornsDarreraRonda = Joc.arrayJugadors.length;

        while (!hiHaGuanyador) {
            Consola.missatgeMostrarTaula();
            Consola.mostrarTaulaComuna(taulaComuna);

            boolean accioCompletada = false;
            int accio;

            Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
            Consola.tornDe(jugadorActual.nom);
            Consola.espais();
            Consola.missatgeCartes();
            Consola.mostrarBaralla(jugadorActual.maCartes);
            Consola.espais();

            if (esDarreraRonda) {
                Consola.missatgeDarreraRonda();
            }

            while (!accioCompletada) {
                accio = Consola.demanarAccio(jugadorActual);

                if (accio == 1) {
                    accioCompletada = jocActual.agafarFitxa(jugadorActual);
                } else if (accio == 2) {
                    accioCompletada = jocActual.tirarFitxes(jugadorActual);
                }
            }

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

    boolean agafarFitxa(Jugador jugador) {
        if (!Joc.barallaPartida.baralla.isEmpty()) {
            Carta cartaRobada = Joc.barallaPartida.baralla.remove(0);
            jugador.maCartes.add(cartaRobada);
            Consola.mostrarCartaRobada(cartaRobada);
            return true;
        } else {
            Consola.missatgeBarallaBuida();
        }
        return false;
    }

    boolean tirarFitxes(Jugador jugador) {
        boolean seguirCreant = true;
        ArrayList<ArrayList<Carta>> combinacionsNoves = new ArrayList<ArrayList<Carta>>();
        int puntsTotals = 0;

        while (seguirCreant && !jugador.getMaCartes().isEmpty()) {
            ArrayList<Carta> combinacioNova = Consola.demanarNovaCombinacio(jugador);

            if (combinacioNova.isEmpty()) {
                return false;
            }

            if (!esCombinacioValida(combinacioNova)) {
                Consola.missatgeCombinacioNoValida();
                return false;
            } else {
                combinacionsNoves.add(combinacioNova);
                taulaComuna.add(combinacioNova);

                for (int j = 0; j < combinacioNova.size(); j++) {
                    Carta cartaELiminar = combinacioNova.get(j);
                    jugador.maCartes.remove(cartaELiminar);
                }
            }
            seguirCreant = Consola.seguirCreantCombinacions(jugador);
        }

        if (!jugador.haFetPrimeraTirada) {
            Consola.missatgePuntsMinimsTirar();

            for (int i = 0; i < combinacionsNoves.size(); i++) {
                ArrayList<Carta> combinacioActual = combinacionsNoves.get(i);
                puntsTotals += comptarPuntsCombinacio(combinacioActual);
            }

            if (puntsTotals >= 30) {
                jugador.haFetPrimeraTirada = true;
                Consola.missatgeCombinacioValida();

                for (int i = 0; i < combinacionsNoves.size(); i++) {
                    ArrayList<Carta> combinacio = combinacionsNoves.get(i);
                    taulaComuna.add(combinacio);
                }
                return true;
            } else {
                // fer el remove de les combinacions afegides d'aquest jugador
                Consola.missatgeMinimPuntsIncorrecte(puntsTotals);

                for (int i = 0; i < combinacionsNoves.size(); i++) {
                    ArrayList<Carta> combinacio = combinacionsNoves.get(i);
                    taulaComuna.add(combinacio);

                    for (int j = 0; j < combinacio.size(); j++) {
                        Carta cartaAfegir = combinacio.get(j);
                        jugador.maCartes.add(cartaAfegir);
                    }
                }
                return false;
            }
        } else {
            Consola.missatgeJugadaAcceptada();
            for (int i = 0; i < combinacionsNoves.size(); i++) {
                ArrayList<Carta> combinacio = combinacionsNoves.get(i);
                taulaComuna.add(combinacio);
            }
            return true;
        }
    }

    static boolean determinarGuanyador() {
        int fitxesMinimes = Joc.arrayJugadors[0].getMaCartes().size();
        Jugador jugadorGuanyador = Joc.arrayJugadors[0];
        int puntsMinims = comptarPuntsCasEmpat(Joc.arrayJugadors[0]);

        for (int i = 1; i < Joc.arrayJugadors.length; i++) {
            if (Joc.arrayJugadors[i].getMaCartes().size() < fitxesMinimes) {
                fitxesMinimes = Joc.arrayJugadors[i].getMaCartes().size();
                jugadorGuanyador = Joc.arrayJugadors[i];
                puntsMinims = comptarPuntsCasEmpat(Joc.arrayJugadors[i]);
            } else if (Joc.arrayJugadors[i].getMaCartes().size() == fitxesMinimes) {
                int puntsJugadorActual = comptarPuntsCasEmpat(Joc.arrayJugadors[i]);
                if (puntsJugadorActual < puntsMinims) {
                    jugadorGuanyador = Joc.arrayJugadors[i];
                    puntsMinims = puntsJugadorActual;
                }
            }
        }
        Consola.missatgeGuanyador(jugadorGuanyador);
        return true;
    }

    static int comptarPuntsCasEmpat(Jugador jugador) {
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

    int comptarPuntsCombinacio(ArrayList<Carta> combinacio) {
        int punts = 0;
        boolean esGrup = esGrupValid(combinacio);
        boolean esEscala = esEscalaValida(combinacio);

        int indexFitxaReal = -1;
        int valorFitxaReal = 0;

        for (int i = 0; i < combinacio.size(); i++) {
            if (!combinacio.get(i).esJoker()) {
                indexFitxaReal = i;
                valorFitxaReal = combinacio.get(i).getValor();
                break;
            }
        }

        for (int i = 0; i < combinacio.size(); i++) {
            if (!combinacio.get(i).esJoker()) {
                punts += combinacio.get(i).getValor();
            } else {
                if (esGrup) {
                    punts += valorFitxaReal;
                } else if (esEscala) {
                    punts += valorFitxaReal + (i - indexFitxaReal);
                }
            }
        }
        return punts;
    }
}
