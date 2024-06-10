package htw.berlin.wi.prog2.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BurgerBuilderTest {

    private final BurgerBuilder builder = new BurgerBuilder();
    private final IngredientBuilder ingBuilder = new IngredientBuilder();

    private final Ingredient sauce = ingBuilder.setName("Mayo").setPrice("0.01").setCals(2000).build(Ingredient.Category.SAUCE);
    private final Ingredient base = ingBuilder.setName("Brot").setPrice("0.02").setCals(1000).build(Ingredient.Category.BASE);

    @Test
    @DisplayName("can build a precomputed burger with two ingredients")
    void buildABurger() {
        builder.setCreationStyle(BurgerBuilder.CreationStyle.PRECOMPUTED);
        Burger burger = builder.add(base).add(sauce).build();

        assertEquals(List.of("Brot", "Mayo"), burger.getIngredientNames());
        assertEquals(new BigDecimal("0.03"), burger.calculatePrice());
        assertEquals(3000, burger.calculateCalories());
    }

    @Test
    @DisplayName("can build two precomputed burgers after another without mixing things up")
    void buildTwoBurgers() {
        builder.setCreationStyle(BurgerBuilder.CreationStyle.PRECOMPUTED);
        Burger burger1 = builder.add(base).add(sauce).build();
        Burger burger2 = builder.add(base).add(sauce).add(sauce).build();

        assertEquals(List.of("Brot", "Mayo"), burger1.getIngredientNames());
        assertEquals(List.of("Brot", "Mayo", "Mayo"), burger2.getIngredientNames());
    }

    @Test
    @DisplayName("can build a dynamically computed burger with two ingredients")
    void buildABurgerDynamically() {
        builder.setCreationStyle(BurgerBuilder.CreationStyle.DYNAMICALLY_COMPUTED);
        Burger burger = builder.add(base).add(sauce).build();

        assertEquals(List.of("Brot", "Mayo"), burger.getIngredientNames());
        assertEquals(new BigDecimal("0.03"), burger.calculatePrice());
        assertEquals(3000, burger.calculateCalories());
    }

    @Test
    @DisplayName("can build two dynamically computed burgers after another without mixing things up")
    void buildTwoBurgersDynamically() {
        builder.setCreationStyle(BurgerBuilder.CreationStyle.DYNAMICALLY_COMPUTED);
        Burger burger1 = builder.add(base).add(sauce).build();
        Burger burger2 = builder.add(base).add(sauce).add(sauce).build();

        assertEquals(List.of("Brot", "Mayo"), burger1.getIngredientNames());
        assertEquals(List.of("Brot", "Mayo", "Mayo"), burger2.getIngredientNames());
    }

    @Test
    @DisplayName("a burger should have at least two ingredients")
    void checkNumberOfIngredients() {
        assertThrows(IllegalBurgerException.class, builder::build);
        assertThrows(IllegalBurgerException.class, () -> builder.add(base).build());
    }
}
