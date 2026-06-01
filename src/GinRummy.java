import com.sun.source.tree.BreakTree;

import java.util.ArrayList;

public class GinRummy extends Normes {
    static ArrayList<Carta> pilaDescartades = new ArrayList<>();
    static int[] puntsAcumulatsJugador = new int[Joc.arrayJugadors.length];
    static boolean esDePilaDescarts = false;

    public static void jugarGinRummy() {
        GinRummy jocActual = new GinRummy();
        boolean hiHaGuanyadorPartida = false;

        while (!hiHaGuanyadorPartida) {
            jocActual.inicialitzarNovaRonda();
            boolean hiHaGuanyadorRonda = false;

            while (!hiHaGuanyadorRonda) {
                boolean haDescartatCarta = false;
                boolean accioCompletada = false;

                Jugador jugadorActual = Joc.arrayJugadors[Torn.jugaActual];
                Consola.tornDe(jugadorActual.nom);
                Consola.espais();
                Consola.missatgeCartes();
                Consola.mostrarBaralla(jugadorActual.maCartes);
                Consola.espais();
                Consola.imprimirNumFitxesBaralla(Joc.barallaPartida.baralla);
                Consola.darreraCartaPilaDescards(pilaDescartades.getLast());
                Consola.espais();

                esDePilaDescarts = false;
                Carta cartaAgafada = jocActual.agafarCarta();
                jugadorActual.maCartes.add(cartaAgafada);


                while (!accioCompletada) {
                    int accio = Consola.demanarAccioGinRummy();

                    if (accio == 1) {
                        accioCompletada = true;
                    } else if (accio == 2) {
                        jocActual.mourePosCarta(jugadorActual);
                    } else if (accio == 3) {
                        boolean tancamentValid = jocActual.tancarRonda(jugadorActual);

                        if (tancamentValid) {
                            int indexRival;
                            if (jugadorActual == Joc.arrayJugadors[0]) {
                                indexRival = 1;
                            } else {
                                indexRival = 0;
                            }
                            Jugador jugadorRival = Joc.arrayJugadors[indexRival];
                            boolean combinacionsRivalAcabades = false;
                            ArrayList<ArrayList<Carta>> combinacionsRival = new ArrayList<>();
                            while (!combinacionsRivalAcabades) {
                                boolean combinacionsRivalValides = true;
                                combinacionsRival = jocActual.tirarCombinacions(jugadorRival);
                                for (int i = 0; i < combinacionsRival.size(); i++) {
                                    boolean esValida = jocActual.esCombinacioValida(combinacionsRival.get(i));
                                    if (!esValida) {
                                        combinacionsRivalValides = false;
                                    }
                                }
                                if (combinacionsRivalValides || combinacionsRival.isEmpty()) {
                                    combinacionsRivalAcabades = true;
                                }
                            }

                            jocActual.mirarGuanyadorRonda(jugadorActual);
                            accioCompletada = true;
                            hiHaGuanyadorRonda = true;

                        }

                    }
                    hiHaGuanyadorPartida = jocActual.haGuanyat(jugadorActual);
                    if (hiHaGuanyadorPartida) {
                        break;
                    }
                    if (hiHaGuanyadorRonda) {
                        break;
                    }
                }

                while (!haDescartatCarta) {
                    Consola.missatgeTriarCartaDescartar();
                    int numCartaDescartar = jocActual.intdexCartaQueVolDescartar(jugadorActual);
                    Carta cartaQueVolDescartar = jugadorActual.maCartes.get(numCartaDescartar);

                    if (cartaAgafada.equals(cartaQueVolDescartar) && esDePilaDescarts) {
                        Consola.missatgeNoEsPotDescartar(cartaQueVolDescartar);
                    } else {
                        pilaDescartades.add(cartaQueVolDescartar);
                        jugadorActual.maCartes.remove(numCartaDescartar);
                        Consola.missatgeSiEsPotDescartar(cartaQueVolDescartar);
                        haDescartatCarta = true;
                    }
                }
                Torn.calcularTorn(Joc.arrayJugadors.length);
            }
        }

    }

