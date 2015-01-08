package hotrodman106.hotcrafthosting.jlimeconsole;

import android.widget.EditText;

import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 * Created by hotrodman106 on 1/7/2015.
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

            CommandParser.parseInput(s, console, null);
        }
    }
}
