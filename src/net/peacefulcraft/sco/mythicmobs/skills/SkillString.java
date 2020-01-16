package net.peacefulcraft.sco.mythicmobs.skills;

import java.util.regex.Matcher;

public class SkillString {
    static int rand;
    
    static Matcher Rmatcher;
    
    public static String parseMobVariables(String s, SkillCaster caster, AbstractEntity target, AbstractEntity trigger) {
      if (s == null)
        return null; 
      Long time = Long.valueOf(System.nanoTime());
      if (s.contains("<mob")) {
        s = s.replace("<mob.hp>", String.valueOf((int)caster.getEntity().getHealth()));
        s = s.replace("<mob.php>", String.valueOf((int)(caster.getEntity().getHealth() / caster.getEntity().getMaxHealth())));
        s = s.replace("<mob.mhp>", String.valueOf(caster.getEntity().getMaxHealth()));
        s = s.replace("<mob.thp>", String.valueOf(caster.getEntity().getHealth()));
        s = s.replace("<mob.uuid>", String.valueOf(caster.getEntity().getUniqueId().toString()));
        if (caster instanceof ActiveMob) {
          ActiveMob am = (ActiveMob)caster;
          if (am.getType().getDisplayName() != null) {
            s = s.replace("<mob.name>", am.getDisplayName());
          } else {
            s = s.replace("<mob.name>", "Unknown");
          } 
          s = s.replace("<mob.level>", String.valueOf(am.getLevel()));
          s = s.replace("<mob.stance>", am.getStance());
          if (am.getType().getMythicEntity() instanceof io.lumine.xikage.mythicmobs.adapters.bukkit.entities.BukkitWolf) {
            Wolf w = (Wolf)BukkitAdapter.adapt(am.getEntity());
            if (w.getOwner() != null) {
              s = s.replace("<mob.owner.name>", w.getOwner().getName().toString());
              s = s.replace("<mob.owner.uuid>", w.getOwner().getUniqueId().toString());
            } 
          } 
          if (am.hasThreatTable())
            if (am.getThreatTable().inCombat()) {
              s = s.replace("<mob.tt.top>", am.getThreatTable().getTopThreatHolder().getName());
            } else {
              s = s.replace("<mob.tt.top>", "Unknown");
            }  
        } else if (caster.getEntity().isPlayer()) {
          s = s.replace("<mob.name>", caster.getEntity().asPlayer().getName());
        } 
        s = s.replace("<mob.l.w>", caster.getEntity().getWorld().getName().toString());
        if (s.contains("<mob.l.x"))
          if (s.contains("<mob.l.x%")) {
            Rmatcher = Patterns.MSMobX.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<mob.l.x%" + Rmatcher.group(1) + ">", Integer.toString(caster.getLocation().getBlockX() + rand));
          } else {
            s = s.replace("<mob.l.x>", Integer.toString(caster.getLocation().getBlockX()));
          }  
        if (s.contains("<mob.l.y"))
          if (s.contains("<mob.l.y%")) {
            Rmatcher = Patterns.MSMobY.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<mob.l.y%" + Rmatcher.group(1) + ">", Integer.toString(caster.getLocation().getBlockY() + rand));
          } else {
            s = s.replace("<mob.l.y>", Integer.toString(caster.getLocation().getBlockY()));
          }  
        if (s.contains("<mob.l.z"))
          if (s.contains("<mob.l.z%")) {
            Rmatcher = Patterns.MSMobZ.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<mob.l.z%" + Rmatcher.group(1) + ">", Integer.toString(caster.getLocation().getBlockZ() + rand));
          } else {
            s = s.replace("<mob.l.z>", Integer.toString(caster.getLocation().getBlockZ()));
          }  
        if (s.contains("<mob.score.")) {
          Rmatcher = Patterns.MobScore.matcher(s);
          while (Rmatcher.find()) {
            String objective = Rmatcher.group(1);
            Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective);
            int score = 0;
            if (obj != null)
              score = obj.getScore(caster.getEntity().getUniqueId().toString()).getScore(); 
            s = s.replace("<mob.score." + objective + ">", "" + score);
          } 
        } 
      } 
      if (s.contains("<caster")) {
        s = s.replace("<caster.hp>", String.valueOf((int)caster.getEntity().getHealth()));
        s = s.replace("<caster.php>", String.valueOf((int)(caster.getEntity().getHealth() / caster.getEntity().getMaxHealth())));
        s = s.replace("<caster.mhp>", String.valueOf(caster.getEntity().getMaxHealth()));
        s = s.replace("<caster.thp>", String.valueOf(caster.getEntity().getHealth()));
        s = s.replace("<caster.uuid>", String.valueOf(caster.getEntity().getUniqueId().toString()));
        if (caster instanceof ActiveMob) {
          ActiveMob am = (ActiveMob)caster;
          if (am.getType().getDisplayName() != null) {
            s = s.replace("<caster.name>", am.getDisplayName());
          } else {
            s = s.replace("<caster.name>", "Unknown");
          } 
          s = s.replace("<caster.level>", String.valueOf(am.getLevel()));
          s = s.replace("<caster.stance>", am.getStance());
          if (am.getType().getMythicEntity() instanceof io.lumine.xikage.mythicmobs.adapters.bukkit.entities.BukkitWolf) {
            Wolf w = (Wolf)BukkitAdapter.adapt(am.getEntity());
            if (w.getOwner() != null) {
              s = s.replace("<caster.owner.name>", w.getOwner().getName().toString());
              s = s.replace("<caster.owner.uuid>", w.getOwner().getUniqueId().toString());
            } 
          } 
          if (am.hasThreatTable())
            if (am.getThreatTable().inCombat()) {
              s = s.replace("<caster.tt.top>", am.getThreatTable().getTopThreatHolder().getName());
            } else {
              s = s.replace("<caster.tt.top>", "Unknown");
            }  
        } else if (caster.getEntity().isPlayer()) {
          s = s.replace("<caster.name>", caster.getEntity().asPlayer().getName());
        } 
        s = s.replace("<caster.l.w>", caster.getEntity().getWorld().getName().toString());
        if (s.contains("<caster.l.x"))
          if (s.contains("<caster.l.x%")) {
            Rmatcher = Patterns.MSMobX.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<caster.l.x%" + Rmatcher.group(1) + ">", Integer.toString(caster.getLocation().getBlockX() + rand));
          } else {
            s = s.replace("<caster.l.x>", Integer.toString(caster.getLocation().getBlockX()));
          }  
        if (s.contains("<caster.l.y"))
          if (s.contains("<caster.l.y%")) {
            Rmatcher = Patterns.MSMobY.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<caster.l.y%" + Rmatcher.group(1) + ">", Integer.toString(caster.getLocation().getBlockY() + rand));
          } else {
            s = s.replace("<caster.l.y>", Integer.toString(caster.getLocation().getBlockY()));
          }  
        if (s.contains("<caster.l.z"))
          if (s.contains("<caster.l.z%")) {
            Rmatcher = Patterns.MSMobZ.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<caster.l.z%" + Rmatcher.group(1) + ">", Integer.toString(caster.getLocation().getBlockZ() + rand));
          } else {
            s = s.replace("<caster.l.z>", Integer.toString(caster.getLocation().getBlockZ()));
          }  
        if (s.contains("<caster.score.")) {
          Rmatcher = Patterns.MobScore.matcher(s);
          while (Rmatcher.find()) {
            String objective = Rmatcher.group(1);
            Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective);
            int score = 0;
            if (obj != null)
              score = obj.getScore(caster.getEntity().getUniqueId().toString()).getScore(); 
            s = s.replace("<caster.score." + objective + ">", "" + score);
          } 
        } 
      } 
      if (s.contains("<target") && target != null) {
        if (target != null && target.isPlayer()) {
          s = s.replace("<target.name>", target.asPlayer().getName());
        } else if (target != null && target.getName() != null) {
          s = s.replace("<target.name>", target.getName());
        } 
        s = s.replace("<target.hp>", String.valueOf((int)target.getHealth()));
        s = s.replace("<target.uuid>", String.valueOf(target.getUniqueId().toString()));
        if (caster instanceof ActiveMob && ((ActiveMob)caster).hasThreatTable())
          s = s.replace("<target.threat>", String.valueOf(((ActiveMob)caster).getThreatTable().getThreat(target))); 
        s = s.replace("<target.l.w>", target.getWorld().getName().toString());
        if (s.contains("<target.l.x"))
          if (s.contains("<target.l.x%")) {
            Rmatcher = Patterns.MSTargetX.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<target.l.x%" + Rmatcher.group(1) + ">", Integer.toString(target.getLocation().getBlockX() + rand));
          } else {
            s = s.replace("<target.l.x>", Integer.toString(target.getLocation().getBlockX()));
          }  
        if (s.contains("<target.l.y"))
          if (s.contains("<target.l.y%")) {
            Rmatcher = Patterns.MSTargetY.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<target.l.y%" + Rmatcher.group(1) + ">", Integer.toString(target.getLocation().getBlockY() + rand));
          } else {
            s = s.replace("<target.l.y>", Integer.toString(target.getLocation().getBlockY()));
          }  
        if (s.contains("<target.l.z"))
          if (s.contains("<target.l.z%")) {
            Rmatcher = Patterns.MSTargetZ.matcher(s);
            Rmatcher.find();
            rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
            s = s.replace("<target.l.z%" + Rmatcher.group(1) + ">", Integer.toString(target.getLocation().getBlockZ() + rand));
          } else {
            s = s.replace("<target.l.z>", Integer.toString(target.getLocation().getBlockZ()));
          }  
        if (s.contains("<target.score.")) {
          Rmatcher = Patterns.TargetScore.matcher(s);
          while (Rmatcher.find()) {
            String objective = Rmatcher.group(1);
            Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective);
            int score = 0;
            if (obj != null)
              if (target.isPlayer()) {
                score = obj.getScore(target.asPlayer().getName()).getScore();
              } else {
                score = obj.getScore(target.getUniqueId().toString()).getScore();
              }  
            s = s.replace("<target.score." + objective + ">", "" + score);
          } 
        } 
      } 
      if (s.contains("<trigger"))
        if (trigger != null) {
          if (trigger.isPlayer()) {
            s = s.replace("<trigger.name>", trigger.asPlayer().getName());
          } else if (trigger.getName() != null) {
            s = s.replace("<trigger.name>", trigger.getName());
          } else {
            s = s.replace("<trigger.name>", "Unknown");
          } 
          s = s.replace("<trigger.hp>", String.valueOf((int)trigger.getHealth()));
          s = s.replace("<trigger.uuid>", String.valueOf(trigger.getUniqueId().toString()));
          if (caster instanceof ActiveMob && ((ActiveMob)caster).hasThreatTable())
            s = s.replace("<trigger.threat>", String.valueOf(((ActiveMob)caster).getThreatTable().getThreat(trigger))); 
          s = s.replace("<trigger.l.w>", trigger.getWorld().getName().toString());
          if (s.contains("<trigger.l.x"))
            if (s.contains("<trigger.l.x%")) {
              Rmatcher = Patterns.MSTriggerX.matcher(s);
              Rmatcher.find();
              rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
              s = s.replace("<trigger.l.x%" + Rmatcher.group(1) + ">", Integer.toString(trigger.getLocation().getBlockX() + rand));
            } else {
              s = s.replace("<trigger.l.x>", Integer.toString(trigger.getLocation().getBlockX()));
            }  
          if (s.contains("<trigger.l.y"))
            if (s.contains("<trigger.l.y%")) {
              Rmatcher = Patterns.MSTriggerY.matcher(s);
              Rmatcher.find();
              rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
              s = s.replace("<trigger.l.y%" + Rmatcher.group(1) + ">", Integer.toString(trigger.getLocation().getBlockY() + rand));
            } else {
              s = s.replace("<trigger.l.y>", Integer.toString(trigger.getLocation().getBlockY()));
            }  
          if (s.contains("<trigger.l.z"))
            if (s.contains("<trigger.l.z%")) {
              Rmatcher = Patterns.MSTriggerZ.matcher(s);
              Rmatcher.find();
              rand = (MythicMobs.r.nextInt(2) == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
              s = s.replace("<trigger.l.z%" + Rmatcher.group(1) + ">", Integer.toString(trigger.getLocation().getBlockZ() + rand));
            } else {
              s = s.replace("<trigger.l.z>", Integer.toString(trigger.getLocation().getBlockZ()));
            }  
          if (s.contains("<trigger.score.")) {
            Rmatcher = Patterns.TriggerScore.matcher(s);
            while (Rmatcher.find()) {
              String objective = Rmatcher.group(1);
              Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective);
              int score = 0;
              if (obj != null)
                if (trigger.isPlayer()) {
                  score = obj.getScore(trigger.asPlayer().getName()).getScore();
                } else {
                  score = obj.getScore(trigger.getUniqueId().toString()).getScore();
                }  
              s = s.replace("<trigger.score." + objective + ">", "" + score);
            } 
          } 
        } else {
          s = s.replace("<trigger.name>", "Unknown");
        }  
      s = parseMessageSpecialChars(s);
      if (s.contains("<random")) {
        Matcher pMatcher = Patterns.VariableRanges.matcher(s);
        while (pMatcher.find()) {
          int min = Integer.parseInt(pMatcher.group(1));
          int max = Integer.parseInt(pMatcher.group(2));
          int num = MythicMobs.r.nextInt(max - min + 1) + min;
          s = s.replace(pMatcher.group(0), "" + num);
        } 
      } 
      if (s.contains("<global.score.")) {
        Rmatcher = Patterns.GlobalScore.matcher(s);
        while (Rmatcher.find()) {
          String objective = Rmatcher.group(1);
          Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective);
          int score = 0;
          if (obj != null)
            score = obj.getScore("__GLOBAL__").getScore(); 
          s = s.replace("<global.score." + objective + ">", "" + score);
        } 
      } 
      if (s.contains("<score.")) {
        Rmatcher = Patterns.GenericScore.matcher(s);
        while (Rmatcher.find()) {
          String objective = Rmatcher.group(1);
          String entry = Rmatcher.group(2);
          Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective);
          int score = 0;
          if (obj != null)
            score = obj.getScore(entry).getScore(); 
          s = s.replace("<score." + objective + "." + entry + ">", "" + score);
        } 
      } 
      return s;
    }
    
    public static String parseMessageSpecialChars(String s) {
      if (s == null)
        return null; 
      s = s.replace("<&co>", ":");
      s = s.replace("<&sq>", "'");
      s = s.replace("<&da>", "-");
      s = s.replace("<&bs>", "\\");
      s = s.replace("<&fs>", "/");
      s = s.replace("<&sp>", " ");
      s = s.replace("<&cm>", ",");
      s = s.replace("<&sc>", ";");
      s = s.replace("<&eq>", "=");
      s = s.replace("<&dq>", "\"");
      s = s.replace("<&rb>", "]");
      s = s.replace("<&lb>", "[");
      s = s.replace("<&rc>", "}");
      s = s.replace("<&lc>", "{");
      s = s.replace("<&nl>", "\n");
      s = s.replace("<&nm>", "#");
      s = s.replace("<&skull>", "☠");
      s = s.replace("<&heart>", "❤");
      s = ChatColor.translateAlternateColorCodes('&', s);
      return s;
    }
    
    public static String unparseMessageSpecialChars(String s) {
      if (s == null)
        return null; 
      s = s.replace("-", "<&da>");
      s = s.replace("\\", "<&bs>");
      s = s.replace("/", "<&fs>");
      s = s.replace(" ", "<&sp>");
      s = s.replace(",", "<&cm>");
      s = s.replace(";", "<&sc>");
      s = s.replace("=", "<&eq>");
      s = s.replace("{", "<&lc>");
      s = s.replace("}", "<&rc>");
      s = s.replace("[", "<&lb>");
      s = s.replace("]", "<&rb>");
      s = s.replace("'", "<&sq>");
      return s;
    }
    
    public static String convertLegacyVariables(String s) {
      if (s == null)
        return null; 
      s = s.replace("<mob", "<caster");
      s = s.replace("<dropper", "<caster");
      s = s.replace("<player", "<caster");
      s = s.replace("<killer", "<trigger");
      s = s.replace("$mobhp", "<mob.hp>");
      s = s.replace("$bosshp", "<mob.hp>");
      s = s.replace("$mobtruehp", "<mob.thp>");
      s = s.replace("$bosstruehp", "<mob.thp>");
      s = s.replace("$mobpercenthp", "<mob.php>");
      s = s.replace("$bosspercenthp", "<mob.php>");
      s = s.replace("$mobmaxhp", "<mob.mhp>");
      s = s.replace("$bossmaxhp", "<mob.mhp>");
      s = s.replace("$mobuuid", "<mob.uuid>");
      s = s.replace("$moblevel", "<mob.lvl>");
      s = s.replace("$level", "<mob.lvl>");
      s = s.replace("$money", "<drops.money>");
      s = s.replace("$xp", "<drops.xp>");
      if (s.contains("$boss_x"))
        if (s.contains("$boss_x%")) {
          Matcher Rmatcher = Patterns.LegacyBossX.matcher(s);
          Rmatcher.find();
          s = s.replace("$boss_x%" + Rmatcher.group(1), "<mob.l.x%" + Rmatcher.group(1) + ">");
        } else {
          s = s.replace("$boss_x", "<mob.l.x>");
        }  
      if (s.contains("$boss_y"))
        if (s.contains("$boss_y%")) {
          Matcher Rmatcher = Patterns.LegacyBossY.matcher(s);
          Rmatcher.find();
          s = s.replace("$boss_y%" + Rmatcher.group(1), "<mob.l.y%" + Rmatcher.group(1) + ">");
        } else {
          s = s.replace("$boss_y", "<mob.l.y>");
        }  
      if (s.contains("$boss_z"))
        if (s.contains("$boss_z%")) {
          Matcher Rmatcher = Patterns.LegacyBossZ.matcher(s);
          Rmatcher.find();
          s = s.replace("$boss_z%" + Rmatcher.group(1), "<mob.l.z%" + Rmatcher.group(1) + ">");
        } else {
          s = s.replace("$boss_z", "<mob.l.z>");
        }  
      if (s.contains("$player_x"))
        if (s.contains("$player_x%")) {
          Matcher Rmatcher = Patterns.LegacyPlayerX.matcher(s);
          Rmatcher.find();
          s = s.replace("$player_x%" + Rmatcher.group(1), "<trigger.l.x%" + Rmatcher.group(1) + ">");
        } else {
          s = s.replace("$player_x", "<trigger.l.x>");
        }  
      if (s.contains("$player_y"))
        if (s.contains("$player_y%")) {
          Matcher Rmatcher = Patterns.LegacyPlayerY.matcher(s);
          Rmatcher.find();
          s = s.replace("$player_y%" + Rmatcher.group(1), "<trigger.l.y%" + Rmatcher.group(1) + ">");
        } else {
          s = s.replace("$player_y", "<trigger.l.y>");
        }  
      if (s.contains("$player_z"))
        if (s.contains("$player_z%")) {
          Matcher Rmatcher = Patterns.LegacyPlayerZ.matcher(s);
          Rmatcher.find();
          s = s.replace("$player_z%" + Rmatcher.group(1), "<trigger.l.z%" + Rmatcher.group(1) + ">");
        } else {
          s = s.replace("$player_z", "<trigger.l.z>");
        }  
      s = s.replace("$player", "<target.name>");
      s = s.replace("$target", "<target.name>");
      s = s.replace("$mobname", "<mob.name>");
      s = s.replace("$boss", "<mob.name>");
      return s;
    }
    
    public static String parseMobString(String s, LivingEntity mob, LivingEntity target) {
      if (s == null)
        return s; 
      ActiveMob am = MythicMobs.inst().getMobManager().getMythicMobInstance((Entity)mob);
      MythicMobs.debug(2, "Parsing Mob String " + s);
      s = parseMessageSpecialChars(s);
      if (s.contains("$mobname"))
        s = s.replace("$mobname", am.getDisplayName()); 
      int rand = 0;
      if (s.contains("$player_x") && 
        target != null)
        if (s.contains("$player_x%")) {
          Matcher Rmatcher = Patterns.LegacyPlayerX.matcher(s);
          Rmatcher.find();
          rand = MythicMobs.r.nextInt(2);
          rand = (rand == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
          s = s.replace("$player_x%" + Rmatcher.group(1), Integer.toString(target.getLocation().getBlockX() + rand));
        } else {
          s = s.replace("$player_x", Integer.toString(target.getLocation().getBlockX()));
        }  
      if (s.contains("$player_yc") && 
        target != null)
        if (s.contains("$player_yc%")) {
          Matcher Rmatcher = Patterns.LegacyPlayerYC.matcher(s);
          Rmatcher.find();
          rand = MythicMobs.r.nextInt(2);
          rand = (rand == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
          s = s.replace("$player_yc%" + Rmatcher.group(1), Integer.toString(target.getLocation().getBlockY() + 1 + rand));
        } else {
          s = s.replace("$player_yc", Integer.toString(target.getLocation().getBlockY() + 1));
        }  
      if (s.contains("$player_y") && 
        target != null)
        if (s.contains("$player_y%")) {
          Matcher Rmatcher = Patterns.LegacyPlayerY.matcher(s);
          Rmatcher.find();
          rand = MythicMobs.r.nextInt(2);
          rand = (rand == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
          s = s.replace("$player_y%" + Rmatcher.group(1), Integer.toString(target.getLocation().getBlockY() + rand));
        } else {
          s = s.replace("$player_y", Integer.toString(target.getLocation().getBlockY()));
        }  
      if (s.contains("$player_z") && 
        target != null)
        if (s.contains("$player_z%")) {
          Matcher Rmatcher = Patterns.LegacyPlayerZ.matcher(s);
          Rmatcher.find();
          rand = MythicMobs.r.nextInt(2);
          rand = (rand == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
          s = s.replace("$player_z%" + Rmatcher.group(1), Integer.toString(target.getLocation().getBlockZ() + rand));
        } else {
          s = s.replace("$player_z", Integer.toString(target.getLocation().getBlockZ()));
        }  
      if (s.contains("$boss_x"))
        if (s.contains("$boss_x%")) {
          Matcher Rmatcher = Patterns.LegacyBossX.matcher(s);
          Rmatcher.find();
          rand = MythicMobs.r.nextInt(2);
          rand = (rand == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
          s = s.replace("$boss_x%" + Rmatcher.group(1), Integer.toString(mob.getLocation().getBlockX() + rand));
        } else {
          s = s.replace("$boss_x", Integer.toString(mob.getLocation().getBlockX()));
        }  
      if (s.contains("$boss_y"))
        if (s.contains("$boss_y%")) {
          Matcher Rmatcher = Patterns.LegacyBossY.matcher(s);
          Rmatcher.find();
          rand = MythicMobs.r.nextInt(2);
          rand = (rand == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
          s = s.replace("$boss_y%" + Rmatcher.group(1), Integer.toString(mob.getLocation().getBlockY() + rand));
        } else {
          s = s.replace("$boss_y", Integer.toString(mob.getLocation().getBlockY()));
        }  
      if (s.contains("$boss_z"))
        if (s.contains("$boss_z%")) {
          Matcher Rmatcher = Patterns.LegacyBossZ.matcher(s);
          Rmatcher.find();
          rand = MythicMobs.r.nextInt(2);
          rand = (rand == 1) ? MythicMobs.r.nextInt(1 + Integer.parseInt(Rmatcher.group(1))) : (0 - MythicMobs.r.nextInt(Integer.parseInt(Rmatcher.group(1))));
          s = s.replace("$boss_z%" + Rmatcher.group(1), Integer.toString(mob.getLocation().getBlockZ() + rand));
        } else {
          s = s.replace("$boss_z", Integer.toString(mob.getLocation().getBlockZ()));
        }  
      if (s.contains("$mobhp"))
        s = s.replace("$mobhp", String.valueOf((int)mob.getHealth())); 
      if (s.contains("$bosshp"))
        s = s.replace("$bosshp", String.valueOf((int)mob.getHealth())); 
      if (s.contains("$mobtruehp"))
        s = s.replace("$mobtruehp", String.valueOf(mob.getHealth())); 
      if (s.contains("$mobmaxhp"))
        s = s.replace("$mobmaxhp", String.valueOf((int)mob.getMaxHealth())); 
      if (s.contains("$mobtruemaxhp"))
        s = s.replace("$mobtruemaxhp", "" + mob.getMaxHealth()); 
      if (s.contains("$mobpercenthp"))
        s = s.replace("$mobpercenthp", String.valueOf((int)(mob.getHealth() / mob.getMaxHealth()))); 
      if (s.contains("$moblevel"))
        s = s.replace("$moblevel", String.valueOf(am.getLevel())); 
      if (s.contains("$level"))
        s = s.replace("$level", String.valueOf(am.getLevel())); 
      if (s.contains("$mobuuid"))
        s = s.replace("$mobuuid", mob.getUniqueId().toString()); 
      if (s.contains("$boss") && 
        am.getType() != null)
        s = s.replace("$boss", parseMobString(am.getDisplayName(), mob, target)); 
      if (s.contains("$world"))
        s = s.replace("$world", mob.getWorld().getName()); 
      if (s.contains("$threattarget"))
        if (am.getThreatTable().getTopThreatHolder() != null) {
          if (am.getThreatTable().getTopThreatHolder() instanceof Player) {
            s = s.replace("$threattargetname", ((Player)am.getThreatTable().getTopThreatHolder()).getName());
          } else if (am.getThreatTable().getTopThreatHolder().getName() != null) {
            s = s.replace("$threattargetname", am.getThreatTable().getTopThreatHolder().getName());
          } else {
            s = s.replace("$threattargetname", "Unknown");
          } 
        } else {
          s = s.replace("$threattargetname", "Unknown");
        }  
      if (s.contains("$threattargetthreat"))
        s = s.replace("$threattargetthreat", String.valueOf((int)am.getThreatTable().getTopTargetThreat())); 
      if (s.contains("$targetthreat"))
        s = s.replace("$targetthreat", String.valueOf((int)am.getThreatTable().getThreat(BukkitAdapter.adapt((Entity)target)))); 
      if (s.contains("$target") || s.contains("$player"))
        if (target != null) {
          if (target instanceof Player) {
            s = s.replace("$player", ((Player)target).getName());
            s = s.replace("$target", ((Player)target).getName());
          } else if (target.getCustomName() != null) {
            s = s.replace("$player", target.getCustomName());
            s = s.replace("$target", target.getCustomName());
          } else {
            s = s.replace("$player", "Unknown");
            s = s.replace("$target", "Unknown");
          } 
        } else {
          s = s.replace("$player", "Unknown");
          s = s.replace("$target", "Unknown");
        }  
      if (s.contains("$trigger"))
        s = s.replace("$trigger", am.getLastAggroCause().getName()); 
      s = ChatColor.translateAlternateColorCodes('&', s);
      MythicMobs.debug(2, "Returning parsed message: " + s);
      return s;
    }
  }