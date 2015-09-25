package ladyjek.twiscode.com.ladyjek.Utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.R;

public class TextFields extends TextView {

    public TextFields(final Context context, final AttributeSet attrs) {
        super(context, attrs, R.attr.textFieldStyle);
    }

}