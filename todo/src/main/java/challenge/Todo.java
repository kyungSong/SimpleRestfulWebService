package challenge;

import java.sql.Timestamp;
import java.util.Date;
/**
 * Created by Kyung Ho Song on 2/16/2017.
 */
public class Todo {
    private String id;
    private String name;
    private Timestamp created;

    public Todo(String id_in, String name_in) {
        id = id_in;
        name = name_in;
        Date date = new Date();
        created = new Timestamp(date.getTime());
    }
}
