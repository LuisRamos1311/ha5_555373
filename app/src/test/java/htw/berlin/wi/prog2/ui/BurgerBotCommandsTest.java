package htw.berlin.wi.prog2.ui;

import htw.berlin.wi.prog2.data.Menu;
import htw.berlin.wi.prog2.data.MenuUtils;
import htw.berlin.wi.prog2.domain.Burger;
import htw.berlin.wi.prog2.domain.Ingredient;
import htw.berlin.wi.prog2.domain.BurgerBuilder;
import htw.berlin.wi.prog2.parsing.ExtendableInputParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BurgerBotCommandsTest {

  @Mock
  private ExtendableInputParser inputParser;

  @Mock
  private BurgerBuilder burgerBuilder;

  @Mock
  private InputEventPublisher inputEventPublisher;

  @InjectMocks
  private BurgerBotCommands classUnderTest;

  @Test
  @DisplayName("should pass user input to input parser")
  void shouldPassInputToParser() {
    String input = "Ich hätte gerne einen Ciabatta Burger mit Sauce";
    classUnderTest.order(input);
    verify(inputParser).idsAndCountFromInput(matches(input), anyMap());
  }

  @Test
  @DisplayName("should print the whole menu if the input was not understood")
  void printMenu() {
    doReturn(emptyMap()).when(inputParser).idsAndCountFromInput(anyString(), anyMap());
    var actual = classUnderTest.order("irgendwas");
    assertTrue(actual.startsWith("Entschuldigung, ich habe dich nicht verstanden. Wähle aus folgenden Zutaten: "
            + MenuUtils.focusOnNames(Menu.getInstance().getAllArticles())));
  }

  @Test
  @DisplayName("should return single burger order summary")
  void returnSingleBurgerOrderSummary() {
    Ingredient baseMock = mock(Ingredient.class);
    doReturn("Brot").when(baseMock).toString();
    Ingredient sauceMock = mock(Ingredient.class);
    doReturn("Sauce").when(sauceMock).toString();
    Burger burgerMock = mock(Burger.class);
    doReturn(List.of(baseMock.toString(), sauceMock.toString())).when(burgerMock).getIngredientNames();
    doReturn(new BigDecimal("3.70")).when(burgerMock).calculatePrice();
    doReturn(Map.of(1L, 1, 9L, 1)).when(inputParser).idsAndCountFromInput(anyString(), anyMap());
    doReturn(burgerBuilder).when(burgerBuilder).add(any());
    doReturn(burgerMock).when(burgerBuilder).build();

    String orderSummary = classUnderTest.order("any order");

    var expectedOrderSummary = "In Ordnung. Dein 1. Burger mit [Brot, Sauce] kostet 3.70 EUR.\nGib <confirm> ein, " +
                               "um die Bestellung abzuschliessen oder bestelle einen weitere Burger mit <order -t '...'>";
    assertEquals(expectedOrderSummary, orderSummary);
  }

  @Test
  @DisplayName("should return order confirmation")
  void returnOrderConfirmation() {
    Burger burgerMock1 = mock(Burger.class);
    Burger burgerMock2 = mock(Burger.class);
    doReturn(new BigDecimal("7.50")).when(burgerMock1).calculatePrice();
    doReturn(new BigDecimal("5.30")).when(burgerMock2).calculatePrice();
    classUnderTest.orderedBurgers.addAll(List.of(burgerMock1, burgerMock2));

    String actual = classUnderTest.confirm();

    var expectedMessage = "Vielen Dank für deine Bestellung. Du hast 2 Burger bestellt. Die Gesamtsumme beträgt 12.80 EUR.";
    assertEquals(expectedMessage, actual);
  }
}
