package tools.test;

import orbitalsimulator.tools.FileUtils;

public class Test {

    public static void main(String... args) {

        System.out.println(FileUtils.readAll("models/test.txt"));

    }

}
