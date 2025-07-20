package com.sportygroup.entity.enums;

public enum BetOutcome {

    HOME, DRAW, AWAY;

    public static BetOutcome fromString(String text) {
        if (text == null) {
            return null;
        }
        for (BetOutcome outcome : BetOutcome.values()) {
            if (outcome.name().equalsIgnoreCase(text)) {
                return outcome;
            }
        }
        return null;
    }

}
