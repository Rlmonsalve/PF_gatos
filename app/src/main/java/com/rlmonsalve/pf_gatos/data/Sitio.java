package com.rlmonsalve.pf_gatos.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Robert on 20/11/2016.
 */

public class Sitio {
    private String name;
    private LatLng location;
    private ArrayList<Objeto> objetcList;
    private int imgId;
    private String info;

    public Sitio(String name, LatLng location, ArrayList<Objeto> objetcList, int id, String text) {
        this.name = name;
        this.location = location;
        this.objetcList = objetcList;
        this.imgId = id;
        this.info = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public ArrayList<Objeto> getObjetcList() {
        return objetcList;
    }

    public void setObjetcList(ArrayList<Objeto> objetcList) {
        this.objetcList = objetcList;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
