package net.peacefulcraft.sco.swordskills.utilities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class DirectionalUtil implements Listener{
    /**Player Directions */
    public enum Movement {
        BACKWARD, FORWARD_SPRINT, FORWARD, LEFT, RIGHT, STANDING, ROTATION, SIDE;
    }

    /**Cardinal Directions */
    public enum CardinalDirection {
        NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
    }

    public static HashMap<UUID, SCOPlayer> playerCheck = new HashMap<>();

    public static void addPlayer(SCOPlayer s) {
        playerCheck.put(s.getUUID(), s);
    }

    public static void removePlayer(SCOPlayer s) {
        playerCheck.remove(s.getUUID());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        //Checking if player should have their direction changed
        if(playerCheck.get(p.getUniqueId()) == null) { return; }

        /**Direction the player is moving. */
        Vector moving = e.getFrom().subtract(e.getTo()).toVector().normalize();
        /**Direction the player is looking at. */
        Vector looking = p.getEyeLocation().getDirection();
        /**Difference between the looking direction and the moving direction. */
        Vector diff = moving.subtract(looking);

        double dot = moving.dot(looking);

        if(diff.length() > 0 && diff.length() < 1.2) {
            //Handing backwards movement
            s.setMovement(Movement.BACKWARD);
        } else if(diff.length() > 1.3 && diff.length() < 1.5) {
            //Handling sideways movement
            if(dot < -0.9 && dot > -1.0) {
                s.setMovement(Movement.LEFT);
            } else if (dot <= -1.0) {
                s.setMovement(Movement.RIGHT);
            }
        } else if(diff.length() > 1.8 && diff.length() <= 2) {
            //Handling moving forward / sprinting
            s.setMovement(s.getPlayer().isSprinting() ? Movement.FORWARD_SPRINT : Movement.FORWARD);
        } else if(Double.isNaN(diff.getX()) || Double.isNaN(diff.getY()) || Double.isNaN(diff.getZ())) {
            //Handling player rotations
            s.setMovement(Movement.ROTATION);
        }
    }

    /**
     * Returns cardinal direction of vector.
     * @param v Vector
     */
    public static CardinalDirection getCardinalDirection(Vector v, Player p) {
        double x = v.getX();
        double z = v.getZ();
        if(Math.abs(x) > Math.abs(z)) {
            return (x > 0.0) ? CardinalDirection.EAST : CardinalDirection.WEST;
        } else {
            return (z > 0.0) ? CardinalDirection.SOUTH : CardinalDirection.NORTH;
        }
    }

    /**
     * Returns players cardinal direciton.
     * @param p Player
     * @param offset Directional offset in intervals of 90
     */
    public static CardinalDirection getCardinalDirection(Player p, int offset) {
        return getCardinalDirection(p.getLocation(), offset);
    }

    /**
     * Returns location cardinal direction 
     * @param offset Direction offset in intervals of 90
    */
    public static CardinalDirection getCardinalDirection(Location loc, int offset) {
        double rotation = (loc.getYaw() - offset) % 360;
        if(rotation < 0) {
            rotation += 360.0;
        }
        if(0 <= rotation && rotation < 22.5) {
            return CardinalDirection.NORTH;
        } else if(22.5 <= rotation && rotation < 67.5) {
            return CardinalDirection.NORTH_EAST;
        } else if(67.5 <= rotation && rotation < 112.5) {
            return CardinalDirection.EAST;
        } else if(112.5 <= rotation && rotation < 157.5) {
            return CardinalDirection.SOUTH_EAST;
        } else if(157.5 <= rotation && rotation < 202.5) {
            return CardinalDirection.SOUTH;
        } else if(202.5 <= rotation && rotation < 247.5) {
            return CardinalDirection.SOUTH_WEST;
        } else if(247.5 <= rotation && rotation < 292.5) {
            return CardinalDirection.WEST;
        } else if(292.5 <= rotation && rotation < 337.5) {
            return CardinalDirection.NORTH_WEST;
        } else if(337.5 <= rotation && rotation < 360.0) {
            return CardinalDirection.NORTH;
        } else {
            return null;
        }
    }


    /**Returns relative blockface directions of entity location */
    public static BlockFace getSideDirections(LivingEntity e) {
		double rot = (e.getLocation().getYaw() - 90) % 360;
		if(rot < 0 ) {
			rot += 360.0;
		}
		if(0 <= rot && rot < 45.0) {
			return BlockFace.NORTH; //Facing North
		} else if(45.0 <= rot && rot < 135.0) {
			return BlockFace.EAST; //Facing East
		} else if(135.0 <= rot && rot < 225.0) {
			return BlockFace.NORTH; //Facing South
		} else if(225.0 <= rot && rot < 315.0) {
			return BlockFace.EAST; //Facing West
		} else if(315.0 <= rot && rot < 360.0) {
			return BlockFace.NORTH; //Facing North
		} else {
			return null;
		}
    }
    
    /**Returns relative blockface directions of player location */
    public static BlockFace getSideDirections(Player p) {
        return getSideDirections((LivingEntity) p);
    }
}
