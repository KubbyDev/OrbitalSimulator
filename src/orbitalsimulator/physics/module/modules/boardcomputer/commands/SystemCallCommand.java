package orbitalsimulator.physics.module.modules.boardcomputer.commands;

import orbitalsimulator.physics.module.modules.boardcomputer.BoardComputer;
import orbitalsimulator.physics.module.modules.boardcomputer.Command;
import orbitalsimulator.physics.module.modules.boardcomputer.Compiler;
import orbitalsimulator.physics.module.modules.boardcomputer.ComputerSystem;

import java.util.Arrays;

public class SystemCallCommand extends Command {

    private ComputerSystem system;
    private String systemMethod;
    private Object args;

    /** Executes a system call command: SystemName SystemMethod argument1 argument2 ... */
    @Override
    public void execute() {
        system.callMethod(systemMethod, args);
    }

    /** Compiles and returns a System call command */
    public static Command compile(String[] words, BoardComputer computer) {

        if(words.length < 2)
            Compiler.throwSyntaxError("SystemName SystemMethod argument1 argument2 ...", words);

        SystemCallCommand res = new SystemCallCommand();
        res.system = computer.systems.find(words[0]);

        if(res.system == null)
            Compiler.throwCompilationError("Error at: ", words, "\nCould not find system " + words[0]);
        if(!res.system.methodExists(words[1]))
            Compiler.throwCompilationError("Error at: ", words, "\nCould not find method " + words[1] + " in system " + words[0]);

        res.args = Arrays.copyOfRange(words, 2, words.length);
        res.systemMethod = words[1];

        return res;
    }
}
