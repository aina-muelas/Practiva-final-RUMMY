import java.util.ArrayList;

public class Jugador {
    String nom;
    ArrayList<Carta> maCartes;
    int puntuacio;
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
        return "Jugador: " + nom + " | Cartes: " + maCartes;
    }
}
