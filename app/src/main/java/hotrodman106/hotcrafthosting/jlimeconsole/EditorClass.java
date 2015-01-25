package hotrodman106.hotcrafthosting.jlimeconsole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class EditorClass extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_class);
		((EditText) findViewById(R.id.in)).setText(getIntent().getStringExtra("code"));
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
				Intent data = new Intent();
				data.putExtra("code", ((EditText) findViewById(R.id.in)).getText().toString());
				setResult(1, data);
				finish();
                return true;
            case R.id.settings:
                Intent launchActivity2= new Intent(EditorClass.this,SettingsActivity.class);
                startActivity(launchActivity2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run (View view){
		Intent data = new Intent();
		data.putExtra("code", ((EditText) findViewById(R.id.in)).getText().toString());
		setResult(0, data);
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
		finish();
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(resultCode){
			case 0:
				try{
					load((File) data.getSerializableExtra("file"));
				} catch(IOException e){
					e.printStackTrace();
				}
				break;
			case 1:
				try{
					save((File) data.getSerializableExtra("file"));
				} catch(IOException e){
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

	public void buttonHandler(View view){
		int reason;
		switch(view.getId()){
			case R.id.fileLoad:
				reason = 0;
				break;
			case R.id.fileSave:
				reason = 1;
				break;
			default:
				reason = 0;
				break;
		}
		Intent launchActivity2= new Intent(EditorClass.this,FileChooser.class);
		launchActivity2.putExtra("requestCode", reason);
		startActivityForResult(launchActivity2, reason);
	}

	public void save(File location) throws IOException{
		if(!location.exists()){
			location.getParentFile().mkdirs();
			location.createNewFile();
		}
		if(location.isDirectory()){
			throw new IOException("Location is a directory");
		}
		FileWriter f = new FileWriter(location);
		f.write(((EditText) findViewById(R.id.in)).getText().toString());
		f.close();
	}

	public void load(File location) throws IOException{
		if(!location.exists()){
			throw new IOException("Location does not exist");
		} else if(location.isDirectory()){
			throw new IOException("Location is a directory");
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(new URL("file://"+location.getAbsolutePath()).openStream()));
		StringBuilder out = new StringBuilder("");
		String temp = in.readLine();
		while(temp != null){
			out.append(temp).append('\n');
			temp = in.readLine();
		}
		in.close();
		((EditText) findViewById(R.id.in)).setText(out.toString());
	}
}
