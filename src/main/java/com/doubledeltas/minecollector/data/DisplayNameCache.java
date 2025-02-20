package com.doubledeltas.minecollector.data;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class DisplayNameCache implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DataManager.updateDisplayName(player);
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        DataManager.updateDisplayName(player);
    }
    
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        // 닉네임이나 칭호 변경 관련 명령어일 때만 업데이트
        String command = event.getMessage().toLowerCase();
        if (command.startsWith("/nick") || command.startsWith("/prefix") || 
            command.startsWith("/nickname") || command.startsWith("/title")) {
            Player player = event.getPlayer();
            DataManager.updateDisplayName(player);
        }
    }

    public static String getDisplayName(UUID uuid) {
        return DataManager.getDisplayName(uuid);
    }
} 