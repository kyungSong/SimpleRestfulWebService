A simple web service built using jetty and javax.

Stores tasks and todos as well as retrieving them from a relational database.

For persistent storage of todos and tasks I used Postgresql database.
Database name is "challenge" and "challenge" holds two relations
"todo" and "task".

"todo" has columns "id", "name", and "created". "id" is a primary key.

"task" has columns "task_id", "name", "description", "status", "created" and
"todo_id". Both "task_id" and "todo_id" are primary keys.

"todo_id" is a foreign key referencing "id" in "todo" relation.

Run main of "App" then enter desired port number to test the program.
