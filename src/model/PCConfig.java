package model;

public class PCConfig {
    private int configId;
    private String configCode;
    private String cpu;
    private int ram;
    private String gpu;
    private double pricePerHour;

    public PCConfig() {
    }

    public PCConfig(String configCode, String cpu, int ram, String gpu, double pricePerHour) {
        this.configCode = configCode;
        this.cpu = cpu;
        this.ram = ram;
        this.gpu = gpu;
        this.pricePerHour = pricePerHour;
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

    @Override
    public String toString() {
        return "PCConfig{" +
                "configId=" + configId +
                ", configCode='" + configCode + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram=" + ram +
                ", gpu='" + gpu + '\'' +
                ", pricePerHour=" + pricePerHour +
                '}';
    }
}



































