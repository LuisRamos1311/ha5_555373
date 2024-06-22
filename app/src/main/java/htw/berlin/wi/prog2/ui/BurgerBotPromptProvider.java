package htw.berlin.wi.prog2.ui;

import jakarta.annotation.PostConstruct;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class BurgerBotPromptProvider implements PromptProvider,Observer {

private boolean getUnderstood = false;

    @Autowired
    private BurgerBotCommands burgerBotCommands;

    private int numberOfBurgers = 0;


    @PostConstruct
    public void init() {
        burgerBotCommands.addObserver(this); // Register as observer once initialized
    }


    @Override
    public AttributedString getPrompt() {

        if (getUnderstood) {
            return new AttributedString("burger-bot (" + (numberOfBurgers) + ") green:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        } else {
            return new AttributedString("burger-bot (" + (numberOfBurgers) + ")  red:> no error",
                    AttributedStyle.DEFAULT.background(AttributedStyle.RED));
        }

    }

    @EventListener
    public void handle(InputEvent event) {
        this.getUnderstood = event.getUnderstood();
        this.getPrompt();
    }

    @Override
    public void update() {
        // Hier wird der Prompt entsprechend der Anzahl der Burger in der Bestellung aktualisiert
        // Beispiel: "burger-bot(3):> " für 3 bestellte Burger
        numberOfBurgers = burgerBotCommands.orderedBurgers.size();
        this.getPrompt();
    }
}
