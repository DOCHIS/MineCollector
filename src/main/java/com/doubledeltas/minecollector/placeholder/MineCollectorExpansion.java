package com.doubledeltas.minecollector.placeholder;

/*
 * === MineCollector PlaceholderAPI 사용 예시 ===
 * 
 * 1. 점수 관련
 * %minecollector_total_score%            - 플레이어의 총 점수
 * %minecollector_collection_score%       - 플레이어의 수집 점수
 * %minecollector_stack_score%           - 플레이어의 스택 점수
 * %minecollector_advancement_score%     - 플레이어의 발전과제 점수
 * %minecollector_collection_stack_score% - 플레이어의 수집+스택 점수 합계
 * 
 * 2. 랭킹 관련
 * %minecollector_total_rank%         - 플레이어의 총점 순위
 * %minecollector_collection_rank%    - 플레이어의 수집 점수 순위
 * %minecollector_stack_rank%         - 플레이어의 스택 점수 순위
 * %minecollector_advancement_rank%   - 플레이어의 발전과제 순위
 * 
 * 3. 과별 점수 합계
 * %minecollector_department_ocean_score%        - 해양과 전체 점수
 * %minecollector_department_agriculture_score%   - 농업과 전체 점수
 * %minecollector_department_fashion_score%      - 패션과 전체 점수
 * %minecollector_department_adventure_score%    - 모험과 전체 점수
 * %minecollector_department_architecture_score% - 건축과 전체 점수
 * 
 * - 과별 순위 (1-5위)
 * %minecollector_department_rank_[1-5]_name%   - n위 학과 이름
 * %minecollector_department_rank_[1-5]_score%  - n위 학과 점수
 * 
 * 4. 순위별 플레이어 정보 (1-100위)
 * - 총점 기준
 * %minecollector_top_[1-100]_prefix%  - n위 플레이어 prefix
 * %minecollector_top_[1-100]_name%    - n위 플레이어 이름
 * %minecollector_top_[1-100]_score%   - n위 플레이어 총점
 * 
 * - 수집 점수 기준
 * %minecollector_top_[1-100]_collection_prefix% - n위 플레이어 prefix
 * %minecollector_top_[1-100]_collection_name%   - n위 플레이어 이름
 * %minecollector_top_[1-100]_collection_score%  - n위 플레이어 수집 점수
 * 
 * === 표시 형식 예시 ===
 * 
 * 1. 플레이어 정보 표시
 * 당신의 총 점수: 1234점
 * 현재 순위: 5위 (100위 밖인 경우 "100+")
 * 수집 점수: 567점
 * 수집+스택 점수: 789점
 * 
 * 2. 순위표 표시
 * === 총점 순위 ===
 * 1위: %minecollector_top_1_prefix% %minecollector_top_1_name% (%minecollector_top_1_score%점)
 * 2위: %minecollector_top_2_prefix% %minecollector_top_2_name% (%minecollector_top_2_score%점)
 * 3위: %minecollector_top_3_prefix% %minecollector_top_3_name% (%minecollector_top_3_score%점)
 * ...
 * 10위: %minecollector_top_10_prefix% %minecollector_top_10_name% (%minecollector_top_10_score%점)
 * 
 * 3. 과별 점수 표시
 * === 과별 총점 ===
 * 1위: 해양과 (15000점)
 * 2위: 농업과 (14000점)
 * 3위: 패션과 (13000점)
 * 4위: 모험과 (12000점)
 * 5위: 건축과 (11000점)
 */

import com.doubledeltas.minecollector.data.DataManager;
import com.doubledeltas.minecollector.data.GameData;
import com.doubledeltas.minecollector.data.GameStatistics;
import com.doubledeltas.minecollector.data.DisplayNameCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.UUID;
import com.doubledeltas.minecollector.data.Department;
import java.util.Arrays;

