package org.roshanp.argparse;

import java.util.HashMap;

/**
 * Includes support for
 * - optional/required switches
 * - optional/required flag indicators
 */
public class ArgParser {

    private HashMap<String, Argument> arguments;
    private String state;

    /*
    supports:
    optional flags/switches (max 2 flags per argument)
    indicators + associated values
    queryable destination
    help
     */
    public ArgParser() {
        arguments = new HashMap<>();
        state = "open";
    }

    public void add(Argument arg) {
        arguments.put(arg.getID(), arg);
    }

    public void clear() {
        arguments = new HashMap<>();
    }

    //returns list of included flags
    public void loadInput(String[] args) {

        boolean isValue = false;
        Argument arg = null;

        for (String ID : args) {
            if (isValue) {
                arg.select(ID);
                isValue = false;
            } else {
                arg = query(ID);
                isValue = true;
                if (arg.isSwitch()) {
                    arg.select("true");
                    isValue = false;
                }
            }
            arg.close();
        }

        this.close();
    }

    //queries both flag and ID
    public Argument query(String id) {
        if (arguments.containsKey(id)) return arguments.get(id);
        for (String arg : arguments.keySet()) {
            if (arguments.get(arg).getFlag().equals(id) || arguments.get(arg).getID().equals(id)) {
                return arguments.get(arg);
            }
        }
        System.err.println("could not recognize argument: '" + id + "'");
        return null;
    }

    public void close() {
        boolean found = false;
        for (String arg : arguments.keySet()) {
            Argument argument = arguments.get(arg);
            if (argument.isRequired() && !argument.isClosed()) {
                System.err.println("argument: " + arg + " not resolved");
                found = true;
            }
        }
        if (!found) state = "closed";
    }

    public HashMap<String, Argument> getArguments() {
        return arguments;
    }

    public String getState() {
        return state;
    }
}
