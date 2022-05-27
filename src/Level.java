public class Level {
    // Variables
    private final int ID;
    private static int count = 1;
    private String title;
    private int[] xValues;
    private int[] yValues;
    private String[] colors;

    // Constructor
    public Level(String title, int[] xValues, int[] yValues, String[] colors){
        this.title = title;
        ID = count;
        count++;
        this.xValues = new int[xValues.length];
        this.yValues = new int[yValues.length];
        this.colors = new String[colors.length];

        // Throws error if the array lengths are not the correct 2:2:1 ratio
        if ((colors.length * 2 != xValues.length) || (colors.length * 2 != yValues.length)) {
            throw new IllegalArgumentException("Invalid array lengths");
        }
        for (int i = 0; i < xValues.length; i++) {
            this.xValues[i] = xValues[i];
            this.yValues[i] = yValues[i];
            this.colors[i/2] = colors[i/2];
        }
    }

    // Getters and setters
    public int getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public int[] getxValues() {
        return xValues;
    }
    public int[] getyValues() {
        return yValues;
    }
    public String[] getColors() {
        return colors;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setxValues(int[] xValues) {
        this.xValues = xValues;
    }
    public void setyValues(int[] yValues) {
        this.yValues = yValues;
    }
    public void setColors(String[] colors) {
        this.colors = colors;
    }

    // Methods
    // Returns the length of the x & y value arrays
    public int size() {
        return xValues.length;
    }

    // toString method
    public String toString() {
        return title;
    }
}
