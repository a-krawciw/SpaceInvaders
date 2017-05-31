package alec.is.awesome.spaceinvaders;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

/**
 * A sub-class of Entity that represents the player
 */
public class ShipEntity extends Entity {
    /**
     * The width and height. It is easier to make a variable rather than hardcode it
     */
    private static int DIMS =  100 * MainActivity.getScreenWidth() / 1005;


    /**
     * The constructor passes standard information to the super constructor
     */
    public ShipEntity() {
        super(R.drawable.playership, 0, 0, MainActivity.getScreenWidth() / 2 - DIMS / 2, MainActivity.getScreenHeight() - DIMS - 10, DIMS, DIMS);
    }

    /**
     * If the hitbox is going off the screen move the ship such that it is on the screen
     */
    @Override
    public void checkHitBox() {
        if(getHitBox().right > MainActivity.getScreenWidth()){
            setX(MainActivity.getScreenWidth() - DIMS);
        } else if(getHitBox().left < 0){
            setX(0);
        }
    }

    /**
     * Currently no action is taken when another entity collides.
     * @param other The Entity that collided with this Entity
     */
    @Override
    public void collideAction(Entity other) {}
}
