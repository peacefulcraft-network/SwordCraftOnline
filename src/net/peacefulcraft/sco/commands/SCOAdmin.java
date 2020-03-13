package net.peacefulcraft.sco.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.mythicmobs.drops.DropManager;
import net.peacefulcraft.sco.mythicmobs.drops.LootBag;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.particles.effect.AnimatedBallEffect;
import net.peacefulcraft.sco.particles.effect.ArcEffect;
import net.peacefulcraft.sco.particles.effect.AtomEffect;
import net.peacefulcraft.sco.particles.effect.BleedEffect;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.utilities.Generator;
import net.peacefulcraft.sco.swordskills.utilities.Validator;

public class SCOAdmin implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("scoadmin")) {
			if (args.length == 0) {
				sender.sendMessage("scoadmin [ swordskillbook | generateitem | playerdata | setplayerdata ]");
				return true;
			}

			if (args[0].equalsIgnoreCase("swordskillbook")) {
				Player p = (Player) sender;
				SwordCraftOnline.getGameManager();
				SwordSkillInventory inv = (SwordSkillInventory) GameManager.findSCOPlayer(p).getData().getInventories()
						.getInventory(InventoryType.SWORD_SKILL);
				
				inv.openInventory();
				
				return true;
			}

			if (args[0].equalsIgnoreCase("generateitem")) {
				Player p = (Player) sender;
				// TODO: ITEM VALIDATION
				//if(!Item.itemExists(args[1])) { return false; }

				// TODO: MAKE UTIL ITEMS SKILLPROVIDERS WITH NO EQUIP ABILITY
				if(args.length == 2) {
					//p.getInventory().addItem(Item.giveItem(args[1], null));
					return true;
				} 
				if(args.length == 3) {
					if(!Validator.teirExists(args[2])) {return false; }

					p.getInventory().addItem(Generator.generateItem(args[1], 1, ItemTier.valueOf(args[2])));
					return true;
				}
				return false;
			}

			if (args[0].equalsIgnoreCase("playerdata")) {
				Player p = (Player) sender;
				
				SCOPlayer s = GameManager.findSCOPlayerByName(args[1]);
				if(s == null) {
					p.sendMessage(ChatColor.RED + "Could not find player");
					return true;
				}

				p.sendMessage(s.getPlayerData());
				return true;
			}

