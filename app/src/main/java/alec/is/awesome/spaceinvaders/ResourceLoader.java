package alec.is.awesome.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

/**
 * A helper class used to load android resources
 */
public class ResourceLoader {
    /**
     * The information about the current execution of this application
     * This contains lots of helpful information such as resources.
     */
    private static Context c;

    /**
     * Finds and Android resource and converts it to a bitmap
     * @param resId The Android resource R.drawable.filename
     * @return A bitmap image
     */
    public static Bitmap getBitmap(int resId){
        return BitmapFactory.decodeResource(c.getResources(), resId);
    }

    /**
     * Finds colours that are stored as resources
     * @param resId The id of the colour R.color.colorname
     * @return An int that represents a colour
     */
    public static int getResColour(int resId){
        return c.getResources().getColor(resId);
    }

    /**
     * Finds a String resource
     * @param resId The id of the string R.strings.stringname
     * @return The string that is stored in the xml
     */
    public static String getResString(int resId){
        return c.getResources().getString(resId);
    }

    /**
     * Called by the Activity to inform the ResourceLoader where it
     * needs to load resources from.
     * @param con The Android Context. Activity is a subclass of Context and the Activity will
     *            pass this.
     */
    public static void init(Context con){
        c = con;
    }
}

