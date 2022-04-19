import java.util.Scanner;
import java.awt.Desktop ; 
import java.net.URI ; 

public class Driver {
	
	static void openURL (String url) { 
		// cribbed from: 
		// http://java-hamster.blogspot.com/2007/06/troubles-with-javaawtdesktop-browse.html 
		
		try { 
			if (Desktop.isDesktopSupported()) { 
				Desktop desktop = Desktop.getDesktop(); 
			
			if (desktop.isSupported(Desktop.Action.BROWSE)) { 
				desktop.browse(new URI(url)); 
			} else { 
				System.out.println("No browser. URL = " + url) ; 
			} 
			} else { 
				System.out.println("No desktop. URL = " + url) ; } 
			} catch (Exception e)
				{ e.printStackTrace(); } 
	}
		
	public static void main(String[] args) {
		// create table and try to read in the file
		CityTable table = new CityTable("US_Cities_LL.txt", 16000);
		table.writeToFile("US_Cities_LL.ser");

		CityTable US_Cities;
		String cName;
		US_Cities = CityTable.readFromFile("US_Cities_LL.ser");
		cName = "Arbutus, MD";
		System.out.println(US_Cities.find(cName));

		String url = "http://www.google.com/maps?z=10&q=";
		String userInput = null;
		Scanner reader = new Scanner(System.in);
		
		while (true) {
			System.out.println("Enter City, State (or 'quit'): ");
			userInput = reader.nextLine();
			if (!userInput.equalsIgnoreCase("quit")) {
				City city = US_Cities.find(userInput);
				if (city != null) {
					System.out.println(city.toString());
				}
			} else { break; }
		}
	}
}
