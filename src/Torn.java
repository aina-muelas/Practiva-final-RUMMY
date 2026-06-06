public class Torn  {
    public static int jugaActual = 0;

    public static void calcularTorn(int totalJugadors) {
        jugaActual = (jugaActual + 1) % totalJugadors;
    }
}
