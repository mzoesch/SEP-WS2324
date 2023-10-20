package app;


/**
 * @param name Name of the command.
 * @param shortName Abbreviation of this command.
 * @param description Description about this command.
 */
public record Command(String name, String shortName, String description) {

    public static final char EscapeCharacter = '\\';

    /**
     * @param name Name of the command.
     * @param shortName Abbreviation of this command.
     * @param description Description about this command.
     */
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

    /**
     * @return Array of all names of this command as Strings.
     */
    public String[] getNames() {
        return new String[]{this.name, this.shortName};
    }

    /**
     * @return String representation of this command.
     */
    public String toString() {
        return String.format("-%s, --%s %s", this.shortName, this.name, this.description);
    }

    /**
     * @param command Command to compare against.
     * @return True if the command is equal to this command, false otherwise.
     */
    public boolean isEqual(String command) {
        return this.name.equals(command) || this.shortName.equals(command);
    }
}
