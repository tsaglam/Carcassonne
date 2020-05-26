package carcassonne.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import carcassonne.control.MainController;
import carcassonne.view.main.MainGUI;
import carcassonne.view.secondary.PreviewGUI;

/**
 * Container class for global key bindings. Offers a global action map and input map which are supposed to be used as
 * parents to the local ones.
 * @author Timur Saglam
 */
public class GlobalKeyBindingManager {
    private static final int NO_MODIFIER = 0;
    private HashMap<String, Action> actions;
    private HashMap<String, KeyStroke> inputs;
    private List<String> inputToActionKeys;
    private MainGUI mainUI;
    private PreviewGUI rotationUI;
    private MainController controller;

    /**
     * Creates the key binding manager.
     * @param controller is the main controller.
     * @param mainUI is the main user interface.
     * @param rotationUI is the user interface for rotating tiles.
     */
    public GlobalKeyBindingManager(MainController controller, MainGUI mainUI, PreviewGUI rotationUI) {
        this.mainUI = mainUI;
        this.rotationUI = rotationUI;
        this.controller = controller;
        actions = new HashMap<>();
        inputs = new HashMap<>();
        inputToActionKeys = new ArrayList<>();
        addZoomKeyBindings();
        addRotationBindings();
        addSelectionBindings();
    }

    /**
     * Adds a new global key binding that can be added to input and action maps.
     * @param inputToActionKey is the key that connects the key stroke and the action.
     * @param keyStroke is the key stroke that defines the key to press.
     * @param action is the action to be executed on the key stroke.
     */
    public void addKeyBinding(String inputToActionKey, KeyStroke keyStroke, Action action) {
        actions.put(inputToActionKey, action);
        inputs.put(inputToActionKey, keyStroke);
        inputToActionKeys.add(inputToActionKey);
    }

    /**
     * Adds the global key bindings of the manager to the specified maps.
     * @param inputMap is the map for the key inputs.
     * @param actionMap is the map for the consequential actions.
     */
    public void addKeyBindingsToMaps(InputMap inputMap, ActionMap actionMap) {
        for (String key : inputToActionKeys) {
            inputMap.put(inputs.get(key), key);
            actionMap.put(key, actions.get(key));
        }
    }

    private void addSelectionBindings() {
        // SELECT TILE ABOVE:
        KeyStroke upStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, NO_MODIFIER);
        Action selectAboveAction = new AbstractAction() {
            private static final long serialVersionUID = 5619589338409339194L;

            @Override
            public void actionPerformed(ActionEvent event) {
                rotationUI.selectAbove();
            }
        };
        addKeyBinding("up", upStroke, selectAboveAction);

        // SELECT TILE BELOW:
        KeyStroke downStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, NO_MODIFIER);
        Action selectBelowAction = new AbstractAction() {
            private static final long serialVersionUID = -8199202670185430564L;

            @Override
            public void actionPerformed(ActionEvent event) {
                rotationUI.selectBelow();
            }
        };
        addKeyBinding("down", downStroke, selectBelowAction);
    }

    private void addRotationBindings() {
        // ROTATE TILE LEFT:
        KeyStroke leftStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, NO_MODIFIER);
        Action rotateLeftAction = new AbstractAction() {
            private static final long serialVersionUID = 5619589338409339194L;

            @Override
            public void actionPerformed(ActionEvent event) {
                rotationUI.rotateLeft();
            }
        };
        addKeyBinding("left", leftStroke, rotateLeftAction);

        // ROTATE TILE RIGHT:
        KeyStroke rightStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, NO_MODIFIER);
        Action rotateRightAction = new AbstractAction() {
            private static final long serialVersionUID = -8199202670185430564L;

            @Override
            public void actionPerformed(ActionEvent event) {
                rotationUI.rotateRight();
            }
        };
        addKeyBinding("right", rightStroke, rotateRightAction);

        // ROTATE TILE RIGHT:
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, NO_MODIFIER);
        Action skipAction = new AbstractAction() {
            private static final long serialVersionUID = -596225951682450564L;

            @Override
            public void actionPerformed(ActionEvent event) {
                controller.requestSkip();
            }
        };
        addKeyBinding("escape", escapeStroke, skipAction);
    }

    private void addZoomKeyBindings() {
        // ZOOM IN:
        KeyStroke plusStroke = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, NO_MODIFIER);
        Action zoomInAction = new AbstractAction() {
            private static final long serialVersionUID = -4507116452291965942L;

            @Override
            public void actionPerformed(ActionEvent event) {
                mainUI.zoomIn();
            }
        };
        addKeyBinding("plus", plusStroke, zoomInAction);
        // ZOOM OUT:
        KeyStroke minusStroke = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, NO_MODIFIER);
        Action zoomOutAction = new AbstractAction() {
            private static final long serialVersionUID = 6989306054283945118L;

            @Override
            public void actionPerformed(ActionEvent event) {
                mainUI.zoomOut();
            }
        };
        addKeyBinding("minus", minusStroke, zoomOutAction);
    }
}
