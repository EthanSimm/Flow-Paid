import javax.swing.*;
import java.awt.*;

public class GameCell extends Component {
    // Variables
    private ImageIcon sprite;
    private final int xPos; // Refers to the grid position, not pixel position
    private final int yPos;
    private int type;
    private String color;
    private String[] cellSpritesArr;

    // Constructors
    public GameCell(int x, int y) {
        xPos = x;
        yPos = y;
        type = 0;
        color = "";
        sprite = new ImageIcon("images/EmptyCell.png");
        this.setPreferredSize(new Dimension(70,70));
    }

    public GameCell(int x, int y, int type, String color) {
        xPos = x;
        yPos = y;
        this.type = type;
        this.color = color.toUpperCase();
        // Defines the cellSpriteArr in accordance to its color
        cellSpritesArr = new String[]{"images/EmptyCell.png",
                "images/" + color + "h.png",
                "images/" + color + "v.png",
                "images/" + color + "c1.png",
                "images/" + color + "c2.png",
                "images/" + color + "c3.png",
                "images/" + color + "c4.png",
                "images/" + color + "ep.png",
                "images/" + color + "epU.png",
                "images/" + color + "epL.png",
                "images/" + color + "epD.png",
                "images/" + color + "epR.png"};
        sprite = new ImageIcon(cellSpritesArr[type]);
        this.setPreferredSize(new Dimension(70,70));
    }

    // Getters and setters
    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public ImageIcon getSprite() {
        return sprite;
    }

    public int getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public void setSprite(ImageIcon sprite) {
        this.sprite = sprite;
    }

    public void setType(int type) {
        this.type = type;
        if (type != 0) {
            sprite = new ImageIcon(cellSpritesArr[type]);
        } else {
            sprite = new ImageIcon("images/EmptyCell.png");
        }
        repaint();
    }

    public void setColor(String color) {
        this.color = color.toUpperCase();
        cellSpritesArr = new String[]{"images/EmptyCell.png",
                "images/" + color + "h.png",
                "images/" + color + "v.png",
                "images/" + color + "c1.png",
                "images/" + color + "c2.png",
                "images/" + color + "c3.png",
                "images/" + color + "c4.png",
                "images/" + color + "ep.png",
                "images/" + color + "epU.png",
                "images/" + color + "epL.png",
                "images/" + color + "epD.png",
                "images/" + color + "epR.png"};
    }

    // Public Methods
    // Paint method - Draws graphic for the tile
    public void paint(Graphics g) {
        sprite.paintIcon(this, g, 0, 0);
    }

    // toString for testing purposes
    public String toString() {
        return xPos + " " + yPos;
    }
}