package challenge;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by Kyung Ho Song on 2/13/2017.
 */
@Path("")
public class Resource {
    public static String pattern = "^\\d{5}[a-z]{2}\\d[-][a-z]{2}\\d{2}[-]\\d{2}[a-z]\\d[-][a-z]\\d[a-z]\\d[-][a-z]{3}\\d[a-z]\\d{3}[a-z]{2}\\d{2}$";
    public static Pattern r;
    public static Matcher m;
    @GET
    @Path("todos")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTodo() {
        return "list of todos";
    }

    @GET
    @Path("todos/:{todo_id}/tasks")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSpecTodo(@PathParam("todo_id") String todo_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if (m.find()) {
            return "todo found";
        } else {
            return "Invalid todo ID.";
        }
    }

    @GET
    @Path("todos/:{todo_id}/tasks/:{task_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTask(@PathParam("todo_id") String todo_id,
                          @PathParam("task_id") String task_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if(!m.find()) {
            return "Invalid todo ID";
        }

        m = r.matcher(task_id);

        if(!m.find()) {
            return "Invalid task ID";
        }

        return "task found";
    }

    @GET
    @Path("todos/:{todo_id}/tasks/done")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFinished(@PathParam("todo_id") String todo_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if (m.find()) {
            //find finished tasks and return them

            return "todo found";
        } else {
            return "Invalid todo ID.";
        }
    }

    @GET
    @Path("todos/:{todo_id}/tasks/not-done")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUnfinished(@PathParam("todo_id") String todo_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if (m.find()) {
            //find unfinished tasks and return them

            return "todo found";
        } else {
            return "Invalid todo ID.";
        }
    }

    @POST
    @Path("todos")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String createTodo(MultivaluedHashMap<String,String> data) {
        //create todo
        return "todo created";
    }
}