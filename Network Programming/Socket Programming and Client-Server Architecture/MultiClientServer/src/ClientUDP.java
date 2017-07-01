import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Pragya on 2/11/2016.
 */

public class ClientUDP
{
    public static void main(String[] args) throws Exception
    {


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

        DatagramSocket dsc = new DatagramSocket();  //creating a socket


        long start = System.currentTimeMillis();

        String s = new String(args[0]+":"+args[1]+":"+args[2]+":"+args[3]+":"+args[4]+":"+args[5]+":"+args[6]+":"+args[7]+":"+args[8]+":"+args[9]);

        //translating servername to IP address
        InetAddress ip = InetAddress.getByName(args[1]);
        int port = Integer.parseInt(args[3]);



        DatagramPacket dp = new DatagramPacket(s.getBytes(), s.length(), ip, port);
        dsc.send(dp);                                           //sending message to server with details

        int i =100;

        byte [] count = new byte[i];
        byte[] receiveData = new byte[1024];
        while(count.length!=0)
        {    	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            dsc.receive(receivePacket);
            System.out.println(new String(receivePacket.getData(), 0, receivePacket.getLength()));

            long end = System.currentTimeMillis();

            long RTT = end-start;   // to calculate Round trip time
            System.out.println("Round trip time "+RTT+" milliseconds");
            i--;
        }

        dsc.close();

    }




}



















