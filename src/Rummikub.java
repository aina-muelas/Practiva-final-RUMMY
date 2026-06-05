import java.util.ArrayList;

public class Rummikub extends Normes {

    public static void jugarRummiKub() {
        Rummikub jocActual = new Rummikub();

        boolean primerTornAcabat = false;
        boolean hiHaGuanyador = false;
        boolean esDarreraRonda = false;
        int tornsDarreraRonda = Joc.arrayJugadors.length;

        while (!hiHaGuanyador) {
            int opcio = Consola.demanarSiVolJugarOGuardar();

            if (opcio == 2) {
                Joc.guardarEstatPartida();
            } else {
                Consola.espais();
                Consola.missatgeMostrarTaula();
                Consola.mostrarTaulaComuna(Joc.taulaComuna);
                Consola.espais();

                boolean haTocatTauler = false;
                boolean accioCompletada = false;
                int accio;

                Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
                Consola.tornDe(jugadorActual.nom);
                Consola.espais();
                Consola.missatgeCartes();
                ordenarCartes(jugadorActual.maCartes);
                Consola.mostrarMaCartes(jugadorActual.maCartes);
                Consola.espais();

                if (jugadorActual.haFetPrimeraTirada) {
                    primerTornAcabat = true;
                }
                if (esDarreraRonda) {
                    Consola.missatgeDarreraRonda();
                }

                while (!accioCompletada) {
                    Consola.imprimirNumFitxesBaralla(Joc.barallaPartida.baralla);
                    Consola.espais();

                    boolean barallabuida = Joc.barallaPartida.baralla.isEmpty();
                    accio = Consola.demanarAccioRummikub(jugadorActual);

                    if (accio == 1) {
                        if (!haTocatTauler && !barallabuida) {
                            accioCompletada = jocActual.agafarFitxa(jugadorActual);
                        } else {
                            Consola.missatgeAccio1RummikubNoValida();
                        }
                    } else if (accio == 2) {
                        boolean tiradaFeta = jocActual.tirarFitxes(jugadorActual);
                        if (tiradaFeta) {
                            haTocatTauler = true;
                        }
                    } else if (accio == 3) {
                        if (jugadorActual.haFetPrimeraTirada && primerTornAcabat) {
                            boolean modificacioFeta = jocActual.modificarTaula(jugadorActual);
                            if (modificacioFeta) {
                                haTocatTauler = true;
                            }
                        } else {
                            Consola.missatgeModificacioNoPossible();
                        }
                    } else if (accio == 4) {
                        if (haTocatTauler || barallabuida) {
                            accioCompletada = true;
                        } else {
                            Consola.missatgeAccio4RummikubNoValida();
                        }
                    }
                }

                hiHaGuanyador = jocActual.haGuanyat(jugadorActual);

                if (!hiHaGuanyador && Joc.barallaPartida.baralla.isEmpty() && !esDarreraRonda) {
                    esDarreraRonda = true;
                } else if (esDarreraRonda & !hiHaGuanyador) {
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
    }

    private boolean agafarFitxa(Jugador jugador) {
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

    private boolean tirarFitxes(Jugador jugador) {
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());

        boolean seguirCreant = true;
        ArrayList<ArrayList<Carta>> combinacionsNoves = new ArrayList<ArrayList<Carta>>();
        int puntsTotals = 0;

        while (seguirCreant && !jugador.getMaCartes().isEmpty()) {
            ArrayList<Carta> combinacioNova = Consola.demanarNovaCombinacioRummyKub(jugador);
            if (combinacioNova.isEmpty()) {
                break;
            }

            combinacioNova = Normes.ordenarCombinacions(combinacioNova);

            if (!esCombinacioValida(combinacioNova)) {
                Consola.missatgeCombinacioNoValida();
                jugador.maCartes.addAll(combinacioNova);
                ordenarCartes(jugador.maCartes);
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
                    Joc.taulaComuna.add(combinacio);
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
                Joc.taulaComuna.add(combinacio);
            }
            return true;
        }
    }

    private boolean modificarTaula(Jugador jugador) {
        ArrayList<Carta> copiaMaInicial = new ArrayList<>(jugador.getMaCartes());
        ArrayList<ArrayList<Carta>> copiaTaulaInicial = new ArrayList<>();

        for (int i = 0; i < Joc.taulaComuna.size(); i++) {
            ArrayList<Carta> combinacioActu = new ArrayList<>(Joc.taulaComuna.get(i));
            copiaTaulaInicial.add(combinacioActu);
        }

        int numCartesMaJugador = jugador.maCartes.size();
        boolean seguirModificant = true;

        while (seguirModificant && !jugador.maCartes.isEmpty()) {
            Consola.espais();
            Consola.missatgeMostrarTaula();
            Consola.mostrarTaulaComuna(Joc.taulaComuna);
            Consola.espais();
            Consola.missatgeCartes();
            Consola.mostrarMaCartes(jugador.maCartes);
            Consola.espais();

            int opcio = Consola.demanarQueModificar(jugador);

            if (opcio == 0) {
                seguirModificant = false;
            } else if (opcio == 1) {
                afegirFitxaCombinacio(jugador);
            } else if (opcio == 2) {
                moureFitxaEntreCombinacions();
            } else if (opcio == 3) {
                moureFitxaNovaCombinacio();
            }
        }

        for (int i = 0; i < Joc.taulaComuna.size(); i++) {
            if (!esCombinacioValida(Joc.taulaComuna.get(i))) {
                Consola.missatgeModificacioNoValida();
                jugador.maCartes.clear();
                jugador.maCartes.addAll(copiaMaInicial);
                Joc.taulaComuna.clear();
                Joc.taulaComuna.addAll(copiaTaulaInicial);
                return false;
            }
        }

        if (jugador.maCartes.size() >= numCartesMaJugador) {
            Consola.missatgeMinimTirarUnaCarta();
            jugador.maCartes.clear();
            jugador.maCartes.addAll(copiaMaInicial);
            Joc.taulaComuna.clear();
            Joc.taulaComuna.addAll(copiaTaulaInicial);
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

        int indexCombinacio = Consola.demanarIndexCombinacio(Joc.taulaComuna);
        if (indexCombinacio < 0 || indexCombinacio >= Joc.taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> combinacio = Joc.taulaComuna.get(indexCombinacio);
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
        int indexOrigen = Consola.demanarIndexCombinacio(Joc.taulaComuna);
        if (indexOrigen < 0 || indexOrigen >= Joc.taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexCarta = Consola.demanarIndexCarta(Joc.taulaComuna.get(indexOrigen));
        if (indexCarta < 0 || indexCarta >= Joc.taulaComuna.get(indexOrigen).size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexDesti = Consola.demanarIndexCombinacio(Joc.taulaComuna);
        if (indexDesti < 0 || indexDesti >= Joc.taulaComuna.size() || indexDesti == indexOrigen) {
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> combinacioDesti = Joc.taulaComuna.get(indexDesti);
        int indexPosicio = Consola.demanarPosicioDinsCombiancio(combinacioDesti);
        if (indexPosicio < 0 || indexPosicio > combinacioDesti.size()) {
            Consola.missatgePosicioNoValida();
            return;
        }

        Carta carta = Joc.taulaComuna.get(indexOrigen).remove(indexCarta);
        combinacioDesti.add(indexPosicio, carta);
        Consola.missatgeCartaAfegida(indexPosicio, indexDesti);

        if (Joc.taulaComuna.get(indexOrigen).size() == 0) {
            Joc.taulaComuna.remove(indexOrigen);
        }
    }

    private void moureFitxaNovaCombinacio() {
        int indexOrigen = Consola.demanarIndexCombinacio(Joc.taulaComuna);
        if (indexOrigen < 0 || indexOrigen >= Joc.taulaComuna.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        int indexCarta = Consola.demanarIndexCarta(Joc.taulaComuna.get(indexOrigen));
        if (indexCarta < 0 || indexCarta >= Joc.taulaComuna.get(indexOrigen).size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        ArrayList<Carta> indexNou = new ArrayList<>();
        Joc.taulaComuna.add(indexNou);

        ArrayList<Carta> indexDesti = Joc.taulaComuna.getLast();
        int posDarrerIndex = Joc.taulaComuna.size();

        ArrayList<Carta> combinacioDesti = indexDesti;

        Carta carta = Joc.taulaComuna.get(indexOrigen).remove(indexCarta);
        combinacioDesti.add(carta);
        Consola.missatgeCartaAfegida(0, posDarrerIndex);

        if (Joc.taulaComuna.get(indexOrigen).size() == 0) {
            Joc.taulaComuna.remove(indexOrigen);
        }
    }

    private static boolean determinarGuanyador() {
        int fitxesMinimes = Joc.arrayJugadors[0].getMaCartes().size();
        Jugador jugadorGuanyador = Joc.arrayJugadors[0];
        jugadorGuanyador.puntuacio = comptarPuntsCasEmpat(jugadorGuanyador);

        for (int i = 1; i < Joc.arrayJugadors.length; i++) {
            Jugador jugadorActual = Joc.arrayJugadors[i];
            int fitxesActuals = jugadorActual.getMaCartes().size();
            if (fitxesActuals < fitxesMinimes) {
                fitxesMinimes = fitxesActuals;
                jugadorGuanyador = jugadorActual;
                jugadorGuanyador.puntuacio = comptarPuntsCasEmpat(jugadorGuanyador);
            } else if (fitxesActuals == fitxesMinimes) {
                jugadorActual.puntuacio = comptarPuntsCasEmpat(jugadorActual);
                if (jugadorActual.puntuacio < jugadorGuanyador.puntuacio) {
                    jugadorGuanyador = jugadorActual;
                }
            }
        }
        Consola.missatgeGuanyador(jugadorGuanyador);
        return true;
    }

    private static int comptarPuntsCasEmpat(Jugador jugador) {
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

    private int comptarPuntsCombinacio(ArrayList<Carta> combinacio) {
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
}
