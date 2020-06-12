package orbitalsimulator.physics.module.modules.boardcomputer.commands;

import orbitalsimulator.physics.module.Module;
import orbitalsimulator.physics.module.modules.boardcomputer.BoardComputer;
import orbitalsimulator.physics.module.modules.boardcomputer.Command;
import orbitalsimulator.physics.module.modules.boardcomputer.Compiler;

/** Sets the desired field in the desired module (must be connected to the computer) to the desired value
 * <br> Syntax: SET moduleName fieldName value */
public class SetCommand extends Command {

    private String moduleName;
    private String fieldName;
    private String fieldValue;

    /** Executes this set command */
    @Override
    public void execute() {
        execute(moduleName, fieldName, fieldValue, parentComputer);
    }

    /** Executes a set command */
    public static void execute(String moduleName, String fieldName, String fieldValue, BoardComputer parentComputer) {
        Module target = parentComputer.parentMobile.findModule(moduleName);
        if(target == null)
            System.err.println("Cound not find module " + moduleName);
        else
            target.updateField(fieldName, fieldValue);
    }

    /** Compiles and returns a Set command */
    public static Command compile(String[] words, BoardComputer parentComputer) {

        if(words.length != 4)
            Compiler.throwSyntaxError("SET moduleName fieldName value", words);

        SetCommand res = new SetCommand();
        res.moduleName = words[1];
        res.fieldName = words[2];
        res.fieldValue = words[3];
        res.parentComputer = parentComputer;
        return res;
    }
}
