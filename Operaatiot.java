import java.util.Arrays;
import java.util.Collections;
/* Luokka Summauspalvelimen operaatioita varten:
 * 1. V‰litettyjen lukujen kokonaissumma
 * 2. Mill‰ palvelimella suurin summa?
 * 3. Vastaanotettujen lukujen kokonaism‰‰r‰
 * 
 * */
public class Operaatiot extends Thread{
  
  private SummausPalvelin[] sPlist;
  private Lokero lokero;
  private boolean lukossa;
  private int summa;
  private int lkm;
  
  
  
  // Konstruktori, sis‰lt‰‰ listan summauspalvelimista
  public Operaatiot(SummausPalvelin[] sPlist){
    this.sPlist = sPlist;
  }
  // Lukitusmetodit, ei valmis
  public void lukitse(boolean lukosssa){
    this.lukossa = lukossa;
  }
  public boolean lukossa(){
    return lukossa;
  }
  
  // T‰h‰n menness‰ v‰litettyjen lukujen kokonaissumma
  public int laskeSumma(){
      
    int summa = 0;
    for ( int i = 0 ; i < sPlist.length ; i++){
     lokero = sPlist[i].getLokero();
     summa = summa + lokero.annaSumma();
    }
    return summa;
  }
  
  // Mill‰ palvelimella suurin summa?
  public SummausPalvelin suurinSumma(){
    
    int[] summat = new int[sPlist.length];
    
    for ( int i = 0 ; i < sPlist.length ; i++){
     lokero = sPlist[i].getLokero();
     summat[i] = lokero.annaSumma();  
    }
    int maksimi = summat[0];
    int indeksi = 0;
    // Suurin arvo k‰ytt‰m‰ll‰ lineaarihakua
    for ( int i = 1 ; i < summat.length ; i++){
      if ( summat[i] > maksimi ){ 
        maksimi = summat[i];
        indeksi = i;
      }
    }
    SummausPalvelin suurin = sPlist[indeksi];
    return suurin;
    
  }
  // Vastaanotettujen lukujen kokonaism‰‰r‰
  public int kokonaisMaara(){

    int summaLkm = 0;
    for ( int i = 0 ; i < sPlist.length ; i++){
      lokero = sPlist[i].getLokero();
      summaLkm = summaLkm + lokero.annaLkm();
    }
    return summaLkm;
  }
  
}