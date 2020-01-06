package orbitalsimulator.physics.module.modules.boardcomputer.commands;

import orbitalsimulator.physics.module.modules.boardcomputer.BoardComputer;
import orbitalsimulator.physics.module.modules.boardcomputer.Command;
import orbitalsimulator.physics.module.modules.boardcomputer.Compiler;

public class WaitCommand extends Command {

    private double time;

    /** Executes a wait command: WAIT timeInMs */
    @Override
    public void execute() {
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
