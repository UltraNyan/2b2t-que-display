package queNotifier;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;


public class Main {
	private final static String USER_AGENT = "Mozilla/5.0";
	
	 static void sendGet(String url) throws Exception {
		 
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
	

	public static class HelloWorld {
	
	    public static void main(String[] args) {
	    	String url="";
	    	
	        //Check the SystemTray is supported
	        if (!SystemTray.isSupported()) {
	            System.out.println("SystemTray is not supported");
	        } else {
		        final Image icon = Toolkit.getDefaultToolkit().getImage("res/2b2t.png");
		        final PopupMenu popup = new PopupMenu();
		        final TrayIcon trayIcon = new TrayIcon(icon);
		        final SystemTray tray = SystemTray.getSystemTray();
		        MenuItem exitItem = new MenuItem("Exit");
		        popup.add(exitItem);
		        exitItem.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	 JOptionPane.showMessageDialog(null, "Ur mom ghey");
		            	 System.exit(0);
		            }
		        });
		       
		        trayIcon.setPopupMenu(popup);
		       
		        try {
		            tray.add(trayIcon);
		        } catch (AWTException e) {
		            System.out.println("You fucked up");
		        }
	        }
	        
	        Properties prop = new Properties();
	        String fileName = "res/queue2screen.cfg";
	        InputStream is = null;
	        try {
	            is = new FileInputStream(fileName);
	        } catch (FileNotFoundException ex) {
	        	JOptionPane.showMessageDialog(null, "You fucked up. Where did you put your cfg file?");
	        	System.exit(1337);
	        }
	        try {
	            prop.load(is);
	        } catch (IOException ex) {
	        	JOptionPane.showMessageDialog(null, "Ur mom ghey");
	        }
	        
	        if (prop.getProperty("url") != null) {
	        	url = prop.getProperty("url");
	        	System.out.println("Device at: "+url);	        	
	        } else {
	        	JOptionPane.showMessageDialog(null, "cfg file looks funky");
	        	System.exit(1337);
	        }
	        
	                
	    	
	    	
			String last = "";
			String newLast = "";			
	    	while(true) {
		    	String home = System.getProperty("user.home");
		    	String logfile = "";
		    	if (System.getProperty("os.name").toLowerCase().contains("windows")){
					logfile = home + "/AppData/Roaming/.minecraft/logs/latest.log";
				 } else {
					 System.out.println(System.getProperty("os.name"));
					 logfile = home + "/.minecraft/logs/latest.log";
				 }
		    	
		        BufferedReader input;
				try {
					input = new BufferedReader(new FileReader(logfile));
	
					String line = null;
					
			        while ((line = input.readLine()) != null) { 
			            newLast = line;
			        }
			        
			        LogMessage message = new LogMessage(newLast);
			        
			        if (!message.getContent().contentEquals(last)) {
			        	last = message.getContent();
			        	System.out.println(message.getType() + ": " + last);

				        if (message.getType() == Type.QUEUE) {
				        	String query = URLEncoder.encode(last, "UTF-8");
				        	//Leak your IP here
			            	String request = "http://"+ url +"/?message=" + query;
			            	try {
								sendGet(request);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								System.out.println("You need really high IQ to get the meme E");
								e.printStackTrace();
							}
				        }
			        }
			        

			        
				} catch (FileNotFoundException e) {			
					e.printStackTrace();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} 
		        
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		    }
	    }
	
	}
}





