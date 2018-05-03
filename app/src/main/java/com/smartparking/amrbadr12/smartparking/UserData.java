package com.smartparking.amrbadr12.smartparking;

//this class represent a user in the firebase database.

public class UserData {
    private String userName;
    private int points;
    private int walletMoney;
    private int timesParked;
    private int trayLocation;
    private String lastParkDate;

    public UserData(String user) {
        this.userName = user;
        points = 0;
        walletMoney = 0;
        timesParked = 0;
        trayLocation = 0;
        lastParkDate = "";
    }

    public String getUserName() {
        return userName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getWalletMoney() {
        return walletMoney;
    }

    public void setWalletMoney(int walletMoney) {
        this.walletMoney = walletMoney;
    }

    public int getTimesParked() {
        return timesParked;
    }

    public void setTimesParked(int timesParked) {
        this.timesParked = timesParked;
    }

    public int getTrayLocation() {
        return trayLocation;
    }

    public void setTrayLocation(int trayLocation) {
        this.trayLocation = trayLocation;
    }

    public String getLastParkDate() {
        return lastParkDate;
    }

    public void setLastParkDate(String lastParkDate) {
        this.lastParkDate = lastParkDate;
    }
}
