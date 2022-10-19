package tradeGame;

import java.util.HashMap;
import java.util.Map;

public class Buyer {

    String[] neededCategories = { "food", "basicConsumerGoods" };

    Map<String, Float> getOptimalPurchases() {
        return null;
    }

    String[] getFoodItems() {
        String[] foods = { "basicFoodCrop", "processedFood", "extraNutritiousFood" };
        return foods;
    }

    Map<String, Float> getFoodItemPrices(String[] foodItems) {
        Map<String, Float> prices = new HashMap<>();
        prices.put("basicFoodCrop", 10.0f);
        prices.put("processedFood", 12.0f);
        prices.put("extraNutritiousFood", 15.0f);
        return prices;
    }

    Map<String, Float> getFoodItemUtility(String[] foodItems) {
        Map<String, Float> prices = new HashMap<>();
        prices.put("basicFoodCrop", 1.0f);
        prices.put("processedFood", 1.5f);
        prices.put("extraNutritiousFood", 2.5f);
        return prices;
    }
}
