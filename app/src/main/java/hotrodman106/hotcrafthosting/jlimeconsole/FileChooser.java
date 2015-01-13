package hotrodman106.hotcrafthosting.jlimeconsole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_file_chooser_internal);

		intent = getIntent();
		toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
		adapter = new FileAdapter(this, getDir("Output", MODE_APPEND).getParentFile(), false);
		System.out.println("Created Adapter");
		view = (GridView) findViewById(R.id.gridView);
		view.setAdapter(adapter);
		System.out.println("Set Adapter");
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
							if(((File) adapter.getItem(position)).listFiles() != null){
								FileChooser.this.setAdapter(new FileAdapter(FileChooser.this, (File) adapter.getItem(position), true));
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

				view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id){
						File file = (File) adapter.getItem(position);
						toast.setText(((File) parent.getItemAtPosition(position)).getName());
						if(file.isDirectory()){
							if(((File) adapter.getItem(position)).listFiles() != null){
								FileChooser.this.setAdapter(new FileAdapter(FileChooser.this, (File) adapter.getItem(position), true));
								toast.show();
							} else{
								//TODO This should work instead of "throwing an error"
								toast.setText("The folder is empty");
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
			default:
				break;
		}
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