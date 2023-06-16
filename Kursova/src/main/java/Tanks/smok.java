package Tanks;


import java.util.*;

import com.example.kursova.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;



public class smok extends StackPane implements Cloneable {


    private static final List<String> names = new ArrayList<>(Arrays.asList("Quadrant","Regulus","Sirius","Taurus","Umbra", "Orion", "Mars", "Jupiter",
            "Saturn", "Neptune", "Uranus", "Mercury", "Venus", "Earth", "Pluto", "Europa", "Titan", "Callisto", "Io", "Ganymede",
            "Luna", "Triton", "Rhea", "Dione", "Ceres", "Vesta", "Hygiea", "Juno", "Pallas", "Eris", "Haumea", "Makemake", "Charon", "Oberon",
            "Miranda", "Ariel", "Umbriel", "Tethys", "Enceladus", "Hyperion", "Phoebe", "Janus", "Epimetheus", "Telesto", "Calypso", "Pandora",
            "Prometheus", "Atlas", "Pan", "Daphnis", "Anthe", "Metis"));

    //Константи
    private final int MAX_HEALTH = 100; // Максимальне hp

    // Властивості об'єкта smok
    private Team team;//Фракція
    private boolean active = false;//Активність
    private final String name; // Ім'я об'єкта
    private double health = MAX_HEALTH;
    private int MSpead = 10;

    // Координати
    private int x;
    private int y;

    private double atackSpead = 0.9;
    private double armor;
    private final int crit = 40;
    private double damage = 60;
    private int dodge = 20;

    private int secondsAlive = 0;

    public static Image smokImage = new Image(Main.class.getResource("smok.png").toString(), 100, 100, false, false);
    ImageView imageView = new ImageView(smokImage); // Текстура
    private Text text;
    private Rectangle hitbox; // Хітбокс
    private ProgressBar healthBar = new ProgressBar();

    static {
         System.out.println("Я статичний блок, я є");
    }

    {
        System.out.println("Я нестатичний блок, я є");
    }

