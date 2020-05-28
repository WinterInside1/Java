import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadServer extends Thread implements Runnable{

    static ServerSocket server=null;
    static Socket Client=null;
    String read;
    static DataOutputStream oos =null;
    static DataInputStream ois =null;
    String Responseline;
    public static  ThreadServer1 threadler[] = new ThreadServer1[20];

    private int portno;
    public ThreadServer(int port){
        this.portno=port;

    }


    @Override
    public void run(){
        try {

            server = new ServerSocket(portno);

            Server.jTextArea1.append("Server Started.\n\n");

            while(true){
                try {

                    Client= server.accept();


                    for(int i=0; i<=9; i++){
                        if(threadler[i]==null)
                        {

                            (threadler[i] = new ThreadServer1(Client,i)).start(); // When the new client arrives, it creates a clientLif for it and goes into listening mode again.
                            Server.jTextArea1.append("\nNew User Connected :"+threadler[i].getName()+"\n");

                            break;
                        }

                    }


                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


        } catch (IOException ex) {
            Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }




}
