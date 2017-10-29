import java.util.*;
import java.io.*;

public class fileio{

  public static void main(String[] args) throws FileNotFoundException{

    while(true){

	try{
      File file = new File("C:\\Data\\MARCO1.txt");
      Scanner scanner = new Scanner(file);

      System.out.println(scanner.nextFloat());

      scanner.close();
      }catch(Exception e){};

    }


  }
}
