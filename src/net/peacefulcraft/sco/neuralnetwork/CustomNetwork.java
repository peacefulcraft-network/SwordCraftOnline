package net.peacefulcraft.sco.neuralnetwork;

import java.util.Map;

import org.bukkit.command.CommandSender;

import me.alexisevelyn.neuralnetwork.Controller;
import me.alexisevelyn.neuralnetwork.NNBaseEntity;

public class CustomNetwork extends NNBaseEntity implements Controller {

    public CustomNetwork(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String learn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBase(NNBaseEntity arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setInputs(CommandSender arg0, String[] arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public String update() {
        // TODO Auto-generated method stub
        return null;
    }

}