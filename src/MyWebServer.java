import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/** MyWebServer.java
 * @author Carter Buce | cmb9400 
 *
 * Version:
 *		$Id: MyWebServer.java,v 1.6 2015/11/30 21:52:09 cmb9400 Exp $
 *
 * Revisions:
 *		$Log: MyWebServer.java,v $
 *		Revision 1.6  2015/11/30 21:52:09  cmb9400
 *		added timeout message
 *
 *		Revision 1.5  2015/11/30 21:24:20  cmb9400
 *		changed system out to system err
 *
 *		Revision 1.4  2015/11/30 21:18:29  cmb9400
 *		added hostname output
 *
 *		Revision 1.3  2015/11/30 21:09:07  cmb9400
 *		fixed serversocket port creation, added timeout
 *
 *		Revision 1.2  2015/11/30 20:58:53  cmb9400
 *		wrote methods
 *
 *		Revision 1.1  2015/11/30 20:01:51  cmb9400
 *		initial commit
 *
 */

/**
 * @author Carter Buce
 * 
 * Create a web server that waits for clients,
 * then creates threads for each client.
 * Times out after 5 minutes.
 */
public class MyWebServer {
	private static ServerSocket socket;
	
	
	public static void main(String[] args){
		try {
			socket = new ServerSocket(0);
			System.out.println("Server created on port " + 
								socket.getLocalPort() +
								" with hostname " + 
								InetAddress.getLocalHost().getHostName());
								
		} 
		catch (IOException e) {
			System.err.println("Something went wrong when creating the server...");
			System.exit(1);
		}
		

		
		while(true){	
			try {
				socket.setSoTimeout(300000); //5 minutes
				Socket received = socket.accept();
				System.out.println("Socket received: " + received.toString());
				Servant srv = new Servant(received);
				srv.start();
			} 
			catch (SocketTimeoutException e1){
				System.out.println("No connections in 5 minutes, shutting down server.");
				System.exit(0);
			}
			catch (IOException e) {
				System.err.println("Something went wrong when accepting a connection...");
				System.exit(1);
			}
			
		}
	}
	
}
