package hotrodman106.hotcrafthosting.jlimeconsole;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditorClass extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.consoleView:
                Intent launchactivity= new Intent(EditorClass.this,MainActivity.class);
                startActivity(launchactivity);
                return true;
            case R.id.settings:
                Intent launchactivity2= new Intent(EditorClass.this,SettingsActivity.class);
                startActivity(launchactivity2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run (View view){
        MultiCommand cmd = new MultiCommand();
        EditText batchinput = (EditText) findViewById(R.id.in);
        String[] lines = batchinput.getText().toString().split(System.getProperty("line.separator"));
        for(int x = 0; x < lines.length; x++){
            cmd.put(lines[x]);
        }
        //XXX
        //Tis a little sketchy
        super.onBackPressed();
        cmd.run(MainActivity.console);
    }
}
