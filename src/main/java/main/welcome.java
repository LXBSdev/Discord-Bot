package main;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class welcome extends ListenerAdapter {

    public void onGuildMemberJoin (GuildMemberJoinEvent event) {

        User user = event.getUser();
        String username = event.getUser().getAsTag();
        String profile = event.getUser().getAvatarUrl();

        EmbedBuilder emb = new EmbedBuilder();

        emb.setTitle("Wilkommen auf LXBS!");
        emb.setColor(0x212e94);
        emb.setDescription("Willkommen **" + username + "** auf lxbs.de!");
        emb.setThumbnail(profile);
        emb.addField("Viel Spaß", "Mehr Infos unter [lxbs.de](http://lxbs.de)", false);

        event.getGuild().getTextChannelById("965307484296347658").sendMessageEmbeds(emb.build()).queue();
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071550714372956241")).queue();

    }

}
