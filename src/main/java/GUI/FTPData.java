package GUI;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.Runnable;
import java.util.*;

public class FTPData implements Runnable {

	public static Map<String, Integer> PBVs = new TreeMap<>();
	public static Map<String, Float> Temps = new TreeMap<>();
	public static Map<String, Float> PTs = new TreeMap<>();

	private FTPClient client;

	/**
	 * Sets up the FTP communication.
	 */
	public FTPData() {}
	
	/**
	 * Establishes the FTP communication with the DAQ FTP server.
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
			client.login("6", "6");
			reply = client.getReplyCode();
			System.out.println("REPLY CODE:\t"+reply);
			client.cwd("hd0");

//			for(FTPFile file: client.listFiles()) {
//				System.out.println(file.getName());
//			}
			
			while(true) {
				try {
					Thread.sleep(500);
					updateValues();
				} catch (InterruptedException e) {
					e.printStackTrace();
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
		long time = System.nanoTime();
		try {
			System.out.println("STarted TRY");

			byte buffer[] = new byte[4096];
			int readCount;
			int length = 0;


			System.out.println("line 103");
			InputStream in = client.retrieveFileStream("DATA");
			BufferedInputStream inbf = new BufferedInputStream(in);
			System.out.println("line 105");

			client.completePendingCommand();

			System.out.println("line 109");

			readCount = inbf.read(buffer);
			String string = "";
			
			for(int i = 0; i < readCount - 1; i ++) {
				string += (char) buffer[i];
			}

			String[] allData = string.split("\\s"), data;
			int i;

			data = allData[1].split("\\,");

			i = 0;
			FTPData.Temps.put("T290", Float.parseFloat(data[i++]));
			FTPData.Temps.put("T291", Float.parseFloat(data[i++]));
			FTPData.Temps.put("T292", Float.parseFloat(data[i++]));
			FTPData.Temps.put("T293", Float.parseFloat(data[i++]));
			FTPData.Temps.put("T390", Float.parseFloat(data[i++]));
			FTPData.Temps.put("T391", Float.parseFloat(data[i++]));
			FTPData.Temps.put("T392", Float.parseFloat(data[i++]));
			FTPData.Temps.put("T393", Float.parseFloat(data[i++]));
			
			i = 0;
			FTPData.PBVs.put("PBV150", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV151", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV250", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV251", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV252", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV253", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV350", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV351", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV352", allData[0].charAt(i++) - 48);
			FTPData.PBVs.put("PBV353", allData[0].charAt(i++) - 48);

			data = allData[2].split("\\,");

			i = 0;
			FTPData.PTs.put("PT120", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT121", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT122", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT220", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT221", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT222", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT223", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT320", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT321", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT322", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT323", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT420", Float.parseFloat(data[i++]));
			FTPData.PTs.put("PT421", Float.parseFloat(data[i++]));

			inbf.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Time taken " + (System.nanoTime() - time));
	}

}
