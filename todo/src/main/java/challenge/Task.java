package challenge;

import java.sql.Timestamp;
/**
 * POJO for task.
 */
class Task {
    public String id;
    public String name;
    public String description;
    public String status;
    public Timestamp created;

    @Override
    public String toString() {
        return String.format("id: %s, name: %s, description: %s, status: %s, created: %s", this.id, this.name, this.description, this.status, this.created);
    }

}
