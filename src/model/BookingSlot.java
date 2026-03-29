package model;

import java.time.LocalDate;
import java.time.LocalTime;


public class BookingSlot {
    private int slotId;
    private String slotCode;
    private LocalTime slotTime;
    private LocalDate slotDate;

    public BookingSlot() {}

    public BookingSlot(String slotCode, LocalTime slotTime, LocalDate slotDate) {
        this.slotCode = slotCode;
        this.slotTime = slotTime;
        this.slotDate = slotDate;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }

    public LocalTime getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(LocalTime slotTime) {
        this.slotTime = slotTime;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    @Override
    public String toString() {
        return "BookingSlot{" +
                "slotId=" + slotId +
                ", slotCode='" + slotCode + '\'' +
                ", slotTime=" + slotTime +
                ", slotDate=" + slotDate +
                '}';
    }
}
