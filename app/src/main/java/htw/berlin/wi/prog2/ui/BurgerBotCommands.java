package htw.berlin.wi.prog2.ui;

import htw.berlin.wi.prog2.data.Menu;
import htw.berlin.wi.prog2.data.MenuUtils;
import htw.berlin.wi.prog2.domain.Burger;
import htw.berlin.wi.prog2.domain.BurgerBuilder;
import htw.berlin.wi.prog2.domain.Ingredient;
import htw.berlin.wi.prog2.parsing.CountingInputParser;
import htw.berlin.wi.prog2.parsing.ExtendableInputParser;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO (2) Annotations @ShellComponent, @ShellMethod, und @ShellOption verwenden
@ShellComponent
public class BurgerBotCommands {

  final List<Burger> orderedBurgers;

  private final ExtendableInputParser parser;
  private final BurgerBuilder builder;
  private final InputEventPublisher inputEventPublisher;
  private final Menu menu;
  //added to the constructor
  private final UserInputWrapper input;



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

    while (!(inputLine.equals("Bestellung abschliessen") || inputLine.equals("Auf Wiedersehen"))) {
      Map<String, Long> keywordsToIds = MenuUtils.focusOnNameAndInvert(articles);
      Map<Long, Integer> ingredientsCount = parser.idsAndCountFromInput(inputLine, keywordsToIds);
      List<Ingredient> ingredients = MenuUtils.ingredientsFromIdAndCount(ingredientsCount, articles);
      if(ingredients.isEmpty()) {
        inputLine = input.ask("Entschuldigung, ich habe dich nicht verstanden. Wähle aus folgenden Zutaten: "
                + MenuUtils.focusOnNames(articles));
      } else {
        for (Ingredient ing : ingredients) builder.add(ing);
        Burger burger = builder.build();
        var comparator = Comparator.comparing(Ingredient::getName);
        List<Ingredient> ingrSorted = ingredients.stream().sorted(comparator).collect(Collectors.toList());
        //added orderedBurgers and return
        orderedBurgers.add(burger);
        return "In Ordnung. Dein Burger mit " + ingrSorted +
                " kostet " + burger.calculatePrice() + " Euro. Willst du die Bestellung abschliessen?";
      }
    }

    // TODO (3) parser, builder, menu, orderedBurgers nutzen, um Bestellungen aufzunehmen
    return "In Ordnung ...";
    // TODO (4) Klasse BurgerBotPromptProvider erstellen und dann inputEventPublisher für Prompt-Updates nutzen
    // TODO (5) Observer-Muster implementieren und hier die notifyObservers-Methode aufrufen
  }

  //edited ShellMethod
  @ShellMethod(value = "Confirm the order and see the total price", key = "confirm")
  public String confirm() {
    // TODO (3) Preis der gesamten Bestellung mithilfe von orderedBurgers berechnen
    //added Stream
    return "Vielen Dank Ihre gesamte Bestellung kostet " + orderedBurgers.stream().mapToInt(burger -> burger.calculatePrice().intValue()).sum() + " Euro";
  }
}
