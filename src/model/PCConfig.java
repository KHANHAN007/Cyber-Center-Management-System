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
//Config 1:
//
//Config Code: (tự sinh)
//CPU: Intel Core i9-13900K
//RAM: 32
//GPU: NVIDIA RTX 4090
//Price per Hour: 150000
//Config 2:
//
//Config Code: (tự sinh)
//CPU: AMD Ryzen 9 7950X
//RAM: 64
//GPU: NVIDIA RTX 4080
//Price per Hour: 120000
//Config 3:
//
//Config Code: (tự sinh)
//CPU: Intel Core i7-13700K
//RAM: 16
//GPU: NVIDIA RTX 4070
//Price per Hour: 80000
//Config 4:
//
//Config Code: (tự sinh)
//CPU: Intel Core i5-13600K
//RAM: 16
//GPU: NVIDIA RTX 4060
//Price per Hour: 50000
//Config 5:
//
//Config Code: (tự sinh)
//CPU: AMD Ryzen 5 7600X
//RAM: 8
//GPU: NVIDIA RTX 3060
//Price per Hour: 30000
