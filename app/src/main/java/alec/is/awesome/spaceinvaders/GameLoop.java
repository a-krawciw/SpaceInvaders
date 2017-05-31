package alec.is.awesome.spaceinvaders;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

/**
 * A class that creates a thread that executes rapidly to deal
 * with game play.
 */
public class GameLoop {
    private CanvasView cv; //The view that we draw on
    private List<Entity> enemyEntities; //The list of enemy ships
    private List<Entity> lasers; //The list of the players shot
    private ShipEntity player; // The player's ship
    private boolean isRunning = false; //Flag to tell the gameloop thread to stop executing


    //volatile keyword allows primitive data to be accessed from multiple threads
    //Read this link for more infomation https://docs.oracle.com/javase/tutorial/essential/concurrency/atomic.html

    private volatile boolean addShot = false; //Flag to tell the gameloop to add a shot to the game
    private volatile int numShot = 0; //The number of enemies shot this game


    public GameLoop(){
        //CopyOnWriteArrayList allows the ArrayList to be written to
        //and read from simultaneously from different threads
        enemyEntities = new CopyOnWriteArrayList<>();
        lasers = new CopyOnWriteArrayList<>();
        player = new ShipEntity();
    }

    /**
     * Save the Canvas that we draw on for the game.
     * @param cv The canvas view to be drawn on.
     */
    public void registerView(CanvasView cv){
        this.cv = cv;
    }

    /**
     * The list of enemies
     * @return The current list of enemy Entities
     */
    public List<Entity> getEnemies(){
        return enemyEntities;
    }

    /**
     * The list of shots
     * @return The current list of Shots
     */
    public List<Entity> getShots(){
        return lasers;
    }

    public Entity getPlayer(){
        return player;
    }

    /**
     * Resets all game variables and clears lists.
     */
    private void resetGame(){
        enemyEntities.clear();
        lasers.clear();
        player = new ShipEntity();
        numShot = 0;
    }

    /**
     * Sets game variables to necessary values and starts a new Gameloop thread.
     */
    public void start(){
        isRunning = true;
        resetGame();
        cv.setGameState(CanvasView.IN_GAME);

        //A thread allows you to run two sets of code simultaneously to each other.
        //to create a new Thread is a Runnable
        //Recommended reading https://www.tutorialspoint.com/java/java_multithreading.htm
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){

                    player.update();

                    //If there are any shots update them and check to see if the collide with any entities
                    if(lasers.size() > 0) {
                        for (Entity l : lasers) {
                            l.update();
                            for (Entity e : enemyEntities) {
                                e.update();

                                if (e.collidedWith(player)) {
                                    cv.setGameState(CanvasView.LOSE);
                                    stop();
                                }//if

                                if (l.collidedWith(e)) {
                                    l.collideAction(e);
                                    e.collideAction(l);
                                    numShot++;
                                }//if

                                if (!e.isValid()) {
                                    enemyEntities.remove(e);
                                }//if

                                //If the alien has made it to the bottom of the screen they lose
                                if (e.getHitBox().bottom > MainActivity.getScreenHeight()) {
                                    cv.setGameState(CanvasView.LOSE);
                                    stop();
                                }//if
                            }//for

                            if (!l.isValid()) {
                                lasers.remove(l);
                            }//if
                        }//for
                    } else {
                        //If there are no lasers simply update the aliens without checking
                        for(Entity l: enemyEntities){
                            l.update();
                            if (!l.isValid()) {
                                enemyEntities.remove(l);
                            }//if
                            if (l.getHitBox().bottom > MainActivity.getScreenHeight()) {
                                cv.setGameState(CanvasView.LOSE);
                                stop();
                            }//if
                        }//for
                    }//else

                    if(numShot >  50){
                        cv.setGameState(CanvasView.WIN);
                        stop();
                    }//if

                    if(addShot){
                        lasers.add(new Shot(player.getX() + player.getWidth() / 2, player.getY()));
                        addShot = false;
                    }//if

                    if((int)(Math.random() * 600) == 12 && enemyEntities.size() < numShot + 1){
                        enemyEntities.add(new AlienShip(Math.random() / 2 - 0.25, Math.random() / 2));
                    }//if

                    //Tell the CanvasView to redraw itself.
                    //Using invalidate will result in an exception because this executes on another Thread.
                    //postInvalidate solves this problem
                    cv.postInvalidate();

                    //Guarantee that the loop does not run so fast that it crashes
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }//catch
                }//Runnable
            }
        }).start();

    }

    /**
     * Use this to set the ship's speed from the Touch Event
     * @param vx The speed to move the ship at
     */
    void moveShip(double vx){
        player.setVx(vx);
    }

    /**
     * Tells the gameloop to add the shots
     */
    void shootShip(){
        addShot = true;
    }

    /**
     * Tells the the loop to stop running at the end of this loop
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Tell the Touch Listener whether or not the loops needs to be restarted
     * @return true if the loop is running, false otherwise
     */
    public boolean isRunning(){
        return isRunning;
    }

    /**
     * Returns the number of aliens shot during this game
     * @return the number of aliens shot so far.
     */
    int getNumAliensShot(){
        return numShot;
    }

}
