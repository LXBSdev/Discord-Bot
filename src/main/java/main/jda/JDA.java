package main.jda;

import net.dv8tion.jda.api.JDABuilder;

public class JDA {
    private static JDABuilder jdaBuilder;
    private static net.dv8tion.jda.api.JDA jda;

    public JDA() {
        jda = jdaBuilder.build();
    }

    public net.dv8tion.jda.api.JDA getJDA() {
        return jda;
    }
}
