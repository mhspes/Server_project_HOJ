import java.lang.Thread;
import java.io.*;
import java.net.*;

// Summauspalvelin 

public class SummausPalvelin extends Thread{
  
  private final int index;
  private ServerSocket serverSocket;      
  private boolean running = true;         // Onko päällä ?
  private Lokero lokero;                  // Lokero saaduille arvoille
  private Operaatiot op;                  // Operaatiot
  private boolean lukko;                  // Lukitus
  
  
  // Konstruktori
  // Summauspalvelimen numero, sekä lokero
  // Luo serversoketin vastaanottamiseen
  public SummausPalvelin(int index, Lokero lokero) throws IOException{
    this.index = index;
    this.lokero = lokero;
    serverSocket = new ServerSocket(0);  // Johonkin vapaaseen porttiin
    
  }
  
  // Palauta portti
  public int getPort(){
    return serverSocket.getLocalPort();
  }
  // Palauta numero
  public int getIndex(){
    return index+1;
  }
  // Palauta lokero
  public Lokero getLokero(){
    return lokero;
  }
  // Lopeta Summauspalvelin
  public void lopeta(){
    this.running = false;
  }
  
  // Säikeen run -metodi
  public void run(){
    try{
      Socket sp = serverSocket.accept(); // SP-kohtainen yhteys
      System.out.println("Yhteys luotu palvelimelle nro: " + (getIndex()));
      InputStream iS = sp.getInputStream();
      ObjectInputStream oIn = new ObjectInputStream(iS);

      while(true){
        if(!running){ // Onko päällä?
          return;
        }
        
        if(oIn.available() > 0){      
          
          int a = oIn.readInt();
          // luettiin 0, lopetus
          if(a == 0){
            oIn.close();
            iS.close();
            sp.close();
          } else {
            System.out.println("Portti " + (getIndex()) + " vastaanotti arvon " + a );
            lokero.kasvataArvoa(a); 
            System.out.println("Portin " + (getIndex()) + " kokonaissumma nyt: " + lokero.annaSumma());
            Thread.sleep(500);
          }
        }
      }
    } catch (Exception e) { 
      System.out.print(e.getMessage());
      e.printStackTrace();
    }
  }
}