public class MineCollectorExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "minecollector";
    }

    @Override
    public String getAuthor() {
        return "DoubleDeltas";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "0";

        GameData data = DataManager.getData(player);
        if (data == null) return "0";

        GameStatistics stats = new GameStatistics(data);

        switch (identifier) {
            // 점수 관련 - 소수점 제거하고 정수로 반환
            case "total_score":
                return String.valueOf((int) stats.getTotalScore());
            case "collection_score":
                return String.valueOf((int) stats.getCollectionScore());
            case "stack_score":
                return String.valueOf((int) stats.getStackScore());
            case "advancement_score":
                return String.valueOf((int) stats.getAdvScore());
            case "collection_stack_score":  
                return String.valueOf((int) (stats.getCollectionScore() + stats.getStackScore()));
                
            // 랭킹 관련
            case "total_rank": {
                int rank = getRank(player, d -> new GameStatistics(d).getTotalScore());
                return rank > 0 ? String.valueOf(rank) : "100+";
            }
            case "collection_rank": {
                int rank = getRank(player, d -> new GameStatistics(d).getCollectionScore());
                return rank > 0 ? String.valueOf(rank) : "100+";
            }
            case "stack_rank": {
                int rank = getRank(player, d -> new GameStatistics(d).getStackScore());
                return rank > 0 ? String.valueOf(rank) : "100+";
            }
            case "advancement_rank": {
                int rank = getRank(player, d -> new GameStatistics(d).getAdvScore());
                return rank > 0 ? String.valueOf(rank) : "100+";
            }

            // 순위별 플레이어 정보
            default:
                if (identifier.matches("top_(\\d+)_prefix")) {
                    int rank = Integer.parseInt(identifier.split("_")[1]);
                    if (rank > 100) return "";
                    return getTopPlayerPrefix(rank - 1, d -> new GameStatistics(d).getTotalScore());
                }
                if (identifier.matches("top_(\\d+)_name")) {
                    int rank = Integer.parseInt(identifier.split("_")[1]);
                    if (rank > 100) return "-";
                    return getTopPlayerName(rank - 1, d -> new GameStatistics(d).getTotalScore());
                }
                if (identifier.matches("top_(\\d+)_score")) {
                    int rank = Integer.parseInt(identifier.split("_")[1]);
                    if (rank > 100) return "0";
                    return String.valueOf((int) getTopPlayerScore(rank - 1, d -> new GameStatistics(d).getTotalScore()));
                }
                if (identifier.matches("top_(\\d+)_collection_name")) {
                    int rank = Integer.parseInt(identifier.split("_")[1]);
                    if (rank > 100) return "-";
                    return getTopPlayerName(rank - 1, d -> new GameStatistics(d).getCollectionScore());
                }
                if (identifier.matches("top_(\\d+)_collection_score")) {
                    int rank = Integer.parseInt(identifier.split("_")[1]);
                    if (rank > 100) return "0";
                    return String.valueOf((int) getTopPlayerScore(rank - 1, d -> new GameStatistics(d).getCollectionScore()));
                }

                // 과별 점수 합계
                if (identifier.startsWith("department_")) {
                    Department dept = null;
                    if (identifier.equals("department_ocean_score")) dept = Department.OCEAN;
                    else if (identifier.equals("department_agriculture_score")) dept = Department.AGRICULTURE;
                    else if (identifier.equals("department_fashion_score")) dept = Department.FASHION;
                    else if (identifier.equals("department_adventure_score")) dept = Department.ADVENTURE;
                    else if (identifier.equals("department_architecture_score")) dept = Department.ARCHITECTURE;
                    
                    if (dept != null) {
                        return String.valueOf((int) getDepartmentScore(dept));
                    }
                }

                // 과별 점수 순위 (1-5위)
                if (identifier.matches("department_rank_(\\d+)_name")) {
                    int rank = Integer.parseInt(identifier.split("_")[2]);
                    if (rank > 5) return "-";
                    return getDepartmentRankName(rank - 1);
                }
                if (identifier.matches("department_rank_(\\d+)_score")) {
                    int rank = Integer.parseInt(identifier.split("_")[2]);
                    if (rank > 5) return "0";
                    return String.valueOf((int) getDepartmentRankScore(rank - 1));
                }
        }
        
        return "0";
    }

    private int getRank(Player player, java.util.function.Function<GameData, Float> keyFunc) {
        List<GameData> ranking = DataManager.getTop100(keyFunc);
        for (int i = 0; i < ranking.size(); i++) {
            GameData data = ranking.get(i);
            if (data != null && data.getUuid() != null && data.getUuid().equals(player.getUniqueId())) {
                return i + 1;
            }
        }
        return -1;
    }

    private String getTopPlayerPrefix(int index, java.util.function.Function<GameData, Float> keyFunc) {
        List<GameData> ranking = DataManager.getTop100(keyFunc);
        if (index >= ranking.size()) return "";
        
        UUID uuid = ranking.get(index).getUuid();
        
        // LuckPerms에서 prefix 가져오기
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().loadUser(uuid).join();
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix != null ? prefix.replace('&', '§') : "";
    }

    private String getTopPlayerName(int index, java.util.function.Function<GameData, Float> keyFunc) {
        List<GameData> ranking = DataManager.getTop100(keyFunc);
        if (index >= ranking.size()) return "-";
        
        UUID uuid = ranking.get(index).getUuid();
        String cachedName = DisplayNameCache.getDisplayName(uuid);
        
        if (cachedName != null) {
            return cachedName;  // prefix 제거
        }
        
        return Bukkit.getPlayer(uuid) != null 
            ? Bukkit.getPlayer(uuid).getDisplayName()
            : Bukkit.getOfflinePlayer(uuid).getName();
    }

    private float getTopPlayerScore(int index, java.util.function.Function<GameData, Float> keyFunc) {
        List<GameData> ranking = DataManager.getTop100(keyFunc);
        if (index >= ranking.size()) return 0;
        return keyFunc.apply(ranking.get(index));
    }

    private float getDepartmentScore(Department dept) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        float totalScore = 0;
        
        for (GameData data : DataManager.getTop100(d -> new GameStatistics(d).getTotalScore())) {
            User user = luckPerms.getUserManager().loadUser(data.getUuid()).join();
            String prefix = user.getCachedData().getMetaData().getPrefix();
            
            if (dept == Department.fromPrefix(prefix)) {
                totalScore += new GameStatistics(data).getTotalScore();
            }
        }
        
        return totalScore;
    }

    private String getDepartmentRankName(int index) {
        List<Department> ranking = Arrays.stream(Department.values())
                .sorted(Comparator.comparing(this::getDepartmentScore).reversed())
                .collect(Collectors.toList());
        
        if (index >= ranking.size()) return "-";
        return ranking.get(index).getKorName();
    }

    private float getDepartmentRankScore(int index) {
        List<Department> ranking = Arrays.stream(Department.values())
                .sorted(Comparator.comparing(this::getDepartmentScore).reversed())
                .collect(Collectors.toList());
        
        if (index >= ranking.size()) return 0;
        return getDepartmentScore(ranking.get(index));
    }
} 