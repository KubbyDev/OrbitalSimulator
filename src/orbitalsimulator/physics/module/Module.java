package orbitalsimulator.physics.module;

import orbitalsimulator.physics.Mobile;

import java.util.ArrayList;

public class Module {

    public Mobile parentMobile;
    public ArrayList<ModuleAction> actions;
    public ArrayList<ModuleConsuption> consuptions;
    public ArrayList<ModuleNeed> needs;

    public Module(ArrayList<ModuleAction> actions, ArrayList<ModuleConsuption> consuptions, ArrayList<ModuleNeed> needs) {
        this.actions = actions;
        this.consuptions = consuptions;
        this.needs = needs;
    }

}
