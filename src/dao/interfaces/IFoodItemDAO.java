package dao.interfaces;

import model.FoodItem;

import java.util.List;

public interface IFoodItemDAO {
    FoodItem findById(int itemId);

    List<FoodItem> findByCategory(int categoryId);

    List<FoodItem> findAll();

    List<FoodItem> findAllWithCategoryAndPrices();

    List<FoodItem> findAllWithCategoryAndPrices(boolean showDeleted);

    void create(FoodItem foodItem);

    void update(FoodItem foodItem);
}
