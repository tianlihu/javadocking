package com.javadocking.toolbar;

import com.javadocking.DockingManager;
import com.javadocking.dock.*;
import com.javadocking.dock.factory.CompositeToolBarDockFactory;
import com.javadocking.dock.factory.ToolBarDockFactory;
import com.javadocking.dockable.*;
import com.javadocking.drag.DragListener;
import com.javadocking.model.FloatDockModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This example shows {@link JButton}s in a tool bar at the borders of a window.
 * The buttons can be dragged and docked alone and together.
 *
 * @author Heidi Rakels
 */
public class JButtonExample extends JPanel {

    // Static fields.

    public static final int FRAME_WIDTH = 700;
    public static final int FRAME_HEIGHT = 450;

    // Constructor.

    public JButtonExample(JFrame frame) {
        super(new BorderLayout());

        // Create the dock model for the docks.
        FloatDockModel dockModel = new FloatDockModel();
        dockModel.addOwner("frame0", frame);

        // Give the dock model to the docking manager.
        DockingManager.setDockModel(dockModel);

        // Create the content components.
        TextPanel textPanel1 = new TextPanel("I am window 1.");
        TextPanel textPanel2 = new TextPanel("I am window 2.");

        // Create the dockables around the content components.
        Icon icon = new ImageIcon(getClass().getResource("/images/text12.gif"));
        Dockable dockable1 = new DefaultDockable("Window1", textPanel1, "Window 1", icon);
        Dockable dockable2 = new DefaultDockable("Window2", textPanel2, "Window 2", icon);

        // Create the child tab dock.
        TabDock tabDock1 = new TabDock();
        TabDock tabDock2 = new TabDock();

        // Add the dockables to the tab dock.
        tabDock1.addDockable(dockable1, new Position(0));
        tabDock2.addDockable(dockable2, new Position(1));

        // Create the split dock.
        SplitDock splitDock = new SplitDock();

        // Add the child docks to the split dock.
        splitDock.addChildDock(tabDock1, new Position(Position.LEFT));
        splitDock.addChildDock(tabDock2, new Position(Position.RIGHT));
        splitDock.setDividerLocation(340);

        // Create the buttons with a dockable around.
        Dockable[] buttonDockables = new Dockable[12];
        buttonDockables[0] = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/icons/add.png")), "Add!");
        buttonDockables[1] = createButtonDockable("ButtonDockableAccept", "Accept", new ImageIcon(getClass().getResource("/icons/accept.png")), "Accept!");
        buttonDockables[2] = createButtonDockable("ButtonDockableCancel", "Cancel", new ImageIcon(getClass().getResource("/icons/cancel.png")), "Cancel!");
        buttonDockables[3] = createButtonDockable("ButtonDockableUndo", "Undo", new ImageIcon(getClass().getResource("/icons/undo.png")), "Undo!");
        buttonDockables[4] = createButtonDockable("ButtonDockableRedo", "Redo", new ImageIcon(getClass().getResource("/icons/redo.png")), "Redo!");
        buttonDockables[5] = createButtonDockable("ButtonDockableRefresh", "Refresh", new ImageIcon(getClass().getResource("/icons/refresh.png")), "Refresh!");
        buttonDockables[6] = createButtonDockable("ButtonDockableBin", "Bin", new ImageIcon(getClass().getResource("/icons/bin.png")), "Bin!");
        buttonDockables[7] = createButtonDockable("ButtonDockableIcons", "Icons", new ImageIcon(getClass().getResource("/icons/icons.png")), "Icons!");
        buttonDockables[8] = createButtonDockable("ButtonDockableList", "List", new ImageIcon(getClass().getResource("/icons/list.png")), "List!");
        buttonDockables[9] = createButtonDockable("ButtonDockableImages", "Images", new ImageIcon(getClass().getResource("/icons/images.png")), "Images!");
        buttonDockables[10] = createButtonDockable("ButtonDockableDivide", "Divide", new ImageIcon(getClass().getResource("/icons/divide.png")), "Divide!");
        buttonDockables[11] = createButtonDockable("ButtonDockableJoin", "Join", new ImageIcon(getClass().getResource("/icons/join.png")), "Join!");

        // Create the border dock.
        BorderDock borderDock = new BorderDock(new CompositeToolBarDockFactory(), splitDock);
        borderDock.setMode(BorderDock.MODE_TOOL_BAR);
        CompositeLineDock compositeToolBarDock = new CompositeLineDock(CompositeLineDock.ORIENTATION_HORIZONTAL, false,
                new ToolBarDockFactory(), DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        borderDock.setDock(compositeToolBarDock, Position.TOP);

        // The line docks for the buttons.
        LineDock toolBarDock1 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        LineDock toolBarDock2 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        compositeToolBarDock.addChildDock(toolBarDock1, new Position(0));
        compositeToolBarDock.addChildDock(toolBarDock2, new Position(1));

        // Add the dockables to the line docks.
        toolBarDock1.addDockable(buttonDockables[0], new Position(0));
        toolBarDock1.addDockable(buttonDockables[1], new Position(1));
        toolBarDock1.addDockable(buttonDockables[2], new Position(2));
        toolBarDock1.addDockable(buttonDockables[3], new Position(3));
        toolBarDock1.addDockable(buttonDockables[4], new Position(4));
        toolBarDock1.addDockable(buttonDockables[5], new Position(5));
        toolBarDock2.addDockable(buttonDockables[6], new Position(0));
        toolBarDock2.addDockable(buttonDockables[7], new Position(1));
        toolBarDock2.addDockable(buttonDockables[8], new Position(2));
        toolBarDock2.addDockable(buttonDockables[9], new Position(3));
        toolBarDock2.addDockable(buttonDockables[10], new Position(4));
        toolBarDock2.addDockable(buttonDockables[11], new Position(5));

        // Add the docks.
        add((Component) borderDock, BorderLayout.CENTER);

        dockModel.addRootDock("borderDock", borderDock, frame);

    }

    /**
     * Creates a dockable with a button as content.
     *
     * @param id          The ID of the dockable that has to be created.
     * @param title       The title of the dialog that will be displayed.
     * @param description The action dscription.
     * @param icon        The icon that will be put on the button.
     * @param message     The message that will be displayed when the action is performed.
     * @return The dockable with a button as content.
     */
    private Dockable createButtonDockable(String id, String title, Icon icon, String message) {

        // Create the action.
        MessageAction action = new MessageAction(this, title, title, icon, "Action!");

        // Create the button.
        JButton button = new JButton(action);

        // Create the dockable with the button as component.
        ButtonDockable buttonDockable = new ButtonDockable(id, button);

        // Add a dragger to the individual dockable.
        createDockableDragger(buttonDockable);

        return buttonDockable;
    }

    /**
     * Adds a drag listener on the content component of a dockable.
     */
    private void createDockableDragger(Dockable dockable) {

        // Create the dragger for the dockable.
        DragListener dragListener = DockingManager.getDockableDragListenerFactory().createDragListener(dockable);
        dockable.getContent().addMouseListener(dragListener);
        dockable.getContent().addMouseMotionListener(dragListener);

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

    // Private classes.

    private class MessageAction extends AbstractAction {

        private Component parentComponent;
        private String message;
        private String name;

        public MessageAction(Component parentComponent, String name, String description, Icon icon, String message) {
            super(null, icon);
            putValue(Action.SHORT_DESCRIPTION, description);
            this.message = message;
            this.name = name;
            this.parentComponent = parentComponent;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JOptionPane.showMessageDialog(parentComponent,
                    message, name, JOptionPane.INFORMATION_MESSAGE);
        }

    }

    // Main method.

    public static void createAndShowGUI() {

        // Create the frame.
        JFrame frame = new JFrame("Split dock");

        // Create the panel and add it to the frame.
        JButtonExample panel = new JButtonExample(frame);
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

