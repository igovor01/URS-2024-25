package com.example.urs_2024_25;

import java.time.LocalDateTime;

public class NfcRecord {
    private long nfcId;
    private LocalDateTime timestamp;

    public void setNfcId(long nfcId) {
        this.nfcId = nfcId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getNfcId() {
        return this.nfcId;
    }

    public LocalDateTime getTimeStamp() {
        return this.timestamp;
    }
}
