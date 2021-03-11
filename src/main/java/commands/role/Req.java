package commands.role;

import commands.role.objects.PlayerClass;
import commands.role.objects.PlayerStats;

public class Req {

    public enum Type {
        GATHERING_SKILL,
        CRAFTING_SKILL,
        TOTAL_LVL,
        ALL_PROF_LVL,
        TOTAL_LVL_PROFS,
        TOTAL_LVL_PROFS_ALL_CLASS,
        HIC_OR_HICH_CLASS;
    }

    Type type = null;
    int lvl = 0;
    int num = 1;
    Req[] subReqs = null;
    int subReqNum = 0;

    Req(Type type, int lvl, int num, Req[] subReq, int subReqNum){
        this.type = type;
        this.lvl = lvl;
        this.num = num;
        this.subReqs = subReq;
        this.subReqNum = subReqNum;
    }

    Req(Type type, int lvl, int num){
        this.type = type;
        this.lvl = lvl;
        this.num = num;
    }

    Req(Type type, int lvl){
        this.type = type;
        this.lvl = lvl;
    }

    public static boolean meetsReq(PlayerStats stats, Req[] reqList, int number, StringBuilder sb, boolean verbose){
        int t = 0;
        for (Req req : reqList){
            if (req.meetsReq(stats, sb, verbose)) {
                t++;
                if (verbose)
                    sb.append(":ballot_box_with_check: - ");
            } else {
                if (verbose)
                    sb.append(":x: - ");
            }
            if (verbose)
                sb.append(req.getNum()).append("+ ").append(req.getType().name()).append(" lvl ").append(req.getLvl()).append("+\n");
        }
        return t >= number;
    }

    public boolean meetsReq(PlayerStats stats, StringBuilder sb, boolean verbose){
        int matchCount = 0;
        switch (this.type){
            case TOTAL_LVL_PROFS:
                for (int i = 0; i < stats.getClasses().size(); i++){
                    PlayerClass playerClass = stats.getClasses().get(i);
                    if (playerClass == null)
                        continue;
                    if (playerClass.getTotalProfLvl() >= this.lvl){
                        matchCount += 1;
                    }
                }
                break;
            case GATHERING_SKILL:
                for (int i = 0; i < stats.getClasses().size(); i++){
                    PlayerClass playerClass = stats.getClasses().get(i);
                    if (playerClass == null)
                        continue;
                    for (int lvl : playerClass.getGatheringLvls()){
                        if (lvl >= this.lvl){
                            matchCount += 1;
                        }
                    }
                }
                break;
            case CRAFTING_SKILL:
                for (int i = 0; i < stats.getClasses().size(); i++){
                    PlayerClass playerClass = stats.getClasses().get(i);
                    if (playerClass == null)
                        continue;
                    for (int lvl : playerClass.getCraftingLvls()){
                        if (lvl >= this.lvl){
                            matchCount += 1;
                        }
                    }
                }
                break;
            case TOTAL_LVL:
                for (int i = 0; i < stats.getClasses().size(); i++){
                    PlayerClass playerClass = stats.getClasses().get(i);
                    if (playerClass == null)
                        continue;
                    if (playerClass.getTotalProfLvl() + playerClass.getCombatLevel() >  this.lvl)
                        matchCount += 1;
                }
                break;
            case ALL_PROF_LVL:
                for (int i = 0; i < stats.getClasses().size(); i++){
                    PlayerClass playerClass = stats.getClasses().get(i);
                    if (playerClass == null)
                        continue;
                    int c = 0;
                    for (int lvl: playerClass.allProfLvls()){
                        if (lvl >= this.lvl)
                            c += 1;
                    }
                    if (c == 12)
                        matchCount += 1;
                }
                break;
            case TOTAL_LVL_PROFS_ALL_CLASS:
                int tLvl = 0;
                for (int i = 0; i < stats.getClasses().size(); i++){
                    PlayerClass playerClass = stats.getClasses().get(i);
                    if (playerClass == null)
                        continue;
                    tLvl += playerClass.getTotalProfLvl();
                }
                if (tLvl >= this.lvl){
                    matchCount += 1;
                }
                break;
            case HIC_OR_HICH_CLASS:
                for (int i = 0; i < stats.getClasses().size(); i++){
                    PlayerClass playerClass = stats.getClasses().get(i);
                    if (playerClass == null)
                        continue;
                    if (playerClass.isHIC() && playerClass.getCombatLevel() >= this.lvl)
                        matchCount += 1;
                }
                break;
        }

        if (matchCount >= this.num){
            if (this.subReqNum == 0) {
                return true;
            } else {
                return meetsReq(stats, this.subReqs, this.subReqNum, sb, verbose);
            }
        } else {
            return false;
        }

    }

    public Type getType() {
        return type;
    }

    public int getNum() {
        return num;
    }

    public int getLvl() {
        return lvl;
    }
}
