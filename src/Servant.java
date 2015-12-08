import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Servant.java
 * @author Carter Buce | cmb9400 
 *
 * Version:
 *		$Id: Servant.java,v 1.13 2015/12/04 21:30:21 cmb9400 Exp $
 *
 * Revisions:
 *		$Log: Servant.java,v $
 *		Revision 1.13  2015/12/04 21:30:21  cmb9400
 *		*** empty log message ***
 *
 *		Revision 1.12  2015/11/30 23:18:25  cmb9400
 *		*** empty log message ***
 *
 *		Revision 1.11  2015/11/30 22:52:09  cmb9400
 *		remove debug
 *
 *		Revision 1.10  2015/11/30 22:47:05  cmb9400
 *		added debug
 *
 *		Revision 1.9  2015/11/30 22:44:12  cmb9400
 *		that didn't work
 *
 *		Revision 1.8  2015/11/30 22:42:41  cmb9400
 *		changed out println to print
 *
 *		Revision 1.7  2015/11/30 22:41:18  cmb9400
 *		..
 *
 *		Revision 1.6  2015/11/30 22:38:58  cmb9400
 *		updated requesting filename
 *
 *		Revision 1.5  2015/11/30 22:35:18  cmb9400
 *		write to println
 *
 *		Revision 1.4  2015/11/30 22:19:27  cmb9400
 *		bufferedwriter pritnln to write
 *
 *		Revision 1.3  2015/11/30 21:24:21  cmb9400
 *		changed system out to system err
 *
 *		Revision 1.2  2015/11/30 20:58:43  cmb9400
 *		wrote methods
 *
 *		Revision 1.1  2015/11/30 20:01:51  cmb9400
 *		initial commit
 *
 */

/**
 * @author Carter Buce 
 * 
 * Store information about the connection when created,
 * send a prompt to the client,
 * read the file name from the client,
 * open the requested file,
 * read the file and send the contents to the client a line at a tie,
 * close the file and connection, then die
 */
public class Servant extends Thread{
	private static Socket socket;
	private static InetAddress address;
	private static int port;
	private static String hostname;
	private static String localhost;
	private static InetAddress localaddress;
	
	/**
	 * initialize the connection with the client
	 * @param skt the socket of the client
	 */
	public Servant(Socket skt) {
		socket = skt;
		address = socket.getInetAddress();
		localaddress = socket.getLocalAddress();
		port = socket.getLocalPort();
		hostname = address.getHostName();
		localhost = localaddress.getHostName();
		
	}
	
	
	/**
	 * Prompt the client for a filename, then attempt to open the file
	 * and send it line-by-line to the client
	 */
	public void run(){
		BufferedReader in;
		PrintWriter out;
		String filename = "";
		
		//create a connection with the client
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println("(" + localhost + " --> " + hostname + ")" + " Enter a filename to see: ");
			filename = in.readLine();	

		}
		catch(IOException e){
			System.err.println("Something went wrong communicating with the client...");
			return;
		}
		
		
		//open the file
		BufferedReader fileReader;
		try{
			fileReader = new BufferedReader(new FileReader(filename));
		}
		catch(IOException e){
			System.err.println("Something went wrong opening the file...");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		
		//read the file and send it to the client
		String line;
		try {
			line = fileReader.readLine();
			while(line != null){
				out.println(line);
				line = fileReader.readLine();
			}
		} catch (IOException e) {
			System.err.println("Something went wrong reading the file...");
			try {
				socket.close();
				fileReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		
		//close the connection
		try{
			System.out.println("Transfer completed.");
			fileReader.close();
			socket.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
	}

}
