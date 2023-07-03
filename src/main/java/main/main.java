package main;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;

public class main {

    private final Dotenv config = Dotenv.configure().load();
    String token = config.get("TOKEN");
    String status = config.get("STATUS");

    private final ShardManager shardManager;

    public main() throws LoginException {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing(status));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
        builder.addEventListeners(new memes(), new support(), new welcome(), new commands(), new contextMenu(),
                new commandRegister());
        shardManager = builder.build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            main bot = new main();
            System.out.println("SUCCESS: The LXBS Support Bot is now online");
        } catch (LoginException exception) {
            System.out.println("ERROR: Provided bot token is invalid");
        }
    }
}
