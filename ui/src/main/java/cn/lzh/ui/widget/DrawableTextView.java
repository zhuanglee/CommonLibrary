package cn.lzh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import cn.lzh.ui.R;


/**
 * 可设置上下左右图片尺寸的TextView
 * @author lzh
 *
 */
public class DrawableTextView extends AppCompatTextView {

	public DrawableTextView(Context context) {
		super(context);
	}

	public DrawableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributeSet(attrs);
	}

	public DrawableTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttributeSet(attrs);
	}

	private void initAttributeSet(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.DrawableTextView);
		Drawable[] drawables = getCompoundDrawables();
		Rect bounds = null;
		int drawableWidth = 0,drawableHeight=0;
		if(drawables[0]!=null){
			drawableWidth=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableLeftWidth, 0);
			drawableHeight=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableLeftHeight, 0);
			if(drawableWidth!=0&&drawableHeight!=0){
				bounds=new Rect(0,0,drawableWidth,drawableHeight);
				drawables[0].setBounds(bounds);
			}
		}
		if(drawables[1]!=null){
			drawableWidth=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableTopWidth, 0);
			drawableHeight=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableTopHeight, 0);
			if(drawableWidth!=0&&drawableHeight!=0){
				bounds=new Rect(0,0,drawableWidth,drawableHeight);
				drawables[1].setBounds(bounds);
			}
		}
		if(drawables[2]!=null){
			drawableWidth=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableRightWidth, 0);
			drawableHeight=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableRightHeight, 0);
			if(drawableWidth!=0&&drawableHeight!=0){
				bounds=new Rect(0,0,drawableWidth,drawableHeight);
				drawables[2].setBounds(bounds);
			}
		}
		if(drawables[3]!=null){
			drawableWidth=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableBottomWidth, 0);
			drawableHeight=typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableBottomHeight, 0);
			if(drawableWidth!=0&&drawableHeight!=0){
				bounds=new Rect(0,0,drawableWidth,drawableHeight);
				drawables[3].setBounds(bounds);
			}
		}
		typedArray.recycle();
		setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);//不调用，图片位置不正确
	}
}