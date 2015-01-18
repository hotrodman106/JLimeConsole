package hotrodman106.hotcrafthosting.jlimeconsole;

import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by hotrodman106 and Coolway99 on 1/1/2015.
 */
public class CommandParser{
    private static final String r = "\n";
    private static HashMap<String,String> stringList = new HashMap<>();
    private static HashMap<String,Integer> intList = new HashMap<>();
    private static HashMap<String,Boolean> booleanList = new HashMap<>();
	private static ArrayList<String> consoleOutput = new ArrayList<>();


    /**
     * @param input   The input String to be parsed
     */

    private static void debug(String input){
        switch (input) {
            case "/debug.close":
                System.exit(0);
                break;
            default:
                if (input.startsWith("/debug.getVar:")) {
                    try {
                        String name = input.replaceFirst(":", "\u0000").split("\u0000")[1];

                        if(stringList.get(name) == null){
							if(booleanList.get(name) == null){
								if(intList.get(name) == null){
									consoleOutput.add("There is no variable in memory by that name!" + r);
								} else {
									consoleOutput.add(intList.get(name) + r);
								}
							} else {
								consoleOutput.add(booleanList.get(name) + r);
                            }
                        }else {
	                        consoleOutput.add(stringList.get(name) + r);
                        }

                    } catch (Exception p){
	                    consoleOutput.add("OI! There is an error with your get variable command!" + r);
                    }
	            } else if (input.startsWith("/debug.clearVar:")){
	                try{
		                String name = input.replaceFirst(":", "\u0000").split("\u0000")[1];
		                if(stringList.remove(name) == null && booleanList.remove(name) == null && intList.remove(name) == null){
			                consoleOutput.add("There is no variable in memory by that name!" + r);
		                } else{
			                consoleOutput.add("Variable " + name + " removed from memory!" + r);
		                }
	                } catch(Exception p){
		                consoleOutput.add("OI! There is an error with your clear variable command!" + r);
	                }
                } else if(input.startsWith("/debug.varType:")){
					try{
						String name = input.replaceFirst(":", "\u0000").split("\u0000")[1];
						if(stringList.get(name) == null){
							if(booleanList.get(name) == null){
								if(intList.get(name) == null){
									consoleOutput.add("There is no variable in memory by that name!" + r);
								} else {
									consoleOutput.add("Variable is an Integer" + r);
								}
							} else {
								consoleOutput.add("Variable is a Boolean" + r);
							}
						}else {
							consoleOutput.add("Variable is a String" + r);
						}
					} catch(Exception e){
						consoleOutput.add("OI! There is an error with your variable type command!" + r);
					}
	            } else {
	                parseInput(input);
	        }
	    }
	}
	private static String getVar(String key){
		if(stringList.get(key) != null){
			return stringList.get(key);
		}
		if(intList.get(key) != null){
			return Integer.toString(intList.get(key));
		}
		if(booleanList.get(key) != null){
			return Boolean.toString(booleanList.get(key));
		}
		return null;
	}
	public static void inputCommand(String input, EditText console, boolean debug){
		doCommand(input, debug);
		for(String x : consoleOutput){
			if(x.startsWith("\u0001")){
				console.setText("");
				try{
					console.append(x.substring(1));
				} catch(Exception e){
					//Hi
				}
			} else {
				console.append(x);
			}
			consoleOutput.clear();
		}
	}
	public static void doCommand(String input, boolean debug){
		char[] in = input.toCharArray();
		boolean inVar = false;
		boolean escaped = false;
		StringBuilder out = new StringBuilder();
		String temp = "";
		for(int x = 0; x < in.length; x++){
			char y = in[x];
			if(escaped){
				temp += y;
				escaped = false;
			} else {
				switch(y){
					case '%':
						if(inVar){
							inVar = false;
							String var = getVar(temp);
							if(var != null){
								out.append(var);
								temp = "";
							} else{
								consoleOutput.add("Oi! That was not a valid variable!" + r + "Name: " + temp + r);
								return;
							}
						} else{
							inVar = true;
							out.append(temp);
							temp = "";
						}
						break;
					case '\\':
						escaped = true;
						break;
					default:
						temp += y;
						break;
				}
			}
		}
		out.append(temp);
		if(debug){
			debug(out.toString());
		} else {
			parseInput(out.toString());
		}
	}
    private static void parseInput(String input) {
        switch (input) {
            case "/ping":
                consoleOutput.add("PONG!" + r);
                break;
            case "/pong":
                consoleOutput.add("PING!" + r);
                break;
            case "/help":
                consoleOutput.clear();
                consoleOutput.add("\u0001COMMAND LIST:" + r
                        + "/ping     PONG!" + r
                        + "/pong    PING!" + r
                        + "/clear     Clears the screen" + r
                        + "/linebreak     Adds a carriage return" + r
                        + "/echo:[String]     Writes a string to the console" + r
                        + "/gettime:[date String]    Outputs the date/time" + r
                        + "/random:[Integer]     Outputs a random number up to the value specified" + r
                        + "/loop:[Integer],[Command]     Loops a command a set number of times" + r
                        + "/if:[Integer],[Integer],[<,>,=,<=,>=],([True Command]),([False Command])      Checks if a statement is true and, if so, runs a command" + r
                        + "/for:[Integer],[Integer],[Integer],[Command]     Loops a command for a set number of times in certain increments" + r);
                break;
            case "/clear":
                consoleOutput.clear();
	            consoleOutput.add("\u0001");
                break;
            case "/linebreak":
                consoleOutput.add(r);
                break;
            default:
                parseAdvanceCommand(input);
                break;
        }
    }

