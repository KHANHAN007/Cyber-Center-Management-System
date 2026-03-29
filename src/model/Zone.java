package model;

public class Zone {
    private int zoneId;
    private String zoneCode;
    private String zoneName;
    private double priceMultiplier;

    public Zone(){}

    public Zone(String zoneCode, String zoneName, double priceMultiplier) {
        this.zoneCode = zoneCode;
        this.zoneName = zoneName;
        this.priceMultiplier = priceMultiplier;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "zoneId=" + zoneId +
                ", zoneCode='" + zoneCode + '\'' +
                ", zoneName='" + zoneName + '\'' +
                ", priceMultiplier=" + priceMultiplier +
                '}';
    }
}
