import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Pragya on 2/11/2016.
 */

public class ServerUDP
{
    public static void main(String[] args) throws Exception, NullPointerException
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
        System.out.println("------Server Started-------");
        System.out.println("Waiting for connection....");
        int serport = Integer.parseInt(args[1]);
        @SuppressWarnings("resource")
        DatagramSocket dss = new DatagramSocket(serport);

        for(;;)
        {

            byte[] buf = new byte[1024];

            DatagramPacket dp = new DatagramPacket(buf, 1024); // creating datagram packet
            dss.receive(dp);

            String s = new String(dp.getData(), 0, dp.getLength()); //receving message from the Client

            String[] split = s.split("\\:"); 					//splitting the string message

            String cmd = split[5];

            int exec = Integer.parseInt(split[7]);
            int tdelay = Integer.parseInt(split[9]);
            InetAddress address =  InetAddress.getLocalHost();

            System.out.println("Status = Connected..");
            System.out.println("Source IP = " + address);

            Calendar cal = Calendar.getInstance();                              //for displaying current time
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            System.out.println("Current time = "+  sdf.format(cal.getTime()));
            System.out.println("Command = "+cmd);


            for (int i = 1; i <= exec; i++)                                      //loop for execution count
            {
                PrintWriter writer = new PrintWriter("UDP.txt");        //creating a file to store command results
                Calendar cal1 = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                String time1 = sdf1.format(cal1.getTime()).toString();

                ProcessBuilder builder = new ProcessBuilder(cmd);                // executing command
                builder.redirectErrorStream(true);
                Process p = builder.start();


                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); //reading result of the command

                String line;
                writer.write(time1);                                            //writing time to the file

                while ((line = br.readLine()) != null)                           //reading line and writing to file

                {
                    writer.append(" ");
                    writer.write(line);                                         //writing result to the file


                }
                writer.close();	                                                 //closing the writer


                BufferedReader br1 = new BufferedReader(new FileReader("UDP.txt")); //opening the file for reading

                String readf = null;

                while ((readf = br1.readLine()) != null)
                {


                    StringBuilder sb1 = new StringBuilder(readf); //storing result in string builder to send

                    String sendData = sb1.toString();


                    byte[] sendbytes = new byte[1024];
                    sendbytes = sendData.getBytes();                                //converting to bytes

                    dss.send(new DatagramPacket(sendbytes, sendbytes.length, dp.getAddress(), dp.getPort())); //sending result back to client
                }
                br1.close(); //reader close


                Thread.sleep(1000 * tdelay);  //gap between two execution

            }

        }

    }

}













