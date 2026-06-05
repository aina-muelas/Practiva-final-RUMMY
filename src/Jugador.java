import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
    String nom;
    ArrayList<Carta> maCartes;
    int puntuacio = 0;
    boolean haFetPrimeraTirada = false;

    Jugador (String nom){
        this.nom = nom;
        this.maCartes = new ArrayList<>();
    }

    public ArrayList<Carta> getMaCartes() {
        return maCartes;
    }

    @Override
    public String toString() {
        return nom;
    }
}