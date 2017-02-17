package challenge;

import java.sql.Timestamp;
import java.util.Date;
/**
 * Created by Kyung Ho Song on 2/16/2017.
 */
public class Task {
    private String id;
    private String name;
    private String description;
    private String status;
    private Timestamp created;

    public Task(String id_in, String name_in, String description_in, String status_in) {
        id = id_in;
        name = name_in;
        description = description_in;
        status = status_in;
        Date date = new Date();
        created = new Timestamp(date.getTime());
    }
}
