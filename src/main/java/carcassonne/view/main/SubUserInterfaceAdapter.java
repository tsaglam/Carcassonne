package carcassonne.view.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carcassonne.view.secondary.SecondaryGUI;

/**
 * {@link WindowAdapter} that allows a main user interface to enslave a number of sub interfaces to minimize them with
 * the main user interface.
 * @author Timur Saglam
 */
public class SubUserInterfaceAdapter extends WindowAdapter {
    private final List<SecondaryGUI> userInterfaces;
    private final List<SecondaryGUI> minimizedInterfaces;

    /**
     * Creates the adapter for any number of sub interfaces.
     * @param subUserInterfaces are the specific sub interfaces.
     */
    public SubUserInterfaceAdapter(SecondaryGUI... subUserInterfaces) {
        userInterfaces = new ArrayList<>(Arrays.asList(subUserInterfaces));
        minimizedInterfaces = new ArrayList<>();
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        minimizedInterfaces.forEach(it -> it.setVisible(true));
        minimizedInterfaces.clear();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        for (SecondaryGUI gui : userInterfaces) {
            if (gui.isVisible()) {
                gui.setVisible(false);
                minimizedInterfaces.add(gui);
            }
        }
    }
}
