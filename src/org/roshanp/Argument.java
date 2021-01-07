package org.roshanp.argparse;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class Argument {

    private final String extendedFlag;
    private final String flag;

    //defines whether an argument has been defined
    private boolean isClosed = false;

    private final Boolean required;

    //if is switch
    private final Boolean isSwitch;
    private Boolean switchValue;

    //if is value
    private final Boolean isValue;
    private String[] optionalValues;
    private String selectedValue;

    public Argument(String extendedFlag, String flag, Boolean required, Boolean isSwitch, String[] optionalValues) {
        if (isSwitch) {
            this.isSwitch = true;
            this.isValue = false;
        } else {
            this.isValue = true;
            this.isSwitch = false;
            if (optionalValues != null) this.optionalValues = optionalValues;
        }

        this.extendedFlag = extendedFlag;
        this.flag = flag;
        this.required = required;
    }

    public void select(String value) {
        if (isValue) {
            if (optionalValues == null) {
                this.selectedValue = value;
            } else {
                for (String option : optionalValues) {
                    if (option.equals(value)) {
                        this.selectedValue = value;
                        return;
                    }
                }
                System.err.println("value [" + value + "]not in options list: " + Arrays.toString(optionalValues));
            }
        } else if (isSwitch) {
            if (value.equals("true")) {
                switchValue = true;
            } else if (value.equals("false")) {
                switchValue = false;
            } else {
                System.err.println("invalid entry for switch flag");
            }
        } else {
            System.err.println("argument improperly configured");
        }
    }

    public String getValue() {
        if (!isSwitch && isValue) {
            return selectedValue;
        } else {
            System.err.println("incorrect argument configuration for string value");
            return null;
        }
    }

    public Boolean getSwitchValue() {
        if (isSwitch && !isValue) {
            return switchValue;
        } else {
            System.err.println("incorrect argument configuration for switch value");
            return null;
        }
    }

    public Boolean isValue() {
        return isValue;
    }

    public Boolean isSwitch() {
        return isSwitch;
    }

    public boolean isRequired() {
        return required;
    }

    public String getID() {
        return extendedFlag;
    }

    public String getFlag() {
        return flag;
    }

    public String[] getOptionalValues() {
        return optionalValues;
    }

    public void close() {
        if (isValue()) {
            switchValue = false;
        } else if (isSwitch()) {
            if (switchValue == null) {
                System.err.println("argument could not be finalized");
            } else {
                selectedValue = null;
            }
        }
        isClosed = true;
    }

    public boolean isClosed() {
        return isClosed;
    }
}
