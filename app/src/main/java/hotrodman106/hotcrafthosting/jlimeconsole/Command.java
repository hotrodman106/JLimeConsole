package hotrodman106.hotcrafthosting.jlimeconsole;

import java.util.ArrayList;

/**
 * Created by Coolway99 on 2015-01-24
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 */
public class Command{
	private final String cmd;
	private final ArrayList<String> args;
	public Command(String cmd){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = new ArrayList<>();
	}
	public Command(String cmd, String[] args){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = new ArrayList<>();
		for(String s : args){
			this.args.add(s);
		}
	}
	public Command(String cmd, ArrayList<String> args){
		System.out.println("Created command object, CMD+"+cmd);
		this.cmd = cmd;
		this.args = args;
	}
	public String getCmd(){
		return cmd;
	}
	public String[] getArgs(){
		return args.toArray(new String[0]);
	}
	public void addArg(String arg){
		this.args.add(arg);
	}
	public void addArgs(String[] args){
		for(String s : args){
			this.args.add(s);
		}
	}
	public void addArgs(ArrayList<String> args){
		this.args.addAll(args);
	}

	@Override
	public String toString(){
		System.out.println(cmd);
		StringBuilder temp = new StringBuilder(cmd);
		if(args != null){
			temp.append(":");
			for(String s : args){
				temp.append(s);
				temp.append(",");
			}
		}
		return temp.toString();
	}
}
