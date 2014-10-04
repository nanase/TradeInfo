package net.nanase.minecraft;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradeInfo extends JavaPlugin {
    private static TradeInfo TradeInfo;
    private int schedulerNum = -1;
    private static FileConfiguration config;

    static Logger log;

    public static final String PluginDir = "plugins/TradeInfo/";

    public TradeInfo() {
        TradeInfo = this;
    }

    @Override
    public void onEnable() {
        config = this.getConfig();
        log = this.getLogger();

        if (!this.checkConfig()) {
            return;
        }

        this.loadLibrary();

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        this.schedulerNum = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                getServer().savePlayers();

                if (getServer().getOnlinePlayers().length == 0) {
                    return;
                }

                World world = getNormalWorld();

                if (world == null) {
                    return;
                }

                JsonRoot root = new JsonRoot();

                if (root.extract(world)) {
                    try {
                        JSON.encode(root, new FileWriter(config.getString("output")));
                    } catch (IOException ex) {
                        getLogger().log(Level.SEVERE, "出力ファイルに書き込めません.", ex);
                    } catch (JSONException ex) {
                        getLogger().log(Level.SEVERE, "JSON の生成に失敗しました.", ex);
                    }
                }
            }
        }, config.getLong("delay"), config.getLong("interval"));                // 1 tick = 0.02 s = 20 ms
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getScheduler().cancelTask(this.schedulerNum);
    }

    private void loadLibrary() {
        try {
            final File[] libs = new File[]{
                    new File(getDataFolder(), "jsonic-1.3.1")};

            for (final File lib : libs) {
                if (!lib.exists()) {
                    JarUtils.extractFromJar(lib.getName(), lib.getAbsolutePath());
                }
            }
            for (final File lib : libs) {
                if (!lib.exists()) {
                    getLogger().warning("There was a critical error loading plugin! Could not find lib: " + lib.getName());
                    Bukkit.getServer().getPluginManager().disablePlugin(this);
                    return;
                }
                addClassPath(JarUtils.getJarUrl(lib));
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{url});
        } catch (final IllegalAccessException | IllegalArgumentException |
                NoSuchMethodException | SecurityException | InvocationTargetException t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url + " to system classloader");
        }
    }

    private boolean checkConfig() {
        if (config.getString("output") == null) {
            this.createConfig();
        }

        if (config.getString("output").isEmpty()) {
            log.warning("出力パスが指定されていません. 設定ファイルを確認してください.");
            return false;
        }

        log.info("output: " + config.getString("output"));

        if (config.getLong("interval") < 0) {
            log.warning("更新間隔が不正です. 設定ファイルを確認してください.");
            return false;
        }

        log.info("interval: " + config.getLong("interval"));

        if (config.getLong("delay") < 0) {
            log.warning("ディレイ時間が不正です. 設定ファイルを確認してください.");
            return false;
        }

        log.info("delay: " + config.getLong("delay"));

        new File(TradeInfo.PluginDir).mkdirs();
        return true;
    }

    private void createConfig() {
        config.set("output", "");
        config.set("interval", 6000L);
        config.set("delay", 0L);
        this.saveConfig();

        log.info("設定ファイルにデフォルト設定を作成しました.");
    }

    private World getNormalWorld() {
        for (World w : Bukkit.getWorlds()) {
            if (w.getEnvironment() != Environment.NORMAL) {
                continue;
            }

            return w;
        }

        return null;
    }

    public static <T extends Object> NBTTagCompound getTag(T object) {
        NBTTagCompound compound = new NBTTagCompound();

        Class<? extends Object> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (("b".equals(method.getName()))
                    && (method.getParameterTypes().length == 1)
                    && (method.getParameterTypes()[0] == NBTTagCompound.class)) {
                try {
                    method.invoke(object, compound);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return compound;
    }
}
