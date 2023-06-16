package Tanks;

import com.example.kursova.Team;
import com.example.kursova.Main;
import javafx.scene.image.Image;

import java.util.Objects;

public class grom extends smok {
    public static Image betaImage = new Image(Objects.requireNonNull(Main.class.getResource("grom.png")).toString(), 100, 100, false, false);

    public grom(String name, double health, double damage, int x, int y) {
        super(name, health, damage, x, y);
        this.imageView.setImage(betaImage);

    }

    public grom() {
        super();
        this.imageView.setImage(betaImage);
    }

    public grom(smok obj) {
        super(obj.getName(), obj.getHealth(), obj.getArmor(), obj.getX(), obj.getY());
        this.imageView.setImage(betaImage);
        obj.getCommand().addObj(this);
    }

    @Override
    public void setFraction(Team team) {
        super.setFraction(team);
    }
}
