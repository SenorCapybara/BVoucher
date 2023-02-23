package com.bahhzinga.bvouchers;

import com.bahhzinga.bvouchers.backend.files.BVoucherFile;
import com.bahhzinga.bvouchers.frontend.command.BVoucherCommand;
import com.bahhzinga.bvouchers.frontend.event.ClaimEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public void onEnable(){

        getServer().getPluginManager().registerEvents(new ClaimEvent(), this);
        getCommand("bvouchers").setExecutor(new BVoucherCommand());
        BVoucherFile.create();
    }
}