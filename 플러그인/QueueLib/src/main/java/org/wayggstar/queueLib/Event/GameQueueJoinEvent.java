package org.wayggstar.queueLib.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.wayggstar.queueLib.GameChannel;
import org.wayggstar.queueLib.GameType;

public class GameQueueJoinEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final GameType gameType;
    private final Player player;

    public GameQueueJoinEvent(GameType gameType, Player player) {
        this.gameType = gameType;
        this.player = player;
    }

    /**
     * 플레이어가 가입한 게임 채널을 반환.
     */
    public GameType getGameType() {
        return gameType;
    }

    /**
     * 대기열에 가입한 플레이어 반환.
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
