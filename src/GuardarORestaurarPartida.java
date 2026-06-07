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
            Consola.missatgeErrorGuardarPartida(nomArxiu);
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
            Consola.noHaTrobatArxiuRestaurar(nomArxiu);
        } catch (IOException e) {
            Consola.noHiHaAccesArxiuRestaurar(nomArxiu);
        } catch (ClassNotFoundException e) {
            Consola.dadesNoPermetenRecuperarPartida(nomArxiu);
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