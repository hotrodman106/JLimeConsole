package hotrodman106.hotcrafthosting.jlimeconsole;

import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Coolway99 on 2015-01-07.
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 */
public class MultiCommand {
    private final ArrayList<String> commandList = new ArrayList<String>();
    public void put(int index, String cmd){
        commandList.add(index, cmd);
    }
    public void put(String cmd){
        System.out.println(cmd);
        this.put(commandList.size(), cmd);
    }
    public void run(EditText console){
        for(String s : commandList){
            System.out.println(s);

            CommandParser.inputCommand(s, console, null, false);
        }
    }
}
