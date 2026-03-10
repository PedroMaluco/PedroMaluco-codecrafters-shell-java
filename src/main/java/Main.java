import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
        
        if(mainCommand.equals("exit") || 
        		mainCommand.equals("echo") || 
        		mainCommand.equals("type") || 
        		mainCommand.equals("pwd") ||
        		mainCommand.equals("cd")) {
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
		
	
	
    
    
    
    public static void advanceOrRetreatDirectory(String wholeCommand) {
    	String absoluteDirPath = wholeCommand.substring(3, wholeCommand.length());
    	File file = new File(absoluteDirPath);
    	if (file.isDirectory() && file.exists() && !absoluteDirPath.startsWith("../") && !absoluteDirPath.startsWith("./")) {
    		System.setProperty("user.dir", absoluteDirPath);
    	}
    	else if (absoluteDirPath.startsWith("../")) {
    		int lastIndex = 0; 
    		int count = 0;
    		String toBeSearchedFor = "../";
    		while(lastIndex != -1) {
    			lastIndex = absoluteDirPath.indexOf(toBeSearchedFor, lastIndex);
    			if (lastIndex != -1) {
    				count++;
    				lastIndex+= toBeSearchedFor.length();
    			}
    		}
    		String currentDir = System.getProperty("user.dir");
    		String[] fragmentedDirPath = currentDir.split("/");
    		String backDir = fragmentedDirPath[fragmentedDirPath.length-(count+1)];
    		String backDirFullPath = "";
    		for (int i=0; i<=fragmentedDirPath.length; i++) {
    			backDirFullPath += fragmentedDirPath[i];
    			if(fragmentedDirPath[i] == backDir) {
    				break;
    			}
    			backDirFullPath+= "/";
    		}
    		System.setProperty("user.dir", backDirFullPath);
    	}
    	else if(absoluteDirPath.startsWith("./")) {
    		String nextDirPath = absoluteDirPath.substring(2);
    		String currentDir = System.getProperty("user.dir");
    		String finalPath = currentDir + "/" + nextDirPath;
    		file = new File(finalPath);
    		System.setProperty("user.dir", file.getAbsolutePath());
    		
    	}
    	
    	else if(absoluteDirPath.equals("~")) {
    		String homeDir = System.getenv("HOME");
    		System.setProperty("user.dir", homeDir);
    	}
    	
    	else {
    		System.out.println("cd: " + absoluteDirPath + ": No such file or directory");
    	}
    }
    	
    
	

	public static void runApp(String wholeCommand) throws IOException {
		String envPath = System.getenv("PATH");
		String[] fragmentedPath = envPath.split(":");
		String[] fragmentedCommand = wholeCommand.split(" ");
		String targetExecutable = fragmentedCommand[0];
		for (int i=0; i < fragmentedPath.length;) {
			File file = new File(fragmentedPath[i], targetExecutable);
			if (file.exists() && file.canExecute()) {
				Process process = new ProcessBuilder(fragmentedCommand).start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String s = null;
				while ((s=reader.readLine()) != null) {
					System.out.println(s);
					
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
		else if(mainCommand.equals("pwd")) {
			System.out.println(System.getProperty("user.dir"));
			return loop = true;
		
		}
		else if (mainCommand.equals("cd")){
			advanceOrRetreatDirectory(wholeCommand);
			return loop = true;
			
		}
		else {
			if(fragmentedCommand[1].equals("exit") ||
					fragmentedCommand[1].equals("echo") || 
					fragmentedCommand[1].equals("type") || 
					fragmentedCommand[1].equals("pwd")) {
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
