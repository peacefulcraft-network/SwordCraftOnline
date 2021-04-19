package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.attribute.Attribute;

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
        public String getFile() { return this.file; }

    private String internalName;
        public String getInternalName() { return this.internalName; }

    private List<MythicConfig> stepConfigs;

    private MythicConfig config;

    /**Name of quest used to store/index quest */
    private String questName;
        public String getQuestName() { return this.questName; }

    /**Loaded Quest Steps */
    private List<QuestStep> questSteps;
        public QuestStep getQuestStep(int i) { return questSteps.get(i); }

    private Boolean isInvalid = false;
        public Boolean isInvalid() { return this.isInvalid; }

    /**Number of steps in quest */
    private int size;
        public int getSize() { return this.size; }

    /**
     * Determines if quest is story
     * If false, quest will be freely assigned to quest givers
     */
    private Boolean isStoryQuest;
        public Boolean isStoryQuest() { return this.isStoryQuest; }

    /**
     * Determines if quest was marked deleted by Cardinal
     * If true, quest cannot be given but can be loaded and completed.
     */
    private Boolean isDeleted;
        public Boolean isDeleted() { return this.isDeleted; }
        public void setDeleted(Boolean b) { this.isDeleted = b; }

    /**
     * Amount of time a quest is available to recieve in game before swapping out
     * In minutes
     */
    private Integer existTime;
        public Integer getExistTime() { return this.existTime; }

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
    private String swordSkillReq = null;
        public String getSwordSkillReq() { return this.swordSkillReq; }

    /**Required quest to complete before gaining quest */
    private String questReq = null;
        public String getQuestReq() { return this.questReq; }

    public Quest(String file, String internalName, List<MythicConfig> steps, MythicConfig data) {
        this.file = file;
        this.internalName = internalName;
        this.questSteps = new ArrayList<>();
        
        //Config is main requirement data, stepConfigs is individual steps.
        this.config = data;
        this.stepConfigs = steps;

        this.questName = config.getString("Name");
        this.size = stepConfigs.size();
        
        //Loading requirements
        this.levelReq = config.getInteger("Requirement.Level", 0);
        this.levelReq = config.getInteger("Requirement.Lvl", 0);
        this.floorReq = config.getInteger("Requirement.Floor", 0);
        this.armorReq = config.getDouble("Requirement.Armor", 0);
        this.speedReq = config.getDouble("Requirement.Speed", 0);
        this.swordSkillReq = config.getString("Requirement.SwordSkill");
        this.questReq = config.getString("Requirement.Quest");

        this.isStoryQuest = config.getBoolean("IsStoryQuest", false);
        this.existTime = config.getInteger("ExistTime", 300);
        this.isDeleted = config.getBoolean("IsDeleted", false);

        //Loading Quest Steps
        for(MythicConfig sConfig : this.stepConfigs) {
            try {
                QuestType type = QuestType.valueOf(sConfig.getString("Type").toUpperCase());
                switch(type) {
                    case KILL:
                        validateStep(new KillQuestStep(sConfig));
                    break; case ESCORT:
                        validateStep(new EscortQuestStep(sConfig));
                    break; case DELIVER:
                        validateStep(new DeliverQuestStep(sConfig));
                    break; case GATHER:
                        validateStep(new GatherQuestStep(sConfig));
                    break; case TRAVEL:
                        validateStep(new TravelQuestStep(sConfig));
                }

            } catch(IllegalArgumentException ex) {
                //We are already catching the exception internally
                return;
            }
        }     
    }

    private void validateStep(QuestStep qs) {
        if(qs.isInvalid()) { 
            this.isInvalid = true; 
            SwordCraftOnline.logDebug("[Quest] Step marked invalid: " + qs.getName());
        }
        
        // If null pointer is thrown it means a step went bad.
        try {
            this.questSteps.add(qs);
            qs.updateDescription(null, null);
        } catch(NullPointerException ex) {
            this.isInvalid = true;
            SwordCraftOnline.logDebug("[Quest] Step marked invalid, null: " + qs.getName());
            return;
        }

        //If the step is first we ensure it uses npc
        if(this.questSteps.indexOf(qs) == 0) {
            if(qs.getGiverName().isEmpty() || qs.getGiverName() == null) {
                this.isInvalid = true;
                SwordCraftOnline.logDebug("[Quest] Step marked invalid, giver: " + qs.getName());
            } else {
                qs.setUsesGiver(true);
            }
        }
    }

    /**@return true if player passes requirements for quest */
    public boolean checkRequirements(SCOPlayer s) {
        if(this.floorReq != 0 && s.getFloor() != this.floorReq) {
            return false;
        }
        if(this.levelReq != 0 && s.getLevel() < this.levelReq) { 
            return false;
        }
        if(this.armorReq != 0 && s.getAttribute(Attribute.GENERIC_ARMOR) < this.armorReq) {
            return false;
        }
        if(this.speedReq != 0 && s.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) < this.speedReq) {
            return false;
        }
        //TODO: Check sword skill is in player inventory
        //TODO: Check if player has completed a quest
        return true;
    }

    /**
     * Returns deep copy of quest
     * @return
     */
    public Quest copy() {
        Quest copy = new Quest(file, internalName, this.stepConfigs, this.config);
        return copy;
    }

}