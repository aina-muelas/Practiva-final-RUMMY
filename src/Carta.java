public class Carta {
    String numero;
    String palOColor;
    int valor;

    public Carta(String numero, String palOColor, int valor){
        this.numero = numero;
        this.palOColor = palOColor;
        this.valor = valor;
    }

    public boolean esJoker() {
        return "Joker".equals(this.numero);
    }

    public int getValor() {
        return valor;
    }

    public String getPalOColor(){ return palOColor; }

    @Override
    public String toString() {
        if (esJoker()) {
            return "[JOKER]";
        }
        return "[" + numero + " de " + palOColor + "]";
    }
}
