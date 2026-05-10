public class Carta {
    String numero;
    String palOColor;

    public Carta(String numero, String palOColor){
        this.numero = numero;
        this.palOColor = palOColor;
    }

    public Carta(String numero){
        this.numero = numero;
    }

    public boolean esJoker() {
        return "Joker".equals(this.numero);
    }

    @Override
    public String toString() {
        if (esJoker()) {
            return "[JOKER]";
        }
        return "[" + numero + " de " + palOColor + "]";
    }
}
