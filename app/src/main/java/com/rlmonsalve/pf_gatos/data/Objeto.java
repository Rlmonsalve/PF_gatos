package com.rlmonsalve.pf_gatos.data;

/**
 * Created by Robert on 20/11/2016.
 */

public class Objeto {

    private String name;
    private int iconId;
    private int id;
    private int stock;
    private boolean unlocked;

    public Objeto(String name, int iconId, int id, int stock, boolean unlocked) {
        this.name = name;
        this.iconId = iconId;
        this.id = id;
        this.stock = stock;
        this.unlocked = unlocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int category) {
        this.id = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
