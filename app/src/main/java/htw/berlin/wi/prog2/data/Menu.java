package htw.berlin.wi.prog2.data;

import htw.berlin.wi.prog2.domain.Ingredient;
import htw.berlin.wi.prog2.domain.IngredientBuilder;

import java.util.*;

import static htw.berlin.wi.prog2.domain.Ingredient.Category.*;

public class Menu {
    private Menu() {}
    private static Menu theInstance = null;

    public static Menu getInstance() {
        if(theInstance == null) theInstance = new Menu();
        return theInstance;
    }
    private IngredientBuilder builder = new IngredientBuilder();

    private Map<Long, Ingredient> articles = Map.of(
            1L, builder.setName("Vollkorn").setPrice("0.60").setCals(120).build(BASE),
            2L, builder.setName("Ciabatta").setPrice("0.70").setCals(100).build(BASE),
            3L, builder.setName("Rindfleisch").setPrice("0.90").setCals(90).build(PROTEIN),
            4L, builder.setName("Beyond-Meat").setPrice("0.90").setCals(80).build(PROTEIN),
            5L, builder.setName("Tomatenscheiben").setPrice("0.40").setCals(20).build(TOPPING),
            6L, builder.setName("Gurken").setPrice("0.60").setCals(30).build(TOPPING),
            7L, builder.setName("Cheddar-Käse").setPrice("0.60").setCals(40).build(TOPPING),
            8L, builder.setName("Eisbergsalat").setPrice("0.30").setCals(20).build(TOPPING),
            9L, builder.setName("Mayo").setPrice("0.30").setCals(40).build(SAUCE),
            10L, builder.setName("Ketchup").setPrice("0.30").setCals(40).build(SAUCE));

    public Map<Long, Ingredient> getAllArticles() { return articles; }
}
