package org.wayggstar.queueLib;

import java.util.*;
import java.util.concurrent.*;

public class GameType {
    private final String name;
    private final int minPlayers;
    private final int maxPlayers;
    private final Runnable onStart;

    private final List<GameChannel> activeChannels = new CopyOnWriteArrayList<>();
    private final Queue<UUID> waitingQueue = new ConcurrentLinkedQueue<>();

    public GameType(String name, int minPlayers, int maxPlayers, Runnable onStart) {
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.onStart = onStart;
    }

    public synchronized void addPlayer(UUID player) {
        GameChannel channel = findAvailableChannel();
        if (channel != null) {
            channel.addPlayer(player);
        } else {
            waitingQueue.add(player);
            checkQueue();
        }
    }

    private void checkQueue() {
        if (waitingQueue.size() >= minPlayers) {
            createNewChannel();
        }
    }

    private GameChannel findAvailableChannel() {
        for (GameChannel channel : activeChannels) {
            if (!channel.isRunning() && channel.getActivePlayers().size() < maxPlayers) {
                return channel;
            }
        }
        return null;
    }

    private void createNewChannel() {
        GameChannel newChannel = new GameChannel(
                name + "_Channel_" + (activeChannels.size() + 1),
                minPlayers,
                maxPlayers,
                onStart
        );
        activeChannels.add(newChannel);

        for (int i = 0; i < maxPlayers && !waitingQueue.isEmpty(); i++) {
            newChannel.addPlayer(waitingQueue.poll());
        }
        newChannel.checkStartCondition();
    }

    public List<GameChannel> getActiveChannels() {
        return Collections.unmodifiableList(activeChannels);
    }

    public Collection<UUID> getWaitingQueue() {
        return Collections.unmodifiableCollection(waitingQueue);
    }

    public void cleanup() {
        for (GameChannel channel : activeChannels) {
            channel.cleanup();
        }
        activeChannels.clear();
        waitingQueue.clear();
    }

    public synchronized void removeInactiveChannels() {
        Iterator<GameChannel> iterator = activeChannels.iterator();
        while (iterator.hasNext()) {
            GameChannel channel = iterator.next();
            if (!channel.isRunning() && channel.getActivePlayers().isEmpty()) {
                iterator.remove();
            }
        }
    }
}
