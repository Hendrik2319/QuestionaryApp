package net.schwarzbaer.spring.questionary.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.DimensionUIResource;

public class ControlWindow extends JFrame
{
    private static ControlWindow instance = null;

    public static ControlWindow getInstance()
    {
        if (instance==null)
            instance = new ControlWindow();
        return instance;
    }
    
    private boolean wasStarted;

    private ControlWindow()
    {
        super("Questionary App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        wasStarted = false;

        JPanel contentPane = new JPanel();
        JButton btnStop = new JButton("Stop Server");
        btnStop.addActionListener(ev -> System.exit(0));
        contentPane.add(btnStop);
        
        setPreferredSize(new DimensionUIResource(300, 100));
        setContentPane(contentPane);
        pack();

    }

    public synchronized void start()
    {
        if (wasStarted)
            return;

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}

        setVisible(true);
        wasStarted = true;
    }
}
