package dao.interfaces;

import model.PCConfig;

import java.util.List;

public interface IPCConfigDAO {
    PCConfig findById(int configId);

    List<PCConfig> findAll();

    void create(PCConfig config);

    void update(PCConfig config);
}
