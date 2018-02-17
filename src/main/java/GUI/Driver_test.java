import java.util.LinkedList;

public class Driver_test {
	public static void main(String args[]) {
		System.out.println("started the driver app");
//		FetchData d = new FetchData();
						
		System.out.println("hello??");
		(new Thread(new FTPData())).start();

		while(true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			System.out.println("waefofeawohofae " + FTPData.PBV350);

		}
		
		
	}
}
