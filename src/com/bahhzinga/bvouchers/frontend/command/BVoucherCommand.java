package com.bahhzinga.bvouchers.frontend.command;

import com.bahhzinga.bvouchers.Main;
import com.bahhzinga.bvouchers.backend.api.BVoucher;
import com.bahhzinga.bvouchers.backend.files.BVoucherFile;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BVoucherCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (s.equalsIgnoreCase("bvouchers")){

            Player player = Bukkit.getPlayer(commandSender.getName());
            if (commandSender instanceof Player){

                if (player.hasPermission("bvoucher.admin")){

                    if (args.length == 0){
                        List<String> help = Arrays.asList(
                                "&8------------ &3&lB&b&lVouchers &8------------ ",
                                " &7Version: &e" + Main.getPlugin(Main.class).getDescription().getVersion(),
                                " &7Author: &eBahzinga",
                                "&8------------ &3&lB&b&lVouchers &8------------ ",
                                " &8- &6/bvouchers &e<player> &e<voucher>&8: &eGive a player a voucher",
                                " &8- &6/bvouchers &esave&8: &7Save the configuration file",
                                "&8------------ &3&lB&b&lVouchers &8------------ ");

                        for (int i = 0;i<help.size();i++){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', help.get(i)));
                        }

                    } else {
                        if (args.length == 1){
                            if (args[0].equalsIgnoreCase("save")){;

                                try {
                                    BVoucherFile.getFileConf().load(BVoucherFile.getFile());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (InvalidConfigurationException e) {
                                    throw new RuntimeException(e);
                                }

                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&3&lB&b&lVouchers &8// &7Successfully reloaded the plugin!"));
                            }
                        }

                        if (args.length == 2){

                            Bukkit.getLogger().info(args[1]);

                            if (Bukkit.getPlayer(args[0]) != null){
                                if (BVoucherFile.getConfig().getConfigurationSection("vouchers").contains(args[1])){

                                    BVoucher voucher = new BVoucher(
                                            args[1],
                                            player.getUniqueId(),
                                            BVoucherFile.getConfig().getString("vouchers." + args[1] + ".displayname"),
                                            BVoucherFile.getConfig().getStringList("vouchers." + args[1] + ".lore"),
                                            BVoucherFile.getConfig().getInt("vouchers." + args[1] + ".customModelData"),
                                            BVoucherFile.getConfig().getString("vouchers." + args[1] + ".action"),
                                            BVoucherFile.getConfig().getInt("vouchers." + args[1] + ".uses"));


                                    voucher.setOwner(voucher.get(), player.getUniqueId());
                                    voucher.setUses(voucher.get(), 10);
                                    voucher.setName(voucher.get(), args[1]);
                                    voucher.setAction(voucher.get(), BVoucherFile.getConfig().getString("vouchers." + args[1] + ".action"));

                                    player.getInventory().addItem(voucher.get());
                                    for (int i = 0; i<BVoucherFile.getConfig().getStringList("vouchers." + args[1] + ".receive.message").size();i++){
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', BVoucherFile.getConfig().getStringList("vouchers." + args[1] + ".receive.message").get(i)));
                                    }
                                    player.playSound(player, Sound.valueOf(BVoucherFile.getConfig().getString("vouchers." + args[1] + ".receive.sound")), 1, 1);

                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lB&b&lVouchers &8// &7There's no record of that voucher."));
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lB&b&lVouchers &8// &7Unable to find that player!"));
                            }

                        }
                    }

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&cError&8] &7You do not have permission to access that command."));
                }

            }

        }


        return false;

    }

}
