package com.example.kursova;

import Tanks.grom;
import Tanks.relsa;
import Tanks.smok;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.*;
import java.util.*;

public class Main extends Application {

    public static final int WINDOW_WIDTH = 1600; // Ширина вікна
    public static final int WINDOW_HEIGHT = 800; // Висота вікна

    public static String Title = "Курсова робота"; // Заголовок вікна
//    public static Image icon = new Image(Objects.requireNonNull(Main.class.getResource("icoStellaris.png")).toString()); // Іконка вікна

    Image imageRed = new Image(Main.class.getResource("BaseRed.png").toString(), 500, 500, false, false);
    ImageView imageBlueView = new ImageView(imageRed);


    public Scene scene;
    public static Console console = new Console(); // Створення консолі для відображення виводу
    public static CustomLog log;

    static {
        try {
            log = new CustomLog(); // Ініціалізація файлу журналу
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final long startTime = System.nanoTime();

    public static World root = new World(5000, 5000); // Створення світу з вказаними розмірами

    @Override

    public void start(Stage primaryStage) {

        Flag flag = new Flag(2300, 2300, 400,600);
        root.addFlag(flag);

        // Створення планетних систем
        imageBlueView.setScaleX(-1);
        Bases blueBase = new Bases("База Синіх", 500, 1000, new ImageView(new Image(Main.class.getResource("BaseBlue.png").toString(), 500, 500, false, false)));
        Bases redBase = new Bases("База Червоних", 3800, 3400, imageBlueView);
//        Bases prozion = new Bases("Проціон", 3500, 3000);



        // Додавання планетних систем до світу
        root.addSystem(blueBase);
        root.addSystem(redBase);
//      root.addSystem(prozion);

        // Додавання меню
        addMenu();

        root.getChildren().add(console); // Додавання консолі до кореневого вузла
        console.setAlignment(Pos.CENTER); // Встановлення вирівнювання консолі по центру
        console.setLayoutX((((double) WINDOW_WIDTH / 2) + console.getWidth()) / 2); // Встановлення положення консолі по горизонталі
        console.setLayoutY(0); // Встановлення положення консолі по вертикалі


        Team blue = new Team("Сині",Color.rgb(0, 0, 255)); // Створення фракції зі синім кольором
        blue.addSystem(blueBase); // Додавання планетної системи "Сонце" до фракції "Люди"
//      blue.addSystem(prozion);


        Team red = new Team("Червоні",Color.rgb(255, 0, 0)); // Створення фракції з полупрозорим червоним кольором
        red.addSystem(redBase); // Додавання планетної системи "Сіріус" до фракції "Гриби"
//        red.addSystem(adam);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT); // Створення сцени з кореневим вузлом світу

        // Налаштування вікна
        primaryStage.setTitle(Title);
//        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.show();


        Set<KeyCode> pressedKeys = new HashSet<>();
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case INSERT -> showMoreCreate();
                case DELETE -> {
                    for (smok obj : root.getListObj()) {
                        if(obj != null && obj.isActive()){
                            obj.dead();
                        }
                    }
                }
                case F1 -> {
                    if (console.isVisible()){
                        console.hide();
                    }else console.show();
                }
                case ESCAPE -> {
                    for (smok obj : root.getListObj()) {
                        if(obj != null && obj.isActive()){
                           obj.setActive(false);
                           obj.getHitbox().setStroke(Color.YELLOW);
                        }
                    }
                }
                default -> {
                }
            }
            pressedKeys.add(event.getCode());
            if (pressedKeys.contains(KeyCode.LEFT)) {
                for (smok obj : root.getListObj()) {
                    if (obj != null && obj.isActive()) {
                        int move = obj.getX() - obj.getMSpead();
                        obj.setX(move);
                        obj.setLayoutX(move);
                    }
                }
            }
            if (pressedKeys.contains(KeyCode.UP)) {
                for (smok obj : root.getListObj()) {
                    if(obj != null && obj.isActive()){
                    int move = obj.getY() - obj.getMSpead();

                    }
                }
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                for (smok obj : root.getListObj()) {
                    if(obj != null && obj.isActive()){
                    int move = obj.getX() + obj.getMSpead();
                    obj.setX(move);
                    obj.setLayoutX(move);
                    }
                }
            }
            if (pressedKeys.contains(KeyCode.DOWN)) {
                for (smok obj : root.getListObj()) {
                    if(obj != null && obj.isActive()){
                    int move = obj.getY() + obj.getMSpead();
                    obj.setY(move);
                    obj.setLayoutY(move);
                }
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode());
        });

    }

