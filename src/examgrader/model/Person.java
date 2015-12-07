package examgrader.model;

// Only here to show inheritance and abstract class usage
public abstract class Person
{
    private String lastName;
    private String firstName;

    public abstract String getFirstName();
    public abstract String getLastName();
    public abstract String getName();
}
