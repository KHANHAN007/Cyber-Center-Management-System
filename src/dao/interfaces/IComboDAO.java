package dao.interfaces;

import model.Combo;

import java.util.List;

public interface IComboDAO {
    Combo findById(int comboId);

    List<Combo> findAll();

    List<Combo> findAll(boolean showDeleted);

    void create(Combo combo);

    void update(Combo combo);
}