    public void addMenu(){

        // Створення верхнього меню
        MenuBar menuBar = new MenuBar();
        menuBar.setOpacity(0.3);

        // Створення обєктів меню
        Menu MenuOptions = new Menu("Настройки");
        menuBar.getMenus().add(MenuOptions);
        MenuItem save = new MenuItem("Зберегти");
        MenuOptions.getItems().add(save);

        save.setOnAction(actionEvent -> {
            SaveLoad.saveLayoutData();
        });

        MenuItem load = new MenuItem("Завантажити");
        MenuOptions.getItems().add(load);

        load.setOnAction(actionEvent -> {
            SaveLoad.loadWorldFromFile();
        });

        Menu MenuCreate = new Menu("Cтворити");
        menuBar.getMenus().add(MenuCreate);

        Menu MenuSearch = new Menu("Інформація");
        menuBar.getMenus().add(MenuSearch);


        MenuItem search = new MenuItem("Інформація");
        search.setOnAction(actionEvent -> info());
        MenuSearch.getItems().add(search);

        // Створення пункту меню "Корабель класу Alef" та його обробник події
        MenuItem menuItemAddAlef = new MenuItem("Танк");
        menuItemAddAlef.setOnAction(event -> showMoreCreate());
        MenuCreate.getItems().add(menuItemAddAlef);

        root.getChildren().add(menuBar);

        //Анімація для Панелі користувача
        menuBar.setOnMouseEntered(event -> {
            menuBar.setOpacity(1); // збільшуємо прозорість, щоб область стала видимою
        });
        menuBar.setOnMouseExited(event -> {
            menuBar.setOpacity(0.3); // зменшуємо прозорість, щоб область знову стала невидимою
        });
    }

