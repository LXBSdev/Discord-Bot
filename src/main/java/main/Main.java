package main;

import main.listeners.buttons.CloseButton;
import main.listeners.buttons.RefreshButton;
import main.listeners.buttons.ReplyButton;
import main.listeners.buttons.TicketButton;
import main.command.*;
import main.listeners.MemberJoinListener;
import main.menus.HelpSelectMenu;
import main.menus.MemberContextMenu;
import main.modals.ReplyModal;
import main.modals.TicketModal;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    private final Dotenv config = Dotenv.configure().load();
    String token = config.get("TOKEN");
    String status = config.get("STATUS");

    private final ShardManager shardManager;


    public Main() throws LoginException {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing(status));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
        builder.addEventListeners(
                new CloseButton(),
                new RefreshButton(),
                new ReplyButton(),
                new TicketButton(),
                new ColourCommand(),
                new CommandRegister(),
                new HelloCommand(),
                new HelpCommand(),
                new IPCommand(),
                new PronounsCommand(),
                new RulesCommand(),
                new SocialsCommand(),
                new SupportCommand(),
                new TicketCommand(),
                new UserInfoCommand(),
                new WebsiteCommand(),
                new MemberJoinListener(),
                new HelpSelectMenu(),
                new MemberContextMenu(),
                new ReplyModal(),
                new TicketModal(),
                new SendMessageCommand(),
                new AddUserToTicketCommand()
        );
        shardManager = builder.build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            Main bot = new Main();
            System.out.println("SUCCESS: The LXBS Support Bot is now online");
        } catch (LoginException exception) {
            System.out.println("ERROR: Provided bot token is invalid");
        }
    }
}