    private static void parseAdvanceCommand(String input) {
        if (input.startsWith("/echo:")) {
            String var = input.substring(input.indexOf(":") + 1).trim();
            consoleOutput.add(var + r);

        } else if (input.startsWith("/random:")) {
            try {
                int var = Integer.parseInt(input.substring(input.lastIndexOf(":") + 1).trim());
                Random random = new Random();
                consoleOutput.add(random.nextInt(var) + r);
            }catch(Exception p){
                consoleOutput.add("OI! That's not a integer! Try inputting a integer!" + r);
            }


        } else if (input.startsWith("/loop:")) {
            String[] vars;
            vars = new String[2];
            vars = input.replaceFirst(":", "\u0000").split("\u0000")[1].split(",");
            try{
                int var1 = Integer.parseInt(vars[0]);
                String cmd = vars[1];
                for(int x = 2; x < vars.length; x++){
                    cmd += ","+vars[x];
                }

                while(var1 != 0){
                    var1--;
                    parseInput(cmd);
                }
            }catch (Exception p){
                consoleOutput.add("OI! There is an error with your loop statement!" + r);
            }

        }else if (input.startsWith("/for:")) {
            try{
            String[] vars;
            vars = new String[4];
            vars = input.replaceFirst(":", "\u0000").split("\u0000")[1].split(",");
                int var2 = Integer.parseInt(vars[1]);
                int var3 =  Integer.parseInt(vars[2]);
                String cmd = vars[3];
                for (int var1 = Integer.parseInt(vars[0]); var1 < var2; var1 += var3) {
                    parseInput(cmd);
                }
            } catch (Exception p) {
                consoleOutput.add("OI! There is an error with your for statement!" + r);
            }
        }else if (input.startsWith("/String:")) {
            try {
                String[] vars = input.replaceFirst(":", "\u0000").split("\u0000")[1].split(",");
                String name = vars[0];
                String string = vars[1];
	            if(string.startsWith("(")){
		            doCommand(string.substring(1, string.length()-1), false);
		            string = consoleOutput.remove(consoleOutput.size()-1);
	            }
				booleanList.remove(name);
				intList.remove(name);
                stringList.put(name, string);
                consoleOutput.add("String " + name + " set to " + string + r);
            } catch (Exception p) {
                consoleOutput.add("OI! There is an error with your String declaration statement!" + r);
            }

        }else if (input.startsWith("/Int:")) {
            try {
                String[] vars = input.replaceFirst(":", "\u0000").split("\u0000")[1].split(",");
                String name = vars[0];
	            String string = vars[1];
	            if(string.startsWith("(")){
		            doCommand(string.substring(1, string.length()-1), false);
		            string = consoleOutput.remove(consoleOutput.size()-1);
	            }
                int integer = Integer.parseInt(string.trim());

				booleanList.remove(name);
				stringList.remove(name);
                intList.put(name, integer);
                consoleOutput.add("Integer " + name + " set to " + integer + r);
            } catch (Exception p) {
                consoleOutput.add("OI! There is an error with your Integer declaration statement!" + r);
            }

        }else if (input.startsWith("/Boolean:")) {
            try {
                String[] vars = input.replaceFirst(":", "\u0000").split("\u0000")[1].split(",");
                String name = vars[0];
	            String string = vars[1];
	            if(string.startsWith("(")){
		            doCommand(string.substring(1, string.length()-1), false);
		            string = consoleOutput.remove(consoleOutput.size()-1);
	            }
                boolean b = Boolean.parseBoolean(string.trim());
				stringList.remove(name);
				intList.remove(name);
                booleanList.put(name, b);
                consoleOutput.add("Boolean " + name + " set to " + b + r);
            } catch (Exception p) {
                consoleOutput.add("OI! There is an error with your Boolean declaration statement!" + r);
            }

        }else if (input.startsWith("/gettime:")) {
            String var = input.substring(input.indexOf(":") + 1).trim();
            try {
                DateFormat df = new SimpleDateFormat(var);
                Date dateobj = new Date();
                consoleOutput.add(df.format(dateobj) + r);
            }catch (Exception p){
                consoleOutput.add("OI! That's not a proper date String! Try inputting a date String!" + r);
            }

        }else if(input.startsWith("/if:")) {
            String[] vars;
            vars = new String[4];
            vars = input.replaceFirst(":", "\u0000").split("\u0000")[1].split(",");

            try {
                int var1 = Integer.parseInt(vars[0]);
                int var2 = Integer.parseInt(vars[1]);
                String operator = vars[2];
                String cmd = vars[3];
                for (int x = 4; x < vars.length; x++) {
                    cmd += "," + vars[x];
                }
                int x = 0, depth = 0;
                String temp = "";
                MultiCommand condition1 = new MultiCommand(), condition2 = new MultiCommand();
                boolean onCondition2 = false, escape = false;
                try{
                    while (x < cmd.length()) {
                        char y = cmd.charAt(x++);
                        switch (y) {
                            case '(':
                                if (++depth != 1) {
                                    temp += y;
                                }
                                break;
                            case ')':
                                if (depth-- != 1) {
                                    temp += y;
                                } else {
                                    if (onCondition2) {
                                        condition2.put(temp);
                                        temp = "";
                                    } else {
                                        condition1.put(temp);
                                        temp = "";
                                    }
                                }
                                break;
                            case ',':
                                if (depth == 0) {
                                    onCondition2 = true;
                                    System.out.println("Switched condition");
                                } else {
                                    temp += y;
                                }
                                break;
                            case '\\':
                                if(!escape){
                                    if (depth == 1) {
                                        escape = true;
                                    } else {
                                        temp += y;
                                    }
                                } else {
                                    temp += y;
                                }
                                break;
                            case '&':
                                if(!escape && depth == 1){
                                    if(onCondition2){
                                        condition2.put(temp);
                                        temp = "";
                                    } else {
                                        condition1.put(temp);
                                        temp = "";
                                    }
                                } else {
                                    escape = false;
                                    temp += y;
                                }
                                break;
                            default:
                                temp += y;
                            break;
                        }
                    }
                    if(depth != 0){
                        throw new ArrayIndexOutOfBoundsException();
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    consoleOutput.add("OI! You didn't close your conditions on your if statements" + r);
                }
                switch(operator){
                    case "=":
                    if(var1 == var2){
                        condition1.run();
                    } else {
                        condition2.run();
                    }
                    break;

                    case "<":
                    if(var1 < var2){
                        condition1.run();
                    } else {
                        condition2.run();
                    }
                    break;

                    case ">":
                    if(var1 > var2){
                        condition1.run();
                    } else {
                        condition2.run();
                    }
                    break;

                    case "<=":
                    if(var1 <= var2){
                        condition1.run();
                    } else {
                        condition2.run();
                    }
                    break;

                    case ">=":
                   if(var1 >= var2){
                       condition1.run();
                   } else {
                       condition2.run();
                   }
                    break;
                }
            }catch (Exception p){
                consoleOutput.add("OI! There is an error with your if statement!" + r);
            }
        }else{
            consoleOutput.add("OI! Command not valid!" + r);
        }
    }
}