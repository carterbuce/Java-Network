import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/** Solicitor.java
 * @author Carter Buce | cmb9400 
 *
 * Version:
 *		$Id: Solicitor.java,v 1.9 2015/12/01 00:30:16 cmb9400 Exp $
 *
 * Revisions:
 *		$Log: Solicitor.java,v $
 *		Revision 1.9  2015/12/01 00:30:16  cmb9400
 *		*** empty log message ***
 *
 *		Revision 1.8  2015/11/30 22:52:09  cmb9400
 *		remove debug
 *
 *		Revision 1.7  2015/11/30 22:50:38  cmb9400
 *		add debug
 *
 *		Revision 1.6  2015/11/30 22:35:18  cmb9400
 *		write to println
 *
 *		Revision 1.5  2015/11/30 22:28:10  cmb9400
 *		fixed socket reader
 *
 *		Revision 1.4  2015/11/30 22:26:02  cmb9400
 *		changed scanner read
 *
 *		Revision 1.3  2015/11/30 22:19:27  cmb9400
 *		bufferedwriter pritnln to write
 *
 *		Revision 1.2  2015/11/30 21:55:34  cmb9400
 *		finished methods
 *
 *		Revision 1.1  2015/11/30 20:01:52  cmb9400
 *		initial commit
 *
 */

/**
 * @author Carter Buce
 * The client class for MyWebServer
 */
public class Solicitor {
	private static Socket socket;
	
	/**
	 * create a connection to the server
	 * @param args the server name and port
	 */
	public static void main(String[] args){
		if(args.length != 2){
			usage();
		}
		
		try{
			socket = new Socket(args[0], Integer.parseInt(args[1]));
		}
		catch(IOException | NumberFormatException e){
			usage();
		}
		
		BufferedReader in;
		PrintWriter out;
		
		//create a read/write connection with the server
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
	
			// get and send input
			System.out.println(in.readLine());
			Scanner sc = new Scanner(System.in); 
			String file = sc.nextLine();
			out.println(file);
			
			String line = in.readLine();
			while(line != null){
				System.out.println(line);
				line = in.readLine();
			}
			
			sc.close();
		}
		catch(IOException e){
			System.err.println("Something went wrong communicating with the server...");
			System.exit(1);
		}
		
		
		//client finished, closes
		try {
			socket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * print the usage message and exit the program
	 */
	public static void usage(){
		System.err.println("Usage: java Solicitor Server_name Server_port");
		System.exit(1);
	}
	
}
