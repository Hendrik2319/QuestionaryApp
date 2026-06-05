package net.schwarzbaer.spring.questionary.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuiStarter
{
    private final @NonNull ConfigurableApplicationContext context;
    private final @NonNull ApplicationArguments args;

    @EventListener(ApplicationReadyEvent.class)
    public void startGui()
    {
        if (args.containsOption("no_controlwindow"))
            return;
        if (args.getNonOptionArgs().contains("-no_controlwindow"))
            return;

        SwingUtilities.invokeLater(()->{
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}

            try { new ControlWindow(context).setVisible(true); }
            catch (Exception ex) { log.error("[GuiStarter.startGui()] %s: %s".formatted(ex.getClass().getSimpleName(), ex.getMessage())); }
        });
    }
}
