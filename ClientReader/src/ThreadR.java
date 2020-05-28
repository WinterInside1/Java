import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ThreadR extends Thread{
    Random rnd = new Random();
    long startTime;
    long endTime ;
    long estimatedTime ;
    double time ;

    long waitingTime;
    Socket socket;
    DataInputStream ois;
    DataOutputStream oos;
    String message;


    public ThreadR(){

        this.waitingTime=5000;
    }

    public void run(){

        try {
            socket = new Socket(ClientReader.jTextField1.getText(),Integer.parseInt(ClientReader.jTextField2.getText()));
            oos= new DataOutputStream(socket.getOutputStream());
            ois= new DataInputStream(socket.getInputStream());
            oos.writeUTF("Reader");
            oos.writeUTF(System.getProperty("user.name"));

            ClientReader.jTextArea1.append("Connected to Server\n");
            while( !ClientReader.stopping){
                startTime = System.nanoTime();
                message=  ois.readUTF() ;

                ClientReader.jTextArea1.append("Data Received");
                ClientReader.check(message);
                endTime = System.nanoTime();
                estimatedTime = endTime - startTime; // We get the elapsed time in nanoseconds
                time = (double)estimatedTime/1000000;
                ClientReader.jTextArea1.append("Receiving Time: "+time+"\n\n");
            }
            ClientReader.jTextArea1.append("Data Reception Stopped");
            ois.close();
            oos.close();
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(ClientReader.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Error connecting to server");
        }

    }

}
