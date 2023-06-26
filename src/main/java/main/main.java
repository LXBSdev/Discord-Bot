package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.EnumSet;

public class main {
    public static void main(String[] args) throws Exception {

        String prefix = "/";

        String status = "with friends on lxbs.de";

        String token = "";

        JDABuilder bauplan = JDABuilder.createDefault(token);

        bauplan.setStatus(OnlineStatus.ONLINE);
        bauplan.setActivity(Activity.playing(status));

        bauplan.setChunkingFilter(ChunkingFilter.ALL);
        bauplan.setMemberCachePolicy(MemberCachePolicy.ALL);
        bauplan.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES);

        EnumSet<CacheFlag> enumSet = EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE,
                CacheFlag.VOICE_STATE);

        bauplan.addEventListeners(new welcome(), new memes(), new commands());

        JDA bot = bauplan.build();
        System.out.println("the bot is now online (LXBS Support Bot)!");
        System.out.print("The prefix is set to : " + prefix);

    }
}
