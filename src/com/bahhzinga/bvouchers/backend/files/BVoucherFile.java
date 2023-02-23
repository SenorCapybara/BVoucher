package com.bahhzinga.bvouchers.backend.files;

import com.bahhzinga.bvouchers.Main;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class BVoucherFile {

    private static File file = new File(Main.getPlugin(Main.class).getDataFolder(), "/config.yml");
    private static FileConfiguration yml = YamlConfiguration.loadConfiguration(file);

    public static void save(){
        YamlConfiguration.loadConfiguration(file);
    }

    public static File getFile(){
        return file;
    }

    public static FileConfiguration getFileConf(){
        return yml;
    }

    public static void create(){



        // If file doesn't exist
        if (!file.exists()){

            File dir = file.getParentFile();
            dir.mkdirs();
            try {
                file.createNewFile();
                yml.set("vouchers.example.displayname", "&eExample Voucher");
                yml.set("vouchers.example.lore", Arrays.asList("This is a test voucher!"));
                yml.set("vouchers.example.custommodeldata", 1);
                yml.set("vouchers.example.uses", 1);
                yml.set("vouchers.example.action", "eco give <PLAYER> 10000000");
                yml.set("vouchers.example.sounds.redeem", "ENTITY_PLAYER_LEVELUP");
                yml.set("vouchers.example.sounds.expire", "BLOCK_ANVIL_BREAK");
                yml.set("vouchers.example.announce_use", true);
                yml.set("vouchers.example.sounds.redeem", "ENTITY_PLAYER_LEVELUP");
                yml.set("vouchers.example.announcement", Arrays.asList("&8[&eVouchers] &e<PLAYER> &7has redeemed a voucher and won &a$10mn&7!"));
                yml.set("vouchers.example.actionbar", "&7You just won &e$10mn&7 from your Voucher!");
                yml.set("vouchers.example.receive.message", Arrays.asList("&8[&eVouchers&8] &7You just received a voucher!", "&7Total uses: &e2", "&7Reward: &a$10 million"));
                yml.set("vouchers.example.receive.sound", "ENTITY_PLAYER_LEVELUP");
                yml.set("vouchers.example.particle", "PORTAL");
                yml.set("vouchers.example.fakeLightning", true);
                yml.set("vouchers.example.bonusRewards.use", true);
                yml.set("vouchers.example.bonusRewards.probability", 0.25);
                yml.set("vouchers.example.bonusRewards", Arrays.asList("give <PLAYER> diamond 8", "eco give <PLAYER> 5000000", "bvoucher <PLAYER> example"));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static FileConfiguration getConfig(){
        return yml;
    }

}
