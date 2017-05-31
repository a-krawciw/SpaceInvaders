package alec.is.awesome.spaceinvaders;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

/**
 * Sub-class of Entity representing an enemy spacecraft
 */
public class AlienShip extends Entity {
    /**
     * Te dimensions of the alien
     */
    private static final int DIMS = 75 * MainActivity.getScreenWidth() / 1005;

    /**
     * Constructor that accepts the initial velocities if the alien
     * @param vx The horizontal velocity
     * @param vy The vertical velocity
     */
    public AlienShip(double vx, double vy) {
        super(R.drawable.enemyship, vx, vy, Math.random() * (MainActivity.getScreenWidth() - DIMS), 0, DIMS, DIMS);
    }

    /**
     * If the alien is going off the side of the screen put it back
     * on the screen and reverse its horizontal direction
     */
    @Override
    public void checkHitBox() {
        if(getHitBox().right > MainActivity.getScreenWidth()){
            setVx(-getVx());
            setX(MainActivity.getScreenWidth() - getWidth());
        } else if(getHitBox().left < 0){
            setVx(-getVx());
            setX(0);
        }
    }

    /**
     * Called after a collision. Currently it simply signals the loop to remove it.
     * @param other The Entity that collided with this Entity
     */
    @Override
    public void collideAction(Entity other) {
        valid = false;
    }
}
