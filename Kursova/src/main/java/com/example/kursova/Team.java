package com.example.kursova;

import Tanks.smok;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.*;

public class Team {
    private final Color fcolor;
    private static final ObservableList<Team> LIST_TEAM = FXCollections.observableArrayList();
    private final ObservableList<Bases> listSystem;
    private final ObservableList<smok> listObj;
    private final String name;

    public Team(String name, Color fcolor) {
        this.name = name;
        this.fcolor = fcolor;
        this.listSystem = FXCollections.observableArrayList();
        this.listObj = FXCollections.observableArrayList();

        LIST_TEAM.add(this);

        listSystem.addListener((ListChangeListener<Bases>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Bases system : change.getAddedSubList()) {
                        system.setFraction(this);
                    }
                }
                if (change.wasRemoved()) {
                    for (Bases system : change.getRemoved()) {
                        system.removeFraction();
                    }
                }
            }
        });
        // Запустити таймер для виклику generateResources кожну секунду
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Bases> copyList = new ArrayList<>(listSystem);
                for (Bases system : copyList) {
                    system.generateResources();
                }
            }
        }, 0, 2000); // Викликати кожну секунду (1000 мс)
    }

    public void addObj(smok obj) {
        listObj.add(obj);
        obj.setFraction(this);
    }

    public void addSystem(Bases bases)  {
        listSystem.add(bases);
    }

    public void removeSystem(Bases bases) {
        listSystem.remove(bases);
    }


    public String getName() {
        return name;
    }

    public Color getFcolor() {
        return fcolor;
    }

    public ObservableList<smok> getListObj() {
        return listObj;
    }

    public static ObservableList<Team> getListFraction(){return LIST_TEAM;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(getFcolor(), team.getFcolor()) && Objects.equals(listSystem, team.listSystem) && Objects.equals(listObj, team.listObj) && Objects.equals(getName(), team.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFcolor(), listSystem, listObj, getName());
    }

    public static Team getFractionByName(String name) {
        for (Team team : LIST_TEAM) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }
}
