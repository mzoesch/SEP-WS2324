package app;

public record Command(String name, String shortName, String description) {

    public static final char EscapeCharacter = '\\';

    public Command(
            String name,
            String shortName,
            String description
    ) {
        this.name = name;
        this.shortName = shortName;
        this.description = description;

        return;
    }

    public String[] getNames() {
        return new String[]{this.name, this.shortName};
    }

    public String toString() {
        return String.format("-%s, --%s %s", this.shortName, this.name, this.description);
    }

    public boolean isEqual(String command) {
        return this.name.equals(command) || this.shortName.equals(command);
    }
}
