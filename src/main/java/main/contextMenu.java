package main;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class contextMenu extends ListenerAdapter {

    private String getActivities(List activitiesList) {
        String activitie = "";
        if (!activitiesList.isEmpty()) {
            Activity tempActiv = (Activity) activitiesList.get(0);
            for (int i = 1; i < activitiesList.size(); i++) {
                tempActiv = (Activity) activitiesList.get(i);
                activitie = activitie + ", " + tempActiv;
            }
        } else {
            activitie = "No activitie";
        }
        return activitie;
    }

    private String getRolesAsString(List rolesList) {
        String roles = "";
        if (!rolesList.isEmpty()) {
            Role tempRole = (Role) rolesList.get(0);
            roles = tempRole.getAsMention();
            for (int i = 1; i < rolesList.size(); i++) {
                tempRole = (Role) rolesList.get(i);
                roles = roles + ", " + tempRole.getAsMention();
            }
        } else {
            roles = "No roles";
        }
        return roles;
    }

    @Override
    public void onUserContextInteraction(@Nonnull UserContextInteractionEvent event) {
        if (event.getName().equals("Get user information")) {
            User user = event.getTarget();
            Member member = event.getTargetMember();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("Member Info");
            emb.setDescription(user.getName() + " joined on " + member.getTimeJoined().format(fmt));
            emb.setColor(member.getColor());
            emb.setThumbnail(user.getAvatarUrl());
            emb.setAuthor("Information on " + user.getAsTag());
            emb.addField("Nickname: ", member.getNickname() == null ? "No Nickname" : member.getNickname(), false);
            emb.addField("Status: ", member.getOnlineStatus().toString(), false);
            emb.addField("Game: ", getActivities(member.getActivities()), false);
            emb.addField("Roles: ", getRolesAsString(member.getRoles()), false);
            emb.addField("Server Bost: ", member.getTimeBoosted() == null ? "Has never boosted this Server"
                    : member.getTimeBoosted().format(fmt), false);

            event.replyEmbeds(emb.build()).setEphemeral(true).queue();
        }
    }
}
