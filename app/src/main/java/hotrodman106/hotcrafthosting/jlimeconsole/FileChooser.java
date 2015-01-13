package hotrodman106.hotcrafthosting.jlimeconsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Coolway99 on 2015-01-09.
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 */
public class FileChooser extends Activity{
	private FileAdapter adapter;
	private GridView view;
	public Toast toast;
	public Intent intent;
	private int depth = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_file_chooser_internal);

		intent = getIntent();
		toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
		adapter = new FileAdapter(this, getDir("Output", MODE_APPEND).getParentFile(), false);
		view = (GridView) findViewById(R.id.gridView);
		view.setAdapter(adapter);
		switch(intent.getIntExtra("requestCode", 0)){
			case 0:
				findViewById(R.id.exportButton).setVisibility(View.GONE);
				findViewById(R.id.save).setVisibility(View.GONE);
				findViewById(R.id.saveName).setVisibility(View.GONE);
				findViewById(R.id.importButton).setVisibility(View.VISIBLE);

				view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					@Override
					 public void onItemClick(AdapterView<?> parent, View view, int position, long id){
						File file = (File) adapter.getItem(position);
						toast.setText(((File) parent.getItemAtPosition(position)).getName());
						if(file.isDirectory()){
							if(position == 0 && depth != 0){
								depth--;
							} else {
								depth++;
							}
							if(((File) adapter.getItem(position)).listFiles() != null){
								FileChooser.this.setAdapter(new FileAdapter(FileChooser.this, (File) adapter.getItem(position), depth!=0));
								toast.show();
							}
						} else{
							Intent i = new Intent();
							i.putExtra("file", file);
							setResult(intent.getIntExtra("requestCode", 0), i);
							finish();
						}
					}
				});
				break;
			case 1:

				((EditText) findViewById(R.id.saveName)).setOnKeyListener(new View.OnKeyListener(){
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event){
						switch(keyCode){
							case KeyEvent.KEYCODE_DPAD_CENTER:
							case KeyEvent.KEYCODE_ENTER:
								save(v);
								return true;
						}
						return false;
					}
				});
				((Button) findViewById(R.id.save)).setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v){
						save(((EditText) findViewById(R.id.saveName)));
					}
				});
				view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id){
						File file = (File) adapter.getItem(position);
						toast.setText(((File) parent.getItemAtPosition(position)).getName());
						if(file.isDirectory()){
							if(((File) adapter.getItem(position)).listFiles() != null){
								FileChooser.this.setAdapter(new FileAdapter(FileChooser.this, (File) adapter.getItem(position), true));
								toast.show();
							}
						} else {
							finish(file, 1);
						}
					}
				});
				break;
			default:
				break;
		}
	}
	private void save(View v){
		save(((EditText) v).getText().toString());
	}
	private void save(String name){
		final File file = new File(adapter.getParent().getAbsolutePath() + "/" + name);
		DialogHandler dialogHandler = new DialogHandler();
		if(file.exists()){
			dialogHandler.confirm(this, "Overwrite", "Do you want to overwrite " + file.getName(), "Yes",
					new Runnable(){
						@Override
						public void run(){
							finish(file, 1);
						}
					}, "No", new Runnable(){
						@Override
						public void run(){
							//No does nothing
						}
					});
		} else {
			finish(file, 1);
		}
	}

	@Override
	public void onBackPressed(){
		finish(null, -1);
	}

	private void finish(File file, int returnCode){
		Intent i = new Intent();
		i.putExtra("file", file);
		setResult(intent.getIntExtra("requestCode", returnCode), i);
		finish();
	}
	protected void setAdapter(FileAdapter adapter){
		this.adapter = adapter;
		view.setAdapter(adapter);
	}
}
class FileAdapter extends BaseAdapter{
	private final Context context;
	private final File parent;
	private final File[] files;
	private final boolean hasParent;
	public FileAdapter(Context context, File parent, Boolean hasParent){
		this.context = context;
		this.parent = parent;
		this.files = parent.listFiles();
		this.hasParent = hasParent;
	}

	@Override
	public int getCount(){
		if(!hasParent){
			return files.length;
		}
		return files.length+1;
	}

	@Override
	public Object getItem(int position){
		if(!hasParent){
			return files[position];
		}
		if(position == 0){
			return parent.getParentFile();
		}
		return files[position-1];
	}

	@Override
	public long getItemId(int position){
		if(!hasParent){
			return files[position].hashCode();
		}
		if(position == 0){
			return parent.hashCode();
		}
		return files[position-1].hashCode();
	}
	public File getParent(){
		return parent;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		FileView fileView;
		if(!hasParent){
			fileView = new FileView(context, files[position].getName(), files[position].isDirectory());
		} else{
			if(position != 0){
				fileView = new FileView(context, files[position - 1].getName(), files[position - 1].isDirectory());
			} else {
				fileView = new FileView(context, "..");
			}
		}
		fileView.setLayoutParams(new GridView.LayoutParams(85, 85));
		fileView.setPadding(8, 8, 8, 8);
		return fileView;
	}
}
class DialogHandler{
	public void confirm(Activity act, String title, String text,
			String trueButton, final Runnable trueAction, String falseButton, final Runnable falseAction){
		AlertDialog alertDialog = new AlertDialog.Builder(act).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(text);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, trueButton, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				trueAction.run();
			}
		});
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, falseButton, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				falseAction.run();
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.show();
	}
}