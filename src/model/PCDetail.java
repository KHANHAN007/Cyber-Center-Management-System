package model;

import enums.PCStatus;

public class PCDetail {
    private int pcId;
    private String pcCode;
    private String pcName;
    private int zoneId;
    private PCStatus status;
    private int configId;
    private String configCode;
    private String cpu;
    private int ram;
    private String gpu;
    private double pricePerHour;

    public PCDetail() {
    }

    public PCDetail(int pcId, String pcCode, String pcName, int zoneId, PCStatus status, int configId,
            String configCode, String cpu, int ram, String gpu, double pricePerHour) {
        this.pcId = pcId;
        this.pcCode = pcCode;
        this.pcName = pcName;
        this.zoneId = zoneId;
        this.status = status;
        this.configId = configId;
        this.configCode = configCode;
        this.cpu = cpu;
        this.ram = ram;
        this.gpu = gpu;
        this.pricePerHour = pricePerHour;
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

    public PCStatus getStatus() {
        return status;
    }

    public void setStatus(PCStatus status) {
        this.status = status;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}
