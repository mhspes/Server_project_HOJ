/* Luokka lokerointia varten
 * */


public class Lokero {
    int lkm;
    int summa;
    
    public Lokero (){
        lkm = 0;
        summa = 0;
    }
    
    public void kasvataArvoa(int luettu){
        summa = summa + luettu;
        lkm = lkm + 1;
    }
    
    public int annaLkm(){
      return lkm;
    }
    public int annaSumma(){
      return summa;
    }
}