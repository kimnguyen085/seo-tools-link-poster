package main.java.com.seo.auto.model;

public class GoogleCredentialsConfig {
    private String spreadsheetId;
    private String range;

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public GoogleCredentialsConfig(String spreadsheetId, String range) {
        this.spreadsheetId = spreadsheetId;
        this.range = range;
    }

    public GoogleCredentialsConfig() {
    }
}
