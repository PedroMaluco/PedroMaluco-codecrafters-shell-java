import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
    	Scanner sc = new Scanner(System.in);
		boolean loop = true;
		
    	while(loop==true) {
        System.out.print("$ ");
        String wholeCommand = sc.nextLine();
        String[] fragmentedCommand = wholeCommand.split(" ");
        String mainCommand = fragmentedCommand[0];
        
        if(mainCommand.equals("exit") || mainCommand.equals("echo") || mainCommand.equals("type")) {
        	loop = mainShellBuiltIns(wholeCommand);
        }
        else if(findExecutableApp(wholeCommand) == true) {
        	runApp(wholeCommand);
        }
        else {
        	System.out.println(wholeCommand+": command not found");
        }
        
   }
    	sc.close();
}
	
	
	
	

	public static void runApp(String wholeCommand) {
		String envPath = System.getenv("PATH");
		String[] fragmentedPath = envPath.split(":");
		String[] fragmentedCommand = wholeCommand.split(" ");
		String targetExecutable = fragmentedCommand[0];
		for (int i=0; i < fragmentedPath.length;) {
			File file = new File(fragmentedPath[i], targetExecutable);
			if (file.exists() && file.canExecute()) {
				try {
					Runtime.getRuntime().exec(fragmentedCommand);
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
				return;
			}
		}

	
	public static boolean findAppType(String wholeCommand) {
		String envPath = System.getenv("PATH");
		String[] fragmentedEnviromentPath = envPath.split(":");
		String[] fragmentedCommand = wholeCommand.split(" ");
		String targetApp = fragmentedCommand[1];
		
		for (int i=0;i<fragmentedEnviromentPath.length;i++) {
			File file = new File(fragmentedEnviromentPath[i], targetApp);
			if(file.exists() && file.canExecute()) {
				System.out.println(targetApp + " is " + file.getAbsolutePath());
				return true;
			}
			if(!file.exists() && i == fragmentedEnviromentPath.length-1) {
				System.out.println(targetApp + ": not found");
				return false;
			}	
		}
		return false;
	}
	
	public static boolean findExecutableApp(String wholeCommand) {
		String envPath = System.getenv("PATH");
		String[] fragmentedEnviromentPath = envPath.split(":");
		String[] fragmentedCommand = wholeCommand.split(" ");
		String targetApp = fragmentedCommand[0];
		
		for (int i=0;i<fragmentedEnviromentPath.length;i++) {
			File file = new File(fragmentedEnviromentPath[i], targetApp);
			if(file.exists() && file.canExecute()) {
				return true;
			}
			if(!file.exists() && i == fragmentedEnviromentPath.length-1) {
				return false;
			}	
		}
		return false;
	}
	
	public static boolean mainShellBuiltIns(String wholeCommand) {
		boolean loop = false;
		String[] fragmentedCommand = wholeCommand.split(" ");
		String mainCommand = fragmentedCommand[0];
		if(mainCommand.equals("exit")) {
			return loop = false;
		}
		else if(mainCommand.equals("echo")) {
			String printable = wholeCommand.substring(5, wholeCommand.length());
			System.out.println(printable);
			return loop = true;
		}
		else {
			if(fragmentedCommand[1].equals("exit") || fragmentedCommand[1].equals("echo") || fragmentedCommand[1].equals("type")) {
				System.out.println(fragmentedCommand[1] + " is a shell builtin");
				return loop = true;
			}
			else {
				findAppType(wholeCommand);
				return loop = true;
			}
		}
	}
}
