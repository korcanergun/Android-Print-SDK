package ly.kite.print.psprintstudio;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;

/**
 * @author Andreas C. Osowski
 */
public class FontUtils {

    public static Spannable spanMissionScript(Context ctx, CharSequence text) {
        return spanCustomTF(ctx, text, "Mission-Script.otf");
    }

    public static Spannable spanCustomTF(Context ctx, CharSequence text, String tf) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new CustomTypefaceSpan(ctx, tf), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }
}
