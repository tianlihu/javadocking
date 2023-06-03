package com.javadocking.dockablecreation;

import com.javadocking.DockingManager;
import com.javadocking.dock.*;
import com.javadocking.dock.docker.BorderDocker;
import com.javadocking.dock.factory.LeafDockFactory;
import com.javadocking.dock.factory.ToolBarDockFactory;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.StateActionDockable;
import com.javadocking.dockable.action.DefaultDockableStateAction;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.event.DockingEvent;
import com.javadocking.event.DockingListener;
import com.javadocking.model.DefaultDockingPath;
import com.javadocking.model.DockingPath;
import com.javadocking.model.FloatDockModel;
import com.javadocking.util.LookAndFeelUtil;
import com.javadocking.visualizer.DockingMinimizer;
import com.javadocking.visualizer.FloatExternalizer;
import com.javadocking.visualizer.SingleMaximizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This example shows dockables that are created when the user presses a button.
 *
 * @author Heidi Rakels
 */
public class MainChartDockingExample extends JPanel {

    // Static fields.

    public static final int FRAME_WIDTH = 900;
    public static final int FRAME_HEIGHT = 650;

    // Fields.

    /** All the dockables of the application. */
    private Dockable[] dockables;
    private int chartCount = 0;
    private DockingPath dockingPath;
    private JMenu windowMenu;

    // Constructors.

    public MainChartDockingExample(JFrame frame) {

        super(new BorderLayout());

        // Create the dock model for the docks, minimizer and maximizer.
        FloatDockModel dockModel = new FloatDockModel("workspace.dck");
        String frameId = "frame";
        dockModel.addOwner(frameId, frame);

        // Give the dock model to the docking manager.
        DockingManager.setDockModel(dockModel);

        // Create the content components.


        // The dockables.
        dockables = new Dockable[5];
        JPanel buttonPanel = createButtonPanel();
        JPanel helloPanel = createHelloPanel();
        JPanel chartPanel1 = new JPanel();
        JPanel chartPanel2 = new JPanel();
        JPanel chartPanel3 = new JPanel();

        // Create the dockables around the content components.
        dockables[0] = createDockable("buttonPanel", buttonPanel, "Create Chart", null, "Create new charts");
        dockables[1] = createDockable("helloPanel", helloPanel, "Hello", null, "Hello, I am another dockable");
        dockables[2] = createDockable("chartPanel1", chartPanel1, "Chart 1", null, "Chart number 1");
        dockables[3] = createDockable("chartPanel2", chartPanel2, "Chart 2", null, "Chart number 2");
        dockables[4] = createDockable("chartPanel3", chartPanel3, "Chart 3", null, "Chart number 3");

        // Give the float dock a different child dock factory.
        // We don't want the floating docks to be splittable.
        FloatDock floatDock = dockModel.getFloatDock(frame);
        floatDock.setChildDockFactory(new LeafDockFactory(false));

        // Create the tab docks.
        TabDock centerTabbedDock = new TabDock();
        TabDock leftTabbedDock = new TabDock();
        TabDock bottomTabbedDock = new TabDock();

        // Add the dockables to these tab docks.
        centerTabbedDock.addDockable(dockables[2], new Position(0));
        centerTabbedDock.addDockable(dockables[3], new Position(1));
        centerTabbedDock.addDockable(dockables[4], new Position(2));
        centerTabbedDock.setSelectedDockable(dockables[2]);
        leftTabbedDock.addDockable(dockables[0], new Position(0));
        bottomTabbedDock.addDockable(dockables[1], new Position(1));


        SplitDock rightSplitDock = new SplitDock();
        rightSplitDock.addChildDock(bottomTabbedDock, new Position(Position.BOTTOM));
        rightSplitDock.addChildDock(centerTabbedDock, new Position(Position.TOP));
        rightSplitDock.setDividerLocation(400);
        SplitDock leftSplitDock = new SplitDock();
        leftSplitDock.addChildDock(leftTabbedDock, new Position(Position.CENTER));
        SplitDock totalSplitDock = new SplitDock();
        totalSplitDock.addChildDock(leftSplitDock, new Position(Position.LEFT));
        totalSplitDock.addChildDock(rightSplitDock, new Position(Position.RIGHT));
        totalSplitDock.setDividerLocation(180);

        // Add the root dock to the dock model.
        dockModel.addRootDock("totalDock", totalSplitDock, frame);

        // Create a maximizer and add it to the dock model.
        SingleMaximizer maximizePanel = new SingleMaximizer(totalSplitDock);
        dockModel.addVisualizer("maximizer", maximizePanel, frame);

        // Create a docking minimizer and add it to the dock model.
        BorderDock minimizerBorderDock = new BorderDock(new ToolBarDockFactory());
        minimizerBorderDock.setMode(BorderDock.MODE_MINIMIZE_BAR);
        minimizerBorderDock.setCenterComponent(maximizePanel);
        BorderDocker borderDocker = new BorderDocker();
        borderDocker.setBorderDock(minimizerBorderDock);
        DockingMinimizer minimizer = new DockingMinimizer(borderDocker);
        dockModel.addVisualizer("minimizer", minimizer, frame);

        // Add an externalizer to the dock model.
        dockModel.addVisualizer("externalizer", new FloatExternalizer(frame), frame);

        // Add this dock also as root dock to the dock model.
        dockModel.addRootDock("minimizerBorderDock", minimizerBorderDock, frame);

        // Add the border dock to this panel.
        this.add(minimizerBorderDock, BorderLayout.CENTER);

        // Add the paths of the docked dockables to the model with the docking paths.
        addDockingPath(dockables[0]);
        addDockingPath(dockables[1]);
        addDockingPath(dockables[2]);
        addDockingPath(dockables[3]);
        addDockingPath(dockables[4]);

        dockingPath = DockingManager.getDockingPathModel().getDockingPath(dockables[2].getID());

        // Create the menubar.
        JMenuBar menuBar = createMenu(dockables);
        frame.setJMenuBar(menuBar);

    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton button = new JButton("Create new chart");
        button.addActionListener(new AddChartListener());
        panel.add(button);
        return panel;
    }

