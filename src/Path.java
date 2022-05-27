import java.util.ArrayList;

public class Path {
    // Variables
    ArrayList<GameCell> cellsInPath;
    boolean isComplete;

    // Constructor
    public Path() {
        cellsInPath = new ArrayList<>();
        isComplete = false;
    }

    // Getters and setters
    public ArrayList<GameCell> getCellsInPath() {
        return cellsInPath;
    }
    public boolean isComplete() {
        return isComplete;
    }
    public void setCellsInPath(ArrayList<GameCell> cellsInPath) {
        this.cellsInPath = cellsInPath;
    }
    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    // Public methods
    public void add(GameCell cell) {
        cellsInPath.add(cell);
    }
    public GameCell get(int index) {
        return cellsInPath.get(index);
    }
    public int size() {
        return cellsInPath.size();
    }
    public String toString() {
        return (cellsInPath.get(0).getColor());
    }
}
