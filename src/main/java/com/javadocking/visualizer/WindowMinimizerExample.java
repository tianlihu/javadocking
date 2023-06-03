package com.javadocking.visualizer;

import com.javadocking.DockingManager;
import com.javadocking.dock.Position;
import com.javadocking.dock.SplitDock;
import com.javadocking.dock.TabDock;
import com.javadocking.dockable.*;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.drag.DragListener;
import com.javadocking.model.DefaultDockingPath;
import com.javadocking.model.DockingPath;
import com.javadocking.model.FloatDockModel;
import com.javadocking.util.PropertiesUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * This example shows the usage of a minimizer and a maximizer.
 * The minimizer is a com.javadocking.visualizer.WindowMinimizer.
 * The maximizer is a com.javadocking.visualizer.SingleMaximizer.
 * The dockables have actions to minimize, maximize, and restore itself.
 * Some dockables are already minimized, when the application is started.
 *
 * @author Heidi Rakels
 */
public class WindowMinimizerExample extends JPanel {

    // Static fields.

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 550;

    // Constructor.

    public WindowMinimizerExample(JFrame frame) {
        super(new BorderLayout());

        // Create the dock model for the docks.
        FloatDockModel dockModel = new FloatDockModel();
        dockModel.addOwner("frame0", frame);

        // Give the dock model to the docking manager.
        DockingManager.setDockModel(dockModel);

        // Create the content components.
        TextPanel textPanel1 = new TextPanel("I am window 1.");
        TextPanel textPanel2 = new TextPanel("I am window 2.");
        TextPanel textPanel3 = new TextPanel("I am window 3.");
        TextPanel textPanel4 = new TextPanel("I am window 4.");
        TextPanel textPanel5 = new TextPanel("I am window 5.");
        TextPanel textPanel6 = new TextPanel("I am window 6.");
        TextPanel textPanel7 = new TextPanel("I am window 7.");
        TextPanel textPanel8 = new TextPanel("I am window 8.");

        // Create the dockables around the content components.
        Icon icon = new ImageIcon(getClass().getResource("/images/text12.gif"));
        Dockable dockable1 = new DefaultDockable("Window1", textPanel1, "Window 1", icon);
        Dockable dockable2 = new DefaultDockable("Window2", textPanel2, "Window 2", icon);
        Dockable dockable3 = new DefaultDockable("Window3", textPanel3, "Window 3", icon);
        Dockable dockable4 = new DefaultDockable("Window4", textPanel4, "Window 4", icon);
        Dockable dockable5 = new DefaultDockable("Window5", textPanel5, "Window 5", icon);
        Dockable dockable6 = new DefaultDockable("Window6", textPanel6, "Window 6", icon);
        Dockable dockable7 = new DefaultDockable("Window7", textPanel7, "Window 7", icon);
        Dockable dockable8 = new DefaultDockable("Window8", textPanel8, "Window 8", icon);

        // Add minimize, maximize and close actions to the dockables.
        dockable1 = addActions(dockable1);
        dockable2 = addActions(dockable2);
        dockable3 = addActions(dockable3);
        dockable4 = addActions(dockable4);
        dockable5 = addActions(dockable5);
        dockable6 = addActions(dockable6);
        dockable7 = addActions(dockable7);
        dockable8 = addActions(dockable8);

        // Create the child tab dock.
        TabDock leftTabDock = new TabDock();
        TabDock rightTabDock = new TabDock();

        // Add the dockables to the tab dock.
        leftTabDock.addDockable(dockable1, new Position(0));
        leftTabDock.addDockable(dockable2, new Position(1));
        rightTabDock.addDockable(dockable3, new Position(0));
        rightTabDock.addDockable(dockable4, new Position(1));

        // Create the split dock.
        SplitDock splitDock = new SplitDock();

        // Add the child docks to the split dock at the left and right.
        splitDock.addChildDock(leftTabDock, new Position(Position.LEFT));
        splitDock.addChildDock(rightTabDock, new Position(Position.RIGHT));
        splitDock.setDividerLocation(395);

        // Add the root dock to the dock model.
        dockModel.addRootDock("splitDock", splitDock, frame);

        // Create a minimizer.
        WindowMinimizer minimizer = new WindowMinimizer(frame, "Windows", 100, 100);
        dockModel.addVisualizer("minimizer", minimizer, frame);

        // Create a maximizer.
        SingleMaximizer maximizer = new SingleMaximizer(splitDock);
        dockModel.addVisualizer("maximizer", maximizer, frame);

        // Add the maximizer to the panel.
        this.add(maximizer, BorderLayout.CENTER);

        // Minimize dockables.
        minimizer.visualizeDockable(dockable5);
        minimizer.visualizeDockable(dockable6);
        minimizer.visualizeDockable(dockable7);
        minimizer.visualizeDockable(dockable8);

        // Add the path of the dockables that are not docked already.
        DockingPath dockingPathToCopy1 = DefaultDockingPath.createDockingPath(dockable1);
        DockingPath dockingPathToCopy2 = DefaultDockingPath.createDockingPath(dockable3);
        DockingPath dockingPath5 = DefaultDockingPath.copyDockingPath(dockable5, dockingPathToCopy1);
        DockingPath dockingPath6 = DefaultDockingPath.copyDockingPath(dockable6, dockingPathToCopy1);
        DockingPath dockingPath7 = DefaultDockingPath.copyDockingPath(dockable7, dockingPathToCopy2);
        DockingPath dockingPath8 = DefaultDockingPath.copyDockingPath(dockable8, dockingPathToCopy2);
        DockingManager.getDockingPathModel().add(dockingPath5);
        DockingManager.getDockingPathModel().add(dockingPath6);
        DockingManager.getDockingPathModel().add(dockingPath7);
        DockingManager.getDockingPathModel().add(dockingPath8);

    }

