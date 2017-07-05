package net.dynamicdungeon.screens;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import net.dynamicdungeon.panels.GuiPanel;
import net.dynamicdungeon.panels.MessagePanel;

public class HelpScreen implements Screen {
    @Override
    public void displayOutput(final GuiPanel terminal, MessagePanel messages) {
	messages.write("Dynamic Dungeon Help");
	messages.write("Descend the Deep Dungeons, find the lost Amulet, and return to");
	messages.write("the surface to win. Use what you find to avoid dying.");
	messages.write("[g] or [,] to pick up");
	messages.write("[d] to drop");
	messages.write("[e] to eat");
	messages.write("[w] to wear or wield");
	messages.write("[?] or [/] for help");
	messages.write("[x] to examine your items");
	messages.write("[;] to look around");
	messages.write("[t] to throw an item");
	messages.write("[q] to quaff a potion");
	messages.write("[r] to read something");
	messages.write("-- press any key to continue --");
    }

    @Override
    public Screen respondToUserInput(final KeyEvent key, MouseEvent mouse) {
	return null;
    }
}
