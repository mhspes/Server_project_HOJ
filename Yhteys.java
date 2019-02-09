import java.lang.Exception;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/* Sis‰lt‰‰ kohdat 1-2
 * */
public class Yhteys{
  
  private String osoite;
  private int portti;
  
  // Datagram-yhteys
  // Parametreiksi kohteen osoite (localhost) ja portti 3126
  public Yhteys(String osoite, int portti){
    this.osoite = osoite;
    this.portti = portti;
  }
  
  // Yhteydenotto datagram paketilla
  public Socket yhdista() throws Exception {
    
    InetAddress targetAddr = InetAddress.getByName(osoite);       // kohteen IP-osoite
    int targetPort = portti;       

    DatagramSocket dgSocket = new DatagramSocket();               // Luodaan Datagram-soketti l‰hetyst‰ varten
    String omaportti = Integer.toString(dgSocket.getLocalPort());
    byte[] data = omaportti.getBytes();                           // Viestiksi oma portti
    DatagramPacket packet = new DatagramPacket(data, data.length, targetAddr, targetPort);
    
    
    // Luodaan TCP-soketti
    ServerSocket localSocket = new ServerSocket(dgSocket.getLocalPort());
    Socket vierasSocket = null;
    
    // Yritet‰‰n yhteydenottoa 5 kertaa
    // 5 s kerrallaan
    for ( int i = 1 ; i <= 5 ; i++) {
      dgSocket.send(packet); 
      try {
        localSocket.setSoTimeout(5000);
        vierasSocket = localSocket.accept();
        return vierasSocket;
    } catch (Exception e) {
      if (i!=5){
      System.out.println("Ei yhteytt‰, yritet‰‰n uudelleen.");
      }
    }
    }
    // Yhteytt‰ ei saatu, suljetaan datagram- ja TCP-soketti
    System.out.println("Yhteytt‰ ei saatu.");  
    localSocket.close();                         
    dgSocket.close();
    return null;

  }
}