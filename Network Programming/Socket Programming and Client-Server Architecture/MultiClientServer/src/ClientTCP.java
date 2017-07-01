/**
 * Created by Pragya on 2/11/2016.
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTCP {

    public static void main(String args[]) throws IOException{
        for(int i=0;i<=9;i++)
        {

            if(args[i].equals("-h"))
            {
                System.out.println("---HELP---"+"\n"+"for executing the code, syntax is java <filename> -s <servername> -p <port> -c <command> -n <executioncount> -d <delay>");
                System.exit(1);
            }
        }
        if (args[0].equals("-s"))
            try
            {	String ser =null;
                ser = args[1];
            }
            catch(Exception e1)
            {
                System.out.println("Enter String value");
                System.exit(2);
            }

        if(args[2].equals("-p"))
            try
            {
                int p = 0;
                p= Integer.parseInt(args[3]);
            }
            catch(NumberFormatException nfe){
                System.out.println(" The port should be an integer value");
                System.exit(3);
            }
        if(args[4].equals("-c"))
            try
            { String c = null;
                c= args[5];
            }
            catch(Exception e2){
                System.out.println(" The command is not recognized");
                System.exit(4);
            }
        if(args[6].equals("-n"))
            try
            { int n = 0;
                n= Integer.parseInt(args[7]);
            }
            catch(Exception e3){
                System.out.println(" The execution count should be an integer value");
                System.exit(5);
            }
        if(args[8].equals("-d"))
            try
            { int d = 0;
                d= Integer.parseInt(args[9]);
            }
            catch(Exception e4){
                System.out.println(" The delay should be an integer value");//
                System.exit(6);
            }

        Socket s1=null;
        BufferedReader br=null;
        BufferedReader is=null;
        PrintWriter os=null;

        try {
            String s = new String(args[0]+" "+args[1]+" "+args[2]+" "+args[3]+" "+args[4]+" "+args[5]+" "+args[6]+" "+args[7]+" "+args[8]+" "+args[9]);


            InetAddress address=InetAddress.getByName(args[1]);
            int serverport = Integer.parseInt(args[3]);

            s1=new Socket(address, serverport);


            long start = System.currentTimeMillis();
            DataOutputStream dos = new DataOutputStream(s1.getOutputStream());
            dos.writeUTF(s);                                                    //sending the message string with execution count and delay and command

            //read line from server

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader (s1.getInputStream()));


            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = inFromServer.readLine()) != null)
            {
                System.out.println(line);
                sb.append(line);
                sb.append(" ");
                long end = System.currentTimeMillis();
                //System.out.println(end);
                long RTT = end-start;
                System.out.println("Round trip time "+RTT+" milliseconds");

            }
            inFromServer.close();
            s1.close(); //closing the client socket


        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
    }




}


