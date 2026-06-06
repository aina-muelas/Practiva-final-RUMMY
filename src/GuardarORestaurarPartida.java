import java.io.*;
import java.util.ArrayList;

public class GuardarORestaurarPartida {
    private String nomArxiu;
    private int modalitat;
    private int tornActual;
    private Jugador[] arrayJugadors;
    private ArrayList<ArrayList<Carta>> taulerComu;
    private Baralla baralla;
    private ArrayList<Carta> pilarDescartades;

    public GuardarORestaurarPartida(String nomArxiu, int modalitat, int torn, Jugador[] arrayJugadors, ArrayList<ArrayList<Carta>> taulerComu, Baralla baralla, ArrayList<Carta> pilarDescartades) {
        this.nomArxiu = nomArxiu;
        this.modalitat = modalitat;
        this.tornActual = torn;
        this.arrayJugadors = arrayJugadors;
        this.taulerComu = taulerComu;
        this.baralla = baralla;
        this.pilarDescartades = pilarDescartades;
    }

    public GuardarORestaurarPartida(String nomArxiu) {
        this.nomArxiu = nomArxiu;
    }

    public void guardarPartida(){
        try {
            File carpeta = new File("partidesRummyGuardades");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }
            ObjectOutputStream fitxerSortida = new ObjectOutputStream(new FileOutputStream( "partidesRummyGuardades" + File.separator + nomArxiu + ".bin"));
            fitxerSortida.writeObject(modalitat);
            fitxerSortida.writeObject(tornActual);
            fitxerSortida.writeObject(arrayJugadors);
            fitxerSortida.writeObject(taulerComu);
            fitxerSortida.writeObject(baralla);
            fitxerSortida.writeObject(pilarDescartades);
            fitxerSortida.close();
        } catch (IOException e) {
            System.out.println("Problema, no s'ha pogut guardar la partida amb el nom '"  + nomArxiu + ".bin'");
        }
    }

    public void restaurarPartida(){
        try {
            ObjectInputStream fitxerEntrada = new ObjectInputStream(new FileInputStream("partidesRummyGuardades" + File.separator + nomArxiu + ".bin"));
            this.modalitat = (int) fitxerEntrada.readObject();
            this.tornActual = (int) fitxerEntrada.readObject();
            this.arrayJugadors = (Jugador[]) fitxerEntrada.readObject();
            this.taulerComu = (ArrayList<ArrayList<Carta>>) fitxerEntrada.readObject();
            this.baralla = (Baralla) fitxerEntrada.readObject();
            this.pilarDescartades = (ArrayList<Carta>) fitxerEntrada.readObject();
            fitxerEntrada.close();
        } catch (FileNotFoundException e) {
            System.out.println("No es troba el fitxer: " + nomArxiu + ".bin");
        } catch (IOException e) {
            System.out.println("Problema d'acces a l'arxiu: " + nomArxiu + ".bin");
        } catch (ClassNotFoundException e) {
            System.out.println("Les dades obtingudes no permeten recuperar la partida");
        }
    }

    public int getModalitat() { return modalitat;}

    public Jugador[] getArrayJugadors() {
        return arrayJugadors;
    }

    public ArrayList<ArrayList<Carta>> getTaulerComu() {
        return taulerComu;
    }

    public Baralla getBaralla() {
        return baralla;
    }

    public ArrayList<Carta> getPilarDescartades() {
        return pilarDescartades;
    }

    public int getTorn() {
        return tornActual;
    }
}