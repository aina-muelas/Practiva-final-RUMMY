import java.util.ArrayList;

public class Jugador {
    String nom;
    ArrayList<Carta> maCartes;
    int puntuacio;

    Jugador (String nom){
        this.nom = nom;
        this.maCartes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Jugador: " + nom + " | Cartes: " + maCartes;
    }
}
