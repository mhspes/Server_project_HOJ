import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.ArrayList;

/* Sis‰lt‰‰ kohdat 1-3
 * 
 * 
 * */
public class Streamer extends Thread{
     
      private InputStream iS;
      private OutputStream oS;
      private ObjectOutputStream oOut;
      private ObjectInputStream oIn;
      private Socket vierasSocket;
      private SummausPalvelin sPlist[]; // Lista summauspalvelimista
      private Lokero lokero;            // Lokero
      private Operaatiot op;            // Olio operaatioita varten
      
      // Konstruktori
      // Luo Objectinput/output streamit yhteydellist‰
      // yhteytt‰ varten.
      public Streamer(Socket vierasSocket) throws Exception {
      
        try {
          
          vierasSocket.setSoTimeout(5000);
          this.vierasSocket = vierasSocket;  
          iS = vierasSocket.getInputStream();
          oS = vierasSocket.getOutputStream();
          oOut = new ObjectOutputStream(oS);
          oIn = new ObjectInputStream(iS);
          
        } catch (SocketException se ) {
          System.out.println(se.getMessage());
          kill();  
        } catch (IOException ioe ) {
          System.out.println(ioe.getMessage());
          kill();
        }
      }
      
      /* Vastaanotetaan kokonaisluku t ja generoidaan t kpl summauspalvelimia
       * vapaisiin porttipaikkoihin.
       * L‰hetet‰‰n porttinumerot Palvelin Y:lle joka muodostaa yhteydet jokaiseen porttiin
       * (ks. SummausPalvelin-luokan run() metodin alku)
       */
      public void portListener() throws Exception {
        
      try {
        vierasSocket.setSoTimeout(5000);
        int t = oIn.readInt();      
        System.out.println("Kokonaisluku " + t + " vastaanotettu.");
        
        sPlist = new SummausPalvelin[t];  // Lista summauspalvelimista
        // Luodaan t kpl summauspalvelimia
        for ( int i = 0 ; i <= t-1 ; i++){
          lokero = new Lokero();
          SummausPalvelin sP = new SummausPalvelin(i,lokero);
          
          sPlist[i] = sP;
          sP.start(); // k‰ynnistet‰‰n s‰ikeen run()-metodi
          int portSP = sPlist[i].getPort();
          // L‰hetet‰‰n summauspalvelimien porttinumerot palvelin Y:lle
          System.out.println("L‰hetet‰‰n porttinro: " + portSP + " osoitteeseen " + vierasSocket.getInetAddress() + " " + vierasSocket.getPort());
          oOut.writeInt(portSP);       
          oOut.flush();
        }
        // L‰hetet‰‰n lista summauspalvelimista Operaatiot-luokalle
        op = new Operaatiot(sPlist);
        
        
        while(true) {
          if(oIn.available() > 0){
            
            int kasky = oIn.readInt(); // Luetaan k‰sky
            int palautus = 0;
        
        if ( kasky < 0 || kasky > 3 ){
          oOut.writeInt(-1);          // K‰sky != {0,1,2,3}, l‰hetet‰‰n -1
        }
        switch(kasky){
          case 0:                     // K‰sky = 0, lopetus
            kill();
            lopetaKaikki();
            return;
          case 1:                     // K‰sky = 1
            palautus = op.laskeSumma();
            System.out.println("Palautetaan yhteissumma: " + palautus);
            oOut.writeInt(palautus);
            oOut.flush();
          case 2:                     // K‰sky = 2
            palautus = (op.suurinSumma()).getIndex();
            System.out.println("Palautetaan suurimman summan omaava SP: " + palautus);
            oOut.writeInt(palautus);
            oOut.flush();
          case 3:                     // K‰sky = 3
            palautus = op.kokonaisMaara();
            System.out.println("Palautetaan saatujen lukujen kokonaism‰‰r‰: " + palautus);
            oOut.writeInt(palautus);
            oOut.flush();
        }
        Thread.sleep(500); // Sleep tulosteiden selkeytymiseksi
        
          }
        }
      } catch ( IOException e) { 
    System.out.println("Lukua ei saatu, lopetetaan..");
    e.printStackTrace();
    }
      oOut.writeInt(-1);
      kill();
    
  }
      // Sulje datavirrat ja soketti
      public void kill() throws IOException {
        oOut.flush();  
        oOut.close();
        oIn.close();
        oS.close();
        iS.close();   
        vierasSocket.close();
      }
      // Lopettaa kaikki SP-s‰ikeet
      public void lopetaKaikki(){
        for ( int i = 0 ; i < sPlist.length ; i ++){
          sPlist[i].lopeta();
        }
      }
      
}