package dao.interfaces;

import model.PC;
import model.PCDetail;

import java.util.List;

public interface IPCDAO {
    PC findById(int pcId);

    PCDetail findDetailWithConfigById(int pcId);

    List<PC> findAll();

    List<PC> findByZone(int zoneId);

    List<PC> findAvailable();

    void create(PC pc);

    void update(PC pc);

    void updateStatus(int pcId, String status);

    void softDelete(int pcId);
}
