package htw.berlin.wi.prog2.ui;

import htw.berlin.wi.prog2.data.Menu;
import htw.berlin.wi.prog2.domain.Burger;
import htw.berlin.wi.prog2.domain.BurgerBuilder;
import htw.berlin.wi.prog2.parsing.ExtendableInputParser;

import java.util.ArrayList;
import java.util.List;

// TODO (2) Annotations @ShellComponent, @ShellMethod, und @ShellOption verwenden
public class BurgerBotCommands {

  final List<Burger> orderedBurgers;

  private final ExtendableInputParser parser;
  private final BurgerBuilder builder;
  private final InputEventPublisher inputEventPublisher;
  private final Menu menu;

  public BurgerBotCommands(ExtendableInputParser parser, BurgerBuilder builder, InputEventPublisher inputEventPublisher) {
    this.inputEventPublisher = inputEventPublisher;
    this.orderedBurgers = new ArrayList<>();
    this.parser = parser;
    this.builder = builder;
    this.menu = Menu.getInstance();
  }

  public String order(String inputLine) {
    // TODO (3) parser, builder, menu, orderedBurgers nutzen, um Bestellungen aufzunehmen
    return "In Ordnung ...";
    // TODO (4) Klasse BurgerBotPromptProvider erstellen und dann inputEventPublisher für Prompt-Updates nutzen
    // TODO (5) Observer-Muster implementieren und hier die notifyObservers-Methode aufrufen
  }

  public String confirm() {
    // TODO (3) Preis der gesamten Bestellung mithilfe von orderedBurgers berechnen
    return "Vielen Dank...";
  }
}
