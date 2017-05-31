package alec.is.awesome.spaceinvaders;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

/**
 * An Android custom view that can be drawn upon and receive touch events.
 * This is similar to a JFrame except that the constructor is called by the operating system,
 * not you.
 */
public class CanvasView extends View implements View.OnTouchListener {
    //Game loop state variables
    public static final int MENU = 0;
    public static final int IN_GAME = 1;
    public static final int LOSE = 2;
    public static final int WIN = 3;

    /**
     * Stores the current state that the game is in.
     */
    private int gameState = MENU;

    /**
     * Contains parameters informing the Canvas how to draw objects.
     * Use this to modify fonts, stroke style and colour.
     */
    private Paint params;

    /**
     * Background colour of the view. By default it is clear
     * This can be modified here or in the Android colours resources file.
     * res/values/colours.xml
     */
    private int backgroundColour;

    /**
     * Store the size of this screen
     */
    public static Rect SCREEN_SIZE;

    /**
     * The game loop managing gameplay
     */
    private GameLoop loop;

    /**
     * The last time that a touch event occurred
     */
    private long lastTouchTime = 0;

    /**
     * The last time that the user took a shot
     */
    private long lastShootTime = 0;

    /**
     * Useful for using the logging live template.
     * Log.i(String tag, String message);
     * is similar to System.out.println(String s);
     */
    String TAG = "Canvas";


    /**
     * This is the constructor called by the operating system when it inflates the activity.
     * @param c The current application creating this CanvasView
     * @param attrs Information about the View as written in the xml
     */
    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        setOnTouchListener(this);

        params = new Paint();
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CanvasView);
        backgroundColour = a.getColor(R.styleable.CanvasView_bColor, Color.argb(1, 0, 0, 0));

        //It is important to destroy the array
        a.recycle();
    }

    /**
     * This makes the gameloop aware of the CanvasView and vice versa
     * @param g The gameloop that is in use for this game.
     */
    public void init(GameLoop g){
        g.registerView(this);
        loop = g;
    }

    /**
     * Modifies the screen size (in case of screen rotation)
     */
    private void updateScreenSize(){
        Rect fullScreen = new Rect();
        getDrawingRect(fullScreen);
        SCREEN_SIZE = fullScreen;
    }

    /**
     * Method that deals with input coming via the phone's touchscreen.
     * Look at MotionEvent for more information. It is worth being aware of the fact that
     * for multi-touch enabled devices the second touch is not an ACTION_DOWN event but rather a
     * POINTER_DOWN event
     * @param v The view from which the touch event originated
     * @param event Information about the event eg. location of click
     * @return True if this is the last view that should be aware of the event false otherwise
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //If the game loop is not running that means that we should start the game
        if(!loop.isRunning() && event.getAction() == MotionEvent.ACTION_DOWN && System.currentTimeMillis() - lastTouchTime > 1000){
            loop.start();
        }

        //During gameplay these control the ship's actions
        if (event.getAction() == MotionEvent.ACTION_UP) {
            loop.moveShip(0);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < loop.getPlayer().getX()) {
                loop.moveShip(-0.7);
            } else if (event.getX() > loop.getPlayer().getX()) {
                loop.moveShip(0.7);
            }
        }
        if(System.currentTimeMillis() - lastShootTime > 400){
            loop.shootShip();
            lastShootTime = System.currentTimeMillis();
        }

        lastTouchTime = System.currentTimeMillis();
        return true;
    }

    /**
     * This is the primary method called by the operating system to draw the View. This
     * is the equivalent to paintComponent
     * @param c The Canvas object that will be used to draw the game on.
     */
    public void onDraw(Canvas c){
        super.onDraw(c);
        updateScreenSize();
        drawBackground(c);

        //Draw all of the enemies, shots and the player
        params.setColor(backgroundColour);
        if(loop != null) {
            for (Entity e : loop.getEnemies()) {
                e.draw(c);
            }

            for (Entity e : loop.getShots()) {
                e.draw(c);
            }

            loop.getPlayer().draw(c);
        }

        //Controls which messages are printed to the screen based on the game state
        if(gameState == MENU){
            drawCentredText(c, "PRESS THE SCREEN TO BEGIN", SCREEN_SIZE.height() / 2);
        } else if(gameState == LOSE){
            drawCentredText(c, "YOU LOSE!", SCREEN_SIZE.height() / 2);
            drawCentredText(c, "PRESS THE SCREEN TO BEGIN", SCREEN_SIZE.height() / 2 + 60);
        } else if(gameState == WIN){
            drawCentredText(c, "YOU WIN!", SCREEN_SIZE.height() / 2);
            drawCentredText(c, "PRESS THE SCREEN TO BEGIN", SCREEN_SIZE.height() / 2 + 60);
        } else if(gameState == IN_GAME){
            params.setColor(Color.WHITE);
            params.setTextSize(30 * MainActivity.getScreenWidth() / 1080);
            c.drawText("Num Kills: " + loop.getNumAliensShot(), 5, 35, params);
        }

    }

    /**
     * Helper method to draw the background which is transparent
     * @param c The Canvas to be drawn upon
     */
    private void drawBackground(Canvas c){
        params.setStyle(Paint.Style.FILL);
        params.setColor(backgroundColour);
        c.drawRect(SCREEN_SIZE, params);
    }

    /**
     * Allow the game loop to let the canvas know at what state the game is in.
     * @param state The new game state to be set
     */
    public void setGameState(int state){
        this.gameState = state;
    }

    /**
     * Helper method that writes text centered horizontally
     * on the screen. Note \n will not work with is method
     * @param c The canvas to draw on
     * @param toWrite The text to display
     * @param y The vertical location of the text.
     */
    private void drawCentredText(Canvas c, String toWrite, int y){
        params.setColor(Color.WHITE);
        params.setTextSize(50 * MainActivity.getScreenWidth() / 1005);
        int width = (int) params.measureText(toWrite) / 2;
        c.drawText(toWrite, SCREEN_SIZE.width() / 2 - width, y, params);
        params.setColor(backgroundColour);
    }
}
