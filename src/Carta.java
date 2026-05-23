public class Carta {
    String numero;
    String palOColor;
    int valor;

    public Carta(String numero, String palOColor, int valor){
        this.numero = numero;
        this.palOColor = palOColor;
        this.valor = valor;
    }

    public Carta(String numero){
        this.numero = numero;
    }

    public boolean esJoker() {
        return "Joker".equals(this.numero);
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        if (esJoker()) {
            return "[JOKER]";
        }
        return "[" + numero + " de " + palOColor + "]";
    }
}
