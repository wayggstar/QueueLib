package org.wayggstar.queueLib.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.wayggstar.queueLib.GameChannel;

public class GameChannelStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final GameChannel channel;

    public GameChannelStartEvent(GameChannel gameChannel){
        this.channel = gameChannel;
    }

    public GameChannel getChannel() {
        return channel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
