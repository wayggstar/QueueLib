package org.wayggstar.queueLib.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.wayggstar.queueLib.GameChannel;

import java.util.Set;

public class GameChannelEndEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final GameChannel gameChannel;

    public GameChannelEndEvent(GameChannel gameChannel) {
        this.gameChannel = gameChannel;
    }

    public GameChannel getGameChannel() {
        return gameChannel;
    }

    public Set<Player> getActivePlayers() {
        return gameChannel.getActivePlayers();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