    private EvaluarMa calcularCombinacions(ArrayList<Carta> maJugador) {
        EvaluarMa millorEvaluacio = new EvaluarMa();
        millorEvaluacio.puntsMorts = comptarPuntsCombinacio(maJugador);
        millorEvaluacio.cartesMortes = new ArrayList<>(maJugador);

        int numCartes = maJugador.size();
        int mascaraMaxima = 1 << numCartes;

        for (int mascara = 7; mascara < mascaraMaxima; mascara++) {
            if (Integer.bitCount(mascara) >= 3) {
                ArrayList<Carta> cartesCombinades = new ArrayList<>();
                ArrayList<Carta> cartesDescartades = new ArrayList<>();

                for (int i = 0; i < numCartes; i++) {
                    if ((mascara & (1 << i)) != 0) {
                        cartesCombinades.add(maJugador.get(mascara));
                    } else {
                        cartesDescartades.add(maJugador.get(mascara));
                    }
                }

                if (esCombinacioValida(cartesCombinades)) {
                    EvaluarMa novaEvaluacio = calcularCombinacions(cartesDescartades);

                    if (novaEvaluacio.puntsMorts < millorEvaluacio.puntsMorts) {
                        millorEvaluacio.puntsMorts = novaEvaluacio.puntsMorts;
                        millorEvaluacio.combinacionsPossibles = new ArrayList<>(novaEvaluacio.combinacionsPossibles);
                        millorEvaluacio.combinacionsPossibles.add(cartesCombinades);
                        millorEvaluacio.cartesMortes = novaEvaluacio.cartesMortes;
                    }
                }
            }
        }
        return millorEvaluacio;
    }


    private void inicialitzarNovaRonda() {
        pilaDescartades.clear();
        for (int i = 0; i < Joc.arrayJugadors.length; i++) {
            Joc.arrayJugadors[i].maCartes.clear();
        }
        Joc.barallaPartida.inicialitzarBaralla(3, 1);
        Joc.barallaPartida.mesclarCartes();
        Joc.repartirCartes(Joc.barallaPartida);

        for (int i = 0; i < Joc.arrayJugadors.length; i++) {
            Normes.ordenarCartes(Joc.arrayJugadors[i].maCartes);
        }

        int tamanyBaralla = Joc.barallaPartida.baralla.size();
        Carta cartaInicial = Joc.barallaPartida.baralla.get(tamanyBaralla - 1);
        Joc.barallaPartida.baralla.remove(tamanyBaralla - 1);
        pilaDescartades.add(cartaInicial);
    }

    private Carta agafarCarta() {
        Carta cartaAgafada = null;
        int opcioAgafar = Consola.demanarDonAgafar();

        if (opcioAgafar == 1) {
            int midaBaralla = Joc.barallaPartida.baralla.size();
            cartaAgafada = Joc.barallaPartida.baralla.get(midaBaralla - 1);
            Joc.barallaPartida.baralla.remove(midaBaralla - 1);
            Consola.mostrarCartaRobada(cartaAgafada);
            esDePilaDescarts = false;
        } else if (opcioAgafar == 2) {
            int midaPilaDescarts = pilaDescartades.size();
            cartaAgafada = pilaDescartades.get(midaPilaDescarts - 1);
            pilaDescartades.remove(midaPilaDescarts - 1);
            Consola.mostrarCartaRobada(cartaAgafada);
            esDePilaDescarts = true;
        }
        return cartaAgafada;
    }

    private int intdexCartaQueVolDescartar(Jugador jugador) {
        int numCartaDescartar = Consola.demanarIndexCarta(jugador.maCartes);
        return numCartaDescartar;
    }

