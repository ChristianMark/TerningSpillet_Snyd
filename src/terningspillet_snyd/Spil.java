/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terningspillet_snyd;

import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class Spil {

    private int[] AntalØjne = new int[7];
    private int[] Kombinationer = new int[7];
    private int hvis_tur = 1;
    public static ArrayList<Raflebaeger> liste_af_raflebaeger;
    private Tur forrige_gæt;
    private Tur nuværende_gæt;
    private int antal_spillere;
    private int antal_terninger;
    private int runde_nr = 1;
    private int taber;
    private String spil_status;

    public String getSpil_status() {
        return spil_status;
    }

    /**
     * Når der starter en ny runde skal der slås med terningerne og mulige kombinationer skal udregnes
     *
     * @param liste_af_raflebaeger
     */
    public Spil(int antal_spillere, int antal_terninger) {
        this.antal_spillere = antal_spillere;
        this.antal_terninger = antal_terninger;

        liste_af_raflebaeger = new ArrayList<Raflebaeger>(); // opret liste-array af "Raflebaeger" objekter

        for (int i = 0; i < antal_spillere-1; i++) {
            liste_af_raflebaeger.add(new Raflebaeger(antal_terninger, false));
            liste_af_raflebaeger.get(i).ryst(); // Ryst raflebageret
        }
            liste_af_raflebaeger.add(new Raflebaeger(antal_terninger, true));
            liste_af_raflebaeger.get(liste_af_raflebaeger.size()-1).Check_for_trapperegel();
            //liste_af_raflebaeger.get(i).ryst(); // Ryst raflebageret
            spil_status = "start";
        
    }

    public int getAntalØjne(int værdi) {
        if (værdi > 0 && værdi < 7) {
            return AntalØjne[værdi];
        } else {
            return 0;
        }

    }

    public int getKombinationer(int værdi) {
        if (værdi > 0 && værdi < 7) {
            return Kombinationer[værdi];
        } else {
            return 0;
        }

    }

    void gæt(int værdi, int antal, int spiller) {

        if (test_spilstatus()) {
            return;
        }

        while (liste_af_raflebaeger.get(hvis_tur - 1).antalTerninger() == 0) {
            Skift_tur();
        }
        
        if(spiller != hvis_tur){
            System.out.println("Det er ikke din tur endnu!");
            return;
        }
        //Hvis_turErDet();
        if (værdi > 1 && værdi < 7 && antal > 0) {
            if (forrige_gæt == null) { // Hvis det er første tur i en ny runde
                forrige_gæt = new Tur(værdi, antal, this.getKombinationer(værdi), hvis_tur);
                Skift_tur();
            } else { // Hvis det er 2+ tur.
                nuværende_gæt = new Tur(værdi, antal, this.getKombinationer(værdi), hvis_tur);
                
                if (nuværende_gæt.Score > forrige_gæt.Score) { // Gyldigt gæt der er højere end forrige_gæt
                    forrige_gæt = nuværende_gæt;
                    Skift_tur();
                } else {
                    System.out.println("\t Din Score er ikke bedre end det bedste \"forrige_gæt\"");
                    nuværende_gæt = null;
                }
            }
            
        }else {
            System.out.print("Ugyldigt gæt! ");
            System.out.println("Antal: " + antal + " Værdi: " + værdi);
        }

    }

    void gæt(int værdi, int antal) {

        if (test_spilstatus()) {
            return;
        }

        while (liste_af_raflebaeger.get(hvis_tur - 1).antalTerninger() == 0) {
            Skift_tur();
        }
        
        //Hvis_turErDet();
        if (værdi > 1 && værdi < 7 && antal > 0) {
            if (forrige_gæt == null) { // Hvis det er første tur i en ny runde
                forrige_gæt = new Tur(værdi, antal, this.getKombinationer(værdi), hvis_tur);
                Skift_tur();
            } else { // Hvis det er 2+ tur.
                nuværende_gæt = new Tur(værdi, antal, this.getKombinationer(værdi), hvis_tur);
                
                if (nuværende_gæt.Score > forrige_gæt.Score) { // Gyldigt gæt der er højere end forrige_gæt
                    forrige_gæt = nuværende_gæt;
                    Skift_tur();
                } else {
                    System.out.println("\t Din Score er ikke bedre end det bedste \"forrige_gæt\"");
                    nuværende_gæt = null;
                }
            }
            
        }else {
            System.out.print("Ugyldigt gæt! ");
            System.out.println("Antal: " + antal + " Værdi: " + værdi);
        }

    }    
    void løgner(int spiller) {
        if (test_spilstatus()) {
            return;
        }
        
        if(spiller != hvis_tur){
            System.out.println("Det er ikke din tur endnu!");
            return;
        }

        if (forrige_gæt == null) {
            System.out.println("Du kan ikke sige, at du selv lyver!");
        } else {
            //Hvis_turErDet();
            if (forrige_gæt.isGyldigKombi()) {
                System.out.println("Der var ihvertfald " + (int) forrige_gæt.Score / 10 + " "
                        + (int) forrige_gæt.Score % 10 + "'er "
                        + "Så det var sandt! Spiller " + hvis_tur + " har tabt!");

                taber = hvis_tur;
            } else {
                System.out.println("Der var ikke " + (int) forrige_gæt.Score / 10 + " "
                        + (int) forrige_gæt.Score % 10 + "'er " + "Så det var falsk! Spiller "
                        + forrige_gæt.spiller + " har tabt!");

                taber = forrige_gæt.spiller;
            }
            hvis_tur = taber;
            runde_slut();
        }

    }

    void printTerninger(int spiller) {
        if (test_spilstatus()) {
            return;
        }
        
        if(spiller-1 > liste_af_raflebaeger.size()-1 || spiller < 0){
            System.out.println("Spiller "+spiller+" findes ikke!");
        }else if(spiller > 0){
            String text = liste_af_raflebaeger.get(spiller-1).toString();
            System.out.println("Spiller " + (spiller) + " " + text);
        }else{
            // Udskriv Spillerne og deres terninger!
            for (int i = 0; i < antal_spillere; i++) {
                String text = liste_af_raflebaeger.get(i).toString();
                System.out.println("Spiller " + (i + 1) + " " + text);
            }            
        }


    }

    void start_rounde() {
        if (test_spilstatus()) {
            return;
        }

        Hvis_turErDet();
        for (Raflebaeger raflebaeger : liste_af_raflebaeger) {
            if(raflebaeger.trappe_regel){
                AntalØjne[1] += raflebaeger.antalTerninger()+1; // Tilføjer trappen
                for (int i = 2; i < 7; i++) {
                    Kombinationer[i] += raflebaeger.antalTerninger()+1; // Tilføjer trappen
                }
            }else{
                AntalØjne[1] += raflebaeger.antalDerViser(1); // Antal 1'ere
                for (int i = 2; i <= 6; i++) {
                    AntalØjne[i] += raflebaeger.antalDerViser(i);
                    Kombinationer[i] += raflebaeger.antalDerViser(i) + raflebaeger.antalDerViser(1);
                }
            }
            

        }
        runde_nr++;
    }

    private void runde_slut() {
        for (int i = 0; i < antal_spillere; i++) {
            if (i != taber - 1) {
                if (liste_af_raflebaeger.get(i).terninger.size() != 0) { // Har terninger
                    liste_af_raflebaeger.get(i).fjernTerning(); // Fjern en terning
                } else { // Har ikke terninger

                }
            }
            liste_af_raflebaeger.get(i).ryst(); // Ryst raflebageret
            //System.out.println("Rystet: "+i);
        }
        for (int i = 0; i <= 6; i++) {
            AntalØjne[i] = 0;
            Kombinationer[i] = 0;
        }
        forrige_gæt = null;
        nuværende_gæt = null;
        spil_status = "runde_slut";

        int tomme = 0;
        for (int i = 0; i < antal_spillere; i++) {
            if (liste_af_raflebaeger.get(i).tom) {
                tomme++;
            }
        }
        if (tomme >= antal_spillere - 1) {
            spil_status = "spil_færdig";
            System.out.println("Spiller " + taber + " har tabt!");
        }
    }

    private void Hvis_turErDet() {
        System.out.println("Det er nu spiller " + hvis_tur + "'s tur ");
    }

    public boolean test_spilstatus() {
        if (spil_status == "spil_færdig") {
            System.out.println("Genstart spillet! Spiller " + taber + " har tabt!");
            return true;
        }
        return false;
    }

    int antal_terninger_ialt() {
        int antal_terninger_ialt = 0;
        for (int i = 0; i < antal_spillere; i++) {
            antal_terninger_ialt += liste_af_raflebaeger.get(i).antalTerninger();
        }
        return antal_terninger_ialt;
    }

    private void Skift_tur() {
        hvis_tur++;
        if (hvis_tur > antal_spillere) {
            hvis_tur = 1;
        }
        Hvis_turErDet();
    }

    public int getHvis_tur() {
        return hvis_tur;
    }

}