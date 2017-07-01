import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Pragya on 2/11/2016.
 */

public class ServerTCPThread {

    @SuppressWarnings("resource")
    public static void main(String args[]) throws IOException
    {
        for(int i=0;i<=1;i++)
        {

            if(args[i].equals("-h"))
            {
                System.out.println("---HELP---"+"\n"+"for executing the code, syntax is java <filename> -p <port>");
                System.exit(1);
            }
            else
                try
                {	int num =0;
                    num = Integer.parseInt(args[1]);
                }
                catch(NumberFormatException nfe){
                    System.out.println(" The port should be an integer value");//
                    System.exit(2);
                }

        }
        int serport = Integer.parseInt(args[1]);
        Socket s=null;
        ServerSocket ss2=null;    //creating server socket
        System.out.println("-------Server Started and Listening-------");
        try{
            ss2 = new ServerSocket(serport); // binding socket to port

        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error");

        }

        while(true)  //accepting the client infinitely
        {
            s= ss2.accept();
            System.out.println("Status = Connected"); //display message when a client is connected
            ServerThread st=new ServerThread(s);  // creating server thread objects to handle multiple clients
            st.start();    //starting thread
        }

    }

}

class ServerThread extends Thread{

    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;

    public ServerThread(Socket s){
        this.s=s;
    }

    public synchronized void run()
    {
        try{ InetAddress address =  InetAddress.getLocalHost();
            System.out.println("Source IP = "+address);

            DataInputStream dis= new DataInputStream(s.getInputStream()); //receiving string message from client
            String recvmsg = dis.readUTF(); //reading the message
            System.out.println(recvmsg);
            String[] split = recvmsg.split("\\s"); //splitting the message so to store each arguments in different variable and use later

            String cmd = split[5];
            int exec = Integer.parseInt(split[7]);
            int tdelay = Integer.parseInt(split[9]);
            Calendar cal = Calendar.getInstance();  //for current time
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            System.out.println("Current Time = "+  sdf.format(cal.getTime()));

            System.out.println("Command = "+cmd);
            System.out.println("Command executed, sending result back to client...");



            for (int i = 1; i <= exec; i++) //loop for execution count
            { try
            {

                File temp = File.createTempFile("TCP", ".tmp");
                BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
                temp.deleteOnExit();

                Calendar cal1 = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                String time1 = sdf1.format(cal1.getTime()) .toString();


                ProcessBuilder builder = new ProcessBuilder(cmd); //executing Unix command
                builder.redirectErrorStream(true);
                Process p = builder.start();

                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                bw.write(time1);
                while ((line = br.readLine()) != null) //reading line and writing to file

                {

                    bw.append(" ");
                    bw.write(line);

                    bw.flush();
                }
                bw.close();

                String str;
                BufferedReader br1 = new BufferedReader(new FileReader(temp));
                while((str = br1.readLine()) != null) {

                    PrintWriter outToClient = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
                    outToClient.println(str);
                    outToClient.flush();
                }
                br1.close();
            }
            catch(IOException e)
            {

                e.printStackTrace();
            }


                Thread.sleep(1000 * tdelay);  //gap between two execution

            }

            s.close(); //closing client

        }

        catch(IOException | InterruptedException e)
        {
            System.out.println(" ");

            System.out.println("Connection Closing..");

            System.out.println("Socket Closed");
        }//end finally
    }


}


