package org.wayggstar.queueLib;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameChannel {
    private final String channelName;
    private final int minPlayers;
    private final int maxPlayers;
    private final Runnable onStart;

    private final Set<UUID> activePlayers = ConcurrentHashMap.newKeySet();
    private boolean isRunning = false;

    public GameChannel(String channelName, int minPlayers, int maxPlayers, Runnable onStart) {
        this.channelName = channelName;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.onStart = onStart;
    }

    public void addPlayer(UUID player) {
        if (isRunning || activePlayers.size() >= maxPlayers) {
            throw new IllegalStateException("채널에 더 이상 플레이어를 추가할 수 없습니다.");
        }
        activePlayers.add(player);
        checkStartCondition();
    }

    public synchronized void checkStartCondition() {
        if (!isRunning && activePlayers.size() >= minPlayers) {
            startGame();
        }
    }

    private void startGame() {
        isRunning = true;
        onStart.run();
    }

    public Set<UUID> getActivePlayers() {
        return Collections.unmodifiableSet(activePlayers);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void cleanup() {
        activePlayers.clear();
        isRunning = false;
    }
}
