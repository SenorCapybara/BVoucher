package com.bahhzinga.bvouchers.frontend.event;

import com.bahhzinga.bvouchers.Main;
import com.bahhzinga.bvouchers.backend.api.BVoucher;
import com.bahhzinga.bvouchers.backend.files.BVoucherFile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.bahhzinga.bvouchers.backend.api.BVoucher.key_name;
import static com.bahhzinga.bvouchers.backend.api.BVoucher.key_uses;

public class ClaimEvent implements Listener {

    @EventHandler
    public void onVoucherClaim(PlayerInteractEvent event) {

        Player p = event.getPlayer();


        new BukkitRunnable() {

            public void run() {
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                String name = meta.getPersistentDataContainer().get(key_name, PersistentDataType.STRING);
                int current = meta.getPersistentDataContainer().get(key_uses, PersistentDataType.INTEGER);
                if (current >= 1) {

                    BVoucher voucher = new BVoucher(name, p.getUniqueId(), "", Arrays.asList(""), 0, "", 1);
                    if (BVoucherFile.getConfig().getConfigurationSection("vouchers").getKeys(false).contains(name)) {

                        if (name != null) {
                            if (voucher.getOwner(item).equals(p.getUniqueId())) {

                                if (voucher.getUses(item) >=1) {


                                    /*
                                    Rewards code
                                    ------------------------------------------------------------
                                     */
                                    // Redemption effects

                                    // Give reward
                                    String action = BVoucherFile.getConfig().getString("vouchers." + name + ".action");
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.replaceAll("<PLAYER>", p.getName()));

                                    // If there's enough uses left
                                    if (voucher.getUses(item) >= 1) {

                                        // Update uses left
                                        meta.getPersistentDataContainer().set(key_uses, PersistentDataType.INTEGER, current - 1);
                                        item.setItemMeta(meta);


                                        // if it runs out of uses remove from inventory
                                        if (voucher.getUses(item) == 0) {
                                            p.getInventory().remove(item);
                                            p.playSound(p, Sound.valueOf(BVoucherFile.getConfig().getString("vouchers." + name +".sounds.expire")), 1, 1);
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eVouchers&8] &7Your voucher has ran out and it's been removed from your inventory!"));

                                        }

                                        if (voucher.getUses(item) > 0) {
                                            // Send message
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eVouchers&8] &7You've redeemed your voucher, uses left: &e" + voucher.getUses(item) + "&7."));

                                            // Play sound
                                            p.playSound(p, Sound.valueOf(BVoucherFile.getConfig().getString("vouchers." + name + ".sounds.redeem")), 1, 1);

                                            // Send action bar
                                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                                    BVoucherFile.getConfig().getString("vouchers." + name + ".actionbar"))));

                                            // Play lightning
                                            if (BVoucherFile.getConfig().getBoolean("vouchers." + name + ".fakeLightning") == true) {
                                                p.getWorld().strikeLightningEffect(p.getLocation());
                                            }

                                            // Play particles
                                            if (BVoucherFile.getConfig().getString("vouchers." + name + ".particle") != null) {
                                                p.getWorld().spawnParticle(Particle.valueOf(BVoucherFile.getConfig().getString("vouchers." + name + ".particle")), p.getLocation(), 100);
                                            }

                                            // Play title
                                            p.sendTitle(
                                                    ChatColor.translateAlternateColorCodes('&', BVoucherFile.getConfig().getString("vouchers." + name + ".title")),
                                                    ChatColor.translateAlternateColorCodes('&', BVoucherFile.getConfig().getString("vouchers." + name + ".subtitle")),
                                                    1, 20, 5);
                                            // Check for bonus rewards
                                            if (BVoucherFile.getConfig().getBoolean("vouchers." + name + ".bonusRewards.use") == true) {

                                                int bonusCount = BVoucherFile.getConfig().getStringList("vouchers." + name + ".bonusRewards.rewards").size();
                                                Random r = new Random();
                                                double randomValue = 0 + (0 - 1) * r.nextDouble();
                                                double bonusChance = BVoucherFile.getConfig().getDouble("vouchers." + name + ".bonusRewards.probability");

                                                int rewardIndex = ThreadLocalRandom.current().nextInt(1, bonusCount + 1);
                                                String bonus = BVoucherFile.getConfig().getStringList("vouchers." + name + ".bonusRewards.rewards").get(rewardIndex);

                                                if (randomValue <= bonusChance) {
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bonus.replaceAll("<PLAYER>", p.getName()));
                                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', BVoucherFile.getConfig().getString("vouchers." + name + ".bonusRewards.message")));
                                                }

                                                if (current < 1){
                                                    p.getInventory().remove(p.getInventory().getItemInMainHand());
                                                }

                                            }

                                            // Announce to server, if announce_use = true
                                            if (BVoucherFile.getConfig().getBoolean("vouchers." + name + ".announce_use") == true) {

                                                for (Player player : Bukkit.getOnlinePlayers()) {
                                                    if (player != p) {
                                                        for (int i = 0; i < BVoucherFile.getConfig().getStringList("vouchers." + name + ".announced").size(); i++) {
                                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                    BVoucherFile.getConfig().getStringList("vouchers." + name + ".announce").get(i)));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }.runTask(Main.getPlugin(Main.class));
    }


}
