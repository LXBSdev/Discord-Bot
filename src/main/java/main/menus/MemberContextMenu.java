package main.menus;

import java.time.format.DateTimeFormatter;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberContextMenu extends ListenerAdapter {

    private String getActivities(List<Activity> activitiesList) {
        StringBuilder activitie = new StringBuilder();
        if (!activitiesList.isEmpty()) {
            Activity tempActiv;
            for (int i = 1; i < activitiesList.size(); i++) {
                tempActiv = activitiesList.get(i);
                activitie.append(", ").append(tempActiv);
            }
        } else {
            activitie = new StringBuilder("No activitie");
        }
        return activitie.toString();
    }

    private String getRolesAsString(List<Role> rolesList) {
        StringBuilder roles;
        if (!rolesList.isEmpty()) {
            Role tempRole = rolesList.get(0);
            roles = new StringBuilder(tempRole.getAsMention());
            for (int i = 1; i < rolesList.size(); i++) {
                tempRole = rolesList.get(i);
                roles.append(", ").append(tempRole.getAsMention());
            }
        } else {
            roles = new StringBuilder("No roles");
        }
        return roles.toString();
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        if (event.getName().equals("Get user information")) {
            User user = event.getTarget();
            Member member = event.getTargetMember();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("Member Info");
            emb.setDescription(user.getAsMention() + " joined on " + member.getTimeJoined().format(fmt));
            emb.setColor(member.getColor());
            emb.setThumbnail(user.getAvatarUrl());
            emb.setAuthor("Information on " + user.getName());
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
