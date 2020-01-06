package orbitalsimulator.physics.module.modules.boardcomputer.systems;

import orbitalsimulator.physics.module.modules.boardcomputer.ComputerSystem;
import orbitalsimulator.physics.module.modules.boardcomputer.SystemMethod;

public class TEST extends ComputerSystem {
    @SystemMethod
    public void test(String... args) {
        System.out.println("System Call: " + args[0]);
    }
}
