package com.example.kursova;

import Tanks.relsa;
import Tanks.smok;
import Tanks.grom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveLoad {

    private static Pane content = Main.root.getContent();

    public static ImageView imageView = new ImageView(new Image(Main.class.getResource("BaseBlue.png").toString(), 500, 500, false, false));

    public static void saveLayoutData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Layout Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.save"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            return;
        }

        // Створіть список для зберігання даних
        List<MicroObj> alefDataList = new ArrayList<>();
        List<MicroObj> betaDataList = new ArrayList<>();
        List<MicroObj> gimelDataList = new ArrayList<>();
        List<FractionObj> fractionsDataList = new ArrayList<>();
        List<MacroObj> spaceSystemDataList = new ArrayList<>();
        List<FlagObj> flagDataList = new ArrayList<>();

        for(Bases bases : Bases.listSystem){
            if(bases instanceof Bases){
                MacroObj obj = new MacroObj(bases.getName(), bases.getX(), bases.getY(), bases.getFraction().getName());
                spaceSystemDataList.add(obj);
            }
        }

        for (Flag flag : Flag.getListFlag()){
            if (flag instanceof Flag){

            }
        }


        for(Team node : Team.getListFraction()){
            if(node instanceof Team){
                FractionObj obj = new FractionObj(node.getName(),node.getFcolor());
                fractionsDataList.add(obj);
            }
        }
        // Пройдіться по елементах контейнера content і перетворіть кожен об'єкт Alef на об'єкт AlefData
        for (Node obj : content.getChildren()) {
            if (obj.getClass() == smok.class) {
                MicroObj alefData = new MicroObj(((smok) obj).getName(), ((smok) obj).getHealth(), ((smok) obj).getArmor(), ((smok) obj).getX(), ((smok) obj).getY(),((smok) obj).isActive(),((smok) obj).getCommand().getName());
                alefDataList.add(alefData);
            } else if (obj.getClass() == grom.class) {
                MicroObj betaData = new MicroObj(((smok) obj).getName(), ((smok) obj).getHealth(), ((smok) obj).getArmor(), ((smok) obj).getX(), ((smok) obj).getY(),((smok) obj).isActive(),((smok) obj).getCommand().getName());
                betaDataList.add(betaData);
            } else if (obj.getClass() == relsa.class) {
                MicroObj gimeLData = new  MicroObj(((smok) obj).getName(), ((smok) obj).getHealth(), ((smok) obj).getArmor(), ((smok) obj).getX(), ((smok) obj).getY(),((smok) obj).isActive(),((smok) obj).getCommand().getName());
                gimelDataList.add(gimeLData);
            }
        }

        SettingsData settingsData = new SettingsData(content.getLayoutX(), content.getLayoutY());
        MacroObjectsData macroObjectsData = new MacroObjectsData(spaceSystemDataList);
        MicroObjectsData microObjectsData = new MicroObjectsData(alefDataList, betaDataList,gimelDataList);
        FractionsData fractionsData = new FractionsData(fractionsDataList);
        FlagObjectsData flagObjectsData = new FlagObjectsData(flagDataList);

        // Створіть об'єкт LayoutData з об'єктом MicroObjectsData
        LayoutData layoutData = new LayoutData(settingsData, microObjectsData,macroObjectsData,fractionsData, flagObjectsData);

        // Серіалізуйте об'єкт LayoutData в JSON
        Gson gson = new Gson();
        String json = gson.toJson(layoutData);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
            System.out.println("Дані збережено у файл: " + file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class LayoutData {
        @SerializedName("settings")
        private SettingsData settings;
        @SerializedName("microObjects")
        private MicroObjectsData microObjects;
        @SerializedName("macroObjects")
        private MacroObjectsData macroObjects;
        @SerializedName("FractionData")
        private FractionsData fractionsData;

        @SerializedName("FlagData")
        private FlagObjectsData flagObjects;

        public LayoutData(SettingsData settings, MicroObjectsData microObjects,MacroObjectsData macroObjectsData, FractionsData fractionsData, FlagObjectsData flagObjects) {
            this.settings = settings;
            this.microObjects = microObjects;
            this.macroObjects = macroObjectsData;
            this.fractionsData = fractionsData;
            this.flagObjects = flagObjects;
        }
    }

    private static class SettingsData {
        @SerializedName("layoutX")
        private double layoutX;
        @SerializedName("layoutY")
        private double layoutY;

        public SettingsData(double layoutX, double layoutY) {
            this.layoutX = layoutX;
            this.layoutY = layoutY;
        }
    }

    private static class MicroObjectsData {
        @SerializedName("alefList")
        private List<MicroObj> alefList;
        @SerializedName("betaList")
        private List<MicroObj> betaList;
        @SerializedName("gimelList")
        private List<MicroObj> gimelList;

        public MicroObjectsData(List<MicroObj> alefList, List<MicroObj> betaList, List<MicroObj> gimelList) {
            this.alefList = alefList;
            this.betaList = betaList;
            this.gimelList = gimelList;
        }
    }


    private static class MacroObjectsData {
        @SerializedName("spaceSystemList")
        private List<MacroObj> sysList;

        public MacroObjectsData(List<MacroObj> sysList) {
            this.sysList = sysList;
        }
    }

    public static class MacroObj {
        @SerializedName("name")
        private String name;
        @SerializedName("x")
        private int x;
        @SerializedName("y")
        private int y;
        @SerializedName("fractionName")
        private String fractionName;

        public MacroObj(String name, int x, int y,String fractionName) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.fractionName = fractionName;
        }
    }
    public static class FlagObj {
        @SerializedName("name")
        private String name;
        @SerializedName("x")
        private int x;
        @SerializedName("y")
        private int y;

        public FlagObj(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }

    private static class FlagObjectsData {
        @SerializedName("FlagList")
        private List<FlagObj> flagList;

        public FlagObjectsData(List<FlagObj> flagList) {
            this.flagList = flagList;
        }
    }

    public static class MicroObj {
        @SerializedName("name")
        private String name;
        @SerializedName("health")
        private double health;
        @SerializedName("damage")
        private double damage;
        @SerializedName("x")
        private int x;
        @SerializedName("y")
        private int y;
        @SerializedName("active")
        private boolean active;
        @SerializedName("fractionNmae")
        private String fractionName;

        public MicroObj(String name, double health, double damage, int x, int y, boolean active, String fractionName) {
            this.name = name;
            this.health = health;
            this.damage = damage;
            this.x = x;
            this.y = y;
            this.active = active;
            this.fractionName = fractionName;
        }
    }

    private static class FractionsData {
        @SerializedName("Fractions")
        private List<FractionObj> fractionList;

        public FractionsData(List<FractionObj> fractionList) {
            this.fractionList = fractionList;
        }
    }

    private static class FractionObj {
        @SerializedName("name")
        private String name;
        @SerializedName("Color")
        private String fcolorName;

        public FractionObj(String name, Color fcolor) {
            this.name = name;
            this.fcolorName = fcolor.toString();
        }
    }

    public static void loadWorldFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Layout Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.save"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return;
        }
        try (Reader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().create();
            LayoutData layoutData = gson.fromJson(reader, LayoutData.class);

            content.getChildren().clear();
            Bases.getListSystem().clear();
            Flag.getListFlag().clear();
            Team.getListFraction().clear();

            content.setLayoutX(layoutData.settings.layoutX);
            content.setLayoutY(layoutData.settings.layoutY);

            for (FractionObj fraction : layoutData.fractionsData.fractionList){
                Team.getListFraction().add(new Team(fraction.name,Color.valueOf(fraction.fcolorName)));
            }

            for (MicroObj alefData : layoutData.microObjects.alefList) {
                smok obj = new smok(alefData.name, alefData.health, alefData.damage, alefData.x, alefData.y);
                obj.setActive(alefData.active);
                Main.root.addObj(obj);
                Team.getFractionByName(alefData.fractionName).addObj(obj);
            }

            for (MicroObj betaData : layoutData.microObjects.betaList) {
                grom obj = new grom(betaData.name, betaData.health, betaData.damage, betaData.x, betaData.y);
                Main.root.addObj(obj);
                Team.getFractionByName(betaData.fractionName).addObj(obj);
            }

            for (MicroObj gimelData : layoutData.microObjects.gimelList) {
                relsa obj = new relsa(gimelData.name, gimelData.health, gimelData.damage, gimelData.x, gimelData.y);
                Main.root.addObj(obj);
                Team.getFractionByName(gimelData.fractionName).addObj(obj);
            }

            for (MacroObj macroObj : layoutData.macroObjects.sysList){
                Bases bases = new Bases(macroObj.name , macroObj.x , macroObj.y, imageView);
                Main.root.addSystem(bases);
                Team.getFractionByName(macroObj.fractionName).addSystem(bases);
            }
            for (FlagObj flagObj : layoutData.flagObjects.flagList){
                Flag flag = new Flag(flagObj.x , flagObj.y, 400,600);
                Main.root.addFlag(flag);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
