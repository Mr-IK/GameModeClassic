package jp.mrik.gamemodeclassic;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class GameModeClassic extends JavaPlugin  implements TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;

        if(!p.hasPermission("minecraft.command.gamemode")){
            p.sendMessage("§cそのコマンドを実行する権限がありません！");
            return true;
        }

        switch (args.length){

            case 1:{
                setPlayerGamemode(p,args[0],null);
                return true;
            }

            case 2:{
                setPlayerGamemode(p,args[0],args[1]);
                return true;
            }

            default:{
                p.sendMessage("/gamemode 0|survival <playerName> : ゲームモードをサバイバルに設定します。");
                p.sendMessage("/gamemode 1|creative <playerName> : ゲームモードをクリエイティブに設定します。");
                p.sendMessage("/gamemode 2|adventure <playerName> : ゲームモードをアドベンチャーに設定します。");
                p.sendMessage("/gamemode 3|spectator <playerName> : ゲームモードをスペクテイターに設定します。");
                p.sendMessage("<playerName>を省くと自分を対象にします。");
                return true;
            }
        }
    }

    public void setPlayerGamemode(Player executor,String gamemode,String targetname){
        GameMode gameMode = getGameModeFromString(gamemode);
        if(gameMode==null){
            executor.sendMessage("§c指定されたゲームモードは存在しません！");
            return;
        }
        Player target;
        if(targetname==null){
            target = executor;
        }else{
            target = getSafeOnlinePlayer(targetname);
            if(target==null){
                executor.sendMessage("§c指定されたプレイヤーは現在オフラインです！");
                return;
            }
        }

        target.setGameMode(gameMode);
        Bukkit.getLogger().info("["+executor.getName()+": "+target.getName()+"のゲームモードを"+gameMode.name()+"に変更しました]");
    }

    public Player getSafeOnlinePlayer(String targetname){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getName().equalsIgnoreCase(targetname)){
                return p;
            }
        }
        return null;
    }

    public GameMode getGameModeFromString(String gamemode){
        gamemode = gamemode.toLowerCase();
        switch (gamemode){
            case "0":
            case "survival": {
                return GameMode.SURVIVAL;
            }

            case "1":
            case "creative": {
                return GameMode.CREATIVE;
            }

            case "2":
            case "adventure": {
                return GameMode.ADVENTURE;
            }

            case "3":
            case "spectator": {
                return GameMode.SPECTATOR;
            }

            default:{
                return null;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("gamemode")) { // checking if my command is the one i'm after

            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion

            if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement

                autoCompletes.add("survival");
                autoCompletes.add("creative");
                autoCompletes.add("adventure");
                autoCompletes.add("spectator");

                return autoCompletes; // then return the list

            }else if (args.length == 2) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement

                for(Player p : Bukkit.getOnlinePlayers()){
                    autoCompletes.add(p.getName());
                }

                return autoCompletes; // then return the list

            }

        }

        return null; // this will return nothing if it wasn't the disguise command I have
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("gamemode").setExecutor(this);
        getCommand("gamemode").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
