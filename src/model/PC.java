package model;

import enums.PCStatus;

public class PC {
    private int pcId;
    private String pcCode;
    private String pcName;
    private int zoneId;
    private int configId;
    private PCStatus status;
    private boolean isDeleted;
    private Zone zone;
    private PCConfig config;

    public PC() {
    }

    public PC(String pcCode, String pcName, int zoneId, int configId) {
        this.pcCode = pcCode;
        this.pcName = pcName;
        this.zoneId = zoneId;
        this.configId = configId;
        this.status = PCStatus.AVAILABLE;
        this.isDeleted = false;
    }
    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getConfigCode() {
        return config != null ? config.getConfigCode() : null;
    }

    public PCStatus getStatus() {
        return status;
    }

    public void setStatus(PCStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public PCConfig getConfig() {
        return config;
    }

    public void setConfig(PCConfig config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "PC{" +
                "pcId=" + pcId +
                ", pcCode='" + pcCode + '\'' +
                ", pcName='" + pcName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
