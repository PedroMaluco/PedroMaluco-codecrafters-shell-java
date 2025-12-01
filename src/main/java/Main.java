import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
    	Scanner sc = new Scanner(System.in);
    	while(true) {
        System.out.print("$ ");
        String wholeCommand = sc.nextLine().toLowerCase();
        String[] fragmentedCommand = wholeCommand.split(" ");
        String mainCommand = fragmentedCommand[0];
        if(mainCommand.equals("exit")) {
        	break;
        }
        if(mainCommand.equals("echo")) {
        	String printable = wholeCommand.substring(5, wholeCommand.length());
        	System.out.println(printable);
        	
        }
        if(mainCommand.equals("type")) {
        	if (fragmentedCommand[1].equals("exit") || fragmentedCommand[1].equals("echo") || fragmentedCommand[1].equals("type")) {
        		String printable = fragmentedCommand[1];
            	System.out.println(printable + " is a shell builtin");
        	}
        	else {
        		String path = System.getenv("PATH");
        		String[] fragmentedPath = path.split(":");
        		
        		for (int i=0; i < fragmentedPath.length; i++) {
        			File file = new File(fragmentedPath[i], fragmentedCommand[1]);
        			if(file.exists() && file.canExecute()) {
        				System.out.println(fragmentedCommand[1] + " is " + file.getAbsolutePath());
        				break;
        			}
        			if(!file.exists() && i == fragmentedPath.length-1) {
        				System.out.println(fragmentedCommand[1] + ": not found");
        			}
        		}
        	}
        }
        if(!fragmentedCommand[0].equals("exit") && !fragmentedCommand[0].equals("echo") && !fragmentedCommand[0].equals("type")) {
        	System.out.println(wholeCommand+": command not found");
        	}
        }
    	sc.close();
    }
}
