public abstract class Obstacle implements Collidable {
    private float positionX;
    private float positionY;
    private float rotation;
    private float hitboxWidth;
    private float hitboxHeight;
    private ObstacleType type;
    
    public boolean checkCollision(final Player player) {
        return false;
    }
}