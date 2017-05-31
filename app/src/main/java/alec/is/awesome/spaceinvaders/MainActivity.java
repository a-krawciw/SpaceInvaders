package alec.is.awesome.spaceinvaders;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

/**
 * This is the main framework of the app. You can do many thnigs with activities
 * but for our purposes we will primarily just use this to start our game loop.
 */
public class MainActivity extends Activity {
    private CanvasView gameDisplay;
    private GameLoop gameLoop;
    private static int statusBarHeight = 0;

    /**
     * Initial method called by the operating system when the Activity (program)
     * is started. This is similar to main, in that this is called at the beginning, but you
     * should be aware that is can be called multiple times.
     * See https://developer.android.com/guide/components/activities/activity-lifecycle.html
     * for more information
     * @param savedInstanceState Information about the last running version of the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Tells the operating system which xml file you are using
        setContentView(R.layout.layout_game);

        //Load images
        ResourceLoader.init(this);
        ImageLibrary.init();

        //Create the gameloop
        gameLoop = new GameLoop();

        //Gets the CanvasView that has already been created
        gameDisplay = (CanvasView) findViewById(R.id.gameScreen);
        gameDisplay.init(gameLoop);

        //Determines thickness of the top bar, to ensure the screen resizes accordingly
        statusBarHeight = getStatusBarHeight();
    }

    /**
     * Find out how many pixels wide the screen is
     * @return The width of the screen in pixels
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Find out how many pixels high the screen is
     * @return The height of the screen in pixels
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels - statusBarHeight;
    }

    /**
     * Gets a rectangle that contains the entire screen
     * @return
     */
    public static Rect getScreenRect() {
        return new Rect(0, 0, getScreenWidth(), getScreenHeight());
    }

    /**
     * Helper method to find the height of the status bar
     * @return The height of the status bar in pixels
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
