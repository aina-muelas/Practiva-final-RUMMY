import java.util.ArrayList;

public abstract class Normes {

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

    public static ArrayList<Carta> ordenarCartes(ArrayList<Carta> combinacio) {
        for (int i = 0; i < combinacio.size() - 1; i++) {
            int valorMinim = i;

            for (int j = i + 1; j < combinacio.size(); j++) {
                if (combinacio.get(j).getValor() < combinacio.get(valorMinim).getValor()) {
                    valorMinim = j;
                }
            }

            Carta temporal = combinacio.get(i);
            combinacio.set(i, combinacio.get(valorMinim));
            combinacio.set(valorMinim, temporal);
        }
        return combinacio;
    }

    public static ArrayList<Carta> ordenarCombinacions(ArrayList<Carta> combinacio) {
        ArrayList<Carta> cartesReals = new ArrayList<>();
        ArrayList<Carta> jokers = new ArrayList<>();

        for (int i = 0; i < combinacio.size(); i++) {
            if (combinacio.get(i).esJoker()) {
                jokers.add(combinacio.get(i));
            } else {
                cartesReals.add(combinacio.get(i));
            }
        }

        ordenarCartes(cartesReals);

        boolean esGrup = true;
        for (int i = 1; i < cartesReals.size(); i++) {
            if (cartesReals.get(i).getValor() != cartesReals.get(0).getValor()) {
                esGrup = false;
                break;
            }
        }

        if (esGrup) {
            cartesReals.addAll(jokers);
            return cartesReals;
        }

        ArrayList<Carta> combinacioOrdenada = new ArrayList<>();
        int indexReal = 0;
        int valorEsperat = cartesReals.get(0).getValor();

        while (indexReal < cartesReals.size() || !jokers.isEmpty()) {
            if (indexReal < cartesReals.size() && cartesReals.get(indexReal).getValor() == valorEsperat) {
                combinacioOrdenada.add(cartesReals.get(indexReal));
                indexReal++;
            } else if (!jokers.isEmpty()) {
                combinacioOrdenada.add(jokers.get(0));
                jokers.remove(0);
            } else {
                break;
            }
            valorEsperat++;
        }
        return combinacioOrdenada;
    }

    public boolean haGuanyat(Jugador jugador) {
        if (jugador.maCartes.isEmpty()) {
            Consola.missatgeGuanyador(jugador);
            return true;
        }
        return false;
    }

    public boolean esCombinacioValida(ArrayList<Carta> combinacio) {
        if (combinacio == null || combinacio.size() < 3 || combinacio.size() >= 13) {
            return false;
        } else if (esGrupValid(combinacio) || esEscalaValida(combinacio)) {
            return true;
        }
        return false;
    }
}
