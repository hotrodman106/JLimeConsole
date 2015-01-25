package hotrodman106.hotcrafthosting.jlimeconsole;

import java.util.ArrayList;

/**
 * Created by Coolway99 on 2015-01-07.
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 */
public class MultiCommand {
	private final ArrayList<Command> commands = new ArrayList<>();
	private final boolean debug;
	public MultiCommand(boolean debug){
		this.debug = debug;
	}
	public void put(String cmd, String[] args){
		this.put(new Command(cmd, args));
	}
	public void put(Command c){
		commands.add(c);
	}
	public Command get(int index){
		return commands.get(index);
	}
	public int size(){
		return commands.size();
	}
	public void run(int level){
		for(Command c : commands){
			System.out.println(c);
			CommandParser.doCommand(c, level, this.debug);
		}
	}
}
