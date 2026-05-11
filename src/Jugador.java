public class Jugador {
    String nom;
    String maCartes;
    int puntuacio;

    Jugador (String nom){
        this.nom = nom;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
