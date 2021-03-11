package commands.role;

import main.Main;
import net.dv8tion.jda.api.entities.Member;

public enum Role {

    ROOKIE_PROFER(819102119583023116L, 0, new Req[]{}),
    SANE_TRYHARD(819102182241992745L, 1, new Req[] {
            new Req(Req.Type.GATHERING_SKILL, 90 , 1),
            new Req(Req.Type.GATHERING_SKILL, 80 , 2),
            new Req(Req.Type.CRAFTING_SKILL , 90 , 1),
            new Req(Req.Type.CRAFTING_SKILL , 80 , 2),
            new Req(Req.Type.TOTAL_LVL      , 700, 1)
    }),
    PROF_TRYHARD(819102278622773289L, 1, new Req[] {
            new Req(Req.Type.GATHERING_SKILL, 110, 1),
            new Req(Req.Type.GATHERING_SKILL, 100, 2),
            new Req(Req.Type.GATHERING_SKILL, 90 , 3),
            new Req(Req.Type.CRAFTING_SKILL , 115, 1),
            new Req(Req.Type.CRAFTING_SKILL , 110, 2),
            new Req(Req.Type.CRAFTING_SKILL , 100, 4),
            new Req(Req.Type.ALL_PROF_LVL   , 85),
            new Req(Req.Type.TOTAL_LVL      , 1200,1),
            new Req(Req.Type.TOTAL_LVL_PROFS_ALL_CLASS, 5000)
    }),
    INSANE_TRYHARD(819315854578548747L, 1, new Req[] {
            new Req(Req.Type.GATHERING_SKILL, 129, 1),
            new Req(Req.Type.GATHERING_SKILL, 122, 2),
            new Req(Req.Type.CRAFTING_SKILL , 125, 1),
            new Req(Req.Type.CRAFTING_SKILL , 120, 3),
            new Req(Req.Type.TOTAL_LVL      , 1500,1),
            new Req(Req.Type.TOTAL_LVL_PROFS_ALL_CLASS, 8000),
            new Req(Req.Type.HIC_OR_HICH_CLASS, 103)
    });

    private final Long discordID;
    private final Req[] requirements;
    private final int needs;

    Role(Long discordID, int needs, Req[] requirements){
        this.discordID    = discordID;
        this.requirements = requirements;
        this.needs = needs;
    }

    public int getNeeds() {
        return needs;
    }

    public Req[] getRequirements() {
        return requirements;
    }

    public net.dv8tion.jda.api.entities.Role getDiscordRole() {
        return Main.jda.getRoleById(this.discordID);
    }

    //Assigns role to user
    public void assignTo(Member user){
        user.getGuild().addRoleToMember(user, getDiscordRole()).queue();
    }

}