    public void showMoreCreate() {
        Stage stage = new Stage();
        stage.setTitle("Настройки створювання");

        TextField textFieldName = new TextField();
        textFieldName.setPromptText("Впишіть ім'я танка");
        textFieldName.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField healthTextField = new TextField();
        healthTextField.setPromptText("Здоров'я");
        healthTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField damageTextField = new TextField();
        damageTextField.setPromptText("Урон");
        damageTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField xTextField = new TextField();
        xTextField.setPromptText("X");
        xTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField yTextField = new TextField();
        yTextField.setPromptText("Y");
        yTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        RadioButton smokRadioButton = new RadioButton("Смок");
        smokRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton gromRadioButton = new RadioButton("Грір");
        gromRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton relsaRadioButton = new RadioButton("Рельса");
        relsaRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        HBox boxF = new HBox();
        boxF.setAlignment(Pos.CENTER);
        ToggleGroup toggleGroupF = new ToggleGroup();

        RadioButton nullRadioButton = new RadioButton("Без фракціх");
        nullRadioButton.setSelected(true);
        nullRadioButton.setToggleGroup(toggleGroupF);
        boxF.getChildren().add(nullRadioButton);
        relsaRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        for (Team team : Team.getListFraction()) {
            RadioButton radioButton = new RadioButton(team.getName());
            radioButton.setToggleGroup(toggleGroupF);
            radioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
            boxF.getChildren().add(radioButton);
        }

        // Встановлюємо радіокнопку "smok" вибраною за замовчуванням
        smokRadioButton.setSelected(true);

        HBox boxRC = new HBox();
        boxRC.setAlignment(Pos.CENTER);
        ToggleGroup toggleGroupRC = new ToggleGroup();
        smokRadioButton.setToggleGroup(toggleGroupRC);
        gromRadioButton.setToggleGroup(toggleGroupRC);
        relsaRadioButton.setToggleGroup(toggleGroupRC);
        boxRC.getChildren().addAll(smokRadioButton, gromRadioButton, relsaRadioButton);


        Button setButton = new Button("Створити");
        setButton.setOnAction(event -> {
            String name = textFieldName.getText().isEmpty() ? smok.getRandomName() : textFieldName.getText();
            int health = healthTextField.getText().isEmpty() ? 100 : Integer.parseInt(healthTextField.getText());
            int damage = damageTextField.getText().isEmpty() ? 20 : Integer.parseInt(damageTextField.getText());
            int x = xTextField.getText().isEmpty() ? smok.getRandomX() : Integer.parseInt(xTextField.getText());
            int y = yTextField.getText().isEmpty() ? smok.getRandomY() : Integer.parseInt(yTextField.getText());

            smok obj = null;

            if (smokRadioButton.isSelected()) {
                obj = new smok(name, health, damage, x, y);
                root.addObj(obj);
            }

            if (gromRadioButton.isSelected()) {
                obj = new grom(name, health, damage, x, y);
                root.addObj(obj);
            }

            if (relsaRadioButton.isSelected()) {
                obj = new relsa(name, health, damage, x, y);
                root.addObj(obj);
            }

            // Додано перевірку на вибір радіобатону зі списку
            for (Node node : boxF.getChildren()) {
                if (!nullRadioButton.isSelected()) {
                    if ( node instanceof RadioButton radioButton) {
                            if (radioButton.isSelected()) {
                                Objects.requireNonNull(obj).setFraction(Objects.requireNonNull(Team.getFractionByName(radioButton.getText())));
                            }
                        }
                }
            }


            stage.close();
        });
        setButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px;");
        Button cancelButton = new Button("Скасувати");
        cancelButton.setOnAction(event -> stage.close());
        cancelButton.setStyle("-fx-background-color: #ddd; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px; -fx-border-color: gray; -fx-border-width: 1px;");

        HBox buttonBox = new HBox(setButton, cancelButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        HBox xy = new HBox(xTextField, yTextField);
        xy.setAlignment(Pos.CENTER);
        VBox centerBox = new VBox();
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(
                textFieldName,
                healthTextField,
                damageTextField,
                new Label("Координати:"),
                xy,
                new Label("Виберіть тип:"),
                boxRC,
                new Label("Виберіть фракцію:"),
                boxF

        );
        centerBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(centerBox, buttonBox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #E0FFFF; -fx-border-color: black; -fx-border-radius: 5px;");

        Scene scene = new Scene(vbox, 350, 375);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Задайте ширину стовпців з PromptText:
        textFieldName.setPrefColumnCount(10);
        healthTextField.setPrefColumnCount(10);
        damageTextField.setPrefColumnCount(10);
        xTextField.setPrefColumnCount(10);
        yTextField.setPrefColumnCount(10);
    }

    public void info() {
        Stage stage = new Stage();

        // Створення елементів
        Label titleLabel = new Label("Статистика");

        Label microObjectsLabel = new Label("Кількість мікрооб'єктів : " + root.getListObj().size());
        Label macroObjectsLabel = new Label("Кількість макрооб'єктів : " + Bases.listSystem.size());
        Label timeLabel = new Label("Час роботи програми : 0 сек.");

        //Список мікрообєктів

        ListView<smok> microObjectsListView = new ListView<>(root.getListObj());
        microObjectsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            public void updateItem(smok item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    // Створити HBox для відображення тексту та об'єкта
                    HBox itemsP = new HBox();

                    Label nameLabel = new Label("Ім'я : " + item.getName());
                    // Створити елементи для поля здоров'я
                    Label healthLabel = new Label("Здоров'я : " + item.getHealth());
                    // Створити елементи для поля урону
                    Label damageLabel = new Label("Урон : " + item.getArmor());
                    // Створити елементи для поля координат
                    Label coordinatesLabel = new Label("Координати :(" + item.getX() + ";" + item.getY() + ")");

                    // Створити кнопки
                    Button searchButton = new Button("Знайти");
                    Button resetButton = new Button("Змінити");
                    Button cloneButton = new Button("Клонувати");
                    Button deleteButton = new Button("Видалити");

                    searchButton.setOnAction(event -> root.setPositionSearch(item));

                    resetButton.setOnAction(event -> {
                        stage.close();
                        reset(item);
                    });

                    cloneButton.setOnAction(event -> {
                        root.addObj(item.clone());
                    });

                    deleteButton.setOnAction(event -> {
                        root.removeObj(item);
                    });

                    // Створити HBox для кнопок
                    HBox buttonBox = new HBox(10, searchButton, resetButton,cloneButton, deleteButton);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);

                    // Створити VBox для всіх елементів
                    VBox vBox = new VBox(10,
                            new HBox(10, nameLabel),
                            new HBox(10, healthLabel),
                            new HBox(10, damageLabel),
                            new HBox(10, coordinatesLabel),
                            buttonBox
                    );
                    vBox.setPadding(new Insets(10));

                    itemsP.getChildren().addAll(item.getSnapshotParameters(), vBox);

                    setGraphic(itemsP);
                }
            }
        });

        //Список макрообєктів
        ListView<Bases> macroObjectsListView = new ListView<>(Bases.listSystem);
        macroObjectsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            public void updateItem(Bases item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    // Створити HBox для відображення тексту та об'єкта
                    HBox itemsP = new HBox();

                    Label nameLabel = new Label("Ім'я : " + item.getName());


//                    Label fractionNameLabel = new Label("Фракція : Відсутня");
//                    if(item.getFraction()!= null){
//                        fractionNameLabel.setText("Фракція : " + item.getFraction().getName());
//                    }

                    // Елементи для кількість ресурсів
                    Label resurseLable = new Label("Кількість танків бази : " + item.getResources());

                    // Елементи для поля координат
                    Label coordinatesLabel = new Label("Координати :(" + item.getX() + ";" + item.getY() + ")");


                    // Створити кнопки
                    Button searchButton = new Button("Знайти");

                    searchButton.setOnAction(event -> root.setPositionSearch(item));


                    // Створити HBox для кнопок
                    HBox buttonBox = new HBox(searchButton);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);

                    // Створити VBox для всіх елементів
                    VBox vBox = new VBox(10,
                            new HBox(10, nameLabel),
//                            new HBox(10, fractionNameLabel),
                            new HBox(10, resurseLable),
                            new HBox(10, coordinatesLabel),
                            buttonBox
                    );
                    vBox.setPadding(new Insets(10));

                    itemsP.getChildren().addAll(item.getObjImage(200), vBox);

                    setGraphic(itemsP);
                }
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    microObjectsLabel.setText("Кількість мікрооб'єктів : " + root.getListObj().size());
                    macroObjectsLabel.setText("Кількість макрооб'єктів : " + Bases.listSystem.size());
                    long currentTime = System.nanoTime();
                    double elapsedTime = (currentTime - startTime) / 1_000_000_000.0;
                    timeLabel.setText("Час роботи програми : " + elapsedTime + " сек.");
                    microObjectsListView.setItems(null);
                    microObjectsListView.setItems(root.getListObj());

                    macroObjectsListView.setItems(null);
                    macroObjectsListView.setItems(Bases.listSystem);

                });
            }
        }, 0, 500);


        // Додавання елементів на сцену
        VBox rootW = new VBox();
        rootW.getChildren().addAll(titleLabel, microObjectsLabel, macroObjectsLabel, timeLabel,
                new Label("Мікрооб'єкти:"), microObjectsListView,
                new Label("Макрооб'єкти:"), macroObjectsListView);
        microObjectsListView.setPrefSize(450, 200);
        macroObjectsListView.setPrefSize(400, 250);
        Scene scene = new Scene(rootW);

        stage.setScene(scene);
        stage.setTitle("Статистика");
        stage.show();
    }

    public void reset( smok obj) {
        Stage stage = new Stage();
        stage.setTitle(obj.getName());

        TextField textFieldName = new TextField();
        textFieldName.setPromptText("Впишіть ім'я корабля");
        textFieldName.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField healthTextField = new TextField();
        healthTextField.setPromptText("Здоров'я");
        healthTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField damageTextField = new TextField();
        damageTextField.setPromptText("Урон");
        damageTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField xTextField = new TextField();
        xTextField.setPromptText("X");
        xTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField yTextField = new TextField();
        yTextField.setPromptText("Y");
        yTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        RadioButton alefRadioButton = new RadioButton("smok");
        alefRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton betaRadioButton = new RadioButton("grom");
        betaRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton gimelRadioButton = new RadioButton("relsa");
        gimelRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        // Встановлюємо радіокнопку "smok" вибраною за замовчуванням
        alefRadioButton.setSelected(true);

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        ToggleGroup toggleGroup = new ToggleGroup();
        alefRadioButton.setToggleGroup(toggleGroup);
        betaRadioButton.setToggleGroup(toggleGroup);
        gimelRadioButton.setToggleGroup(toggleGroup);
        box.getChildren().addAll(alefRadioButton, betaRadioButton, gimelRadioButton);

        Button setButton = new Button("Змінити");
        setButton.setOnAction(event -> {
            String name = textFieldName.getText().isEmpty() ? obj.getName() : textFieldName.getText();
            double health = healthTextField.getText().isEmpty() ? obj.getHealth() : Integer.parseInt(healthTextField.getText());
            double damage = damageTextField.getText().isEmpty() ? obj.getArmor() : Integer.parseInt(damageTextField.getText());
            int x = xTextField.getText().isEmpty() ? obj.getX() : Integer.parseInt(xTextField.getText());
            int y = yTextField.getText().isEmpty() ? obj.getY() : Integer.parseInt(yTextField.getText());
            root.removeObj(obj);
            if (alefRadioButton.isSelected()) {
                root.addObj(new smok(name, health, damage, x, y));
            }

            if (betaRadioButton.isSelected()) {
                root.addObj(new grom(name, health, damage, x, y));
            }

            if (gimelRadioButton.isSelected()) {
                root.addObj(new relsa(name, health, damage, x, y));
            }
            stage.close();
        });
        setButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px;");
        Button cancelButton = new Button("Скасувати");
        cancelButton.setOnAction(event -> stage.close());
        cancelButton.setStyle("-fx-background-color: #ddd; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px; -fx-border-color: gray; -fx-border-width: 1px;");

        HBox buttonBox = new HBox(setButton, cancelButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        HBox xy = new HBox(xTextField, yTextField);
        xy.setAlignment(Pos.CENTER);
        VBox centerBox = new VBox();
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(
                textFieldName,
                healthTextField,
                damageTextField,
                new Label("Координати:"),
                xy,
                new Label("Виберіть тип:"),
                box
        );
        centerBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(centerBox, buttonBox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #FFE4B5; -fx-border-color: black; -fx-border-radius: 5px;");

        Scene scene = new Scene(vbox, 350, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

// Задайте ширину стовпців з PromptText:
        textFieldName.setPrefColumnCount(10);
        healthTextField.setPrefColumnCount(10);
        damageTextField.setPrefColumnCount(10);
        xTextField.setPrefColumnCount(10);
        yTextField.setPrefColumnCount(10);


    }
}

