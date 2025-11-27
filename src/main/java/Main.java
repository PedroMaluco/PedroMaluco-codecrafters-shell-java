import java.lang.reflect.Executable;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
    	Scanner sc = new Scanner(System.in);
    	while(true) {
        System.out.print("$ ");
        String command = sc.nextLine().toLowerCase();
        String[] fragmented = command.split(" ");
        if(fragmented[0].equals("exit")) {
        	break;
        }
        if(fragmented[0].equals("echo")) {
        	String printable = command.substring(5, command.length());
        	System.out.println(printable);
        	
        }
        if(fragmented[0].equals("type")) {
        	if (fragmented[1].equals("exit") || fragmented[1].equals("echo") || fragmented[1].equals("type")) {
        		String printable = fragmented[1];
            	System.out.println(printable + " is a shell builtin");
        	}
        	else {
        		String minusType = command.substring(5, command.length());
        		String[] path = minusType.split(":/");
        		for(String exe : path) {
        			
        		}
        	
        	}
        }
        if(!fragmented[0].equals("exit") && !fragmented[0].equals("echo") && !fragmented[0].equals("type")) {
        	System.out.println(command+": command not found");
        	}
        }
    	sc.close();
    }
}
