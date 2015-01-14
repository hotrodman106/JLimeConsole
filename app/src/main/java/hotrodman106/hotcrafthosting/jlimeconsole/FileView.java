package hotrodman106.hotcrafthosting.jlimeconsole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Coolway99 on 2015-01-11
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 */
public class FileView extends View{
	private final Bitmap image;
	private final Paint mainPaint = new Paint();
	private final boolean isUproot;
	private final String name;
	public FileView(Context context, String name, boolean isDirectory){
		super(context);
		this.name = name;
		this.image = (isDirectory ? BitmapFactory.decodeResource(getResources(), R.drawable.folder) :
				BitmapFactory.decodeResource(getResources(), R.drawable.file));
		this.isUproot = false;
		init();
	}
	public FileView(Context context, String name){
		super(context);
		this.name = name;
		this.image = BitmapFactory.decodeResource(getResources(), R.drawable.uproot);
		this.isUproot = true;
		init();
	}
	private void init(){
		mainPaint.setColor(0xFFFFFFFF);
		mainPaint.setTextAlign(Paint.Align.CENTER);
		if(isUproot){
			mainPaint.setTextSize(50);
		} else {
			mainPaint.setTextSize(14);
		}
	}
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//canvas.drawLines(new float[]{0, 0, 84, 0,/**/ 84, 0, 84, 84,/**/ 84, 84, 0, 84,/**/ 0, 84, 0, 0}, secondaryPaint);
		canvas.drawBitmap(image, (85-image.getWidth())/2, 0, mainPaint);
		canvas.drawText(name, 42.5f, 75, mainPaint);
	}
}
