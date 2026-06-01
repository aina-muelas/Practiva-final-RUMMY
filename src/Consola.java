import java.util.ArrayList;
import java.util.Scanner;

public class Consola {
    public static Scanner scanner = new Scanner(System.in);

    public static void mostrarBaralla(ArrayList<Carta> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            Carta c = cartes.get(i);
            System.out.println((i + 1) + ". " + c.toString());
        }
    }

    public static void mostrarTaulaComuna(ArrayList<ArrayList<Carta>> taula) {
        if (taula.isEmpty()) {
            System.out.println("La taula esta buida");
            return;
        }
        for (int i = 0; i < taula.size(); i++) {
            System.out.println("Combinació " + (i + 1) + ": " + taula.get(i));
        }
    }

    public static int triarModalitat() {
        int modalitat = 0;
        while (modalitat <= 0) {
            System.out.println("Indica a quina modalitat vols jugar: '1': Rummy clàssic, '2': rummyKub, '3': GinRummy, '4'; Rummy Argtenmtino");
            modalitat = scanner.nextInt();
            scanner.nextLine();
            if (modalitat <= 0 || modalitat > 4) {
                System.out.println("Modalitat de joc no valida, torna a triar");
            }
        }
        return modalitat;
    }

    public static int triarNumJugadors(int modalitatTriada) {
        int numJugadors = 0;
        if (modalitatTriada == 1) {
            while (numJugadors < 2 || numJugadors > 6) {
                System.out.println("Introdueix el num de jugadors per al Rummy clàssic, recorda: minim 2 i maxim 6");
                numJugadors = scanner.nextInt();
                scanner.nextLine();
                if (numJugadors < 2 || numJugadors > 6) {
                    System.out.println("Num de jugadors no valid");
                }
            }
        } else if (modalitatTriada == 2) {
            while (numJugadors < 2 || numJugadors > 4) {
                System.out.println("Introdueix el num de jugadors per al Rummikub, recorda: minim 2 i maxim 4");
                numJugadors = scanner.nextInt();
                scanner.nextLine();
                if (numJugadors < 2 || numJugadors > 4) {
                    System.out.println("Num de jugadors no valid");
                }
            }
        } else if (modalitatTriada == 3) {
            System.out.println("el num de jugadors per al GinRummy es 2");
            numJugadors = 2;
        } else if (modalitatTriada == 4) {
            while (numJugadors < 2 || numJugadors > 6) {
                System.out.println("Introdueix el num de jugadors per al Rummy Argentino, recorda: minim 2 i maxim 6");
                numJugadors = scanner.nextInt();
                scanner.nextLine();
                if (numJugadors < 2 || numJugadors > 6) {
                    System.out.println("Num de jugadors no valid");
                }
            }
        }
        return numJugadors;
    }

    public static String triarNom(int num) {
        String nom;
        System.out.println("es el torn del jugador " + num + " per triar el seu nom");
        nom = scanner.nextLine();
        return nom;
    }

    public static int demanarAccioRummikub(Jugador jugadorActual) {
        int opcio = 0;
        while (opcio != 1 && opcio != 2 && opcio != 3 && opcio!= 4) {
            System.out.println("Que vols fer??");
            System.out.println("1. Robar una carta");
            System.out.println("2. Crear/afegir noves combinacions a la taula");
            System.out.println("3. Modificar combinacions de la taula (només si ja has fet la primera tirada de min 30 punts)");
            System.out.println("4. Finalitzar torn (només si ja has fet una de les accions anteriors, o no queden mes fitxes a la baralla)");
            System.out.println("Introdueix la teva opció: ");
            opcio = scanner.nextInt();

            if (opcio != 1 && opcio != 2 && opcio != 3 && opcio != 4) {
                System.out.println("Opció no vàlida, torna a triar");
            }
        }
        return opcio;
    }

    public static ArrayList<Carta> demanarNovaCombinacio(Jugador jugador) {
        ArrayList<Carta> combinacioNova = new ArrayList<>();
        boolean combinacioAcabada = false;

        while (!combinacioAcabada && !jugador.maCartes.isEmpty()) {
            if (!jugador.haFetPrimeraTirada) {
                System.out.println("Encara no has tirat cap combinació o no has acabat la jugada. Recorda, minim 30 punts i no pots modificar cap combinació de la taula");
            }
            System.out.println("Les teves cartes per fer la nova combinacio són les segÚents:");
            mostrarBaralla(jugador.maCartes);

            System.out.println("Tria el número de carta que vols afegir a la combinació, posa 0 si vols finalitzar la combinació: ");
            int opcio = scanner.nextInt();
            scanner.nextLine();

            if (opcio == 0) {
                combinacioAcabada = true;
            } else if (opcio > 0 && opcio <= jugador.maCartes.size()) {
                Carta cartaTriada = jugador.maCartes.remove(opcio - 1);
                combinacioNova.add(cartaTriada);
                System.out.println("S'ha afegit la carta a la nova combinació");
            } else {
                System.out.println("Número no vàlid, torna a intentar-ho");
            }
        }

        if (!combinacioNova.isEmpty()) {
            System.out.println("Combinació guardada de forma provisional");
        }
        return combinacioNova;
    }

    public static int demanarQueModificar (Jugador jugador) {
        System.out.println("Que vols fer?");
        System.out.println("1. Afegir una carta de la teva mà a una combinació");
        System.out.println("2. Moure una carta d'una combinació a una altra");
        System.out.println("3. Moure una carta d'una combinació a una nova");
        System.out.println("0. Sortir/acabar modificacions");
        int opcio = Consola.scanner.nextInt();
        Consola.scanner.nextLine();
        return opcio;
    }

    public static int demanarIndexCarta(ArrayList<Carta> cartes) {
        System.out.println("Tria el número de carta: ");
        mostrarBaralla(cartes);
        int opcio = scanner.nextInt() - 1;
        scanner.nextLine();
        return opcio;
    }

    public static int demanarIndexCombinacio(ArrayList<ArrayList<Carta>> taula) {
        System.out.println("Tria el número de combinació: ");
        mostrarTaulaComuna(taula);
        int opcio = scanner.nextInt() - 1;
        scanner.nextLine();
        return opcio;
    }

    public static int demanarPosicioDinsCombiancio(ArrayList<Carta> combinacio) {
        System.out.println("En quina posició la vols afegir? (1 = davant de tot, " + (combinacio.size() + 1) + " = darrere de tot)");
        mostrarBaralla(combinacio);
        int opcio = scanner.nextInt() - 1;
        scanner.nextLine();
        return opcio;
    }

    public static boolean seguirCreantCombinacions(Jugador jugador) {
        ArrayList<Carta> maCartesJugador = jugador.getMaCartes();

        if (!maCartesJugador.isEmpty()) {
            System.out.println("Vols crear una nova combinació? )(s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (!resposta.equals("s")) {
                return false;
            }
        }
        return true;
    }

    public static int demanarDonAgafar () {
        int opcio = 0;

        while(opcio != 1 && opcio != 2) {
            System.out.println("Tria d'on vols agafar la carta: 1.Baralla, 2.Pila Descartades");
            opcio = scanner.nextInt();
            scanner.nextLine();

            if (opcio != 1 && opcio != 2) {
                System.out.println("Opció no vàlida, torna a triar");
            }
        }
        return opcio;
    }

    public static int demanarAccioGinRummy() {
        int accio = 0;

        while(accio != 1 && accio != 2 && accio != 3) {
            System.out.println("Tria una opció:");
            System.out.println("1. Acabar acció (per si ja has acabat d'ordenar les cartes o no les vols tocar) ");
            System.out.println("2. Ordenar les cartes");
            System.out.println("3. Tancar ronda (Knock / Gin)");
            accio = scanner.nextInt();
            scanner.nextLine();

            if (accio != 1 && accio != 2 && accio != 3) {
                System.out.println("Opció no vàlida, torna a triar");
            }
        }
        return accio;
    }

    public static ArrayList<Carta> demanarNovaCombinacioGin (Jugador jugador) {
        ArrayList<Carta> combinacioNova = new ArrayList<>();
        boolean combinacioAcabada = false;

        while (!combinacioAcabada && !jugador.maCartes.isEmpty()) {
            System.out.println("Les teves cartes per fer la nova combinacio són les segÚents:");
            mostrarBaralla(jugador.maCartes);

            System.out.println("Tria el número de carta que vols afegir a la combinació, posa 0 si vols finalitzar la combinació: ");
            int opcio = scanner.nextInt();
            scanner.nextLine();

            if (opcio == 0) {
                combinacioAcabada = true;
            } else if (opcio > 0 && opcio <= jugador.maCartes.size()) {
                Carta cartaTriada = jugador.maCartes.remove(opcio - 1);
                combinacioNova.add(cartaTriada);
                System.out.println("S'ha afegit la carta a la nova combinació");
            } else {
                System.out.println("Número no vàlid, torna a intentar-ho");
            }
        }

        if (!combinacioNova.isEmpty()) {
            System.out.println("Combinació guardada");
        }
        return combinacioNova;
    }


    // comentaris comuns a totes les modalitats
    public static void espais() { System.out.println("------------------------------------------------------------"); }
    public static void tornDe(String nom) { System.out.println("És el torn del jugador/a: " + nom); }
    // comentaris baralla / cartes
    public static void missatgeMostrarTaula() { System.out.println("Les combinacions que hi ha a la taula són les següents:"); }
    public static void missatgeCartes() { System.out.println("Les teves cartes actuals: "); }
    public static void mostrarCartaRobada(Carta cartaRobada) { System.out.println("Has robat la carta: " + cartaRobada); }
    public static void missatgeBarallaBuida() { System.out.println("La baralla esta buida, tria una altre opció"); }
    public static void imprimirNumFitxesBaralla(ArrayList<Carta> baralla) { System.out.println("A la baralla queden " + baralla.size() + " fitxes"); }
    public static void darreraCartaPilaDescards(Carta cartaDescartada) { System.out.println("La carta que pots agafar de la pila de descards és: " + cartaDescartada); }
    // comentaris sobre els punts
    // missatge combinacions
    public static void missatgeCombinacioNoValida() { System.out.println("La teva combinacio no es valida"); }
    public static void missatgeJugadaAcceptada() { System.out.println("Jugada acceptada i afegida a la taula"); }
    // missatges combinacions
    public static void missatgeModificacioNoValida() { System.out.println("Les modificacions no són vàlides, es restaura l'estat anterior"); }
    public static void missatgeModificacioValida() { System.out.println("Les modificacions són vàlides, es guardara el nou estat"); }
    //missatges guanyadors
    public static void missatgeGuanyador(Jugador jugador) { System.out.println("Enhorabona " + jugador.nom + " has guanyat!!!"); }
    // altres
    public static void missatgeMinimTirarUnaCarta() { System.out.println("Has de tirar almenys una carta de la teva mà per completar el torn"); }
    public static void missatgeIndexNoValid() { System.out.println("l'índex triat no es vàlid"); }
    public static void missatgePosicioNoValida() { System.out.println("la posicio triada no es vàlida"); }
    public static void missatgeCartaAfegida(int indexPosicio, int indexCombinacio) { System.out.println("Carta afegida a la posició " + (indexPosicio + 1) + " de la combinació " + (indexCombinacio + 1)); }

    public static void missatgeAccio1RummikubNoValida() { System.out.println("No es pot agafar una carta, ja has modificat el tauler o no hi ha fitxes a la baralla"); }
    public static void missatgeAccio4RummikubNoValida() { System.out.println("No es pots finalitzar el torn, has de modificar/afegir combinacions o agafar una fitxa"); }

    // comentaris del rummikub
    public static void missatgeMinimPuntsIncorrecte(int punts) { System.out.println("Es la teva primera tirada, has de tirar MINIM 30 punts, i les teves combinacions fan: " + punts); }
    public static void missatgeCombinacioValida() { System.out.println("Primera combinacio de 30 punts o més acceptada!"); }
    public static void missatgeModificacioNoPossible() { System.out.println("No pots modificar la taula, recorda que per poder fer-ho has d'haver fet la primera jugada de minim 30 punts"); }
    public static void missatgeDarreraRonda() { System.out.println("Atenció, estas a la darrera ronda de la partida"); }
    // comentaris gin rummy
    public static void missatgeNoEsPotDescartar(Carta carta) { System.out.println("la carta " + carta + " no es pot decartar, ja es la mateix que has agafat de la pila de descarts"); }
    public static void missatgeSiEsPotDescartar(Carta carta) { System.out.println("la carta " + carta + " s'ha descartat correctament de la teva ma"); }
    public static void missatgeTriarCartaDescartar() { System.out.println("tria l'index de la carta que vols descartar, recorda que no pot ser la mateixa que acabdes d'agafar si l'has agafada de la pila de descarts"); }
    public static void missatgePerdutRondaGin(Jugador jugador) { System.out.println("Has perdut la ronda " + jugador.nom + " el rival te igual o menys punts que tu"); }
    public static void missatgeKnockGin(Jugador jugador) { System.out.println("Enhorabona " + jugador.nom + " has fet un Knock! Guanyes la ronda"); }
    public static void missatgeGinGin(Jugador jugador) { System.out.println("Enhorabona " + jugador.nom + " has fet un gin! Guanyes la ronda"); }
}
