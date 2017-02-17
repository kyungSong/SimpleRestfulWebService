package challenge;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that handles requests
 */
@Path("")
public class Resource {
    public static String pattern = "^\\d{5}[a-z]{2}\\d[-][a-z]{2}\\d{2}[-]\\d{2}[a-z]\\d[-][a-z]\\d[a-z]\\d[-][a-z]{3}\\d[a-z]\\d{3}[a-z]{2}\\d{2}$";
    public static Pattern r;
    public static Matcher m;
    public static ConnectToPostgre database = new ConnectToPostgre("localhost:5432", "challenge", "postgres", "");
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
    public ObjectMapper mapper = new ObjectMapper();


    @GET
    @Path("todos")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTodo() {
        List todoList = database.operations.getTodos();
        mapper.setDateFormat(dateFormat);
        String returnVal = "";
        try {
            returnVal = mapper.writeValueAsString(todoList);
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }

        return returnVal;
    }

    @GET
    @Path("todos/{todo_id}/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecTodo(@PathParam("todo_id") String todo_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);
        String returnVal = "";
        if (m.find()) {
            List taskList = database.operations.getTasks(todo_id);
            mapper.setDateFormat(dateFormat);

            try {
                returnVal = mapper.writeValueAsString(taskList);
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            return "Invalid todo ID.";
        }
        return returnVal;
    }

    @GET
    @Path("todos/{todo_id}/tasks/{task_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTask(@PathParam("todo_id") String todo_id,
                          @PathParam("task_id") String task_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);
        mapper.setDateFormat(dateFormat);

        if(!m.find()) {
            return "Invalid todo ID";
        }

        m = r.matcher(task_id);

        if(!m.find()) {
            return "Invalid task ID";
        }
        String returnVal = "";
        try {
            Task returnedTask = database.operations.getTask(todo_id, task_id);
            if (returnedTask.id == null) {
                return "";
            }
            returnVal = mapper.writeValueAsString(returnedTask);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return returnVal;
    }

    @GET
    @Path("todos/{todo_id}/tasks/done")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFinished(@PathParam("todo_id") String todo_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        String returnVal = "";
        if (m.find()) {
            List taskList = database.operations.getTasksStatus(todo_id, true);
            mapper.setDateFormat(dateFormat);

            try {
                returnVal = mapper.writeValueAsString(taskList);
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            return "Invalid todo ID.";
        }
        return returnVal;
    }

    @GET
    @Path("todos/{todo_id}/tasks/not-done")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUnfinished(@PathParam("todo_id") String todo_id) {
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        String returnVal = "";
        if (m.find()) {
            List taskList = database.operations.getTasksStatus(todo_id, false);
            mapper.setDateFormat(dateFormat);

            try {
                returnVal = mapper.writeValueAsString(taskList);
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            return "Invalid todo ID.";
        }
        return returnVal;
    }

    @POST
    @Path("todos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createTodo(String data) throws IOException{
        //create a todo.
        mapper.setDateFormat(dateFormat);
        Todo temp = mapper.readValue(data, Todo.class);

        temp = database.operations.createTodo(temp.name);

        return mapper.writeValueAsString(temp);
    }

    @POST
    @Path("todos/{todo_id}/tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createTask(String data, @PathParam("todo_id") String todo_id) throws IOException{
        mapper.setDateFormat(dateFormat);
        Task temp = mapper.readValue(data, Task.class);

        temp = database.operations.createTask(todo_id, temp.name, temp.description);

        return mapper.writeValueAsString(temp);


    }
    @PUT
    @Path("todos/{todo_id}/tasks/{task_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTask(String data, @PathParam("todo_id") String todo_id,
                             @PathParam("task_id") String task_id) throws IOException{
        mapper.setDateFormat(dateFormat);
        Task temp = mapper.readValue(data, Task.class);

        temp = database.operations.updateTask(todo_id, task_id, temp.name, temp.description, temp.status);

        return mapper.writeValueAsString(temp);

    }

    @DELETE
    @Path("todos/{todo_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTodo(@PathParam("todo_id") String todo_id) {
        database.operations.deleteTodo(todo_id);

        return "{}";
    }

    @DELETE
    @Path("todos/{todo_id}/tasks/{task_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTask(@PathParam("todo_id") String todo_id,
                             @PathParam("task_id") String task_id) {
    database.operations.deleteTask(todo_id, task_id);

    return "{}";
    }
}