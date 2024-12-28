package org.wayggstar.queueLib;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameTypeManager {
    private final Map<String, GameType> gameTypes = new ConcurrentHashMap<>();

    public void registerGameType(String typeName, int minPlayers, int maxPlayers, Runnable onStart) {
        if (gameTypes.containsKey(typeName)) {
            throw new IllegalArgumentException("이미 존재하는 게임 타입입니다.");
        }
        gameTypes.put(typeName, new GameType(typeName, minPlayers, maxPlayers, onStart));
    }

    public GameType getGameType(String typeName) {
        return gameTypes.get(typeName);
    }

    public Map<String, GameType> getGameTypes() {
        return Collections.unmodifiableMap(gameTypes);
    }
}
