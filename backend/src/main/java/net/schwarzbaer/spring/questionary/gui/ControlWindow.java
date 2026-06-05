package net.schwarzbaer.spring.questionary.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.NonNull;

class ControlWindow extends JFrame
{
    private final @NonNull ConfigurableApplicationContext context;

    ControlWindow(@NonNull ConfigurableApplicationContext context)
    {
        super("Questionary App");
        this.context = context;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        JButton btnStop = new JButton("Stop Server");
        btnStop.addActionListener(ev -> stopServer());
        btnStop.setPreferredSize(new Dimension(300, 50));
        contentPane.add(btnStop);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { stopServer(); }
            @Override public void windowClosed (WindowEvent e) { stopServer(); }
        });
        
        setContentPane(contentPane);
        pack();
    }

    private void stopServer()
    {
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }
}
