package com.rlmonsalve.pf_gatos.data;

/**
 * Created by Robert on 20/11/2016.
 */

public class Gato {
    private String name;
    private int iconId;
    private int modelId;
    private boolean favorite;
    private boolean unlocked;
    private Objeto likes;
    private Objeto dislikes;

    public Gato(String name, int icon, int model, boolean fav, boolean unlock, Objeto lk, Objeto dlk){
        this.name = name;
        this.iconId = icon;
        this.modelId = model;
        this.favorite = fav;
        this.unlocked = unlock;
        this.likes = lk;
        this.dislikes=dlk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public Objeto getLikes() {
        return likes;
    }

    public void setLikes(Objeto likes) {
        this.likes = likes;
    }

    public Objeto getDislikes() {
        return dislikes;
    }

    public void setDislikes(Objeto dislikes) {
        this.dislikes = dislikes;
    }
}