			if (args[0].equalsIgnoreCase("setplayerdata")) {
				Player p = (Player) sender;
				
				if(args.length == 1) {
					p.sendMessage(ChatColor.GOLD + "Valid arguments: " + ChatColor.RED + "critical_chance, crit_chance, critical_multiplier, crit_mult, " + 
									"player_kills, parry, admin_override, admin_over");
					return true;
				}
				SCOPlayer s = GameManager.findSCOPlayerByName(args[1]);
				if(s == null) {
					p.sendMessage(ChatColor.RED + "Could not find player");
					return true;
				}

				double i;
				if(args.length == 2) {
					i = 1;
				} else {
					i = Double.parseDouble(args[3]);
				}

				String data = args[2];
				if(data.equalsIgnoreCase("critical_chance") || data.equalsIgnoreCase("crit_chance")) {
					s.setCriticalChance((int) i);
					p.sendMessage(ChatColor.GOLD + "Critical Chance set to: " + ChatColor.RED + i);
					return true;
				} else if(data.equalsIgnoreCase("critical_multiplier") || data.equalsIgnoreCase("crit_mult")) {
					s.setCriticalMultiplier(i);
					p.sendMessage(ChatColor.GOLD + "Critical Multiplier set to: " + ChatColor.RED + i);
					return true;
				} else if(data.equalsIgnoreCase("player_kills")) {
					p.sendMessage(ChatColor.GOLD + "Player Kills set to: " + ChatColor.RED + i);
					s.setPlayerKills((int) i);
					return true;
				} else if(data.equalsIgnoreCase("admin_override") || data.equalsIgnoreCase("admin_over")) {
					p.sendMessage(ChatColor.GOLD + "Admin Override set to: " + ChatColor.RED + s.hasOverride());
					s.setAdminOverride(!s.hasOverride());
					return true;
				} else if(data.equalsIgnoreCase("parry")) {
					p.sendMessage(ChatColor.GOLD + "Parry Chance set to: " + ChatColor.RED + i);
					s.setParryChance((int) i);
					return true;
				} else {
					p.sendMessage(ChatColor.GOLD + "Valid arguments: " + ChatColor.RED + "critical_chance, crit_chance, critical_multiplier, crit_mult, " + 
								"player_kills, parry, admin_override, admin_over");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("swordskills")) {
				SCOPlayer s;
				if(args.length > 0) {
					s = GameManager.findSCOPlayerByName(args[1]);
					if(s == null) { sender.sendMessage(ChatColor.RED + "Could not find player"); return true; }
				}else {
					s = GameManager.findSCOPlayer((Player) sender);
				}
				
				sender.sendMessage(s.getName() + "'s currently active Sword Skills");
				for(SwordSkill skill : s.getSwordSkillManager().getSkills()) {
					sender.sendMessage("- " + skill.getProvider().getName());
				}
				return true;
			}

			if(args[0].equalsIgnoreCase("mm")) {
				if(args[1].equalsIgnoreCase("loadDropTables")) {
					SwordCraftOnline.getPluginInstance().getDropManager().loadDropTables();
					return true;
				}
				if(args[1].equalsIgnoreCase("loadmobs")) {
					SwordCraftOnline.getPluginInstance().getMobManager().loadMobs();
					return true;
				}
				if(args[1].equalsIgnoreCase("spawn")) {
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.GREEN + "Cannot perform command from console.");
						return true;
					}
					Player p = (Player)sender;
					if(SwordCraftOnline.getPluginInstance().getMobManager().getMMList().keySet().contains(args[2])) {
						ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(args[2], p.getLocation());
						if(am != null) {
							sender.sendMessage(ChatColor.GREEN + "Spawned " + args[2]);
							System.out.println("Spawned " + args[2]);
							return true;
						}
						sender.sendMessage(ChatColor.GREEN + "Error Loading " + args[2] + "Active Mob Instance Null.");
						System.out.println("[MOB SPAWN DEBUG] Active Mob Instance Null");
						return true;
					}
					sender.sendMessage(ChatColor.GREEN + "File for " + args[2] + " Not Found.");
					System.out.println("[MOB SPAWN DEBUG] Not found: " + args[2]);
					return true;
				}
				if(args[1].equalsIgnoreCase("killall")) {
					int amount = SwordCraftOnline.getPluginInstance().getMobManager().removeAllMobs();
					sender.sendMessage(ChatColor.GREEN + "Removed " + amount + " Mythic Mobs!");
					System.out.println("[MOB KILL] Removed " + amount + " Mythic Mobs.");
					return true;
				}
				if(args[1].equalsIgnoreCase("list")) {
					if(args[2].equalsIgnoreCase("mobs")) {
						List<ActiveMob> mobs = new ArrayList<ActiveMob>(SwordCraftOnline.getPluginInstance().getMobManager().getActiveMobs());
						sender.sendMessage(ChatColor.GREEN + "There are: " + mobs.size() + " Active Mobs.");
						if(mobs.size() > 0) {
							String l = ChatColor.GREEN + "Mobs: \n";
							for(int i = 1; i < mobs.size(); i++) {
								l += ChatColor.GREEN + "" + i + ". " + mobs.get(i).getDisplayName() + "\n";
							}
							sender.sendMessage(ChatColor.GREEN + l);
							return true;
						}
						return true;
					} else if (args[2].equalsIgnoreCase("droptables")) {
						Set<String> tables = SwordCraftOnline.getPluginInstance().getDropManager().getDroptableMap().keySet();
						String s = ChatColor.GREEN + "DropTables: \n";
						for(String ss : tables) {
							s += ChatColor.GREEN + ss + "\n";
						}
						sender.sendMessage(s);
						return true;
					}
				}
				if(args[1].equalsIgnoreCase("generatedroptable")) {
					//Sender is console. Simulate lootbag in console.
					if(!(sender instanceof Player)) {
						if(SwordCraftOnline.getPluginInstance().getDropManager().isInDropTable(args[2])) {
							SwordCraftOnline.logInfo("Simulating lootbag in console...");
							try{
								LootBag d = SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(args[2]).generate();
								sender.sendMessage(d.getInfo());
							} catch(Exception e) {
								SwordCraftOnline.logInfo("Specified Droptable has invalid drops.");
								e.printStackTrace();
							}
							return true;
						}
						//Droptable not loaded.
						SwordCraftOnline.logInfo("Attempted to load invalid droptable.");
						return true;
					}

					//Sender is player. Drop actual lootbag on player.
					Player p = (Player) sender;
					if(SwordCraftOnline.getPluginInstance().getDropManager().isInDropTable(args[2])) {
						SCOPlayer s = GameManager.findSCOPlayer(p);
						if(s == null) {
							p.sendMessage(ChatColor.GREEN + "Join SCO to generate droptable.");
							SwordCraftOnline.logInfo("Failed to generate droptable. Sender not in SCO.");
							return true;
						}
						SwordCraftOnline.logInfo("Loading lootbag from manager...");
						String info = "";
						try{
							LootBag d = SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(args[2]).generate(s);
							info = d.getInfo();
							DropManager.drop(p.getLocation(), d);
						} catch(NullPointerException e) {
							p.sendMessage(ChatColor.GREEN + "Specified droptable has invalid drops");
						}
						p.sendMessage(ChatColor.GREEN + "Generated droptable.");
						SwordCraftOnline.logInfo("Lootbag successully loaded from manager.");
						sender.sendMessage("LootBag Info: " + info);
						return true;
					}
					p.sendMessage(ChatColor.GREEN + "Specified droptable not loaded.");
					SwordCraftOnline.logInfo("Attempted to load invalid droptable.");
					return true;
				}
				if(args[1].equalsIgnoreCase("getdata")) {
					if(args[2].equalsIgnoreCase("Droptable")) {
						sender.sendMessage(SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(args[3]).getInfo());
						return true;
					}
					//TODO: Add support for mob
				}
			}

			if(args[0].equalsIgnoreCase("particletest")) {
				Player p = (Player)sender;
				if(args[1].isEmpty()) {
					p.sendMessage("Please enter valid particle.");
				}
				SwordCraftOnline.logInfo("Loading " + args[1] + " particle...");
				Effect effect = SwordCraftOnline.getEffectManager().getEffectByClassName(args[1]);
				/*
				if(effect instanceof ArcEffect) {
					SwordCraftOnline.logInfo("Particle is Arc Effect instance");
					ArcEffect arc = (ArcEffect)effect;
					arc.setLocation(p.getLocation());
					arc.setTargetLocation(p.getLocation().add(10,0,5));
					arc.height = 3;
					arc.setTargetPlayer(p);
					arc.start();
				} else if(effect instanceof AtomEffect) {
					SwordCraftOnline.logInfo("Particle is Atom Effect instance");
					AtomEffect atom = (AtomEffect)effect;
					atom.setTargetPlayer(p);
					atom.setLocation(p.getLocation());
					atom.start();
				}*/
				effect.setTargetPlayer(p);
				effect.setLocation(p.getLocation());
				effect.start();
				SwordCraftOnline.logInfo("Particle started...");

				return true;
			}

		}
		return false;
	}

}
