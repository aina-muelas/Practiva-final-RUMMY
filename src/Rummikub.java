import java.util.ArrayList;

public class Rummikub extends Normes {
    public static ArrayList<ArrayList<Carta>> taulaComuna = new ArrayList<>();

    public static void jugarRummiKub() {
        Rummikub jocActual = new Rummikub();

        boolean hiHaGuanyador = false;
        boolean esDarreraRonda = false;
        int tornsDarreraRonda = Joc.arrayJugadors.length;

        while (!hiHaGuanyador) {
            Consola.espais();
            Consola.missatgeMostrarTaula();
            Consola.mostrarTaulaComuna(taulaComuna);
            Consola.espais();

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
                Consola.imprimirNumFitxesBaralla(Joc.barallaPartida.baralla);
                Consola.espais();
                accio = Consola.demanarAccio(jugadorActual);

                if (accio == 1) {
                    accioCompletada = jocActual.agafarFitxa(jugadorActual);
                } else if (accio == 2) {
                    accioCompletada = jocActual.tirarFitxes(jugadorActual);
                } else if (accio == 3) {
                    if (jugadorActual.haFetPrimeraTirada) {
                        accioCompletada = jocActual.modificarTaula(jugadorActual);
                    } else {
                        Consola.missatgeModificacioNoPossible();
                    }
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
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());

        boolean seguirCreant = true;
        ArrayList<ArrayList<Carta>> combinacionsNoves = new ArrayList<ArrayList<Carta>>();
        int puntsTotals = 0;

        while (seguirCreant && !jugador.getMaCartes().isEmpty()) {
            ArrayList<Carta> combinacioNova = Consola.demanarNovaCombinacio(jugador);

            if (combinacioNova.isEmpty()) {
                break;
            }

            if (!esCombinacioValida(combinacioNova)) {
                Consola.missatgeCombinacioNoValida();
                jugador.maCartes.addAll(combinacioNova);
            } else {
                combinacionsNoves.add(combinacioNova);
            }
            seguirCreant = Consola.seguirCreantCombinacions(jugador);
        }

        if (combinacionsNoves.isEmpty()) {
            jugador.maCartes.clear();
            jugador.maCartes.addAll(copiaMaInicial);
            return false;
        }

        if (!jugador.haFetPrimeraTirada) {

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
                Consola.missatgeMinimPuntsIncorrecte(puntsTotals);

                jugador.maCartes.clear();
                jugador.maCartes.addAll(copiaMaInicial);
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

    boolean modificarTaula(Jugador jugador) {
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());
        ArrayList<ArrayList<Carta>> copiaTaulaInicial = new ArrayList<>();

        for (int i = 0; i < taulaComuna.size(); i++) {
            ArrayList<Carta> combinacioActu = new ArrayList<>(taulaComuna.get(i));
            copiaTaulaInicial.add(combinacioActu);
        }

        int numCartesMaJugador = jugador.maCartes.size();
        boolean seguirModificant = true;

        while (seguirModificant && !jugador.maCartes.isEmpty()) {
            Consola.espais();
            Consola.missatgeMostrarTaula();
            Consola.mostrarTaulaComuna(taulaComuna);
            Consola.espais();
            Consola.missatgeCartes();
            Consola.mostrarBaralla(jugador.maCartes);
            Consola.espais();

            int opcio = Consola.demanarQueModificar(jugador);

            if (opcio == 0) {
                seguirModificant = false;
            } else if (opcio == 1) {
                afegirFitxaCombinacio(jugador);
            } else if (opcio == 2) {
                moureFitxaEntreCombinacions();
            }
        }

        for (int i = 0; i < taulaComuna.size(); i++) {
            if (!esCombinacioValida(taulaComuna.get(i))) {
                Consola.missatgeModificacioNoValida();
                jugador.maCartes.clear();
                jugador.maCartes.addAll(copiaMaInicial);
                taulaComuna.clear();
                taulaComuna.addAll(copiaTaulaInicial);
                return false;
            }
        }

        if (jugador.maCartes.size() >= numCartesMaJugador) {
            Consola.missatgeMinimTirarUnaCarta();
            jugador.maCartes.clear();
            jugador.maCartes.addAll(copiaMaInicial);
            taulaComuna.clear();
            taulaComuna.addAll(copiaTaulaInicial);
            return false;
        }

        Consola.missatgeModificacioValida();
        return true;
    }

    private void afegirFitxaCombinacio(Jugador jugador) {
        int indexCarta = Consola.demanarIndexCarta(jugador.maCartes);
        if (indexCarta < 0 || indexCarta >= jugador.maCartes.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexCombinacio = Consola.demanarIndexCombinacio(taulaComuna);
        if (indexCombinacio < 0 || indexCombinacio >= taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> combinacio = taulaComuna.get(indexCombinacio);
        int indexPosicio = Consola.demanarPosicioDinsCombiancio(combinacio);
        if (indexPosicio < 0 || indexPosicio > combinacio.size()) {
            Consola.missatgePosicioNoValida();
            return;
        }

        Carta carta = jugador.maCartes.get(indexCarta);
        jugador.maCartes.remove(indexCarta);
        combinacio.add(indexPosicio, carta);
        Consola.missatgeCartaAfegida(indexPosicio, indexCombinacio);
    }

    private void moureFitxaEntreCombinacions() {
        int indexOrigen = Consola.demanarIndexCombinacio(taulaComuna);
        if (indexOrigen < 0 || indexOrigen >= taulaComuna.size()){
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexCarta = Consola.demanarIndexCarta(taulaComuna.get(indexOrigen));
        if (indexCarta < 0 || indexCarta >= taulaComuna.get(indexOrigen).size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexDesti = Consola.demanarIndexCombinacio(taulaComuna);
        if (indexDesti < 0 || indexDesti >= taulaComuna.size() || indexDesti == indexOrigen){
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> combinacioDesti = taulaComuna.get(indexDesti);
        int indexPosicio = Consola.demanarPosicioDinsCombiancio(combinacioDesti);
        if (indexPosicio < 0 || indexPosicio > combinacioDesti.size()) {
            Consola.missatgePosicioNoValida();
            return;
        }

        Carta carta = taulaComuna.get(indexOrigen).remove(indexCarta);
        combinacioDesti.add(indexPosicio, carta);
        Consola.missatgeCartaAfegida(indexPosicio, indexDesti);
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
