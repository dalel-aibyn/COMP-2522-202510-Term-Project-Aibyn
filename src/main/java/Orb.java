public class Orb extends Obstacle {
    private static final float YUMP_BOOST = 15.0f; // tentative

    @Override
    public boolean checkCollision() {
        return false;
    }

    private void jumpPlayer(final Player player) {
        // something something YUMP_BOOST
    }
}