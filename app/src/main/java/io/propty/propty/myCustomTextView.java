package io.propty.propty;

/**
 * Created by Michael on 1/28/2016.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Mike on 10/26/2015.
 */
public class myCustomTextView extends TextView {
    public myCustomTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/hello.ttf"));
    }
}
