package com.doubledeltas.minecollector.command.impl;

import com.doubledeltas.minecollector.MineCollector;
import com.doubledeltas.minecollector.command.CommandRoot;
import com.doubledeltas.minecollector.command.impl.book.BookOpenCommand;
import com.doubledeltas.minecollector.item.itemCode.StaticItem;
import com.doubledeltas.minecollector.util.MessageUtil;
import com.doubledeltas.minecollector.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class BookCommand extends CommandRoot {

    public BookCommand() {
        this.subcommands = List.of(new BookOpenCommand());
    }

    @Override
    public String getName() { return "도감"; }

    @Override
    public boolean onRawCommand(CommandSender sender, Command command, String label, String[] args) {
        // OP나 콘솔이 아닌 경우 실행 불가
        if (sender instanceof Player && !sender.isOp()) {
            MessageUtil.send(sender, "이 명령어는 관리자만 사용할 수 있습니다!");
            return false;
        }
        
        // 플레이어 지정 필수
        if (args.length == 0) {
            MessageUtil.send(sender, "도감을 지급할 플레이어를 지정해주세요!");
            return false;
        }
        
        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            // 한글 닉네임으로도 플레이어를 찾아봅니다
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getName().equals(playerName) || 
                    onlinePlayer.getDisplayName().equals(playerName)) {
                    target = onlinePlayer;
                    break;
                }
            }
            
            if (target == null) {
                MessageUtil.send(sender, "해당 플레이어를 찾을 수 없습니다!");
                return false;
            }
        }

        ItemStack collectionBook = MineCollector.getInstance().getItemManager().getItem(StaticItem.COLLECTION_BOOK);
        
        // 도감 아이템이 null인 경우 처리
        if (collectionBook == null) {
            MessageUtil.send(sender, "도감 아이템을 생성하는 데 실패했습니다!");
            return false;
        }

        if (target.getInventory().contains(collectionBook) || target.getInventory().getItemInOffHand().equals(collectionBook)) {
            MessageUtil.send(sender, target.getName() + "님은 이미 도감을 가지고 있습니다!");
            if (sender instanceof Player) {
                SoundUtil.playFail((Player) sender);
            }
        }
        else {
            target.getInventory().addItem(collectionBook);
            MessageUtil.send(target, "도감을 인벤토리에 넣어드렸어요! 다음엔 잃어버리지 않게 조심하세요!");
            MessageUtil.send(sender, target.getName() + "님에게 도감을 지급했습니다!");
            SoundUtil.playHighRing(target);
        }

        return false;
    }
}
