import javax.swing.*;

/**
 * Models a single cell and holds its properties.
 *
 * @author Jake Koontz
 * Last update: 7/13/17
 */

public class Cell extends JButton {
    private int row;
    private int col;

    // states of cell
    private boolean cleared;    // cell has been opened up
    private boolean checked;    // cell has been checked for an empty space
    private boolean flagged;    // cell has been flagged
    private boolean marked;     // cell has been flagged or question marked
    private boolean markedAndEmpty;     // cell has been confirmed empty and marked

    /**
     * Initialize cell.
     *
     * @param row vertical location of cell
     * @param col horizontal location of cell
     */
    public Cell(int row, int col) {
        super();    // calls constructor for JButton
        this.row = row;
        this.col = col;
    }

    /**
     * Reset all states of cell.
     */
    public void reset() {
        cleared = false;
        checked = false;
        flagged = false;
        marked = false;
        markedAndEmpty = false;
    }

    // mutator methods

    public void setCleared(boolean state) {
        cleared = state;
    }

    public void setChecked(boolean state) {
        checked = state;
    }

    public void setFlagged(boolean state) {
        flagged = state;
    }

    public void setMarked(boolean state) {
        marked = state;
    }

    public void setMarkedAndEmpty(boolean state) {
        markedAndEmpty = state;
    }

    // accessor methods

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isCleared() {
        return cleared;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isMarked() {
        return marked;
    }

    public boolean isMarkedAndEmpty() {
        return markedAndEmpty;
    }
}