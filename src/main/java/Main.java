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
        	String printable = command.substring(4, command.length());
        	System.out.println(printable);
        	
        }
        else {
        	System.out.println(command+": command not found");
        	}
        }
    	sc.close();
    }
}