    /**
     * Decorates the given dockable with a state actions.
     *
     * @param dockable The dockable to decorate.
     * @return The wrapper around the given dockable, with actions.
     */
    private Dockable addActions(Dockable dockable) {

        Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), new int[0]);
        int[] states = {DockableState.NORMAL, DockableState.MINIMIZED, DockableState.MAXIMIZED};
        wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), states);
        return wrapper;

    }

    /**
     * This is the class for the content.
     */
    private class TextPanel extends JPanel implements DraggableContent {

        private JLabel label;

        public TextPanel(String text) {
            super(new FlowLayout());

            // The panel.
            setMinimumSize(new Dimension(80, 80));
            setPreferredSize(new Dimension(150, 150));
//			setBackground(Color.white);
//            setBorder(BorderFactory.createLineBorder(Color.lightGray));

            // The label.
            label = new JLabel(text);
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(label);
        }

        // Implementations of DraggableContent.

        public void addDragListener(DragListener dragListener) {
            addMouseListener(dragListener);
            addMouseMotionListener(dragListener);
            label.addMouseListener(dragListener);
            label.addMouseMotionListener(dragListener);
        }
    }

    /**
     * A visualizer that shows minimized dockables in a dialog.
     *
     * @author Heidi Rakels.
     */
    private class WindowMinimizer implements Visualizer {

        // Fields.

        /** The panel with the minimizers. */
        private MinimizerPanel minimizerPanel;
        /** The dialog that contains the minimized dockable headers. */
        private JDialog dialog;

        // Constructors.

        /**
         * Constructs a visualizer that shows minimized dockables in a dialog.
         *
         * @param    owner    The window owner.
         * @param    title    The window title.
         * @param    x        The x-position for the window.
         * @param    y        The y-position for the window.
         */
        public WindowMinimizer(JFrame owner, String title, int x, int y) {

            minimizerPanel = new MinimizerPanel(MinimizerPanel.ORIENTATION_VERTICAL);

            // Create the dialog.
            dialog = new JDialog(owner);
            dialog.setTitle(title);
            dialog.setLocation(x, y);
            dialog.setContentPane(minimizerPanel);
            dialog.pack();

        }

        /**
         * Constructs a visualizer that shows minimized dockables in a dialog.
         *
         * @param    owner    The window owner.
         * @param    title    The window title.
         * @param    x        The x-position for the window.
         * @param    y        The y-position for the window.
         */
        public WindowMinimizer(JDialog owner, String title, int x, int y) {

            minimizerPanel = new MinimizerPanel(MinimizerPanel.ORIENTATION_VERTICAL);

            // Create the dialog.
            dialog = new JDialog(owner);
            dialog.setTitle(title);
            dialog.setLocation(x, y);
            dialog.setContentPane(minimizerPanel);
            dialog.pack();

        }


        // Implementations of Visualizer.

        public int getState() {
            return minimizerPanel.getState();
        }

        public boolean canVisualizeDockable(Dockable dockableToVisualize) {
            return minimizerPanel.canVisualizeDockable(dockableToVisualize);
        }

        public void visualizeDockable(Dockable dockableToVisualize) {

            minimizerPanel.visualizeDockable(dockableToVisualize);
            if (minimizerPanel.getVisualizedDockableCount() > 0) {
                dialog.pack();
                dialog.setVisible(true);
            }

            // Don't forget this, otherwise the delegate will be registered as the visualizer.
            dockableToVisualize.setState(DockableState.MINIMIZED, this);

        }

        public int getVisualizedDockableCount() {
            return minimizerPanel.getVisualizedDockableCount();
        }

        public Dockable getVisualizedDockable(int index) throws IndexOutOfBoundsException {

            return minimizerPanel.getVisualizedDockable(index);

        }

        public void removeVisualizedDockable(Dockable dockableToRemove) {

            minimizerPanel.removeVisualizedDockable(dockableToRemove);
            if (minimizerPanel.getVisualizedDockableCount() == 0) {
                dialog.setVisible(false);
            }
            dialog.pack();

        }

        /**
         * Loads the properties of this maximizer. The dockables that were maximized,
         * when the model was saved, are not maximized again.
         */
        public void loadProperties(String prefix, Properties properties, Map dockablesMap, Window owner) throws IOException {

            // Set the position of the dialog.
            int xPosition = 0;
            xPosition = PropertiesUtil.getInteger(properties, prefix + "xPosition", xPosition);
            int yPosition = 0;
            yPosition = PropertiesUtil.getInteger(properties, prefix + "yPosition", yPosition);
            dialog.setLocation(xPosition, yPosition);

            // Load the properties of the delegate.
            minimizerPanel.loadProperties(prefix, properties, dockablesMap, owner);

        }

        /**
         * Saves the properties of this maximizer. The dockables that are maximized,
         * are not saved.
         */
        public void saveProperties(String prefix, Properties properties) {

            // Save the position of the dialog.
            PropertiesUtil.setInteger(properties, prefix + "xPosition", dialog.getLocation().x);
            PropertiesUtil.setInteger(properties, prefix + "yPosition", dialog.getLocation().y);

            // Save the properties of the delegate.
            minimizerPanel.saveProperties(prefix, properties);

        }

    }

    // Main method.

    public static void createAndShowGUI() {

        // Create the frame.
        JFrame frame = new JFrame("Split dock");

        // Create the panel and add it to the frame.
        WindowMinimizerExample panel = new WindowMinimizerExample(frame);
        frame.getContentPane().add(panel);

        // Set the frame properties and show it.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);

    }

    public static void main(String args[]) {
        Runnable doCreateAndShowGUI = new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }

}