    // Конструктори
    public smok(String name, double health, double armor, int x, int y) {
        this.name = name;
        this.health = health;
        this.x = x;
        this.y = y;
        this.armor = armor;

        createText();
        createHealthBar();
        createHitbox();
        createImageView();
        setPrefSize(smokImage.getWidth(), smokImage.getHeight() + 20);
        getChildren().addAll(hitbox, imageView, healthBar, text);

        CustomLog.log("Конструктор " + "Smok" + " було викликано");
        CustomLog.log("Було створено об'єкт " + this.name + " за координатами: " + x + " " + y);

        setOnMouseClicked(event -> {
            active = !active;
            if (active) {
                hitbox.setStroke(Color.YELLOW);
                System.out.println("Об'єкт " + smok.this + " було активовано");
            } else {
                hitbox.setStroke(Color.TRANSPARENT);
                System.out.println("Об'єкт " + smok.this + " було деактивовано");
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                makeDecision(random.nextInt(300));
            }
        }, 0, 100);


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (smok object : Main.root.getListObj()) {
                    if (!(object.getCommand() == getCommand())) {
                    if (getBoundsInParent().intersects(object.getBoundsInParent())) {
                        Platform.runLater(() -> {
                        attack(object);
                        });}
                    }
                }
            }
        }, 0L, (long) (1000 * atackSpead));
    }

    public smok() {
        this(getRandomName(), 100, 20, getRandomX(),getRandomY());
    }

    public smok(@NotNull smok original) {
        this(original.name, original.health , original.armor, original.x , original.y);}

    // Гетери

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getHealth() {
        return health;
    }

    public int getMSpead() {
        return MSpead;
    }

    public double getArmor() {
        return armor;
    }

    public double getDamage() {
        return damage;
    }

    public Team getCommand() {
        return team;
    }

    public boolean isActive() {
        return active;
    }

    //Cетери

    public void setX(int x) {
        this.x = x;
        this.setLayoutX(x);
    }

    public void setY(int y) {
        this.y = y;
        this.setLayoutY(y);
    }

    public void setHealth(double health) {
        this.health = health;
        healthBar.setProgress( health /MAX_HEALTH );
    }

    public void setFraction(Team team) {
        this.team = team;
        hitbox.setFill(team.getFcolor());
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //Реалізація обєктів

    private void createText() {
        text = new Text(name);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        StackPane.setAlignment(text, Pos.BOTTOM_CENTER);
    }

    private void createHealthBar() {
        healthBar = new ProgressBar();
        healthBar.setPrefWidth(smokImage.getWidth());
        healthBar.setProgress( health /MAX_HEALTH );
        healthBar.setStyle("-fx-accent: #41F514;");
        StackPane.setAlignment(healthBar, Pos.BOTTOM_CENTER);
    }

    private void createHitbox() {
        hitbox = new Rectangle(smokImage.getWidth(), smokImage.getHeight() + 20);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.TRANSPARENT);
        hitbox.setStrokeWidth(2);
    }

    private void createImageView() {
        imageView = new ImageView(smokImage);
        StackPane.setAlignment(imageView, Pos.TOP_CENTER);
    }

    // Методи

    public void dead() {
        Main.root.removeObj(this);
    }

    @Override
    public smok clone() {
        try {
            smok clone;
            clone = (smok) super.clone();
            clone.imageView = new ImageView(imageView.getImage());
            clone.text = new Text(text.getText());
            clone.hitbox = new Rectangle(hitbox.getWidth(), hitbox.getHeight());
            clone.healthBar = new ProgressBar(healthBar.getProgress());

            // Копіюємо атрибути графічних об'єктів
            clone.imageView.setX(imageView.getX());
            clone.imageView.setY(imageView.getY());
            clone.imageView.setFitWidth(imageView.getFitWidth());
            clone.imageView.setFitHeight(imageView.getFitHeight());

            clone.text.setX(text.getX());
            clone.text.setY(text.getY());

            clone.hitbox.setX(hitbox.getX());
            clone.hitbox.setY(hitbox.getY());

            clone.healthBar.setLayoutX(healthBar.getLayoutX());
            clone.healthBar.setLayoutY(healthBar.getLayoutY());

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeDecision(int event) {

        if (!isActive()) {
            if (event >= 0 && event <= 75) { // Атакувати найближчий корабель
                moveTo(getEnemyObj());
            } else if (event > 75 && event <= 175) { // Захоплюємо іншу систему
                moveTo(getOtherCommandSystem());
            }
        }
    }

    public smok getEnemyObj() {
        double shortestDistance = Double.MAX_VALUE;
        smok nearestMicroObject = null;

        List<smok> listObj = Main.root.getListObj();
        Iterator<smok> iterator = listObj.iterator();

        while (iterator.hasNext()) {
            smok microObject = iterator.next();
            if (microObject != this && this.team != microObject.team) {
                double distance = calculateDistance(microObject);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestMicroObject = microObject;
                }
            }
        }

        return nearestMicroObject;
    }


    private double calculateDistance(Node obj) {
        if (obj!= null){
        double deltaX = obj.getLayoutX() - this.x;
        double deltaY = obj.getLayoutY() - this.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }else return 0.0;
    }

    // Оголосити Timeline як поле класу
    private Timeline timeline;


    public void moveTo(Node targetNode) {
        if (isAnimationRunning() || targetNode == null || active) {
            return;
        }

        final double[] startPosition = getStartPosition();

        double[] targetPosition = getTargetPosition(targetNode);
        if (hasReachedTarget(startPosition, targetPosition)) {
            return;
        }

        double[] distanceToTarget = calculateDistanceToTarget(startPosition, targetPosition);
        double totalDistance = calculateTotalDistance(distanceToTarget);
        int numSteps = calculateNumSteps(totalDistance);

        double[] stepDistance = calculateStepDistance(distanceToTarget, numSteps);

        startMovementAnimation(startPosition, stepDistance, numSteps, targetPosition);
        startRotationAnimation(distanceToTarget);
    }

    private boolean isAnimationRunning() {
        return timeline != null && timeline.getStatus() == Animation.Status.RUNNING;
    }

    private double[] getStartPosition() {
        return new double[]{this.getLayoutX(), this.getLayoutY()};
    }

    private double[] getTargetPosition(Node targetNode) {
        return new double[]{
                targetNode.getLayoutX() + targetNode.getBoundsInLocal().getWidth() / 2,
                targetNode.getLayoutY() + targetNode.getBoundsInLocal().getHeight() / 2
        };
    }

    private boolean hasReachedTarget(double[] startPosition, double[] targetPosition) {
        return startPosition[0] == targetPosition[0] && startPosition[1] == targetPosition[1];
    }

    private double[] calculateDistanceToTarget(double[] startPosition, double[] targetPosition) {
        return new double[]{targetPosition[0] - startPosition[0], targetPosition[1] - startPosition[1]};
    }

    private double calculateTotalDistance(double[] distanceToTarget) {
        return Math.sqrt(Math.pow(distanceToTarget[0], 2) + Math.pow(distanceToTarget[1], 2));
    }

    private int calculateNumSteps(double totalDistance) {
        return (int) (totalDistance / MSpead);
    }

    private double[] calculateStepDistance(double[] distanceToTarget, int numSteps) {
        return new double[]{distanceToTarget[0] / numSteps, distanceToTarget[1] / numSteps};
    }

    private void startMovementAnimation(double[] startPosition, double[] stepDistance, int numSteps, double[] targetPosition) {
        timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            startPosition[0] += stepDistance[0];
            startPosition[1] += stepDistance[1];
            setX((int) startPosition[0]);
            setY((int) startPosition[1]);
        }));
        timeline.setCycleCount(numSteps);
        timeline.setOnFinished(event -> {
            setX((int) targetPosition[0]);
            setY((int) targetPosition[1]);
        });
        timeline.play();
    }

    private void startRotationAnimation(double[] distanceToTarget) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), this.getImageView());
        rotateTransition.setToAngle(Math.toDegrees(Math.atan2(distanceToTarget[1], distanceToTarget[0])) + 90);
        rotateTransition.play();
    }


    @Override
    public String toString() {
        return "Tank{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", x=" + x +
                ", y=" + y +
                ", damage=" + armor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        smok аlef = (smok) o;
        return health == аlef.health && MSpead == аlef.MSpead && Double.compare(аlef.armor, armor) == 0 && Objects.equals(name, аlef.name);
    }

    public ImageView getSnapshotParameters() {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT); // встановити прозорий фон
        WritableImage image = this.snapshot(snapshotParameters, null);
        return new ImageView(image);
    }

    public static String getRandomName() {
        java.util.Random random = new java.util.Random();//Визиваємо метод Random
        int index = random.nextInt(names.size());//Отримуємо рандомне число він 0 до кількості імен в списку
        return names.get(index);
    }

    public static int getRandomX() {
        java.util.Random random = new java.util.Random();//Визиваємо метод Random
        return random.nextInt((int) Main.root.getContent().getPrefWidth());
    }

    public static int getRandomY() {
        java.util.Random random = new java.util.Random();//Визиваємо метод Random
        return random.nextInt((int) Main.root.getContent().getPrefHeight());
    }

    public Bases getOtherCommandSystem() {
        Bases bases = null;

        Iterator<Bases> iterator = Bases.listSystem.iterator();

        while (iterator.hasNext()) {
            Bases sys = iterator.next();
            if (sys.getFraction() == null || !sys.getFraction().equals(this.team)) {
                if (calculateDistance(sys) > calculateDistance(bases)) {
                    bases = sys;
                }
            }
        }

        return bases;
    }


    public Bases getNearCommandSystem(){
        Bases bases = null;
        for(Bases sys : Bases.listSystem){
            if(sys.getFraction() != null || sys.getFraction().equals(this.team)){
                if(calculateDistance(sys)>calculateDistance(bases)){
                    bases = sys;
                }
            }
        }
        return bases;
    }

    public void attack(smok obj) {
        double attackDamage = damage;
        if (obj != null ) {
            if (obj.getCommand() == null || !obj.getCommand().equals(this.team)) {
                // Розрахунок пошкодження атаки
//                double attackDamage = damage;

//                // Перевірка на критичний удар
//                if (Math.random() * 100 <= crit) {
//                    attackDamage *= 2; // Подвоєння пошкодження у випадку критичного удару
//                    System.out.println("Критичний удар!");
//                }

                // Зменшення пошкодження в залежності від броні цілі
                attackDamage -= obj.armor;

//                // Перевірка на ухилення
//                if (Math.random() * 100 <= obj.dodge) {
//                    System.out.println("Ціль ухилилася від атаки!");
//                    return; // Вихід з функції, якщо ціль ухилилася
//                }

                // Застосування пошкодження до цілі
                obj.setHealth(health - attackDamage);

                // Перевірка на смерть цілі
                if (obj.health <= 0) {
                    System.out.println("Ціль " + obj.name + " була убита!");
                    obj.dead();
                } else {
                    System.out.println("Ціль " + obj.name + " зазнала пошкоджень: " + attackDamage + " HP");
                }
            }
        }
    }

}
