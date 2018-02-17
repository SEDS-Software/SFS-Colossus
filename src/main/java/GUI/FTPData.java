import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Runnable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class FTPData implements Runnable{
	
	public static int PBV150, PBV151, 
				PBV250, PBV251, PBV252, PBV253, 
				PBV350, PBV351, PBV352, PBV353;
	
	public static InputStream in;
	public static BufferedInputStream inbf;
	
	public static float T290, T291, T292, T293, 
					T390, T391, T392, T393;
	
	public static float PT120, PT121, PT122,
					PT220, PT221, PT222, PT223,
					PT320, PT321, PT322, PT323,
					PT420, PT421;
	
	private FTPClient client;
	
	private LinkedList<Byte> values;
	
	private static boolean connected = false;
	
	/**
	 * Sets up the FTP communication
	 */
	public FTPData() {
	}
	
	/**
	 * Establishes the FTP communication with the DAQ FTP server
	 */
	
	public void run() {
		makeConnection();
	}
	
	public void makeConnection() {
		client = new FTPClient();
		System.out.println("making the FTP Client");
		try {
			int reply;
			System.out.println("about to connect");
			client.connect("192.168.1.28");
			reply = client.getReplyCode();
			client.login("6", "6");
			System.out.println("REPLY CODE:\t"+reply);
			client.cwd("hd0");

//			for(FTPFile file: client.listFiles()) {
//				System.out.println(file.getName());
//			}
			

			
			
			while(true) {
				try {
					Thread.sleep(250);
					updateValues();
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		finally {
			System.out.println("inside finally");
		}
	}
	
	
	public void updateValues() {
		Thread temp = (new Thread(new updateValues(client)));
		temp.start();	
	}
	
	/**
	 * Updates the values of the instance variables
	 */
	
}
class updateValues implements Runnable{
	
	LinkedList<Byte> values = new LinkedList<Byte>();
	
	FTPClient client;
	
	public updateValues(FTPClient client) {
		this.client = client;
				
	}
	
	
	
	public void run() {
		byte[] result = null;
		
		
		try {
			result = null;
			
			
			byte buffer[] = new byte[4096];
			int readCount;
			int length = 0;
			
			InputStream in = client.retrieveFileStream("DATA");
			BufferedInputStream inbf = new BufferedInputStream(in);

			client.completePendingCommand();
			
			readCount = inbf.read(buffer);
			String string = "";
			
			
			for(int i = 0; i < readCount - 1; i ++) {
				char lmao = (char) buffer[i];
				string += lmao;
			}
			
			String[] allData = string.split("\\s");
			String[] data = allData[1].split("\\,");
			int i = 0;

			
			FTPData.T290 = Float.parseFloat(data[i++]);
			FTPData.T291 = Float.parseFloat(data[i++]);
			FTPData.T292 = Float.parseFloat(data[i++]);
			FTPData.T293 = Float.parseFloat(data[i++]);
			FTPData.T390 = Float.parseFloat(data[i++]);
			FTPData.T391 = Float.parseFloat(data[i++]);
			FTPData.T392 = Float.parseFloat(data[i++]);
			FTPData.T393 = Float.parseFloat(data[i++]);
			
			i = 0;
			
			FTPData.PBV150 = allData[0].charAt(i++) - 48;
			FTPData.PBV151 = allData[0].charAt(i++) - 48;
			FTPData.PBV250 = allData[0].charAt(i++) - 48;
			FTPData.PBV251 = allData[0].charAt(i++) - 48;
			FTPData.PBV252 = allData[0].charAt(i++) - 48;
			FTPData.PBV253 = allData[0].charAt(i++) - 48;
			FTPData.PBV350 = allData[0].charAt(i++) - 48;
			FTPData.PBV351 = allData[0].charAt(i++) - 48;
			FTPData.PBV352 = allData[0].charAt(i++) - 48;
			FTPData.PBV353 = allData[0].charAt(i++) - 48;
			
			
			data = allData[2].split("\\,");
			i = 0;
			
			FTPData.PT120 = Float.parseFloat(data[i++]);
			FTPData.PT121 = Float.parseFloat(data[i++]); 
			FTPData.PT122 = Float.parseFloat(data[i++]);
			FTPData.PT220 = Float.parseFloat(data[i++]);
			FTPData.PT221 = Float.parseFloat(data[i++]);
			FTPData.PT222 = Float.parseFloat(data[i++]);
			FTPData.PT223 = Float.parseFloat(data[i++]);
			FTPData.PT320 = Float.parseFloat(data[i++]);
			FTPData.PT321 = Float.parseFloat(data[i++]);
			FTPData.PT322 = Float.parseFloat(data[i++]);
			FTPData.PT323 = Float.parseFloat(data[i++]);
			FTPData.PT420 = Float.parseFloat(data[i++]);
			FTPData.PT421 = Float.parseFloat(data[i++]);
		
			
			
		}catch (IOException e) {
			//e.printStackTrace();
		}catch(Exception e) {
			//e.printStackTrace();
		}finally {
			
		}
		
	}
	
	
}
