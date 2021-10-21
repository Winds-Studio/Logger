package com.carpour.logger.Events.onInventories;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onFurnace implements Listener {

    public final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInv(FurnaceExtractEvent event){

        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();
        String worldName = player.getWorld().getName();
        int blockX = event.getBlock().getLocation().getBlockX();
        int blockY = event.getBlock().getLocation().getBlockY();
        int blockZ = event.getBlock().getLocation().getBlockZ();
        Material item = event.getItemType();
        int amount = event.getItemAmount();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (main.getConfig().getBoolean("Log.Furnace")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, "🔥 **|** \uD83D\uDC6E\u200D♂️ Has extracted **" + amount + "** of **" + item + "** at X= **" + blockX + "** Y= **" + blockY + "** Z= **" + blockZ + "**", false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has extracted " + amount + " of " + item + " at X= " + blockX + " Y= " + blockY + " Z= " + blockZ + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.furnace(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertFurnace(serverName, player, item, amount, blockX, blockY, blockZ, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getFurnaceFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has extracted " + amount + " of " + item + " at X= " + blockX + " Y= " + blockY + " Z= " + blockZ + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord Integration
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, "🔥 **|** \uD83D\uDC6E\u200D♂️ Has extracted **" + amount + "** of **" + item + "** at X= **" + blockX + "** Y= **" + blockY + "** Z= **" + blockZ + "**", false);

            } else {

                Discord.furnace(player, "🔥 Has extracted **" + amount + "** of **" + item + "** at X= **" + blockX + "** Y= **" + blockY + "** Z= **" + blockZ + "**", false);
            }

            //MySQL Handling
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.furnace(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertFurnace(serverName, player, item, amount, blockX, blockY, blockZ, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}