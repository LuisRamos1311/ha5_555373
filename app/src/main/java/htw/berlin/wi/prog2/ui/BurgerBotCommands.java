package htw.berlin.wi.prog2.ui;

import htw.berlin.wi.prog2.data.Menu;
import htw.berlin.wi.prog2.data.MenuUtils;
import htw.berlin.wi.prog2.domain.Burger;
import htw.berlin.wi.prog2.domain.BurgerBuilder;
import htw.berlin.wi.prog2.domain.Ingredient;
import htw.berlin.wi.prog2.parsing.ExtendableInputParser;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO (2) Annotations @ShellComponent, @ShellMethod, und @ShellOption verwenden
@ShellComponent
public class BurgerBotCommands implements Observable {

  final List<Burger> orderedBurgers;

  private final ExtendableInputParser parser;
  private final BurgerBuilder builder;
  private final InputEventPublisher inputEventPublisher;
  private final Menu menu;
  //added to the constructor
  private final UserInputWrapper input;

  private List<Observer> observers = new ArrayList<>();




  public BurgerBotCommands(ExtendableInputParser parser, BurgerBuilder builder, InputEventPublisher inputEventPublisher) {
    this.inputEventPublisher = inputEventPublisher;
    this.orderedBurgers = new ArrayList<>();
    this.parser = parser;
    this.builder = builder;
    this.menu = Menu.getInstance();
    this.input = new UserInputWrapper(System.in, System.out);
  }
  //edited ShellMethod
  @ShellMethod(value = "Create a new burger order", key = "order")
  public String order(@ShellOption(help = "The input string") String inputLine) {

    Map<Long, Ingredient> articles = menu.getAllArticles();

    //goes through the list of ingredients created in ha2
    //if the ingredient list is empty returns "sorry text" + menu list
    //else, launch the burger build method with the ingredients
    while (!(inputLine.equals("Bestellung abschliessen") || inputLine.equals("Auf Wiedersehen"))) {
      Map<String, Long> keywordsToIds = MenuUtils.focusOnNameAndInvert(articles);
      Map<Long, Integer> ingredientsCount = parser.idsAndCountFromInput(inputLine, keywordsToIds);
      List<Ingredient> ingredients = MenuUtils.ingredientsFromIdAndCount(ingredientsCount, articles);
      if(ingredients.isEmpty()) {
        inputEventPublisher.publishInputEvent(false);
        return "Entschuldigung, ich habe dich nicht verstanden. Wähle aus folgenden Zutaten: "
                + MenuUtils.focusOnNames(articles);
      } else {
        inputEventPublisher.publishInputEvent(true);
        for (Ingredient ing : ingredients) builder.add(ing);
        Burger burger = builder.build();
        //List of ingredients from the burger build are sorted,
        //and with the map method each ingredient is returned as a String and returns it as a list
        List<String> ingrSorted = burger.getIngredientNames().stream().sorted().map(ingredient -> ingredient.toString()).toList();
        //added orderedBurgers and return
        orderedBurgers.add(burger);
        notifyObservers();
        return "In Ordnung. Dein 1. Burger mit " + ingrSorted +
                " kostet " + burger.calculatePrice() + " EUR.\nGib <confirm> ein, um die Bestellung abzuschliessen oder bestelle einen weitere Burger mit <order -t '...'>";
      }
    }

    return "In Ordnung ...";
  }

  //edited ShellMethod
  //confirm method means order is closed
  //It returns how many burgers and the price in type double from a map
  @ShellMethod(value = "Confirm the order and see the total price", key = "confirm")
  public String confirm() {
    //added Stream
    return "Vielen Dank für deine Bestellung. Du hast " + orderedBurgers.size() +
            " Burger bestellt. Die Gesamtsumme beträgt " +
            String.format("%.2f", orderedBurgers.stream()
            .mapToDouble(burger -> burger.calculatePrice().doubleValue())
            .sum()) + " EUR.";
  }

  @Override
  public void addObserver(Observer obs) {
    observers.add(obs);
  }

  @Override
  public void removeObserver(Observer obs) {
    observers.remove(obs);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }
}
