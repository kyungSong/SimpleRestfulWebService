package challenge;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;
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
    private final static Logger logger = Logger.getLogger(Resource.class.getName());


    @GET
    @Path("todos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodo() {
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("Received request to get todo for /todos");
        List todoList = database.operations.getTodos();
        mapper.setDateFormat(dateFormat);
        String returnVal = "";
        try {
            returnVal = mapper.writeValueAsString(todoList);
        } catch(Exception e) {
            logger.warning("An error occurred. Error message is: " + e.getMessage());
            System.err.println(e.getMessage());
        }
        logger.info("sending retrieved list of todos to the client.");
        return Response.ok(returnVal).build();
    }

    @GET
    @Path("todos/{todo_id}/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecTodo(@PathParam("todo_id") String todo_id) throws IOException {
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("Received request to get todo with todo_id: " + todo_id);
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);
        String returnVal = "";
        if (m.find()) {
            List taskList = database.operations.getTasks(todo_id);
            mapper.setDateFormat(dateFormat);
            try {
                returnVal = mapper.writeValueAsString(taskList);
            } catch(Exception e) {
                logger.warning("An error occurred. Error message is: " + e.getMessage());
                System.err.println(e.getMessage());
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid todo ID").build();
        }
        logger.info("sending retrieved todo with todo_id: " + todo_id + " to the client");
        return Response.ok(returnVal).build();
    }

    @GET
    @Path("todos/{todo_id}/tasks/{task_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("todo_id") String todo_id,
                          @PathParam("task_id") String task_id) {
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("received request to retrieve task with task_id " + task_id + " for todo with todo_id " + todo_id);
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);
        mapper.setDateFormat(dateFormat);

        if(!m.find()) {
            logger.warning("invalid todo id");
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid todo ID").build();
        }

        m = r.matcher(task_id);

        if(!m.find()) {
            logger.warning("invalid task id");
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid task ID").build();
        }
        String returnVal = "";
        try {
            Task returnedTask = database.operations.getTask(todo_id, task_id);
            if (returnedTask.id == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Task " + task_id + " for todo " + todo_id + " does not exist.").build();
            }
            returnVal = mapper.writeValueAsString(returnedTask);

        } catch (Exception e) {
            logger.warning("An error occurred. Error message is: " + e.getMessage());
            System.err.println(e.getMessage());
        }
        logger.info("sending retrieved task with task_id " + task_id + " for todo with todo_id " + todo_id + " to the client");
        return Response.ok(returnVal).build();
    }

    @GET
    @Path("todos/{todo_id}/tasks/done")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFinished(@PathParam("todo_id") String todo_id) {
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("received request to get finished tasks of the todo with todo_id " + todo_id);
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        String returnVal = "";
        if (m.find()) {
            List taskList = database.operations.getTasksStatus(todo_id, true);
            mapper.setDateFormat(dateFormat);

            try {
                returnVal = mapper.writeValueAsString(taskList);
            } catch(Exception e) {
                logger.warning("An error occurred. Error message is: " + e.getMessage());
                System.err.println(e.getMessage());
            }
        } else {
            logger.warning("invalid todo id");
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid todo ID.").build();
        }
        logger.info("returning finished tasks of the todo with todo_id " + todo_id + " to the client");
        return Response.ok(returnVal).build();
    }

    @GET
    @Path("todos/{todo_id}/tasks/not-done")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnfinished(@PathParam("todo_id") String todo_id) {
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("received request to retrieve unfinished tasks of the todo with todo_id " + todo_id);
        r = Pattern.compile(pattern);
        m = r.matcher(todo_id);

        String returnVal = "";
        if (m.find()) {
            List taskList = database.operations.getTasksStatus(todo_id, false);
            mapper.setDateFormat(dateFormat);

            try {
                returnVal = mapper.writeValueAsString(taskList);
            } catch(Exception e) {
                logger.warning("An error occurred. Error message is: " + e.getMessage());
                System.err.println(e.getMessage());
            }
        } else {
            logger.warning("invalid todo id");
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid todo ID.").build();
        }
        logger.info("returning unfinished tasks of the todo with todo_id " + todo_id + " to the client");
        return Response.ok(returnVal).build();
    }

    @POST
    @Path("todos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTodo(String data) throws IOException{
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("Received request to create new todo.");
        //create a todo.
        mapper.setDateFormat(dateFormat);
        Todo temp = mapper.readValue(data, Todo.class);

        temp = database.operations.createTodo(temp.name);

        logger.info("returning newly created todo to the client.");
        return Response.status(Response.Status.CREATED).entity(mapper.writeValueAsString(temp)).build();
    }

    @POST
    @Path("todos/{todo_id}/tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(String data, @PathParam("todo_id") String todo_id) throws IOException{
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("received request to create new task for todo with a todo_id " + todo_id);
        mapper.setDateFormat(dateFormat);
        Task temp = mapper.readValue(data, Task.class);

        temp = database.operations.createTask(todo_id, temp.name, temp.description);

        logger.info("sending newly created task to the client.");
        return Response.status(Response.Status.CREATED).entity(mapper.writeValueAsString(temp)).build();


    }
    @PUT
    @Path("todos/{todo_id}/tasks/{task_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(String data, @PathParam("todo_id") String todo_id,
                             @PathParam("task_id") String task_id) throws IOException{
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("received request to update the task with task_id " + task_id +
                    " for a todo with todo_id " + todo_id);
        mapper.setDateFormat(dateFormat);
        Task temp = mapper.readValue(data, Task.class);

        temp = database.operations.updateTask(todo_id, task_id, temp.name, temp.description, temp.status);

        logger.info("returning updated task to the client.");
        return Response.ok(mapper.writeValueAsString(temp)).build();

    }

    @DELETE
    @Path("todos/{todo_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTodo(@PathParam("todo_id") String todo_id) {
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("received request to delete a todo with todo_id " + todo_id);
        database.operations.deleteTodo(todo_id);

        return Response.ok("{}").build();
    }

    @DELETE
    @Path("todos/{todo_id}/tasks/{task_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTask(@PathParam("todo_id") String todo_id,
                             @PathParam("task_id") String task_id) {
        if (database.conn == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("database is down.").build();
        }
        logger.info("received request to delete a task with task_id " + task_id + " from a todo with todo id " + todo_id);
        database.operations.deleteTask(todo_id, task_id);

        return Response.ok("{}").build();
    }
}