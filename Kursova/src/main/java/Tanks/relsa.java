package Tanks;

import com.example.kursova.Main;
import javafx.scene.image.Image;

public class relsa extends grom {
    public static Image gimelImage = new Image(Main.class.getResource("relsa.png").toString(), 100, 100, false, false);

    public relsa(String name, double health, double damage, int x, int y) {
        super(name, health, damage, x, y);
        this.imageView.setImage(gimelImage);
    }

    public relsa() {
        super();
    }

}
