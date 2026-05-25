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
