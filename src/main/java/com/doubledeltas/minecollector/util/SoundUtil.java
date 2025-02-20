package com.doubledeltas.minecollector.util;

import com.doubledeltas.minecollector.MineCollector;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class SoundUtil {
    /**
     * 높은 음의 경험치 획득 소리를 재생합니다.
     * 주로 긍정적인 알림에 사용됩니다.
     */
    public static void playHighRing(Player player) {
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
    }

    /**
     * 비콘 비활성화 소리를 재생합니다.
     * 주로 실패나 오류 알림에 사용됩니다.
     */
    public static void playFail(Player player) {
        player.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 1, 2);
    }

    /**
     * 책장 넘기는 소리를 여러 번 연속해서 재생합니다.
     * 여러 페이지를 빠르게 넘기는 효과를 줍니다.
     */
    public static void playPageAll(Player player) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        for (int i=0; i<6; i++) {
            scheduler.runTaskLater(MineCollector.getInstance(), () -> {
                for (int j=0; j<2; j++)
                    player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1F, 2F);
            }, 2 + i);
        }
    }

    /**
     * 베틀에서 무늬를 선택하는 소리를 재생합니다.
     * 단일 페이지 전환이나 선택 효과에 사용됩니다.
     */
    public static void playPage(Player player) {
        player.playSound(player, Sound.UI_LOOM_SELECT_PATTERN, 0.5F, 1F);
    }

    /**
     * 레벨업과 비콘 활성화 소리를 조합하여 재생합니다.
     * 아이템 수집 완료시 사용됩니다.
     */
    public static void playCollect(Player player) {
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
        player.playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 1F, 2F);
    }

    /**
     * 도전 과제 완료 소리와 부드러운 알림음을 재생합니다.
     * 전설급 아이템 획득시 사용됩니다.
     */
    public static void playLegend(Player player) {
        SoundUtil.playGentleAlert(player);
        player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1F, 2F);
    }

    /**
     * 차임벨과 종소리를 조합한 부드러운 알림음을 재생합니다.
     * 일반적인 알림에 사용되며, 조용하고 부드러운 효과를 줍니다.
     */
    public static void playGentleAlert(Player player) {
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 0.7F, 1.2F);
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5F, 1F);
    }
}
