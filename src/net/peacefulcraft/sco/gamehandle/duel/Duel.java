package net.peacefulcraft.sco.gamehandle.duel;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class Duel {
    
    private SCOPlayer challenger;

    private SCOPlayer challenged;

    private int moneyWager = 0;

    /**
     * Create base duel with two players opposing
     * @param s1
     * @param s2
     */
    public Duel(SCOPlayer challenger, SCOPlayer challenged) {
        this.challenger = challenger;
        this.challenged = challenged;

        //Marking both players in duel
        challenger.setDuel(this);
        challenged.setDuel(this);
    }

    public void setWager(int money) {
        this.moneyWager = money;
    }

    public int getWager() {
        return this.moneyWager;
    }

}