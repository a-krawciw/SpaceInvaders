package alec.is.awesome.spaceinvaders;

import android.graphics.Bitmap;
import android.util.SparseArray;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

/**
 * A class that stores a copy of the image in RAM and allows all
 * Entities of the same type to draw the same image. This takes up
 * far less memory.
 */
public class ImageLibrary {
    /**
     * This stores all of the images that need to be used in the game.
     * Sparse Arrays work but  haveing indices that are not consecutive.
     * This allows us to use the Android Resource id as a key, even though we don't know what it will be
     */
    private static SparseArray<Bitmap> images;

    /**
     * Called by the main activity AFTER the resource loader has been initialized.
     * Calling this before will result in an error
     */
    public static void init(){
        images = new SparseArray<>();

        images.append(R.drawable.background, ResourceLoader.getBitmap(R.drawable.background));
        images.append(R.drawable.enemyship, ResourceLoader.getBitmap(R.drawable.enemyship));
        images.append(R.drawable.laserenemy, ResourceLoader.getBitmap(R.drawable.laserenemy));
        images.append(R.drawable.laserplayer, ResourceLoader.getBitmap(R.drawable.laserplayer));
        images.append(R.drawable.playership, ResourceLoader.getBitmap(R.drawable.playership));
    }

    /**
     * Gets an image based on the Android resource id
     * @param resId The android resource id R.drawable.filename
     * @return The bitmap image of the resource
     */
    public static Bitmap getImage(int resId){
        return images.get(resId);
    }
}
