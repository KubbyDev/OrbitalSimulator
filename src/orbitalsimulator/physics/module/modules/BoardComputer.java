package orbitalsimulator.physics.module.modules;

import orbitalsimulator.physics.module.Module;
import orbitalsimulator.tools.FileUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class BoardComputer extends Module {

    private LinkedList<String> commands; //A queue of all commands to be executed
    private double waitingTime = 0; //The time left to wait before executing the next command (seconds)

    public BoardComputer(String program) {
        mass = 10;
        //Program compilation
        commands = Arrays.stream(program.split("\n")).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public boolean areNeedsSatisfied() {
        return true;
    }

    @Override
    public void doActions() {

        waitingTime -= parentMobile.deltaTime();
        if(waitingTime > 0)
            return;

        String command = commands.pollFirst();
        while(command != null && !command.equals("") && waitingTime <= 0) {

            String[] words = command.split(" ");

            //If the command is of type: SET moduleName fieldName value
            if(words[0].equals("SET"))
                executeSet(words);

            //If the command is of type: WAIT timeInMs
            if(words[0].equals("WAIT"))
                executeWait(words);

            if(waitingTime <= 0)
                command = commands.pollFirst();
        }
    }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automaticly by MobileSave
     * <br> Arguments should be: programPath */
    public static Module parse(String... args) { return new BoardComputer(FileUtils.readAll(args[0])); }

    /** Executes a set command: SET moduleName fieldName value
     * <br> Set the moduleName to self to target the executing module */
    private void executeSet(String[] words) {
        Module target = words[1].equals("self") ? this : parentMobile.findModule(words[1]);
        target.receiveCommand("SET", words[2], Double.parseDouble(words[3]));
    }

    /** Executes a wait command: WAIT timeInMs */
    private void executeWait(String[] words) { waitingTime = Double.parseDouble(words[1])/1000; }
}
