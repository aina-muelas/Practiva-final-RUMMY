import java.io.Serializable;
public class Carta  implements Serializable {
    String numero;
    String palOColor;
    int valor;
    boolean esPotMoure = true;

    public Carta(String numero, String palOColor, int valor){
        this.numero = numero;
        this.palOColor = palOColor;
        this.valor = valor;
    }

    public boolean esJoker() {
        return "Joker".equals(this.numero);
    }

    public boolean esAS() {
        return "AS".equals(this.numero);
    }

    public boolean esMono() { return this.numero.equals("2"); }

    public int getValor() {
        return valor;
    }

    public String getPalOColor(){ return palOColor; }

    public void setEsPotMoure(boolean estat){ this.esPotMoure = estat; }

    public boolean getEsPotMoure() { return  this.esPotMoure;}

    @Override
    public String toString() {
        if (esJoker()) {
            return "[JOKER]";
        }
        return "[" + numero + " de " + palOColor + "]";
    }
}
