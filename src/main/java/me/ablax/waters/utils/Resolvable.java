package me.ablax.waters.utils;

import java.util.function.Function;

/**
 * @author Murad Hamza on 30.5.2021 г.
 */
public class Resolvable {
    private final String name;
    private final String displayName;
    private Function<Long, String> nameFunction;

    public Resolvable(final String name, final String displayName, final Function<Long, String> nameFunction) {
        this.name = name;
        this.displayName = displayName;
        this.nameFunction = nameFunction;
    }

    public Resolvable(final String name, final String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Function<Long, String> getNameFunction() {
        return nameFunction;
    }

    public void setNameFunction(final Function<Long, String> nameFunction) {
        this.nameFunction = nameFunction;
    }


}
