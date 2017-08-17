/**
 * Calculates various statistics for a game.
 *
 * @author Jake Koontz
 * Last update: 8/5/17
 */

public class GameStats {
    private Game game;  // game statistics are being calculated for
    private int numSpaces;  // total spaces on the board
    private int[] counts;   // array containing counts of each possible number

    /**
     * Initializes fields and calculates counts.
     *
     * @param game game for which statistics are being calculated
     */
    public GameStats(Game game) {
        this.game = game;
        numSpaces = game.getHeight() * game.getWidth();

        counts = new int[9];
        calcCounts();   // calculate counts of each number
    }

    /**
     * Calculate total count of each number.
     */
    private void calcCounts() {
        for (int i = 0; i < game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                int value = game.getValue(i, j);

                if (value >= 0) {
                    counts[value]++;    // increment count of correct number
                }
            }
        }
    }

    /**
     * Return total number of spaces.
     *
     * @return String of number of spaces
     */
    public String getNumSpaces() {
        return String.format("Number of spaces:  %d", numSpaces);
    }

    /**
     * Calculate the mine density of the game.
     *
     * @return String of the percentage of spaces that are mines
     */
    public String getMineDensity() {
        return String.format("Mine Density:  %.2f%%", ((double)game.getNumMines() / numSpaces) * 100);
    }

    /**
     * Calculate the frequency of each number.
     *
     * @return String containing each frequency
     */
    public String getFrequencies() {
        StringBuilder sb = new StringBuilder("Number Frequency:\n");

        // add frequency for each number
        for (int i = 0; i < counts.length; i++) {
            sb.append(String.format("     %d:  %d (%.2f%%)", i, counts[i], ((double)counts[i] / numSpaces) * 100));

            // add newline for every line but the last
            if (i != counts.length - 1)
                sb.append("\n");
        }

        return sb.toString();
    }
}