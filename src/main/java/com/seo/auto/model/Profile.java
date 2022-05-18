package main.java.com.seo.auto.model;

public class Profile {
    private String name;
    private String contractId;
    private String note;
    private Credential bczCredentials;
    private Credential ellocoCredentials;
    private Credential flipboardCredentials;
    private Credential getPocketCredentials;
    private Credential instapaperCredentials;
    private Credential scoopitCredentials;
    private Credential tumblrCredentials;
    private Credential vingleCredentials;
    private Credential wpCredentials;
    private Credential folkdCredentials;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile(String name) {
        this.name = name;
    }

    public Profile() {
    }

    public Credential getBczCredentials() {
        return bczCredentials;
    }

    public void setBczCredentials(Credential bczCredentials) {
        this.bczCredentials = bczCredentials;
    }

    public Credential getEllocoCredentials() {
        return ellocoCredentials;
    }

    public void setEllocoCredentials(Credential ellocoCredentials) {
        this.ellocoCredentials = ellocoCredentials;
    }

    public Credential getFlipboardCredentials() {
        return flipboardCredentials;
    }

    public void setFlipboardCredentials(Credential flipboardCredentials) {
        this.flipboardCredentials = flipboardCredentials;
    }

    public Credential getGetPocketCredentials() {
        return getPocketCredentials;
    }

    public void setGetPocketCredentials(Credential getPocketCredentials) {
        this.getPocketCredentials = getPocketCredentials;
    }

    public Credential getInstapaperCredentials() {
        return instapaperCredentials;
    }

    public void setInstapaperCredentials(Credential instapaperCredentials) {
        this.instapaperCredentials = instapaperCredentials;
    }

    public Credential getScoopitCredentials() {
        return scoopitCredentials;
    }

    public void setScoopitCredentials(Credential scoopitCredentials) {
        this.scoopitCredentials = scoopitCredentials;
    }

    public Credential getTumblrCredentials() {
        return tumblrCredentials;
    }

    public void setTumblrCredentials(Credential tumblrCredentials) {
        this.tumblrCredentials = tumblrCredentials;
    }

    public Credential getVingleCredentials() {
        return vingleCredentials;
    }

    public void setVingleCredentials(Credential vingleCredentials) {
        this.vingleCredentials = vingleCredentials;
    }

    public Credential getWpCredentials() {
        return wpCredentials;
    }

    public void setWpCredentials(Credential wpCredentials) {
        this.wpCredentials = wpCredentials;
    }

    public Credential getFolkdCredentials() {
        return folkdCredentials;
    }

    public void setFolkdCredentials(Credential folkdCredentials) {
        this.folkdCredentials = folkdCredentials;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
