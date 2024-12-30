package org.wayggstar.queueLib;
/**
 * GameType 및 관련 GameChannel을 관리.
 */
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.wayggstar.queueLib.Event.GameQueueJoinEvent;
import org.wayggstar.queueLib.Event.GameQueueLeaveEvent;

import java.util.*;

public class GameTypeManager {
    private final Map<String, GameType> gameTypes = new HashMap<>();
    private final Map<String, List<GameChannel>> gameChannels = new HashMap<>();

    /**
     * 새로운 GameType 등록.
     */
    public void registerGameType(String typeName, int minPlayers, int maxPlayers) {
        if (gameTypes.containsKey(typeName)) {
            throw new IllegalArgumentException("이미 존재하는 게임 타입입니다.");
        }
        gameTypes.put(typeName, new GameType(typeName, minPlayers, maxPlayers));
    }

    /**
     * 대기열에 플레이어 추가.
     */
    public void addPlayerToQueue(String typeName, Player player) {
        GameType gameType = gameTypes.get(typeName);
        if (gameType == null) {
            throw new IllegalArgumentException("존재하지 않는 게임 타입입니다.");
        }

        Queue<Player> waitingQueue = gameType.getWaitingQueue();
        synchronized (waitingQueue) {
            waitingQueue.add(player);
            Bukkit.getPluginManager().callEvent(new GameQueueJoinEvent(gameType, player));
            checkAndCreateChannels(gameType);
        }
    }

    /**
     * 대기열 확인 및 채널 생성.
     */
    private void checkAndCreateChannels(GameType gameType) {
        Queue<Player> waitingQueue = gameType.getWaitingQueue();
        List<GameChannel> channels = gameChannels.computeIfAbsent(gameType.getName(), k -> new ArrayList<>());

        while (waitingQueue.size() >= gameType.getMinPlayers()) {
            List<Player> playersForChannel = new ArrayList<>();
            for (int i = 0; i < gameType.getMaxPlayers() && !waitingQueue.isEmpty(); i++) {
                playersForChannel.add(waitingQueue.poll());
            }

            String channelName = gameType.getName() + "_channel_" + UUID.randomUUID().toString().substring(0, 5);
            GameChannel channel = new GameChannel(gameType, channelName, () -> {
            });

            for (Player player : playersForChannel) {
                channel.addPlayerToChannel(player);
            }

            channels.add(channel);
            channel.startGame();
        }
    }

    /**
     * 플레이어를 대기열에서 제거.
     */
    public void removePlayerFromQueue(String typeName, Player player) {
        GameType gameType = gameTypes.get(typeName);
        if (gameType == null) return;

        Queue<Player> waitingQueue = gameType.getWaitingQueue();
        synchronized (waitingQueue) {
            if (waitingQueue.remove(player)) {
                Bukkit.getPluginManager().callEvent(new GameQueueLeaveEvent(gameType, player));
                player.sendMessage("대기열에서 제거되었습니다.");
            }
        }
    }

    public GameType getGameType(String typeName) {
        return gameTypes.get(typeName);
    }

    public List<GameChannel> getChannels(String typeName) {
        return gameChannels.getOrDefault(typeName, Collections.emptyList());
    }
}