    private void mourePosCarta(Jugador jugador) {
        int indexCarta = Consola.demanarIndexCarta(jugador.maCartes);
        if (indexCarta < 0 || indexCarta >= jugador.maCartes.size()) {
            Consola.missatgeIndexNoValid();
            return;
        }

        Carta carta = jugador.maCartes.get(indexCarta);
        jugador.maCartes.remove(indexCarta);

        int indexNovaPos = Consola.demanarPosicioDinsCombiancio(jugador.maCartes);
        if (indexNovaPos < 0 || indexNovaPos > jugador.maCartes.size()) {
            Consola.missatgePosicioNoValida();
            return;
        }

        jugador.maCartes.add(indexNovaPos, carta);
    }

    private boolean tancarRonda(Jugador jugador) {
        ArrayList<Carta> copiaMa = new ArrayList<>(jugador.maCartes);
        ArrayList<ArrayList<Carta>> combinacionsTriades = tirarCombinacions(jugador);

        for (int i = 0; i < combinacionsTriades.size(); i++) {
            ArrayList<Carta> combinacioActu = combinacionsTriades.get(i);
            boolean esValida = esCombinacioValida(combinacioActu);
            if (!esValida) {
                jugador.maCartes.clear();
                jugador.maCartes.addAll(copiaMa);
                return false;
            }
        }

        if (!jugador.maCartes.isEmpty()) {
            int indexCartaDescartar = 0;
            Carta cartaDescartada = jugador.maCartes.get(0);
            for (int i = 1; i < jugador.maCartes.size(); i++) {
                if (jugador.maCartes.get(i).getValor() > cartaDescartada.getValor()) {
                    cartaDescartada = jugador.maCartes.get(i);
                    indexCartaDescartar = i;
                }
            }
            jugador.maCartes.remove(indexCartaDescartar);
        }

        int puntsRestantsMa = comptarPuntsCombinacio(jugador.maCartes);
        if (puntsRestantsMa > 10) {
            jugador.maCartes.clear();
            jugador.maCartes.addAll(copiaMa);
            return false;
        }
        return true;
    }

    private ArrayList<ArrayList<Carta>> tirarCombinacions(Jugador jugador) {
        ArrayList<ArrayList<Carta>> combinacionsTriades = new ArrayList<>();
        boolean volFerCombinacions = true;

        while (volFerCombinacions) {
            ArrayList<Carta> combinacioActual = Consola.demanarNovaCombinacioGin(jugador);
            combinacionsTriades.add(combinacioActual);
            volFerCombinacions = Consola.seguirCreantCombinacions(jugador);
        }
        return combinacionsTriades;
    }

    private void mirarGuanyadorRonda(Jugador jugadorQueHaTancat) {
        int indexJugadorTanca;
        int indexRival;

        if (jugadorQueHaTancat == Joc.arrayJugadors[0]) {
            indexJugadorTanca = 0;
            indexRival = 1;
        } else {
            indexJugadorTanca = 1;
            indexRival = 0;
        }
        Jugador jugadorRival = Joc.arrayJugadors[indexRival];

        int puntsJugadorTancador = comptarPuntsCombinacio(jugadorQueHaTancat.maCartes);
        int puntsRival = comptarPuntsCombinacio(jugadorRival.getMaCartes());

        if (puntsJugadorTancador == 0) {
            int puntsGuanyats = puntsRival + 25;
            puntsAcumulatsJugador[indexJugadorTanca] += puntsGuanyats;
            Consola.missatgeGinGin(jugadorQueHaTancat);
        } else if (puntsJugadorTancador < puntsRival) {
            int puntsGuanyats = puntsRival - puntsJugadorTancador;
            puntsAcumulatsJugador[indexJugadorTanca] += puntsGuanyats;
            Consola.missatgeKnockGin(jugadorQueHaTancat);

        } else {
            int puntsGuanyats = puntsJugadorTancador - puntsRival;
            puntsAcumulatsJugador[indexRival] += puntsGuanyats;
            Consola.missatgePerdutRondaGin(jugadorQueHaTancat);
        }
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
        for (int i = 0; i < puntsAcumulatsJugador.length; i++) {
            Jugador jugadorActual = Joc.arrayJugadors[i];
            if (puntsAcumulatsJugador[i] >= 100) {
                Consola.missatgeGuanyador(jugadorActual);
                return true;
            }
        }
        return false;
    }
}