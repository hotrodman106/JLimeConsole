package hotrodman106.hotcrafthosting.jlimeconsole;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity{
    public static EditText console;
    public static EditText input;
    public static Intent i;
	public static Activity activity;

	private String code = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = new Intent(MainActivity.this,EditorClass.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
		activity = this;
		ModuleManager.init(getDir("Modules", MODE_APPEND));
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		code = data.getStringExtra("code");
		if(code == null){
			return;
		}
		switch(resultCode){
			case 0:
				CommandParser.inputCommand(code.split("\n"), console, false);
				break;
			default:
				break;
		}
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

	public static void setForeground(int R, int G, int B){
		console.setTextColor(0xFF000000|R<<16|G<<8|B);
	}
	public static void setBackground(int R, int G, int B){
		console.setBackgroundColor(0xFF000000|R<<16|G<<8|B);
	}
	public static void resetColors(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		int rB = sharedPrefs.getInt("R_background_preference", 0);
		int gB = sharedPrefs.getInt("G_background_preference", 0);
		int bB = sharedPrefs.getInt("B_background_preference", 0);
		int rF = sharedPrefs.getInt("R_font_preference", 0);
		int gF = sharedPrefs.getInt("G_font_preference", 255);
		int bF = sharedPrefs.getInt("B_font_preference", 0);
		setForeground(rF, gF, bF);
		setBackground(rB, gB, bB);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.limeEdit:
                Intent launchActivity= new Intent(MainActivity.this,EditorClass.class);
				launchActivity.putExtra("code", code);
                startActivityForResult(launchActivity, 0);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.settings:
                Intent launchActivity2 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(launchActivity2);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int rB = sharedPrefs.getInt("R_background_preference", 0);
            int gB = sharedPrefs.getInt("G_background_preference", 0);
            int bB = sharedPrefs.getInt("B_background_preference", 0);
            int rF = sharedPrefs.getInt("R_font_preference", 0);
            int gF = sharedPrefs.getInt("G_font_preference", 255);
            int bF = sharedPrefs.getInt("B_font_preference", 0);
            String font = sharedPrefs.getString("type_font_preference", "1");

            MainActivity.console.setBackgroundColor(Color.rgb(rB, gB, bB));
            MainActivity.console.setTextColor(Color.rgb(rF,gF,bF));
            switch(font) {
                case "1":
                    MainActivity.console.setTypeface(Typeface.DEFAULT);
                    break;
                case "2":
                    MainActivity.console.setTypeface(Typeface.DEFAULT_BOLD);
                    break;
                case "3":
                    MainActivity.console.setTypeface(Typeface.MONOSPACE);
                    break;
                case "4":
                    MainActivity.console.setTypeface(Typeface.SANS_SERIF);
                    break;
                case "5":
                    MainActivity.console.setTypeface(Typeface.SERIF);
                    break;
                default:
            }
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
            CommandParser.inputCommand(input.getText().toString(), console, true);
            input.setText("");
        }
    }
}





