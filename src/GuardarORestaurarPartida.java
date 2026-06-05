import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class GuardarORestaurarPartida {
    private Jugador[] arrayJugadors;
    private ArrayList<ArrayList<Carta>> taulerComu;
    private Baralla baralla;
    private ArrayList<Carta> pilarDescartades;

    public GuardarORestaurarPartida(Jugador[] arrayJugadors, ArrayList<ArrayList<Carta>> taulerComu, Baralla baralla, ArrayList<Carta> pilarDescartades) {
        this.arrayJugadors = arrayJugadors;
        this.taulerComu = taulerComu;
        this.baralla = baralla;
        this.pilarDescartades = pilarDescartades;
    }

    public void guardarPartida(){
        try {
            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream("d:/jocRummy.bin"));
            //Escribimos la matriz tal cuál está en memoria RAM, como archivo binario
            //escritor.writeObject(tablero);
            escritor.close();
        } catch (FileNotFoundException e) {
            System.out.println("No s'ha trobat l'arxiu: jocRummy.bin");
        } catch (IOException e) {
            System.out.println("Problema d'acces a l'arxiu: jocRummy.bin");
        }
    }

    public void cargarPartida(){
        try {
            ObjectInputStream lector = new ObjectInputStream(new FileInputStream("d:/jocRummy.bin"));
            //Leemos los bytes del archivo y nos retornará un Object, al que haremos casting a matriz int[]
            // tablero = (int[][]) lector.readObject();
            lector.close();
        } catch (FileNotFoundException e) {
            System.out.println("No es troba el fitxer: jocRummy.bin");
        } catch (IOException e) {
            System.out.println("Problema d'acces a l'arxiu: jocRummy.bin");
        } catch (ClassNotFoundException e) {
            System.out.println("LEs dades obtingudes no permeten recuperar la partida");
        }
    }


    public Jugador[] getArrayJugadors() {
        return arrayJugadors;
    }

    public void setArrayJugadors(Jugador[] arrayJugadors) {
        this.arrayJugadors = arrayJugadors;
    }

    public ArrayList<ArrayList<Carta>> getTaulerComu() {
        return taulerComu;
    }

    public void setTaulerComu(ArrayList<ArrayList<Carta>> taulerComu) {
        this.taulerComu = taulerComu;
    }

    public Baralla getBaralla() {
        return baralla;
    }

    public void setBaralla(Baralla baralla) {
        this.baralla = baralla;
    }

    public ArrayList<Carta> getPilarDescartades() {
        return pilarDescartades;
    }

    public void setPilarDescartades(ArrayList<Carta> pilarDescartades) {
        this.pilarDescartades = pilarDescartades;
    }
}
