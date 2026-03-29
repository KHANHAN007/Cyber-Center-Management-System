package dao.interfaces;

import model.VoucherCode;
import java.util.List;

public interface IVoucherCodeDAO {
    VoucherCode findById(int voucherId);

    VoucherCode findByCode(String code);

    List<VoucherCode> findAll();

    List<VoucherCode> findAllActive();

    void create(VoucherCode voucherCode);

    void update(VoucherCode voucherCode);

    void delete(int voucherId);

    void incrementUsageCount(int voucherId);
}
