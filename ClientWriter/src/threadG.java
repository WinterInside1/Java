
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
public class threadG extends Thread implements Runnable{

    Random rnd = new Random();
    long startTime;
    long endTime ;
    long estimatedTime ;
    double time ;

    long waitingTime;
    Socket soket;
    DataInputStream ois;
    DataOutputStream oos;

    public threadG(){
        this.waitingTime=2000;
    }

    @Override
    public void run(){
        try {
            soket = new Socket(ClientWriter.jTextField1.getText(),
                    Integer.parseInt(ClientWriter.jTextField2.getText()));
            oos = new DataOutputStream(soket.getOutputStream());
            oos.writeUTF("Writer");
            while(ClientWriter.a){

                startTime = System.nanoTime();



                ClientWriter.jTextArea1.append("Connected to the server\n");
                oos.writeUTF(ClientWriter.jLabel10.getText()+";"+ClientWriter.jLabel11.getText()+
                        ";"+ClientWriter.systematizingIs("yyyy/MM/dd H:mm:ss")+";"
                        +ClientWriter.ramrandom()+";"+cpuRandom()+";"+ClientWriter.DiskInfo()+";");

                endTime = System.nanoTime();
                estimatedTime = endTime - startTime; // We get the elapsed time in nanoseconds
                time = (double)estimatedTime/1000000;
                ClientWriter.jTextArea1.append(
                        "Submission Time: "+time+"\n\n");
                threadG.sleep(waitingTime);
            }
            oos.close();
            soket.close();

// TODO add your handling code here:
        } catch (IOException ex) {
            Logger.getLogger(ClientWriter.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Error connecting to server");
        } catch (InterruptedException ex) {
            Logger.getLogger(threadG.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public   int cpuRandom(){

        return (int) (rnd.nextDouble()*100);

    }


}







