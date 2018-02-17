package GUI;

public class FTPDriverExample {
	public static void main(String args[]) {
		System.out.println("started the driver app");
		(new Thread(new FTPData())).start();
		while(true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("waefofeawohofae " + FTPData.PBVs.get("PBV350"));
		}
	}
}
