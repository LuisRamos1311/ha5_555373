package htw.berlin.wi.prog2.ui;

import htw.berlin.wi.prog2.domain.BurgerBuilder;
import htw.berlin.wi.prog2.parsing.CountingInputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BurgerBotCommandsIntegrationTest {

  private BurgerBotCommands classUnderTest;

  @BeforeEach
  void setup() {
    classUnderTest = new BurgerBotCommands(
        new CountingInputParser(),
        new BurgerBuilder(),
        new InputEventPublisher());
  }

  @Test
  @DisplayName("should understand a simple order and list the burger's individual ingredients and it's price")
  void testSimpleOrder() {
    String simpleOrder = "Ich hätte gerne einen Ciabatta Burger mit Gurken, Eisbergsalat, Beyond-Meat und Ketchup";

    var actual = classUnderTest.order(simpleOrder);

    var expectedMessage = "In Ordnung. Dein 1. Burger mit [Beyond-Meat, Ciabatta, Eisbergsalat, Gurken, Ketchup] kostet 2.80 EUR.";
    assertTrue(actual.startsWith(expectedMessage));
  }
}
