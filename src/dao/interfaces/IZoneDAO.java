package dao.interfaces;

import model.Zone;

import java.util.List;

public interface IZoneDAO {
    Zone findById(int zoneId);
    List<Zone> findAll();
    void create(Zone zone);
    void update(Zone zone);
}
