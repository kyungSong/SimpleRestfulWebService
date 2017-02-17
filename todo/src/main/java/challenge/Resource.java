package challenge;

import org.codehaus.jackson.map.ObjectMapper;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.HashMap;
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
            return "tasks found";
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createTodo(String data) throws IOException{
        //create todo
        HashMap<String, String> todo = new ObjectMapper().readValue(data, HashMap.class);
        System.out.println(todo.get("name"));
        System.out.println(data.getClass().getName());

        String jsonInString = new ObjectMapper().writeValueAsString(data);
        return jsonInString;
    }

    @POST
    @Path("todos/:{todo_id}/tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createTask(String data, @PathParam("todo_id") String todo_id) throws IOException{

        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if (m.find()) {
            //find unfinished tasks and return them
            //create task
            HashMap<String, String> task = new ObjectMapper().readValue(data, HashMap.class);
            for (String key: task.keySet()) {
                System.out.println(key + " : " + task.get(key));
            }
            return "task created";
        } else {
            return "Invalid todo ID.";
        }

    }
    @PUT
    @Path("todos/:{todo_id}/tasks/:{task_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTask(String data, @PathParam("todo_id") String todo_id,
                             @PathParam("task_id") String task_id) throws IOException{

        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if (m.find()) {
            //get the task ids of that "todo" and look for the task
            boolean found = true;
            if(found) {
                HashMap<String, String> task = new ObjectMapper().readValue(data, HashMap.class);
                for (String key: task.keySet()) {
                    System.out.println(key + " : " + task.get(key));
                }
                return "task updated";
            }

            return "Invalid task id.";
        } else {
            return "Invalid todo ID.";
        }

    }

    @DELETE
    @Path("todos/:{todo_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTodo(@PathParam("todo_id") String todo_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if(m.find()) {
            //find todo that matches todo_id and delete
            return "todo deleted";
        }
        return "todo not found";
    }

    @DELETE
    @Path("todos/:{todo_id}/tasks/:{task_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTask(@PathParam("todo_id") String todo_id,
                             @PathParam("task_id") String task_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        if(m.find()) {
            //find todo that matches todo_id and look for the teask
            boolean found = true;
            if(found) {
                //delete the task
                return "task deleted";
            }
            return "task not found";
        }
        return "todo not found";
    }
}