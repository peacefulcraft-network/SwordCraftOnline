package net.peacefulcraft.sco.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CraftingInventory;
import net.peacefulcraft.sco.inventories.InfusionInventory;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.mythicmobs.drops.DropManager;
import net.peacefulcraft.sco.mythicmobs.drops.LootBag;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.Centipede;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicPet;
import net.peacefulcraft.sco.mythicmobs.mobs.bosses.BossIdentifier;
import net.peacefulcraft.sco.mythicmobs.mobs.bosses.MythicBoss;
import net.peacefulcraft.sco.mythicmobs.spawners.ActiveSpawner;
import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.structures.structures.ComplexPillarArea;
import net.peacefulcraft.sco.structures.structures.Cylinder;
import net.peacefulcraft.sco.structures.structures.Path;
import net.peacefulcraft.sco.structures.structures.Pillar;
import net.peacefulcraft.sco.structures.structures.Wall;
import net.peacefulcraft.sco.structures.structures.WallWave;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillManager;
import net.peacefulcraft.sco.swordskills.SwordSkillTest;
import net.peacefulcraft.sco.swordskills.modules.SwordSkillModule;
import net.peacefulcraft.sco.swordskills.utilities.CriticalHit;
import net.peacefulcraft.sco.swordskills.utilities.Parry;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class SCOAdmin implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("scoadmin")) {
			if (args.length == 0) {
				sender.sendMessage("scoadmin [ swordskillbook | generateitem | playerdata | setplayerdata | debug]");
				return true;
			}

			if (args[0].equalsIgnoreCase("generateitem")) {
				Player p = (Player) sender;
				SCOPlayer s = GameManager.findSCOPlayer(p);
				ItemIdentifier item = ItemIdentifier.generateIdentifier(args[1], ItemTier.valueOf(args[2]), Integer.valueOf(args[3]));
				if(item == null || item.getMaterial().equals(Material.AIR)) {
					p.sendMessage("Failed to create item: " + args[1]);
					return true;
				}
				s.getPlayerInventory().addItem(item);
				return true;
			}

			if (args[0].equalsIgnoreCase("inventory")) {
				if(args[1].equalsIgnoreCase("craft")) {
					Player p = (Player) sender;
					SCOPlayer s = GameManager.findSCOPlayer(p);

					try{
						if(args[2].equalsIgnoreCase("display") || args[2].equalsIgnoreCase("d")) {
							new CraftingInventory(s, args[3]).openInventory(s);
							return true;
						}
					} catch (IndexOutOfBoundsException ex) {
						SwordCraftOnline.logInfo("[SCOAdmin] IndexOutOfBoundsError running command.");
						new CraftingInventory(s).openInventory(s);
						return true;
					}
					SwordCraftOnline.logInfo("[SCOAdmin] Hit this end point for some reason.");
					new CraftingInventory(s).openInventory(s);
					return true;
				}
				if(args[1].equalsIgnoreCase("infusion")) {
					Player p = (Player) sender;
					SCOPlayer s = GameManager.findSCOPlayer(p);

					new InfusionInventory(s).openInventory(s);
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("crafting")) {
				if(args[1].equals("reload")) {
					SwordCraftOnline.getPluginInstance().getCraftingManager().load();
					return true;
				}
				return true;
			}

			if (args[0].equalsIgnoreCase("playerdata")) {
				Player p = (Player) sender;

				SCOPlayer s = GameManager.findSCOPlayer(args[1]);
				if (s == null) {
					p.sendMessage(ChatColor.RED + "Could not find player. Are they online?");
					return true;
				}

				if(args.length > 2) {
					if(args[2].equalsIgnoreCase("inventory")) {
						
						if(args.length > 3) {
							if(args[3].equalsIgnoreCase("swordskill")) {
								s.getSwordSkillInventory().openInventory(s);
								return true;
							} else {
								p.sendMessage(ChatColor.GOLD + "Valid agruments" + ChatColor.RED + " swordskill");
								return true;
							}
						}

					} else {
						p.sendMessage(ChatColor.GOLD + "Valid arguments:" + ChatColor.RED + " inventory");
						return true;
					}
				}

				p.sendMessage(s.getPlayerData());
				return true;
			}

			if (args[0].equalsIgnoreCase("setplayerdata")) {
				Player p = (Player) sender;

				if (args.length == 1) {
					p.sendMessage(ChatColor.GOLD + "Valid arguments: " + ChatColor.RED
							+ "critical_chance, crit_chance, critical_multiplier, crit_mult, "
							+ "player_kills, parry, admin_override, admin_over");
					return true;
				}
				SCOPlayer s = GameManager.findSCOPlayer(args[1]);
				if (s == null) {
					p.sendMessage(ChatColor.RED + "Could not find player");
					return true;
				}

				double i;
				if (args.length == 2) {
					i = 1;
				} else {
					i = Double.parseDouble(args[3]);
				}

				String data = args[2];
				/*
				if (data.equalsIgnoreCase("critical_chance") || data.equalsIgnoreCase("crit_chance")) {
					s.setCombatModifier(CombatModifier.CRITICAL_CHANCE,(int) i, -1);
					p.sendMessage(ChatColor.GOLD + "Critical Chance set to: " + ChatColor.RED + i);
					return true;
				} else if (data.equalsIgnoreCase("critical_multiplier") || data.equalsIgnoreCase("crit_mult")) {
					s.setCombatModifier(CombatModifier.CRITICAL_MULTIPLIER, i, -1);
					p.sendMessage(ChatColor.GOLD + "Critical Multiplier set to: " + ChatColor.RED + i);
					return true;
				} else if (data.equalsIgnoreCase("player_kills")) {
					p.sendMessage(ChatColor.GOLD + "Player Kills set to: " + ChatColor.RED + i);
					s.setPlayerKills((int) i);
					return true;
				} else if (data.equalsIgnoreCase("admin_override") || data.equalsIgnoreCase("admin_over")) {
					p.sendMessage(ChatColor.GOLD + "Admin Override set to: " + ChatColor.RED + s.hasOverride());
					s.setAdminOverride(!s.hasOverride());
					return true;
				} else if (data.equalsIgnoreCase("parry")) {
					p.sendMessage(ChatColor.GOLD + "Parry Chance set to: " + ChatColor.RED + i);
					s.setCombatModifier(CombatModifier.PARRY_CHANCE, i, -1);
					return true;
				} else {
					p.sendMessage(ChatColor.GOLD + "Valid arguments: " + ChatColor.RED
							+ "critical_chance, crit_chance, critical_multiplier, crit_mult, "
							+ "player_kills, parry, admin_override, admin_over");
					return true;
				}
				*/
			}

			if (args[0].equalsIgnoreCase("swordskills")) {
				SCOPlayer s;
				if (args.length > 0) {
					s = GameManager.findSCOPlayer(args[1]);
					if (s == null) {
						sender.sendMessage(ChatColor.RED + "Could not find player");
						return true;
					}
				} else {
					s = GameManager.findSCOPlayer((Player) sender);
				}

				sender.sendMessage(s.getName() + "'s currently active Sword Skills");
				for (SwordSkill skill : s.getSwordSkillManager().getSkills()) {
					sender.sendMessage("- " + skill.getProvider().getName());
				}
				return true;
			}

			final String helpMessage = "Mythic Mob Commands: \nLoadDropTables: Refreshes droptables from config. \n"
					+ "LoadMobs: Refreshes mobs from config. \nSpawn { Mob Internal }: Spawns mob on player location. \n"
					+ "KillAll: Kills all active MythicMobs on server. \n"
					+ "List { Mobs or DropTables }: Lists all loaded mobs or droptables. \n"
					+ "GenerateDropTable { DropTable Internal }: Drops lootbag on player or simulates in console. \n"
					+ "GetData { DropTable Internal or Mob Internal }: Retrieves data of loaded droptable or mob. \n";

			/**
			 * Handling for MM commands
			 */
			if (args[0].equalsIgnoreCase("mm")) {
				// Help command. Keep updated with options.
				if (args.length == 1 || args[1].equalsIgnoreCase("help")) {
					sender.sendMessage(helpMessage);
					return true;
				}

				// Reloads droptables then mobs.
				// Prevents null pointer errors on reloading mobs before droptables.
				if (args[1].equalsIgnoreCase("reload")) {
					// Despawns all active mobs
					SwordCraftOnline.getPluginInstance().getMobManager().removeAllMobs(true);
					SwordCraftOnline.getPluginInstance().getDropManager().loadDropTables();
					SwordCraftOnline.getPluginInstance().getMobManager().loadMobs();

					SwordCraftOnline.getPluginInstance().getSpawnerManager().save();
					SwordCraftOnline.getPluginInstance().getSpawnerManager().loadSequence();
					return true;
				}

				if (args[1].equalsIgnoreCase("SpawnPet")) {
					Player p = (Player) sender;
					if(SwordCraftOnline.getPluginInstance().getMobManager().getMMList().keySet().contains(args[2])) {
						try {
							SCOPlayer s = GameManager.findSCOPlayer(p);
							MythicPet pet = new MythicPet(args[2], s);
							
							if(pet.spawn(p.getLocation(), 5) != null) {
								sender.sendMessage(ChatColor.GREEN + "Spawned pet " + args[2]);
								return true;
							}
						} catch(IndexOutOfBoundsException ex) {
							return true;
						}
					}
					sender.sendMessage(ChatColor.GREEN + "File for " + args[2] + " Not Found.");
					SwordCraftOnline.logInfo("[MOB SPAWN] Not found: " + args[2]);
					return true;
				}

				// Spawns instance of activemob on players location
				if (args[1].equalsIgnoreCase("spawn")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.GREEN + "Cannot perform command from console.");
						return true;
					}
					Player p = (Player) sender;
					if (SwordCraftOnline.getPluginInstance().getMobManager().getMMList().keySet().contains(args[2])) {
						try {
							ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(args[2],
									p.getLocation(), Integer.valueOf(args[3]));
							if (am != null) {
								sender.sendMessage(ChatColor.GREEN + "Spawned " + args[2]);
								SwordCraftOnline.logInfo("Spawned " + args[2]);
								return true;
							}
						} catch (IndexOutOfBoundsException ex) {
							ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(args[2],
									p.getLocation());
							if (am != null) {
								sender.sendMessage(ChatColor.GREEN + "Spawned " + args[2]);
								SwordCraftOnline.logInfo("Spawned " + args[2]);
								return true;
							}
						}
						sender.sendMessage(ChatColor.GREEN + "Error Loading " + args[2] + " Active Mob Instance Null.");
						SwordCraftOnline.logInfo("[MOB SPAWN] Active Mob Instance Null");
						return true;
					}
					sender.sendMessage(ChatColor.GREEN + "File for " + args[2] + " Not Found.");
					SwordCraftOnline.logInfo("[MOB SPAWN] Not found: " + args[2]);
					return true;
				}

				// Kills all active instances of mobs
				if (args[1].equalsIgnoreCase("killall")) {
					int amount = SwordCraftOnline.getPluginInstance().getMobManager().removeAllMobs(false);
					if (sender instanceof Player) {
						sender.sendMessage(ChatColor.GREEN + "Removed " + amount + " Mythic Mobs!");
					}
					SwordCraftOnline.logInfo("[MOB KILL] Removed " + amount + " Mythic Mobs.");
					return true;
				}

				// Lists all loaded mobs or droptables
				if (args[1].equalsIgnoreCase("list")) {
					try {
						if (args[2].equalsIgnoreCase("activemobs")) {
							List<ActiveMob> mobs = new ArrayList<ActiveMob>(SwordCraftOnline.getPluginInstance().getMobManager().getActiveMobs());
							sender.sendMessage(ChatColor.GREEN + "There are: " + mobs.size() + " Active Mobs.");
							if (mobs.size() > 0) {
								String l = ChatColor.GREEN + "Mobs: \n";
								for (int i = 0; i < mobs.size(); i++) {
									l += ChatColor.GREEN + "" + (i+1) + ". " + mobs.get(i).getDisplayName() + "\n";
								}
								sender.sendMessage(ChatColor.GREEN + l);
								return true;
							}
							return true;
						} else if (args[2].equalsIgnoreCase("mythicmobs")) {
							ArrayList<String> mobs = new ArrayList<String>(SwordCraftOnline.getPluginInstance().getMobManager().getMMList().keySet());
							sender.sendMessage(ChatColor.GREEN + "There are: " + mobs.size() + " Mythic Mobs.");
							if(mobs.size() > 0) {
								String l = ChatColor.GREEN + "Mobs: \n";
								for(int i = 0; i < mobs.size(); i++) {
									l += ChatColor.GREEN + "" + (i+1) + ". " + mobs.get(i) + "\n";
								}
								sender.sendMessage(ChatColor.GREEN + l);
							}
							return true;
						} else if (args[2].equalsIgnoreCase("droptables")) {
							Set<String> tables = SwordCraftOnline.getPluginInstance().getDropManager().getDroptableMap()
									.keySet();
							String s = ChatColor.GREEN + "DropTables: \n";
							for (String ss : tables) {
								s += ChatColor.GREEN + ss + "\n";
							}
							sender.sendMessage(s);
							return true;
						} else if (args[2].equalsIgnoreCase("activespawners")) {
							sender.sendMessage(SwordCraftOnline.getPluginInstance().getSpawnerManager().getActiveSpawnerList());
							return true;
						} else if (args[2].equalsIgnoreCase("spawners")) {
							sender.sendMessage(SwordCraftOnline.getPluginInstance().getSpawnerManager().getSpawnerStringList());
							return true;
						}
					} catch(IndexOutOfBoundsException ex) {
						sender.sendMessage("Command usage: /scoadmin mm list [type]");
						return true;
					}
				}

				//Generates droptable in console or player location
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

				//Gets data contained in yml file for mobs and droptables
				if(args[1].equalsIgnoreCase("getdata")) {
					try {
						if(args[2].equalsIgnoreCase("Droptable")) {
							sender.sendMessage(SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(args[3]).getInfo());
							return true;
						}
					}catch(IndexOutOfBoundsException ex) {
						sender.sendMessage("Command usage: /scoadmin mm getdata [type]");
						return true;
					}
					//TODO: Add support for mob
				}

				//Changing mob health bar displays
				if(args[1].equalsIgnoreCase("sethealthbar")) {
					try {
						SwordCraftOnline.getSCOConfig().setHealthBarConfig(args[2]);
						SwordCraftOnline.getPluginInstance().getMobManager().updateHealthBars();
						return true;
					}catch(IndexOutOfBoundsException ex) {
						sender.sendMessage("Command usge: /scoadmin mm sethealthbar [healthBarType]");
						return true;
					}
				}

				//Testing spawner capabilities
				if(args[1].equalsIgnoreCase("spawner")) {
					try{
						if(args[2].equalsIgnoreCase("toggle")) {
							SwordCraftOnline.getPluginInstance().getSpawnerManager().toggleSpawnerTask();
							return true;
						}
						if(args[2].equalsIgnoreCase("set")) {
							ActiveSpawner as = SwordCraftOnline.getPluginInstance().getSpawnerManager().setSpawner(args[3], ((Player)sender).getTargetBlock(null, 5).getLocation());
							if(as == null) {
								sender.sendMessage("Null Error on set command");
							}
							return true; 
						}
						if(args[2].equalsIgnoreCase("remove")) {
							Player p = (Player)sender;
							SwordCraftOnline.getPluginInstance().getSpawnerManager().unregisterSpawner(p.getTargetBlock(null, 5).getLocation());
							return true;
						}
						if(args[2].equalsIgnoreCase("highlight")) {
							SwordCraftOnline.getPluginInstance().getSpawnerManager().highlight();
							return true;
						}
					}catch(IndexOutOfBoundsException ex) {
						sender.sendMessage("Invalid arguments for spawner command.");
						return true;
					}
				}

				//Testing boss mob capabilites
				if(args[1].equalsIgnoreCase("boss") || args[1].equalsIgnoreCase("b")) {
					try{
						if(args[2].equalsIgnoreCase("spawn")) {
							MythicBoss mb = BossIdentifier.getBoss(args[3], 20);
							mb = mb.spawn(((Player)sender).getLocation());
							if(mb == null) {
								SwordCraftOnline.logInfo("Error spawning Boss " + args[3]);
							}
							return true;
						}
					}catch(IndexOutOfBoundsException ex) {
						sender.sendMessage("Invalid arguments for boss command.");
						return true;
					}
				}

				//Testing console combat simulation
				if(args[1].equalsIgnoreCase("testcombat")) {
					try{
						int damage = Integer.valueOf(args[2]);
						SwordCraftOnline.logInfo("[Combat Sim] Beginning Simulation...");
						MythicMob mm1 = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob("ConsoleOne");
						MythicMob mm2 = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob("ConsoleTwo");

						double d = CriticalHit.simulate(mm1, damage);
						if(d == damage) {
							SwordCraftOnline.logInfo("[Combat Sim] ConsoleOne dealt: " + String.valueOf(d) + "...");
						} else {
							SwordCraftOnline.logInfo("[Combat Sim] ConsoleOne dealt critical hit: " + String.valueOf(d) + "...");
						}

						double parryDam = Parry.simulate(mm2, d);
						if(parryDam != d) {
							SwordCraftOnline.logInfo("[Combat Sim] ConsoleTwo parried. Reduced damage to: " + String.valueOf(parryDam) + "....");
						}

						SwordCraftOnline.logInfo("[Combat Sim] Simulation Complete.\n");

						return true;

					}catch(IndexOutOfBoundsException ex) {
						sender.sendMessage("Invalid damage arguments. Command usage: /testcombat [damage amt]");
						return true;
					}
				}

				if(args[1].equalsIgnoreCase("togglenightwave")) {
					SwordCraftOnline.getPluginInstance().getSpawnerManager().toggleNightwave();
					sender.sendMessage("Spawner Manager set nightwave to: " + String.valueOf(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave()));
					return true;
				}
			}

			if(args[0].equalsIgnoreCase("quest")) {
				if(args[1].equalsIgnoreCase("reload")) {
					SwordCraftOnline.getPluginInstance().getQuestManager().reload();
					return true;
				}
			
				return true;
			}

			if(args[0].equalsIgnoreCase("particletest")) {
				Player p = (Player)sender;
				if(args[1].isEmpty()) {
					p.sendMessage("Please enter valid particle.");
				}
				SwordCraftOnline.logInfo("Loading " + args[1] + " particle...");
				Effect effect = SwordCraftOnline.getEffectManager().getEffectByClassName(args[1]);
				if(effect == null) { 
					SwordCraftOnline.logSevere("Attempted to load invalid particle.");
					return true;
				}
				//effect.setTargetPlayer(p);
				effect.setLocation(p.getLocation());
				effect.start();
				SwordCraftOnline.logInfo("Particle started...");

				return true;
			}

			if(args[0].equalsIgnoreCase("structure")) {
				Player p = (Player)sender;
				if(args[1].isEmpty()) {
					p.sendMessage("Enter a structure.");
					return true;
				}
				Structure struct = null;
				if(args[1].equalsIgnoreCase("Pillar")) {
					struct = new Pillar(5, 3, Material.STONE, true, 5);
					struct.setTargetLocation(p.getLocation());
				} else if(args[1].equalsIgnoreCase("Path")) {
					struct = new Path(2, 10, Material.STONE, true, 5);
					struct.setTargetEntity(p);
				} else if(args[1].equalsIgnoreCase("Wall")) {
					struct = new Wall(5, 7, Material.STONE, true, 5);
					struct.setTargetEntity(p);
					struct.setAdvancedCleanup(true);
				} else if(args[1].equalsIgnoreCase("ComplexPillarArea")) {
					struct = new ComplexPillarArea(10, 5, 7, Material.STONE, true, 5);
					struct.setTargetLocation(p.getLocation());
				} else if(args[1].equalsIgnoreCase("WallWave")) {
					struct = new WallWave(5, 7, 10, Material.STONE, true, 5);
					struct.setTargetEntity(p);
					((WallWave)struct).setWallGap(1);
				} else if(args[1].equalsIgnoreCase("Cylinder")) {
					struct = new Cylinder(5, 5, true, Material.STONE, true, 5);
					struct.setTargetLocation(p.getLocation());
				}
				if(struct == null) {
					SwordCraftOnline.logInfo("[DEBUG] Struct is null");
					return true;
				}
				struct.construct();
				return true;
			}

			if(args[0].equalsIgnoreCase("debug")) {
				if( !(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "Command only executable by a player.");
					return true;
				}

				if(args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Valid Options: " + ChatColor.GOLD + "[playername] [ swordskill ]");
					return true;
				}

				SCOPlayer s = SwordCraftOnline.getGameManager().findSCOPlayer((Player) sender);
				if(s == null) {
					sender.sendMessage(ChatColor.RED + "No SCOPlayer with name " + args[1] + " found.");
					return true;
				}

				if(args.length < 3) {
					sender.sendMessage(ChatColor.RED + "Valid Options: " + ChatColor.GOLD + "[ hud | info | dummyskill]");
					return true;
				}

				if(args[2].equalsIgnoreCase("hud")) {
					sender.sendMessage(ChatColor.RED + "Coming soon... " + ChatColor.GOLD + ":)");
					return true;

				} else if(args[2].equalsIgnoreCase("info")) {
					SwordSkillManager sm = s.getSwordSkillManager();
					for(SwordSkill ss : sm.getSkills()) {
						sender.sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + ss.getClass().toString() + ChatColor.GOLD + "--------");
						for(SwordSkillModule ssm : ss.getModules()) {
							sender.sendMessage(ChatColor.GOLD + "- " + ssm.getClass().toString());
						}
					}
					return true;

				} else if(args[2].equalsIgnoreCase("dummyskill")) {
					new SwordSkillTest(s, null);
					sender.sendMessage("Registered Dummy Skill");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Valid Options: " + ChatColor.GOLD + "[ hud | info ]");
					return true;
				}
			}

			if(args[0].equalsIgnoreCase("centipedetest")){
				Player p = (Player)sender;
				if(args[1].equalsIgnoreCase("spawn")) {
					int size = 3;
					int repeat = 20;
					try{ 
						size = Integer.valueOf(args[2]);
					} catch(Exception ex) { SwordCraftOnline.logDebug("No size value set."); }
					try{
						repeat = Integer.valueOf(args[3]);
					} catch(Exception exx) { SwordCraftOnline.logDebug("No repeatition value set."); }

					Centipede test = new Centipede("Test", size, repeat);
					test.spawn(p.getLocation());
					if(!(test.setTarget(p))) {
						p.sendMessage("Failed to set target.");
					}
					return true;		
				}	
				if(args[1].equalsIgnoreCase("remove")) {
					SwordCraftOnline.getPluginInstance().getMobManager().removeAllCentipede();
					return true;
				}	
			}
		}
		return false;
	}

}
