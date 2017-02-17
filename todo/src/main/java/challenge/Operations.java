package challenge;

import com.mifmif.common.regex.Generex;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Class to execute queries.
 */
public class Operations {
    private Connection conn;
    private final static Logger logger = Logger.getLogger(Operations.class.getName());

    public Operations(Connection conn) {
        this.conn = conn;
    }

    /*
    * Get the list of todos.
    *
    * @return = list of todos.*/
    public List<Todo> getTodos() {
        logger.info("getting list of todos");
        List todoList = new ArrayList();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT *" +
                                                "FROM todo");
            //for all tuples returned, create POJO and add it to the list.
            while(rs.next()) {
                Todo temp = new Todo();
                temp.id = rs.getString("id");
                temp.name = rs.getString("name");
                temp.created = rs.getTimestamp("created");
                todoList.add(temp);
            }
            st.close();
            rs.close();
        } catch (SQLException se) {
            logger.warning("Threw SQLException creating list of todos");
            System.err.println("Threw SQLException creating list of todos");
            System.err.println(se.getMessage());
        }
        logger.info("returning list of todos.");
        return todoList;
    }

    /*
    * Get list of tasks for a given todo.
    *
    * @ param todo_id = the id that references a todo instance.
    * @return List that contains list of tasks bound to a specified todo.*/
    public List<Task> getTasks(String todo_id) {
        logger.info("getting tasks for todo_id: " + todo_id);
        List taskList = new ArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT *" +
                                                " FROM task" +
                                                " WHERE todo_id = " +  "'" + todo_id + "'");


            while(rs.next()) {
                Task temp = new Task();
                temp.id = rs.getString("task_id");
                temp.name = rs.getString("name");
                temp.description = rs.getString("description");
                temp.status = rs.getString("status");
                temp.created = rs.getTimestamp("created");
                taskList.add(temp);
            }
            st.close();
            rs.close();
        } catch(SQLException  se) {
            logger.warning("Threw SQLException creating list of tasks for todo_id: " + todo_id);
            System.err.println("Threw SQLException creating list of tasks for todo_id: " + todo_id);
            System.err.println(se.getMessage());
        }
        logger.info("returning list of tasks for todo_id: " + todo_id);
        return taskList;
    }
    /*
    * get a specific task (denoted by task id) assigned to a specific todo.
    *
    * @param todo_id = a todo id.
    * @param task_id = a task id.
    *
    * @return = a task object.*/
    public Task getTask(String todo_id, String task_id) {
        logger.info("getting task with task id " + task_id + " for todo id " + todo_id);
        Task temp = new Task();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT *" +
                                                " FROM task " +
                                                " WHERE todo_id = " + "'" + todo_id + "'" + " AND task_id = " + "'" + task_id + "'");

            while(rs.next()) {
                temp.id = rs.getString("task_id");
                temp.name = rs.getString("name");
                temp.description = rs.getString("description");
                temp.status = rs.getString("status");
                temp.created = rs.getTimestamp("created");
            }
            st.close();
            rs.close();
        } catch (SQLException se) {
            System.err.println("Threw SQLException getting a task with task_id " + task_id);
            System.err.println(se.getMessage());
        }
        logger.info("returning task with task id " + task_id + " for todo id " + todo_id);
        return temp;
    }
    /*
* Get list of tasks, that are either done or not done, for a given todo.
*
* @ param todo_id = the id that references a todo instance.
* @ param status = decides which group of tasks (done or not done) should be returned
* @return List that contains list of tasks bound to a specified todo.*/
    public List<Task> getTasksStatus(String todo_id, boolean status) {
        if(status) {
            logger.info("getting tasks for todo_id " + todo_id + " that are done.");
        } else {
            logger.info("getting tasks for todo_id " + todo_id + " that are not done.");
        }
        List taskList = new ArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs;
            if(status) {
                rs = st.executeQuery("SELECT *" +
                        " FROM task" +
                        " WHERE todo_id = " +  "'" + todo_id + "' AND status = 'DONE'");
            } else {
                rs = st.executeQuery("SELECT *" +
                        " FROM task" +
                        " WHERE todo_id = " +  "'" + todo_id + "' AND status = 'NOT_DONE'");
            }


            Task temp = new Task();
            while(rs.next()) {
                temp.id = rs.getString("task_id");
                temp.name = rs.getString("name");
                temp.description = rs.getString("description");
                temp.status = rs.getString("status");
                temp.created = rs.getTimestamp("created");
                taskList.add(temp);
            }
            st.close();
            rs.close();
        } catch(SQLException  se) {
            logger.warning("Threw SQLException creating list of tasks for todo_id:" + todo_id + " for status: " + status);
            System.err.println("Threw SQLException creating list of tasks for todo_id: " + todo_id + " for status: " + status);
            System.err.println(se.getMessage());
        }
        if(status) {
            logger.info("returning tasks for todo_id " + todo_id + " that are done.");
        } else {
            logger.info("returning tasks for todo_id " + todo_id + " that are not done.");
        }
        return taskList;
    }

    /*Creates a new todo.
    *
    * @param name = name of the todo.
    *
    * @return = a Todo object.*/
    public Todo createTodo(String name) {
        logger.info("creating a new todo with name: " + name);
        Todo temp = new Todo();
        try {
            //generate random string that matches regex for todo_id.
            Generex generex = new Generex("\\d{5}[a-z]{2}\\d[-][a-z]{2}\\d{2}[-]\\d{2}[a-z]\\d[-][a-z]\\d[a-z]\\d[-][a-z]{3}\\d[a-z]\\d{3}[a-z]{2}\\d{2}");
            String randomTodo_id = generex.random();


            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO todo (id, name) VALUES ('" + randomTodo_id + "', '" + name + "')");


            ResultSet rs = st.executeQuery("SELECT *" +
                                                " FROM todo" +
                                                " WHERE id = '" + randomTodo_id + "'");


            while(rs.next()) {
                temp = new Todo();
                temp.id = rs.getString("id");
                temp.name = rs.getString("name");
                temp.created = rs.getTimestamp("created");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            //if key constraint violation was the problem.
            if (ex.getSQLState().equals("23505")) {
                logger.warning("todo with same todo_id already exists. attempting to create one with a new todo_id.");
                return createTodo(name);
            } else {
                logger.warning("Threw an SQLException while creating todo. The error message is: " + ex.getMessage());
                System.err.println(ex.getMessage());
            }

        } catch (Exception e) {
            logger.warning("Threw other exception. The error message is: " + e.getMessage());
            System.err.println(e.getMessage());
        }
        logger.info("returning a new todo with name: " + name);
        return temp;
    }
    /*
    * create a new task. If a todo tuple with a given todo_id doesn't exist, return empty Task.
    *
    * @param todo_id = todo id to add new task to.
    * @param name = name of the task
    * @param description = description of the task
    *
    * @return a task object.*/
    public Task createTask(String todo_id, String name, String description) {
        logger.info("creating a new task for todo_id " + todo_id + " with name " + name);
        Task temp = new Task();
        String randomTask_id = "";
        try {
            //generate random string that matches regex
            Generex generex = new Generex("\\d{5}[a-z]{2}\\d[-][a-z]{2}\\d{2}[-]\\d{2}[a-z]\\d[-][a-z]\\d[a-z]\\d[-][a-z]{3}\\d[a-z]\\d{3}[a-z]{2}\\d{2}");
            randomTask_id = generex.random();

            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO task (task_id, name, description, status, todo_id) VALUES ('" +
                                    randomTask_id + "','" + name + "','" + description + "', 'NOT_DONE', '" +
                                    todo_id + "')");

            ResultSet rs = st.executeQuery("SELECT * " +
                                                "FROM task " +
                                                "WHERE task_id = '" + randomTask_id +
                                                "' AND todo_id = '" +todo_id + "'");
            while(rs.next()) {
                temp = new Task();
                temp.id = rs.getString("task_id");
                temp.name = rs.getString("name");
                temp.description = rs.getString("description");
                temp.status = rs.getString("status");
                temp.created = rs.getTimestamp("created");
            }
            st.close();
            rs.close();
        } catch(SQLException ex) {
            if(ex.getSQLState().equals("23503")) {
                //todo with given todo_id doesn't exist
                logger.warning("todo with given todo_id does not exist. Returning empty task.");
                return new Task();
            } else if (ex.getSQLState().equals("23505")) {
                //task key is duplicate. Run the method recursively until random string generator finds
                //task key that hasn't been used.
                logger.warning("Task_id " + randomTask_id + " already exists within tasks of the given todo. " +
                                    "Attempting again with different task_id.");
                return createTask(todo_id, name, description);

            } else {
                System.err.println(ex.getMessage());
            }

        }
        logger.info("returning a new task for todo_id " + todo_id + " with name " + name);
        return temp;
    }

    /*
    * Updates task. If you provide wrong task or todo_id, nothing happens and an empty task is returned.
    *
    * @param todo_id = a todo id of the todo that task is tied to.
    * @param task_id = a task id of the task to be updated
    * @param name = (possibly new) name of the task
    * @param description = (possibly new) description of the task
    * @param status = (possibly switched) status of the task
    *
    * @return a task object holding updated task.*/
    public Task updateTask(String todo_id, String task_id, String name, String description, String status) {
        logger.info("Updating task with task_id " + task_id + " for a todo with todo_id " + todo_id);
        Task temp = new Task();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE task SET name = '" + name + "', description = '" +
                                    description + "', status = '" + status +"'" +
                                    " WHERE todo_id = '" + todo_id + "' AND task_id = '" + task_id + "'");
            ResultSet rs = st.executeQuery("SELECT * " +
                    "FROM task " +
                    "WHERE task_id = '" + task_id +
                    "' AND todo_id = '" +todo_id + "'");
            while(rs.next()) {
                temp = new Task();
                temp.id = rs.getString("task_id");
                temp.name = rs.getString("name");
                temp.description = rs.getString("description");
                temp.status = rs.getString("status");
                temp.created = rs.getTimestamp("created");
            }
            st.close();
            rs.close();
        } catch(SQLException ex) {
            logger.warning("Threw an SQLException with error message: " + ex.getMessage());
            System.out.println(ex.getMessage());
        }
        logger.info("Returning updated task with task_id " + task_id + " for a todo with todo_id " + todo_id);
        return temp;
    }

    /*
    * Deletes todo.
    * When there are tasks tied to the provided todo_id still remains,
    * automatically delete those tasks then delete the todo.
    *
    * @param todo_id = todo_id of a todo to delete*/
    public void deleteTodo(String todo_id) {
        logger.info("deleting todo with todo_id: " + todo_id);
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM task WHERE todo_id = '" + todo_id + "'");
            st.executeUpdate("DELETE FROM todo WHERE id = '" + todo_id + "'");
            logger.info("deletion complete.");
        } catch(SQLException ex) {
            logger.warning("An Error occurred during deletion. Error message is: " + ex.getMessage());
            System.err.println(ex.getMessage());
        }

    }

    /*
    * Deletes a task.
    * If given task does not exist (either task or todo_id is wrong/doesn't exist),
    * nothing is deleted.
    *
    * @param todo_id = todo_id of a todo that holds the task
    * @param task_id = id of the task to be deleted*/
    public void deleteTask(String todo_id, String task_id) {
        logger.info("deleting a task with todo_id: " + todo_id + " and task id: " + task_id);
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM task WHERE todo_id = '" + todo_id + "' AND task_id = '" + task_id + "'");
            logger.info("deletion complete");
        } catch (SQLException ex) {
            logger.warning("An Error occurred during deletion. Error message is: " + ex.getMessage());
            System.err.println(ex.getMessage());
        }

    }


}
