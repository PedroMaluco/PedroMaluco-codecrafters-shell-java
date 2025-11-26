import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
    	Scanner sc = new Scanner(System.in);
    	while(true) {
        System.out.print("$ ");
        String command = sc.nextLine().toLowerCase();
        String[] fragmented = command.split(" ");
        if(fragmented[1].equals("exit")) {
        	break;
        }
        if(fragmented[1].equals("echo")) {
        	System.out.println(command);
        	
        }
        else {
        	System.out.println(command+": command not found");
        	}
        }
    	sc.close();
    }
}
