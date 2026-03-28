import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;

public class ComboBoxTooltipDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JComboBox Item Tooltips");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            // Combo box data
            String[] items = {"Apple", "Banana", "Cherry", "Date"};

            List<String> tooltips = Arrays.asList("A crunchy red or green fruit", "A long yellow fruit", "A small red stone fruit",
                    "A sweet brown fruit from date palms");

            JComboBox<String> comboBox = new JComboBox<>(items);

            // Attach custom renderer with tooltips
            comboBox.setRenderer(new ToolTipRenderer(tooltips));

            frame.add(comboBox);
            frame.setSize(350, 120);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Custom renderer that sets tooltip per item
    static class ToolTipRenderer extends DefaultListCellRenderer {
        private final List<String> tooltips;

        ToolTipRenderer(List<String> tooltips) {
            this.tooltips = tooltips;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (index >= 0 && index < tooltips.size()) {
                list.setToolTipText(tooltips.get(index));
            } else {
                list.setToolTipText(null);
            }

            return c;
        }
    }
}
