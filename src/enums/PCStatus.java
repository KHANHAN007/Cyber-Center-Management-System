package enums;

public enum PCStatus {
    AVAILABLE("AVAILABLE", "Available"),
    BOOKED("BOOKED", "Booked"),
    IN_USE("IN_USE", "In use"),
    MAINTENANCE("MAINTENANCE", "Under maintenance");

    private final String value;
    private final String description;

    PCStatus(String value, String description){
        this.value = value;
        this.description = description;
    }

    public String getValue(){
        return value;
    }

    public String getDescription(){
        return description;
    }

    public static PCStatus fromValue(String value){
        for(PCStatus status : PCStatus.values()){
            if(status.value.equalsIgnoreCase(value)){
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PCStatus: " + value);
    }

    public enum ItemStatus {
    }
}
