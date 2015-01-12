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
	private final Paint secondaryPaint = new Paint();
	private final String name;
	//public Drawable file = getResources().getDrawable(R.drawable.file);
	public FileView(Context context, String name, boolean isDirectory){
		super(context);
		this.name = name;
		this.image = (isDirectory ? BitmapFactory.decodeResource(getResources(), R.drawable.folder) :
				BitmapFactory.decodeResource(getResources(), R.drawable.file));
		init();
	}
	private void init(){
		mainPaint.setColor(0xFFFFFFFF);
		mainPaint.setTextAlign(Paint.Align.CENTER);
		secondaryPaint.setColor(0xFF00FF00);
	}
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//canvas.drawLines(new float[]{0, 0, 84, 0,/**/ 84, 0, 84, 84,/**/ 84, 84, 0, 84,/**/ 0, 84, 0, 0}, secondaryPaint);
		canvas.drawBitmap(image, (85-image.getWidth())/2, 0, mainPaint);
		canvas.drawText(name, 42.5f, 75, mainPaint);
	}
}
