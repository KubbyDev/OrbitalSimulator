package orbitalsimulator.physics.module.modules.boardcomputer;

import orbitalsimulator.physics.module.modules.boardcomputer.commands.SetCommand;
import orbitalsimulator.physics.module.modules.boardcomputer.commands.WaitCommand;
import orbitalsimulator.physics.module.modules.boardcomputer.commands.SystemCallCommand;

import java.util.LinkedList;

public class Compiler {

    /** Compiles all the commands to speed up execution. Can detect some programming errors */
    public static LinkedList<Command> compile(String program, BoardComputer parentComputer) {

        LinkedList<Command> res = new LinkedList<>();
        String[] lines = program.split("\n");

        for(String line : lines) {

            //Skips empty lines
            if(isBlank(line))
                continue;

            String[] words = line.split(" ");

            //SET commands: SET moduleName fieldName value
            if(words[0].equals("SET"))
                res.addLast(SetCommand.compile(words, parentComputer));
            //WAIT commands: WAIT timeInMs
            else if(words[0].equals("WAIT"))
                res.addLast(WaitCommand.compile(words, parentComputer));
            //If it is none of them, it's a call to an system
            else
                res.addLast(SystemCallCommand.compile(words, parentComputer));

        }

        return res;
    }

    // Returns false if the string contains one character other than space and tabulation, true otherwise
    private static boolean isBlank(String line) {
        for(char c : line.toCharArray())
            if(c != ' ' && c != '\t')
                return false;
        return true;
    }

    public static void throwCompilationError(String beforeMessage, String[] words, String afterMessage) {
        StringBuilder message = new StringBuilder(beforeMessage);
        for(String word : words)
            message.append(word).append(" ");
        throw new IllegalArgumentException(message.toString() + afterMessage);
    }

    public static void throwSyntaxError(String syntax, String[] words) throws IllegalArgumentException {
        throwCompilationError("Syntax error at line: ", words, "\nExpected: " + syntax);
    }
}
