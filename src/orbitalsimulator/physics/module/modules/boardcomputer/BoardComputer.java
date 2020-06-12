package orbitalsimulator.physics.module.modules.boardcomputer;

import orbitalsimulator.physics.module.Module;
import orbitalsimulator.tools.FileUtils;

import java.util.LinkedList;

public class BoardComputer extends Module {

    private LinkedList<Command> commands; //A queue of all commands to be executed
    public Systems systems; //A container for all the systems of the computer (SAS for example)
    public double waitingTime = 0; //The time left to wait before executing the next command (seconds)

    /** Constructs a BoardComputer */
    public BoardComputer(String program) {
        mass = 10;
        systems = new Systems(this); //Detects and prepares all the systems that can be used by the computer
        commands = Compiler.compile(program, this); //Compiles all the commands in the program
    }

    @Override
    public boolean areNeedsSatisfied() { return true; }

    @Override
    public void doActions() {

        systems.update();

        waitingTime -= parentMobile.deltaTime();
        if(waitingTime > 0)
            return;

        //TODO: Electric consumption

        //Executes the program until there is no more instructions or it has to wait
        while(!commands.isEmpty() && waitingTime <= 0)
            commands.pollFirst().execute();
    }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automaticly by MobileSave
     * <br> Arguments should be: programPath */
    public static Module parse(String... args) { return new BoardComputer(FileUtils.readAll(args[0])); }
}
