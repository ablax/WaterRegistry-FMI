package me.ablax.waters.utils;

import java.util.function.Function;

/**
 * @author Murad Hamza on 30.5.2021 г.
 */
public class Resolvable {
    private final String name;
    private final String displayName;
    private Function<Long, String> nameFunction;
    private Function<String, Long> idFunction;

    public Resolvable(final String name, final String displayName, final Function<Long, String> nameFunction, final Function<String, Long> idFunction) {
        this.name = name;
        this.displayName = displayName;
        this.nameFunction = nameFunction;
        this.idFunction = idFunction;
    }

    public Resolvable(final String name, final String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public Function<String, Long> getIdFunction() {
        return idFunction;
    }

    public void setIdFunction(final Function<String, Long> idFunction) {
        this.idFunction = idFunction;
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
