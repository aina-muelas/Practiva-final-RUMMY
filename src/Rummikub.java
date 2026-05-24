import java.util.ArrayList;

public class Rummikub implements NormesBasiques {
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
        ArrayList<ArrayList<Carta>> combinacionsNoves = Consola.demanarNovesCombinacions(jugador);

        if (combinacionsNoves.isEmpty()) {
            return false;
        }

        int puntsTotals = 0;
        for (int i = 0; i < combinacionsNoves.size(); i++) {
            ArrayList<Carta> combinacioActual = combinacionsNoves.get(i);

            if (!esCombinacioValida(combinacioActual)) {
                Consola.missatgeCombinacioNoValida();
                return false;
            }
            puntsTotals += comptarPuntsCombinacio(combinacioActual);
        }

        if (!jugador.haFetPrimeraTirada) {
            Consola.missatgePuntsMinimsTirar();

            if (puntsTotals >= 30) {
                jugador.haFetPrimeraTirada = true;
                Consola.missatgeCombinacioValida();

                for (int i = 0; i < combinacionsNoves.size(); i++) {
                    ArrayList<Carta> combinacio = combinacionsNoves.get(i);
                    taulaComuna.add(combinacio);

                    for (int j = 0; j < combinacio.size(); j++) {
                        Carta cartaEliminar = combinacio.get(j);
                        jugador.maCartes.remove(cartaEliminar);
                    }
                }
                return true;
            } else {
                Consola.missatgeMinimPuntsIncorrecte(puntsTotals);
                return false;
            }
        } else {
            Consola.missatgeJugadaAcceptada();
            for (int i = 0; i < combinacionsNoves.size(); i++) {
                ArrayList<Carta> combinacio = combinacionsNoves.get(i);
                taulaComuna.add(combinacio);

                for (int j = 0; j < combinacio.size(); j++) {
                    Carta cartaELiminar = combinacio.get(j);
                    jugador.maCartes.remove(cartaELiminar);
                }
            }
            return true;
        }
    }

    boolean esGrupValid(ArrayList<Carta> combinacio) {
        if (combinacio.size() > 4) {
            return false;
        }
        for (int i = 0; i < combinacio.size() - 1; i++) {
            if (combinacio.get(i).esJoker()) continue;

            for (int j = i + 1; j < combinacio.size(); j++) {
                if (combinacio.get(j).esJoker()) continue;

                if (combinacio.get(i).getValor() != combinacio.get(j).getValor()) {
                    return false;
                }
                if (combinacio.get(i).getPalOColor().equals(combinacio.get(j).getPalOColor())) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean esEscalaValida(ArrayList<Carta> combinacio) {
        String colorEscala = "";

        for (int i = 0; i < combinacio.size(); i++) {
            if (combinacio.get(i).esJoker()) continue;
            colorEscala = combinacio.get(i).getPalOColor();
            break;
        }

        for (int j = 0; j < combinacio.size(); j++) {
            if (combinacio.get(j).esJoker()) continue;

            if (!combinacio.get(j).getPalOColor().equals(colorEscala)) {
                return false;
            }
        }

        int indexPrimeraFitxaReal = -1;
        for (int i = 0; i < combinacio.size(); i++) {
            if (combinacio.get(i).esJoker()) continue;

            if (indexPrimeraFitxaReal == -1) {
                indexPrimeraFitxaReal = i;
            } else {
                int valorActual = combinacio.get(i).getValor();
                int valorAnterior = combinacio.get(indexPrimeraFitxaReal).getValor();

                int diferenciaValors = valorActual - valorAnterior;
                int diferenciaPosicions = i - indexPrimeraFitxaReal;

                if (diferenciaValors != diferenciaPosicions) {
                    return false;
                }
            }
        }

        return true;
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

    @Override
    public boolean haGuanyat(Jugador jugador) {
        if (jugador.maCartes.isEmpty()) {
            Consola.missatgeGuanyador(jugador);
            return true;
        }
        return false;
    }

    public boolean esCombinacioValida(ArrayList<Carta> combinacio) {
        if (combinacio == null || combinacio.size() < 3) {
            return false;
        } else if (esGrupValid(combinacio) || esEscalaValida(combinacio)) {
            return true;
        }
        return false;
    }
}
