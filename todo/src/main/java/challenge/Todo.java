package challenge;

import java.sql.Timestamp;
/**
 * POJO for todo.
 */
public class Todo {
    public String id;
    public String name;
    public Timestamp created;

    @Override
    public String toString() {
        return String.format("id: %s, name: %s, created: %s", this.id, this.name, this.created);
    }
}
