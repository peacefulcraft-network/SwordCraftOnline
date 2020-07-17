package net.peacefulcraft.sco.quests;

import java.util.List;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;
import net.peacefulcraft.sco.quests.quests.DeliverQuestStep;
import net.peacefulcraft.sco.quests.quests.EscortQuestStep;
import net.peacefulcraft.sco.quests.quests.GatherQuestStep;
import net.peacefulcraft.sco.quests.quests.KillQuestStep;
import net.peacefulcraft.sco.quests.quests.TravelQuestStep;

public class Quest {
    
    private String file;

    private String internalName;

    private MythicConfig config;

    /**Name of quest used to store/index quest */
    private String questName;
        public String getQuestName() { return this.questName; }

    /**Quest steps stored in string */
    private List<String> questStepStr;

    /**Loaded Quest Steps */
    private List<QuestStep> questSteps;
        public QuestStep getQuestStep(int i) { return questSteps.get(i); }

    /**Number of steps in quest */
    private int size;
        public int getSize() { return this.size; }

    /**Required to visit this floor before gaining quest */
    private Integer floorReq;
        public int getFloorReq() { return this.floorReq; }

    /**Required level to visit before gaining quest */
    private Integer levelReq;
        public int getLevelReq() { return this.levelReq; }

    /**Required armor level of player before gaining quest */
    private Double armorReq;
        public Double getArmorReq() { return this.armorReq; }

    /**Required speed level of player before gaining quest */
    private Double speedReq;
        public Double getSpeedReq() { return this.speedReq; }

    /**Required sword skill equipped before gaining quest */
    private String swordSkillReq;
        public String getSwordSkillReq() { return this.swordSkillReq; }

    public Quest(String file, String internalName, MythicConfig mc) {
        this.file = file;
        this.internalName = internalName;
        this.config = mc;

        this.questName = config.getString("Name");
        this.questStepStr = config.getStringList("QuestSteps");
        this.size = questStepStr.size();
        
        //Loading requirements
        this.levelReq = config.getInteger("Requirement.Level", 0);
        this.levelReq = config.getInteger("Requirement.Lvl", 0);
        this.floorReq = config.getInteger("Requirement.Floor", 0);
        this.armorReq = config.getDouble("Requirement.Armor", 0);
        this.speedReq = config.getDouble("Requirement.Speed", 0);
        this.swordSkillReq = config.getString("Requirement.SwordSkill");

        //Loading Quest Steps
        try{
            for(String s : this.questStepStr) {
                //Fetching quest type, passing original to QuestStep
                String[] split = (s.split("$Rewards:")[0]).split(" ");
                QuestType type = QuestType.valueOf(split[0]);
                
                switch(type){
                    case KILL:
                        validateStep(new KillQuestStep(type, s));
                    break; case DELIVER:
                        validateStep(new DeliverQuestStep(type, s));
                    break; case TRAVEL:
                        validateStep(new TravelQuestStep(type, s));
                    break; case GATHER:
                        validateStep(new GatherQuestStep(type, s));
                    break; case ESCORT:
                        validateStep(new EscortQuestStep(type, s));
                }
            }
        } catch(IllegalArgumentException ex) {
            SwordCraftOnline.logInfo("Issue loading QuestStep for: " + questName + " , invalid QuestType.");
            return;
        }        
    }

    private boolean validateStep(QuestStep qs) {
        if(qs.isInvalid()) {
            SwordCraftOnline.logInfo("Invalid quest step in: " + questName);
            return false;
        }
        this.questSteps.add(qs);
        return true;
    }

    /**@return true if player passes requirements for quest */
    public boolean checkRequirements(SCOPlayer s) {
        if(this.floorReq != 0 && s.getFloor() != this.floorReq) {
            return false;
        }
        if(this.levelReq != 0 && s.getLevel() < this.levelReq) { 
            return false;
        }
        if(this.armorReq != 0 && s.getArmor() < this.armorReq) {
            return false;
        }
        if(this.speedReq != 0 && s.getMovementSpeed() < this.speedReq) {
            return false;
        }
        return true;
    }

}