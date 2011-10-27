package org.artifactory.cli.main;

/**
 * Date: 10/27/11
 * Time: 12:09 PM
 *
 * @author Fred Simon
 */
public class CliLog {
    public static void print(String msg) {
        System.out.print(msg);
    }

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void error(String msg) {
        System.err.println(msg);
    }
}
