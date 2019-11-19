package carcassonne.view.main;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link WindowAdapter} that allows a main user interface to enslave a number of sub components to minimize them with
 * the main user interface.
 * @author Timur Saglam
 */
public class SubComponentAdapter extends WindowAdapter {
    private final List<Component> subComponents;
    private final List<Component> minimizedComponents;

    /**
     * Creates the adapter for any number of sub components.
     * @param components are the specific sub components.
     */
    public SubComponentAdapter(Component... components) {
        subComponents = new ArrayList<>(Arrays.asList(components));
        minimizedComponents = new ArrayList<>();
    }

    @Override
    public void windowDeiconified(WindowEvent event) {
        minimizedComponents.forEach(it -> it.setVisible(true));
        minimizedComponents.clear();
    }

    @Override
    public void windowIconified(WindowEvent event) {
        for (Component gui : subComponents) {
            if (gui.isVisible()) {
                gui.setVisible(false);
                minimizedComponents.add(gui);
            }
        }
    }
}