    private class AddChartListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {

            // Create the chart.
            JPanel chartPanel = new JPanel();

            // Create the dockable for the chart.
            Dockable dockable = createDockable("chartPanel" + chartCount, chartPanel, "Chart " + chartCount, null, "Chart number " + chartCount);

            // Where do we want the dockable to be placed?
            DockingPath newDockingPath = DefaultDockingPath.copyDockingPath(dockable, dockingPath);
            DockingManager.getDockingPathModel().add(newDockingPath);

            // Add the dockable.
            DockingManager.getDockingExecutor().changeDocking(dockable, dockingPath);

            JCheckBoxMenuItem cbMenuItem = new DockableMenuItem(dockable);
            windowMenu.add(cbMenuItem);

        }
    }

    private JPanel createHelloPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton button = new JButton("Hello, I am another content of a dockable");
        button.addActionListener(new HelloListener());
        panel.add(button);
        return panel;
    }

    private class HelloListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            JOptionPane.showMessageDialog(MainChartDockingExample.this, "Hello");
        }
    }

    /**
     * Decorates the given dockable with all state actions.
     *
     * @param dockable The dockable to decorate.
     * @return The wrapper around the given dockable, with actions.
     */
    private Dockable addAllActions(Dockable dockable) {

        Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), DockableState.statesClosed());
        wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), DockableState.statesAllExceptClosed());
        return wrapper;

    }


    /**
     * Creates a dockable for a given content component.
     *
     * @param id      The ID of the dockable. The IDs of all dockables should be different.
     * @param content The content of the dockable.
     * @param title   The title of the dockable.
     * @param icon    The icon of the dockable.
     * @throws IllegalArgumentException If the given ID is null.
     * @return The created dockable.
     */
    private Dockable createDockable(String id, Component content, String title, Icon icon, String description) {

        // Create the dockable.
        DefaultDockable dockable = new DefaultDockable(id, content, title, icon);

        // Add a description to the dockable. It will be displayed in the tool tip.
        dockable.setDescription(description);

        Dockable dockableWithActions = addAllActions(dockable);

        return dockableWithActions;

    }

    /**
     * Creates the menubar with menus: File, Window, Look and Feel, and Drag Painting.
     *
     * @param dockables The dockables for which a menu item has to be created.
     * @return The created menu bar.
     */
    private JMenuBar createMenu(Dockable[] dockables) {
        // Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        // Build the File menu.
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("The File Menu");
        menuBar.add(fileMenu);

        // Build the Window menu.
        windowMenu = new JMenu("Window");
        windowMenu.setMnemonic(KeyEvent.VK_W);
        windowMenu.getAccessibleContext().setAccessibleDescription("The Window Menu");
        menuBar.add(windowMenu);

        // The JMenuItem for File
        JMenuItem menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Exit te application");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        fileMenu.add(menuItem);

        // The JMenuItems for the dockables.
        for (int index = 0; index < dockables.length; index++) {
            // Create the check box menu for the dockable.
            JCheckBoxMenuItem cbMenuItem = new DockableMenuItem(dockables[index]);
            windowMenu.add(cbMenuItem);
        }

        return menuBar;

    }


    /**
     * Creates a docking path for the given dockable. It contains the information
     * how the dockable is docked now. The docking path is added to the docking path
     * model of the docking manager.
     *
     * @param     dockable    The dockable for which a docking path has to be created.
     * @return The docking path model. Null if the dockable is not docked.
     */
    private DockingPath addDockingPath(Dockable dockable) {

        if (dockable.getDock() != null) {
            // Create the docking path of the dockable.
            DockingPath dockingPath = DefaultDockingPath.createDockingPath(dockable);
            DockingManager.getDockingPathModel().add(dockingPath);
            return dockingPath;
        }

        return null;

    }

    // Private classes.

    /**
     * A check box menu item to add or remove the dockable.
     */
    private class DockableMenuItem extends JCheckBoxMenuItem {
        public DockableMenuItem(Dockable dockable) {
            super(dockable.getTitle(), dockable.getIcon());

            setSelected(dockable.getDock() != null);

            DockableMediator dockableMediator = new DockableMediator(dockable, this);
            dockable.addDockingListener(dockableMediator);
            addItemListener(dockableMediator);
        }
    }

    /**
     * A listener that listens when menu items with dockables are selected and deselected.
     * It also listens when dockables are closed or docked.
     */
    private class DockableMediator implements ItemListener, DockingListener {

        private Dockable dockable;
        private Action closeAction;
        private Action restoreAction;
        private JMenuItem dockableMenuItem;

        public DockableMediator(Dockable dockable, JMenuItem dockableMenuItem) {

            this.dockable = dockable;
            this.dockableMenuItem = dockableMenuItem;
            closeAction = new DefaultDockableStateAction(dockable, DockableState.CLOSED);
            restoreAction = new DefaultDockableStateAction(dockable, DockableState.NORMAL);

        }

        public void itemStateChanged(ItemEvent itemEvent) {

            dockable.removeDockingListener(this);
            if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                // Close the dockable.
                closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
            } else {
                // Restore the dockable.
                restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
            }
            dockable.addDockingListener(this);

        }

        public void dockingChanged(DockingEvent dockingEvent) {
            if (dockingEvent.getDestinationDock() != null) {
                dockableMenuItem.removeItemListener(this);
                dockableMenuItem.setSelected(true);
                dockableMenuItem.addItemListener(this);
            } else {
                dockableMenuItem.removeItemListener(this);
                dockableMenuItem.setSelected(false);
                dockableMenuItem.addItemListener(this);
            }
        }

        public void dockingWillChange(DockingEvent dockingEvent) {
        }

    }

    // Main method.

    public static void createAndShowGUI() {

        // Remove the borders from the split panes and the split pane dividers.
        LookAndFeelUtil.removeAllSplitPaneBorders();

        // Create the frame.
        JFrame frame = new JFrame("Main Example");

        // Set the default location and size.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // Create the panel and add it to the frame.
        MainChartDockingExample panel = new MainChartDockingExample(frame);
        frame.getContentPane().add(panel);

        // Set the frame properties and show it.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

