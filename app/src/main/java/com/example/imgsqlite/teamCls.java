package com.example.imgsqlite;

public class teamCls {
    private int ID;
    private String NAME;
    private byte[] LOGO;
    private String STADIUM;
    private String CAPACITY;

    public teamCls(int ID, String NAME, byte[] LOGO, String STADIUM, String CAPACITY) {
        this.ID = ID;
        this.NAME = NAME;
        this.LOGO = LOGO;
        this.STADIUM = STADIUM;
        this.CAPACITY = CAPACITY;
    }

    public int getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public byte[] getLOGO() {
        return LOGO;
    }

    public String getSTADIUM() {
        return STADIUM;
    }

    public String getCAPACITY() {
        return CAPACITY;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setLOGO(byte[] LOGO) {
        this.LOGO = LOGO;
    }

    public void setSTADIUM(String STADIUM) {
        this.STADIUM = STADIUM;
    }

    public void setCAPACITY(String CAPACITY) {
        this.CAPACITY = CAPACITY;
    }
}
