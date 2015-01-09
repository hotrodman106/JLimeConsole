package hotrodman106.hotcrafthosting.jlimeconsole;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.view.View.OnKeyListener;
import android.widget.TextView;

import java.io.BufferedReader;
import java.text.AttributedCharacterIterator;


public class MainActivity extends ActionBarActivity{
    public static EditText console;
    public static EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.limeEdit:
                Intent launchactivity= new Intent(MainActivity.this,EditorClass.class);
                startActivity(launchactivity);
                return true;
            case R.id.settings:
                Intent launchactivity2 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(launchactivity2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }}
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            console = (EditText) rootView.findViewById(R.id.editText);
            input = (EditText) rootView.findViewById(R.id.editText2);

            input.setOnKeyListener(new OnKeyListener()
            {
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if (event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        switch (keyCode)
                        {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                submit(null);
                                return true;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });

            return rootView;
        }


    }



    public static void submit(View view) {
        if (!input.getText().toString().equals("")) {
            CommandParser.parseInput(input.getText().toString(), console, view);
            input.setText("");
        }
    }

    }





