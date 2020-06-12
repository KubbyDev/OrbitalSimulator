package orbitalsimulator.physics.module.modules.boardcomputer.commands;

import orbitalsimulator.physics.module.modules.boardcomputer.BoardComputer;
import orbitalsimulator.physics.module.modules.boardcomputer.Command;
import orbitalsimulator.physics.module.modules.boardcomputer.Compiler;

/** Makes the computer sleep for a given amount of milliseconds
 * <br> Syntax: WAIT timeInMs */
public class WaitCommand extends Command {

    private double time;

    /** Executes this wait command */
    @Override
    public void execute() {
        execute(time, parentComputer);
    }

    /** Executes a wait command */
    public static void execute(double time, BoardComputer parentComputer) {
        parentComputer.waitingTime = time;
    }

    /** Compiles and returns a Wait command */
    public static Command compile(String[] words, BoardComputer computer) {

        if(words.length != 2)
            Compiler.throwSyntaxError("WAIT timeInMs", words);

        WaitCommand res = new WaitCommand();

        try {
            res.time = Double.parseDouble(words[1]) / 1000;
        } catch(Exception e) {
            Compiler.throwCompilationError("Compilation error at: ", words, "\nCould not parse " + words[1] + " to double\n");
        }

        res.parentComputer = computer;
        return res;
    }
}
