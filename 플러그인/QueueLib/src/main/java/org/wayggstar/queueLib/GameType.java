package org.wayggstar.queueLib;

import org.bukkit.entity.Player;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 게임의 타입을 정의하는 클래스.
 * 이름, 최소/최대 플레이어 수 등 기본 설정만 포함.
 */
public class GameType {
    private final String name;
    private final int minPlayers;
    private final int maxPlayers;
    private final Queue<Player> waitingQueue = new ConcurrentLinkedQueue<>();

    public GameType(String name, int minPlayers, int maxPlayers) {
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Queue<Player> getWaitingQueue() {
        return waitingQueue;
    }
}