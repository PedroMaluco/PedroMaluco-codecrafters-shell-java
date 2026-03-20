import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
        		mainCommand.equals("cat") ||
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
		
	
	public static void evaluateString(String wholeCommand) throws IOException {
		String[] fragmentedCommand = wholeCommand.split(" ");
		String firstField = fragmentedCommand[0];
		if(firstField.equals("echo")) {
		String toBePrinted = "";
		boolean first = true;
			for(String str : fragmentedCommand) {
				if(first == true)
					first=false;
				else
				toBePrinted+=str + " ";
			}
		
		int quoteCount = 0;
		int lastIndex = 0; 
		
		if(toBePrinted.startsWith("\"")){
		String doubleQuote = "\"\"";
		int doubleQuoteCount = 0;
			while(lastIndex != -1) {
				
				lastIndex = toBePrinted.indexOf(doubleQuote, lastIndex);
					if (lastIndex != -1) {
						doubleQuoteCount++;
						quoteCount++;
						lastIndex+= doubleQuote.length();
				}
			}
			if(toBePrinted.contains("\"\"")) {
				//System.out.println("Got into double quotes!");
				String finalStringToBePrinted = "";
				List<String> args = parser(toBePrinted);
				for (String str : args) {
					finalStringToBePrinted+=str;
				}
				System.out.println(finalStringToBePrinted);
			}
			else {
				char[] toCharArray = toBePrinted.toCharArray();
				String finalStringToBePrinted = "";
				boolean space = true;
				
				for(char c : toCharArray) {
					if(c != ' ' && c != '"') {
						finalStringToBePrinted+=c;
						space = true;
					}
					if(c == ' ' && space == true) {
						finalStringToBePrinted+=" ";
						space = false;
					}
				}
				System.out.println(finalStringToBePrinted);
			}
			
			
			
			
			

			
			
		}
		
		else if(toBePrinted.startsWith("'")) {
			int singleQuoteCount = 0;
			String singleQuote = "'";
			while(lastIndex != -1) {
				lastIndex = toBePrinted.indexOf(singleQuote, lastIndex);
				if (lastIndex != -1) {
					singleQuoteCount++;
					quoteCount++;
					lastIndex+= singleQuote.length();
				}
			}
			if(toBePrinted.startsWith("'") && toBePrinted.endsWith("'") && quoteCount == 2 && singleQuoteCount == 2) {
				String finalStringToBePrinted = toBePrinted.replaceAll("'", "");
				System.out.println(finalStringToBePrinted);
			}
			else if(quoteCount == 0) {
				char[] toCharArray = toBePrinted.toCharArray();
				String finalStringToBePrinted = "";
				boolean space = true;
				
				for(int i=0; i<toCharArray.length; i++) {
					if(toCharArray[i] != ' ') {
						finalStringToBePrinted+=toCharArray[i];
						space = true;
					}
					if(toCharArray[i] == ' ' && space == true) {
						finalStringToBePrinted+=" ";
						space = false;
					}
				}
				System.out.println(finalStringToBePrinted);
			}
			
			
			else {
				String replacedDoubles = toBePrinted.replace("''", "");
				String[] splittedString = replacedDoubles.split("'");
				String finalStringToBePrinted = "";
				
				for (int i=0;i<splittedString.length;i++) {
					finalStringToBePrinted+=splittedString[i];
				}
				System.out.println(finalStringToBePrinted);
				
				}
			}
		
		
		else {
				List<String> arguments = parser(wholeCommand);
				Process pb = new ProcessBuilder(arguments).start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(pb.getInputStream()));
				String s = null;
				while ((s=reader.readLine()) != null) {
					System.out.println(s);
					
				}
			}		
		}
	}
	
	public static List<String> parser(String input){
		List<String> tokens = new ArrayList<>();
		StringBuilder currentArg = new StringBuilder();
		boolean insideSingleQuotes = false;
		boolean insideDoubleQuotes = false;
		
		for (char c : input.toCharArray()) {
			if (c == '\'' && !insideDoubleQuotes) {
				insideSingleQuotes = !insideSingleQuotes;
			}
			else if (c == '"' && !insideSingleQuotes) {
				insideDoubleQuotes = !insideDoubleQuotes;
			}
			else if (c == ' ' && !insideDoubleQuotes && !insideSingleQuotes) {
				if (!currentArg.isEmpty()) {
					tokens.add(currentArg.toString());
					currentArg = new StringBuilder();
					currentArg.append(c);
				}
			}
			else {
				currentArg.append(c);
			}
		}
		if (!currentArg.isEmpty());
			tokens.add(currentArg.toString());
		
			return tokens;
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
	
	public static boolean mainShellBuiltIns(String wholeCommand) throws IOException {
		boolean loop = false;
		String[] fragmentedCommand = wholeCommand.split(" ");
		String mainCommand = fragmentedCommand[0];
		if(mainCommand.equals("exit")) {
			return loop = false;
		}
		else if(mainCommand.equals("echo") || mainCommand.equals("cat")) {
			evaluateString(wholeCommand);
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
