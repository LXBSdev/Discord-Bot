package main;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.*;

public class welcome extends ListenerAdapter {

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {

        User user = event.getUser();
        Member member = event.getMember();
        String profile = event.getUser().getAvatarUrl();

        EmbedBuilder emb = new EmbedBuilder();

        emb.setTitle("Welcome to LXBS!");
        emb.setColor(member.getColor());
        emb.setDescription("Welcome **" + user.getAsMention() + "** to lxbs.online!");
        emb.setThumbnail(profile);
        emb.addField("Have Fun", "More infos at [lxbs.online](http://lxbs.online)", false);

        event.getGuild().getTextChannelById("965307484296347658").sendMessageEmbeds(emb.build()).queue();
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071550714372956241"))
                .queue();

    }

}
