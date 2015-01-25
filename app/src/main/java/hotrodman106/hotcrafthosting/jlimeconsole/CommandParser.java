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
	/*
	 * \u0005<index>\u0006 will reference the multiCommand with the specified index
	 */
	private static ArrayList<MultiCommand> multiCommandList = new ArrayList<>();
	private static ArrayList<StringBuilder> stringBuilderList = new ArrayList<>();
	private static boolean parsed = false;


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
	public static void inputCommand(String[] input, EditText console, boolean debug){
		for(int x = 0; x < input.length; x++){
			String s = input[x];
			multiCommandList.add(new MultiCommand(debug));
			stringBuilderList.add(new StringBuilder());
			String[] in = s.replaceFirst(":", "\u0000").split("\u0000");
			multiCommandList.get(0).put(new Command(in[0]));
			int y = doCommand(in[0], (in.length == 2 ? in[1] : null), 0, debug);
			multiCommandList.clear();
			stringBuilderList.clear();
			parsed = false;
			if(y != -1){
				if(y == -2){
					consoleOutput.add("Error at line "+x+r);
					break;
				}
				x = y-1;
			}
		}
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
		}
		consoleOutput.clear();
	}
	public static void inputCommand(String input, EditText console, boolean debug){
		multiCommandList.add(new MultiCommand(debug));
		stringBuilderList.add(new StringBuilder());
		String[] in = input.replaceFirst(":", "\u0000").split("\u0000");
		multiCommandList.get(0).put(new Command(in[0]));
		doCommand(in[0], (in.length == 2 ? in[1] : null), 0, debug);
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
		}
		consoleOutput.clear();
		multiCommandList.clear();
		stringBuilderList.clear();
		parsed = false;
	}
	public static int doCommand(Command c, int startDepth, boolean debug){
		return doCommand(c.getCmd(), c.getArgs(), startDepth, debug);
	}
	public static int doCommand(String cmd, String args, int startDepth, boolean debug){
		if(!parsed && args != null){
			char[] in = args.toCharArray();
			boolean inVar = false;
			boolean escaped = false;
			String temp = "";
			String command = null;
			ArrayList<Integer> depth = new ArrayList<>();
			ArrayList<Integer> commandDepth = new ArrayList<>();
			int level = 0;
			depth.add(startDepth);
			commandDepth.add(0);
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
									stringBuilderList.get(depth.get(level)).append(var);
									temp = "";
								} else {
									consoleOutput.add("Oi! That was not a valid variable!" + r + "Name: " + temp + r);
									return -2;
								}
							} else {
								inVar = true;
								temp = "";
							}
							break;
						case '\\':
							escaped = true;
							break;
						case '(':
							multiCommandList.add(new MultiCommand(debug));
							stringBuilderList.add(new StringBuilder());
							depth.add(stringBuilderList.size() - 1);
							stringBuilderList.get(depth.get(level++)).append(temp);
							commandDepth.add(0);
							temp = "";
							command = null;
							break;
						case ')':
							multiCommandList.get(depth.get(level)).get(commandDepth.remove(level)).addArg(stringBuilderList.get(depth.get(level)).append(temp).toString());
							stringBuilderList.get(depth.get(level-1)).append("\u0005" + depth.remove(level--) + "\u0006");
							command = null;
							temp = "";
							break;
						case '&':
							multiCommandList.get(depth.get(level)).get(commandDepth.get(level)).addArg(stringBuilderList.get(depth.get(level)).append(temp).toString());
							stringBuilderList.set(depth.get(level), new StringBuilder());
							commandDepth.set(level, commandDepth.get(level)+1);
							command = null;
							temp = "";
							break;
						case ',':
							multiCommandList.get(depth.get(level)).get(commandDepth.get(level)).addArg(stringBuilderList.get(depth.get(level)).append(temp).toString());
							stringBuilderList.set(depth.get(level), new StringBuilder());
							temp = "";
							break;
						case ':':
							if(command != null){
								consoleOutput.add("OI! You have : where you already have declared a command!" + r);
								return -2;
							}
							multiCommandList.get(depth.get(level)).put(new Command(temp));
							command = temp;
							temp = "";
							break;
						default:
							temp += y;
							break;
					}
				}
			}
			multiCommandList.get(startDepth).get(commandDepth.get(level)).addArg(stringBuilderList.get(startDepth).append(temp).toString());
			parsed = true;
		}
		return multiCommandList.get(startDepth).run(startDepth);
	}
	public static int doCommand(String cmd, String[] args, int startDepth, boolean debug){
		if(cmd.startsWith("!")){
			return header(cmd, args);
		}
		if(debug){
			return debug(cmd, args, startDepth);
		}
		return parseInput(cmd, args, startDepth);
	}
	public static int doCommand(String cmd, int startDepth){
		if(cmd.startsWith("\u0005")){
			return getOut(cmd, startDepth);
		}
		return parseInput(cmd, null, startDepth);
	}

	private static int getOut(String code, int startDepth){
		int intCode = Integer.parseInt(code.substring(code.indexOf('\u0005')+1, code.indexOf('\u0006')));
		return getOut(intCode, startDepth);
	}
	private static int getOut(int code, int startDepth){
		return multiCommandList.get(code).run(startDepth);
	}

	private static int header(String cmd, String[]args){
		try{
			//TODO MODULES
			throw new Exception("bleh");
		} catch(Exception e){
			consoleOutput.add("ERROR WITH HEADER COMMAND");
			return -2;
		}
		//return -1;
	}

	private static int debug(String cmd, String[] args, int startDepth){
		switch (cmd){
			case "/debug.close":
				System.exit(0);
				break;
			case "/debug.getVar":
				try{
					String name = args[0];
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
					} else {
						consoleOutput.add(stringList.get(name) + r);
					}
				} catch (Exception p){
					consoleOutput.add("OI! There is an error with your get variable command!" + r);
				}
				break;
			case "/debug.clearVar":
				try{
					String name = args[0];
					if(stringList.remove(name) == null && booleanList.remove(name) == null && intList.remove(name) == null){
						consoleOutput.add("There is no variable in memory by that name!" + r);
					} else{
						consoleOutput.add("Variable " + name + " removed from memory!" + r);
					}
				} catch(Exception p){
					consoleOutput.add("OI! There is an error with your clear variable command!" + r);
				}
				break;
			case "/debug.varType":
				try{
					String name = args[0];
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
				break;
			default:
				return parseInput(cmd, args, startDepth);
		}
		return -1;
	}
    private static int parseInput(String cmd, String[] args, int startDepth) {
        switch (cmd) {
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
                        + "/if:[Integer][<,>,=,<=,>=][Integer],([True Command]),([False Command])      Checks if a statement is true and, if so, runs a command" + r
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
                return parseAdvanceCommand(cmd, args, startDepth);
        }
	    return -1;
    }
    private static int parseAdvanceCommand(String cmd, String[] args, int startDepth) {
	    try{
		    switch(cmd){
			    case "/echo":
				    consoleOutput.add(args[0] + r);
				    break;
			    case "/random":
				    try{
					    int var = Integer.parseInt(args[0].trim());
					    Random random = new Random();
					    consoleOutput.add(random.nextInt(var) + r);
				    } catch(Exception p){
					    consoleOutput.add("OI! That's not a integer! Try inputting a integer!" + r);
					    return -2;
				    }
				    break;
			    case "/loop":
				    try{
					    int var1 = Integer.parseInt(args[0]);
					    String command = args[1];
					    while(var1 != 0){
						    var1--;
						    doCommand(command, startDepth);
					    }
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your loop statement!" + r);
					    return -2;
				    }
				    break;
			    case "/for":
				    try{
					    int var2 = Integer.parseInt(args[1].trim());
					    int var3 = Integer.parseInt(args[2].trim());
					    String command = args[3];
					    for(int var1 = Integer.parseInt(args[0].trim()); var1 < var2; var1 += var3){
						    doCommand(command, startDepth);
					    }
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your for statement!" + r);
					    return -2;
				    }
				    break;
			    case "/String":
				    try{
					    String name = args[0];
					    String string = args[1];

					    booleanList.remove(name);
					    intList.remove(name);
					    stringList.put(name, string);
					    consoleOutput.add("String " + name + " set to " + string + r);
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your String declaration statement!" + r);
					    return -2;
				    }
				    break;
			    case "/Int":
				    try{
					    String name = args[0];
					    int integer = Integer.parseInt(args[1].trim());

					    booleanList.remove(name);
					    stringList.remove(name);
					    intList.put(name, integer);
					    consoleOutput.add("Integer " + name + " set to " + integer + r);
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your Integer declaration statement!" + r);
					    return -2;
				    }
				    break;
			    case "/Boolean":
				    try{
					    String name = args[0];
					    boolean b = Boolean.parseBoolean(args[1].trim());

					    stringList.remove(name);
					    intList.remove(name);
					    booleanList.put(name, b);
					    consoleOutput.add("Boolean " + name + " set to " + b + r);
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your Boolean declaration statement!" + r);
					    return -2;
				    }
				    break;
			    case "/getTime":
				    try{
					    DateFormat df = new SimpleDateFormat(args[0]);
					    Date dateObj = new Date();
					    consoleOutput.add(df.format(dateObj) + r);
				    } catch(Exception p){
					    consoleOutput.add("OI! That's not a proper date String! Try inputting a date String!" + r);
					    return -2;
				    }
				    break;
			    case "/if":
				    try{
					    String[] num = args[0].split("[<=>]+");
					    String operator = args[0].split("\\d+")[1];
					    int var1 = Integer.parseInt(num[0]);
					    int var2 = Integer.parseInt(num[1]);
					    String trueCommand = args[1];
					    String falseCommand;
					    try{
						    falseCommand = args[2];
					    } catch(ArrayIndexOutOfBoundsException e){
						    falseCommand = "\u0002";
					    }
					    switch(operator){
						    case "=":
							    if(var1 == var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case "<":
							    if(var1 < var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case ">":
							    if(var1 > var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case "<=":
							    if(var1 <= var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }

						    case ">=":
							    if(var1 >= var2){
								    return doCommand(trueCommand, startDepth);
							    } else {
								    return doCommand(falseCommand, startDepth);
							    }
						    default:
							    consoleOutput.add("OI! Invalid Operator for your if statement!" + r);
							    return -2;
					    }
				    } catch(Exception p){
					    consoleOutput.add("OI! There is an error with your if statement!" + r);
					    return -2;
				    }
				case "\u0002":
					break;
				case "/goto":
					try{
						int x = Integer.parseInt(args[0].trim());
						if(x < 0){
							consoleOutput.add("OI! There is no such thing as a negative line!" + r);
							return -2;
						}
						return x - 1;
					} catch(Exception e){
						consoleOutput.add("OI! Not a valid integer");
						return -2;
					}
				case "/last":
					try{
						int x = Integer.parseInt(args[0].trim()) - 1;
						while(x > 0){
							consoleOutput.remove(consoleOutput.size()-1);
							x--;
						}
					} catch(Exception e){
						consoleOutput.add("OI! Not a valid integer");
						return -2;
					}
					break;
			    default:
				    consoleOutput.add("OI! Command not valid!" + r);
		    }
	    } catch(ArrayIndexOutOfBoundsException e){
		    consoleOutput.add("OI! A command didn't have the right number of arguments!" + r);
		    return -2;
	    }
	    return -1;
    }
}