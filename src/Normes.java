import java.util.ArrayList;

public abstract class Normes {

    public static int agafarRang(Carta carta) {
        switch (carta.numero) {
            case "AS":
                return 1;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            case "Joker":
                return 0;
            default:
                return carta.getValor();
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

                if (agafarRang(combinacio.get(i)) != agafarRang(combinacio.get(j))) {
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
        String palOColorEscala = "";

        for (int i = 0; i < combinacio.size(); i++) {
            if (combinacio.get(i).esJoker()) continue;
            palOColorEscala = combinacio.get(i).getPalOColor();
            break;
        }

        for (int j = 0; j < combinacio.size(); j++) {
            if (combinacio.get(j).esJoker()) continue;

            if (!combinacio.get(j).getPalOColor().equals(palOColorEscala)) {
                return false;
            }
        }

        int indexPrimeraFitxaReal = -1;
        for (int i = 0; i < combinacio.size(); i++) {
            if (combinacio.get(i).esJoker()) continue;

            if (indexPrimeraFitxaReal == -1) {
                indexPrimeraFitxaReal = i;
            } else {
                int valorActual = agafarRang(combinacio.get(i));
                int valorAnterior = agafarRang(combinacio.get(indexPrimeraFitxaReal));

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
                if (agafarRang(combinacio.get(j)) < agafarRang(combinacio.get(valorMinim))) {
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
            if (agafarRang(cartesReals.get(i)) != agafarRang(cartesReals.get(0))) {
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
        int valorInicial = agafarRang(cartesReals.get(0));
        int valorEsperat = agafarRang(cartesReals.get(0));

        while (indexReal < cartesReals.size() || !jokers.isEmpty()) {
            if (indexReal < cartesReals.size() && agafarRang(cartesReals.get(indexReal)) == valorEsperat) {
                combinacioOrdenada.add(cartesReals.get(indexReal));
                indexReal++;
                valorEsperat++;
            } else if (!jokers.isEmpty()) {
                if (valorEsperat <= 13) {
                    combinacioOrdenada.add(jokers.get(0));
                    jokers.remove(0);
                    valorEsperat++;
                } else if (valorInicial > 1) {
                    combinacioOrdenada.add(0, jokers.get(0));
                    jokers.remove(0);
                    valorInicial--;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return combinacioOrdenada;
    }

    public boolean esCombinacioValida(ArrayList<Carta> combinacio) {
        if (combinacio == null || combinacio.size() < 3 || combinacio.size() > 13) {
            return false;
        }
        ArrayList<Carta> combinacioOrdenada = ordenarCombinacions(combinacio);
        if (combinacioOrdenada.size() != combinacio.size()) {
            return false;
        }

        if (esGrupValid(combinacioOrdenada) || esEscalaValida(combinacioOrdenada)) {
            combinacio.clear();
            combinacio.addAll(combinacioOrdenada);
            return true;
        }
        return false;
    }

    public abstract boolean haGuanyat(Jugador jugador);

    public abstract void calcularPuntsMa(Jugador jugador);
}
