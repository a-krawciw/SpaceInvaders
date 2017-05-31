package alec.is.awesome.spaceinvaders;

import android.graphics.Rect;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

/**
 * Sub class of Entity. This class is used to represent
 * the lasers shot by the player.
 */
public class Shot extends Entity {
    //Static fields that make dimensions easier than hardcoding
    private static final int WID = 10 * MainActivity.getScreenWidth() / 1005;
    private static final int HEI = 40 * MainActivity.getScreenWidth() / 1005;

    /**
     * Constructor that fills in standard information but accepts the startnig location of the shot
     * @param x The horizontal position of the shot
     * @param y The vertical position of the shot
     */
    public Shot(double x, double y) {
        super(R.drawable.laserplayer, 0, -0.5, x, y, WID, HEI);
    }

    /**
     * Currently we remove the shot if it has left the screen.
     */
    @Override
    public void checkHitBox() {
        if(!Rect.intersects(getHitBox(), MainActivity.getScreenRect())){
            valid = false;
        }//if
    }//checkHitBox

    /**
     * Currently the entity is purely removed if it collides with
     * any other Entity.
     * @param other The Entity that collided with this Entity
     */
    @Override
    public void collideAction(Entity other) {
        valid = false;
    }//collideAction

}
