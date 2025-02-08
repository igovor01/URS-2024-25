package com.example.urs_2024_25.liststudents;

public class NfcRecord {
    private long nfcId;
    private String timestamp;

    public void setNfcId(long nfcId) {
        this.nfcId = nfcId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getNfcId() {
        return this.nfcId;
    }

    public String getTimeStamp() {
        return this.timestamp;
    }
}
