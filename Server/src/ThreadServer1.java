

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadServer1 extends Thread implements Runnable{


    private  Socket Client=null;
    String read;
    static DataOutputStream oos=null;
    static DataInputStream ois =null;
    private static Object object = new Object();
    private static Lock lock = new ReentrantLock();
    String Responseline;
    int threadIncoming;
    String address="";
    long someDate;


    private int portno;
    public ThreadServer1(Socket serverclient,int incomingThread){
        this.Client=serverclient;
        this.threadIncoming =incomingThread;
        this.someDate =5000;
    }


    private String readFile( String file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");


        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }

    public String info(String infoformat){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(infoformat);
        return sdf.format(calendar.getTime());
    }
    @Override
    public void run(){


        try {

            try (
                    DataInputStream reading = new DataInputStream(Client.getInputStream())) {
                switch (reading.readUTF()) {
                    case "Reader":{
                        String Reade="";
                        try {
                            oos = new DataOutputStream(Client.getOutputStream());
                            ois = new DataInputStream(Client.getInputStream());
                            Reade= ois.readUTF();
                            address=Client.getLocalAddress().toString();
                            Server.jTextArea1.append("Reader connected.Ip address: "+address+"\n");
                            Server.jTextArea1.append("Reader Adr: "+Reade+"\n");
                            String stop="";
                            String post;
                            while(oos!=null){


                                synchronized (object){
                                    post= readFile("someclients.server");

                                }

                                oos.writeUTF(post);
                                Server.jTextArea1.append("Data Sent: "+ info("H:mm:ss")+" ->"+Reade+"\n");
                                ThreadServer1.sleep(someDate);

                            }


                            oos.close();
                            ois.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThreadServer1.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    Client.close();
                    Server.jTextArea1.append(ThreadServer.threadler[this.threadIncoming].getName()+
                            " Availability"+":"+ ThreadServer.threadler[this.threadIncoming].getState()+"\n");

                    Server.jTextArea1.append("Writer Left.Ip address: "+address+"\n\n");
                    if((this.threadIncoming)!=0){
                        Server.jTextArea1.append(ThreadServer.threadler[this.threadIncoming -1].getName()+
                                " Availability"+":"+ ThreadServer.threadler[this.threadIncoming -1].getState()+"\n\n");

                    }
                    break;
                    //*****************************************************************************************************************************

                    case "Writer":{
                        String alınanver="";
                        address=Client.getLocalAddress()+"";
                        try {
                            ois = new DataInputStream(Client.getInputStream());


                            Server.jTextArea1.append("Writer Left.Ip address: "+Client.getLocalAddress()+"\n");
                            while ((Responseline = ois.readUTF()) != null) {
                                alınanver=address+";"+Responseline;
                                Server.jTextArea2.append("Sender Address : "+address+"   "+alınanver+"\n");
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
                        }



                        lock.lock();
                        try {
                            // The file writing function is called here and the necessary information is written and received
                            FileWriter fwtriter = new FileWriter("someclients.server",true);
                            BufferedWriter writer = new BufferedWriter(fwtriter);
                            writer.append(alınanver);
                            writer.newLine();
                            writer.close();
                            fwtriter.close();  } finally {

                        }

                        lock.unlock();
                        synchronized (object){//Prevents access to other processes while reading data

                            Server.check();
                        }

                    }
                    Server.jTextArea1.append(ThreadServer.threadler[this.threadIncoming].getName()+" " +
                            "Availability"+":"+ ThreadServer.threadler[this.threadIncoming].getState()+"\n");
                    Client.close();
                    Server.jTextArea1.append("Writer Left.Ip address: "+address+"\n\n");
                    if((this.threadIncoming)!=0){
                        Server.jTextArea1.append(ThreadServer.threadler[this.threadIncoming -1].getName()+" " +
                                "Availability"+":"+ ThreadServer.threadler[this.threadIncoming -1].getState()+"\n\n");

                    }

                    break;


                }
            }


        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }




}
