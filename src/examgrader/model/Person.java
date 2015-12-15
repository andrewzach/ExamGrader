package examgrader.model;

/**
 * An abstract class representing a Person. Only here to show inheritance and abstract class usage
 */
public abstract class Person
{
    private String lastName;
    private String firstName;

    /**
     * Returns the person's first name as string
     * @return first name of the person
     */
    public abstract String getFirstName();
    /**
     * Returns the person's last name as string
     * @return last name of the person
     */
    public abstract String getLastName();
    /**
     * Returns the person's id as string
     * @return id of the person
     */
    public abstract String getName();
}
