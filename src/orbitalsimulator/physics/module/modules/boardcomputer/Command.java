package orbitalsimulator.physics.module.modules.boardcomputer;

/** Base class of all commands
 * A command is a line in a board computer program */
public abstract class Command {
    protected BoardComputer parentComputer = null;
    public void execute() {
        throw new AssertionError("We forgot to override execute() in a command class");
    }
}
