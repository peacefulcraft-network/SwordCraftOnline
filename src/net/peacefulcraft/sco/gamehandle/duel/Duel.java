package net.peacefulcraft.sco.gamehandle.duel;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class Duel {
    
    private SCOPlayer challenger;

    private SCOPlayer challenged;

    /**
     * Create base duel with two players opposing
     * @param s1
     * @param s2
     */
    public Duel(SCOPlayer challenger, SCOPlayer challenged) {
        this.challenger = challenger;
        this.challenged = challenged;

        //Marking both players in duel
        challenger.setInDuel(true);
        challenged.setInDuel(true);
    }

}