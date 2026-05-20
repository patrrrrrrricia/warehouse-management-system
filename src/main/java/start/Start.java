package start;

import presentation.View;
import presentation.Controller;

/**
 * START - clasa principala de intrare in aplicatie
 *       - metoda 'main' care initializeaza fereastra principala (view)
 * si controller ul asociat, lansand astfel interfata grafica in executie
 */
public class Start
{
    /**
     * metoda de lansare a aplicatiei
     * @param args argumentele liniei de comanda
     */
    public static void main(String[] args)
    {
        //interfata grafica
        View mainView = new View();
        new Controller(mainView);
        mainView.setVisible(true);
    }
}
