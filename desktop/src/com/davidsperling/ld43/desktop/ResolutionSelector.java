package com.davidsperling.ld43.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import sun.security.krb5.internal.crypto.Des;

public class ResolutionSelector extends JFrame implements ActionListener {
    private enum ActionCommand {
        DISPLAY_MODE,
        FULL_SCREEN,
        START,
        CANCEL
    }

    private JButton startButton;
    private JButton cancelButton;
    private JLabel displayModeLabel;
    private JComboBox<Graphics.DisplayMode> displayModeComboBox;
    private JCheckBox fullScreenCheckbox;

    private JPanel formPanel;
    private JPanel buttonPanel;

    private Graphics.DisplayMode[] displayModes;

    private static final Border standardBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    private ResolutionSelector(String title) {
        super(title);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setActionCommand(ActionCommand.START.name());

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand(ActionCommand.CANCEL.name());

        displayModeLabel = new JLabel("Display Mode");
        displayModes = LwjglApplicationConfiguration.getDisplayModes();

        Graphics.DisplayMode currentDisplayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
        displayModeComboBox = new JComboBox<Graphics.DisplayMode>(displayModes);
        displayModeComboBox.setSelectedIndex(displayModeComboBox.getItemCount() - 1);
        for (int i = 0; i < displayModes.length; i++) {
            if (currentDisplayMode.equals(displayModes[i])) {
                displayModeComboBox.setSelectedIndex(i);
            }
        }

        displayModeComboBox.setActionCommand(ActionCommand.DISPLAY_MODE.name());
        displayModeComboBox.addActionListener(this);

        fullScreenCheckbox = new JCheckBox("Full Screen");
        fullScreenCheckbox.setSelected(true);

        formPanel = new JPanel(new FlowLayout());
        buttonPanel = new JPanel(new FlowLayout());

        GridBagConstraints c;

        c = createGridBagConstraints(0, 0);
        formPanel.add(displayModeLabel, c);

        c = createGridBagConstraints(1, 0);
        formPanel.add(displayModeComboBox);

        c = createGridBagConstraints(1,0);
        formPanel.add(new JPanel(), c);

        c = createGridBagConstraints(1, 1);
        formPanel.add(fullScreenCheckbox, c);

        buttonPanel.add(cancelButton);
        buttonPanel.add(startButton);
    }

    private GridBagConstraints createGridBagConstraints(int x, int y) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;

        gridBagConstraints.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
        gridBagConstraints.fill = (x == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;

        gridBagConstraints.insets = (x == 0) ? new Insets(5, 0, 5, 5) : new Insets(5, 5, 5, 0);
        gridBagConstraints.weightx = (x == 0) ? 0.1 : 1.0;
        gridBagConstraints.weighty = 1.0;
        return gridBagConstraints;
    }

    public static ResolutionSelector createAndShowGUI() {
        ResolutionSelector.setLookAndFeel();
        ResolutionSelector resolutionSelector = new ResolutionSelector("Blast Ant Omega");
        resolutionSelector.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        resolutionSelector.addComponentsToPane(resolutionSelector.getContentPane());

        resolutionSelector.pack();

        resolutionSelector.setLocationRelativeTo(null);
        resolutionSelector.setVisible(true);

        return resolutionSelector;
    }

    public void addComponentsToPane(Container pane) {
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBorder(standardBorder);

        JPanel formPanelWrapper = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Display Settings");
        formPanelWrapper.setBorder(titledBorder);
        formPanelWrapper.add(formPanel);

        innerPanel.add(formPanelWrapper, BorderLayout.CENTER);
        innerPanel.add(buttonPanel, BorderLayout.PAGE_END);
        pane.add(innerPanel);
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ActionCommand actionCommand = ActionCommand.valueOf(actionEvent.getActionCommand());
        if (actionCommand == ActionCommand.START) {
            DesktopLauncher.setDisplayMode((Graphics.DisplayMode) displayModeComboBox.getSelectedItem());
            DesktopLauncher.setFullScreen(fullScreenCheckbox.isSelected());
            DesktopLauncher.startGame();
            this.setVisible(false);
        } else if (actionCommand == ActionCommand.CANCEL) {
            this.setVisible(false);
            this.dispose();
        }
    }
}
