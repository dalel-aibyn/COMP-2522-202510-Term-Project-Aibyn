public class Portal extends Obstacle {
    private final GameMode targetMode;

    public Portal(final GameMode targetMode) {
        this.targetMode = targetMode;
    }

    @Override
    public boolean checkCollision() {
        return false;
    }

    private void switchMode(final Player player) {
        // yada yada so on and so forth etc etc
    }
}