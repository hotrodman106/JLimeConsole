package hotrodman106.hotcrafthosting.jlimeconsole;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;

/**
 * Created by hotrodman106 on 1/1/2015.
 */
public class CommandParser extends Activity {
    private static final String r = "\n";
    private static HashMap<String,String> stringlist = new HashMap<String,String>();


    /**
     * @param input   The input String to be parsed
     * @param console   The Editext used to show outputed data
     * @param view    The view of the android project
     */
    public static void debug(String input, EditText console, View view){
        switch (input) {
            case "/debug.close":
                System.exit(0);
                break;
            default:
                if (input.startsWith("/debug.getString:")) {
                    try {
                        String name = input.replaceFirst(":", "\u0000").split("\u0000")[1];
                        if(stringlist.get(name) == null){
                            MainActivity.console.append("There is no String in memory by that name!" + r);
                        }else {
                            MainActivity.console.append(stringlist.get(name) + r);
                        }

                    } catch (Exception p) {
                        console.append("OI! There is an error with your get String command!" + r);
                    }
                    }else if (input.startsWith("/debug.clearString:")) {
                try {
                    String name = input.replaceFirst(":", "\u0000").split("\u0000")[1];
                    if(stringlist.get(name) == null){
                        MainActivity.console.append("There is no String in memory by that name!" + r);
                    }else {
                        MainActivity.console.append("String " + name + " removed from memory!" + r);
                        stringlist.remove(name);
                    }

                } catch (Exception p) {
                    console.append("OI! There is an error with your clear String command!" + r);
                }
            }else{
                    parseInput(input,console,view);
        }
    }}
    public static void parseInput(String input, EditText console, View view) {
        switch (input) {
            case "/ping":
                console.append("PONG!" + r);
                break;
            case "/pong":
                console.append("PING!" + r);
                break;
            case "/help":
                console.setText("");
                console.append("COMMAND LIST:" + r
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
                console.setText("");
                break;
            case "/linebreak":
                console.append(r);
                break;
            default:
                parseAdvanceCommand(input, console);
                break;
        }
    }

    public static void parseAdvanceCommand(String input, EditText console) {
        if (input.startsWith("/echo:")) {
            String var = input.substring(input.indexOf(":") + 1).trim();
            console.append(var + r);

        } else if (input.startsWith("/random:")) {
            try {
                int var = Integer.parseInt(input.substring(input.lastIndexOf(":") + 1).trim());
                Random random = new Random();
                console.append(random.nextInt(var) + r);
            }catch(Exception p){
                console.append("OI! That's not a integer! Try inputting a integer!" + r);
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
                    parseInput(cmd,console,null);
                }
            }catch (Exception p){
                console.append("OI! There is an error with your loop statement!" + r);
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
                    parseInput(cmd, console, null);
                }
            } catch (Exception p) {
                console.append("OI! There is an error with your for statement!" + r);
            }
        }else if (input.startsWith("/String:")) {
            try {
                String[] vars = input.replaceFirst(":", "\u0000").split("\u0000")[1].split(",");
                String name = vars[0];
                String string = vars[1];

                stringlist.put(name, string);
                MainActivity.console.append("String " + name + " set to " + string + r);
            } catch (Exception p) {
                console.append("OI! There is an error with your String declaration statement!" + r);
            }

        }else if (input.startsWith("/gettime:")) {
            String var = input.substring(input.indexOf(":") + 1).trim();
            try {
                DateFormat df = new SimpleDateFormat(var);
                Date dateobj = new Date();
                console.append(df.format(dateobj) + r);
            }catch (Exception p){
                console.append("OI! That's not a proper date String! Try inputting a date String!" + r);
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
                        System.out.println("Processing char: "+y);
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
                                        System.out.println("Put command " + temp + " in condition 2");
                                        temp = "";
                                    } else {
                                        condition1.put(temp);
                                        System.out.println("Put command " + temp + " in condition 1");
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
                    console.append("OI! You didn't close your conditions on your if statements" + r);
                }
                switch(operator){
                    case "=":
                    if(var1 == var2){
                        condition1.run(console);
                    } else {
                        condition2.run(console);
                    }
                    break;

                    case "<":
                    if(var1 < var2){
                        condition1.run(console);
                    } else {
                        condition2.run(console);
                    }
                    break;

                    case ">":
                    if(var1 > var2){
                        condition1.run(console);
                    } else {
                        condition2.run(console);
                    }
                    break;

                    case "<=":
                    if(var1 <= var2){
                        condition1.run(console);
                    } else {
                        condition2.run(console);
                    }
                    break;

                    case ">=":
                   if(var1 >= var2){
                       condition1.run(console);
                   } else {
                       condition2.run(console);
                   }
                    break;
                }
            }catch (Exception p){
                console.append("OI! There is an error with your if statement!" + r);
            }
        }else{
            console.append("OI! Command not valid!" + r);
        }
    }
}