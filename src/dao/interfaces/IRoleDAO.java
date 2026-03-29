package dao.interfaces;

import model.Role;

import java.util.List;

public interface IRoleDAO {
    Role findById(int roleId);
    Role findByCode(String roleCode);
    List<Role> findAll();
    void create(Role role);
}
