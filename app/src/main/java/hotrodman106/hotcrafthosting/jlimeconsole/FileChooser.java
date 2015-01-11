package hotrodman106.hotcrafthosting.jlimeconsole;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Coolway on 2015-01-09.
 */
public class FileChooser extends Activity{
	private FileAdapter adapter;
	private GridView view;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_file_chooser_internal);
	}

	@Nullable
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs){
		adapter = new FileAdapter(context, getDir("Output", MODE_APPEND).getParentFile());
		System.out.println("Created Adapter");
		((GridView) findViewById(R.id.gridView)).setAdapter(adapter);
		System.out.println("Set Adapter");
		/*view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				System.out.println(parent + ", " + view + ", " + position + ", " + id);
			}
		});*/
		return super.onCreateView(name, context, attrs);
	}
}
class FileAdapter extends BaseAdapter{
	private Context context;
	private File parent;
	public FileAdapter(Context context, File parent){
		this.context = context;
		this.parent = parent;
	}

	@Override
	public int getCount(){
		return parent.listFiles().length;
	}

	@Override
	public Object getItem(int position){
		return null;
	}

	@Override
	public long getItemId(int position){
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		System.out.println("Getting file "+position+"/"+getCount());
		TextView textView = new TextView(context);
		textView.setLayoutParams(new GridView.LayoutParams(85, 85));
		textView.setText(this.parent.listFiles()[position].getAbsolutePath());
		textView.setPadding(8, 8, 8, 8);
		return textView;
	}
}