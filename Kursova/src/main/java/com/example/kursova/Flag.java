package com.example.kursova;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Flag extends StackPane {

    private final ImageView imageView;

    private int x;

    private static ObservableList<Flag> listFlag = FXCollections.observableArrayList();
    private Text num;
    private Text text;

    private boolean pickedUp;

    private int y;

    public Flag(int x, int y, int height, int width) {
        this.imageView = new ImageView(new Image(Main.class.getResource("flag.png").toString(), height, width, false, false));
        this.x = x;
        this.y = y;

        num = new Text(String.valueOf(3));
        num.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        if (pickedUp) {
            text = new Text("Не підібраний");
            text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        }else {
            text = new Text("підібраний");
        }
        VBox vbox1 = new VBox(num);
        VBox vbox2 = new VBox(text);
        vbox1.setLayoutX(200);
        vbox1.setLayoutY(200);
        vbox2.setLayoutX(200);
        vbox2.setLayoutY(500);
        listFlag.add(this);

        getChildren().addAll(imageView);
        getChildren().addAll(vbox1, vbox2);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static ObservableList<Flag> getListFlag() {
        return listFlag;
    }
}
