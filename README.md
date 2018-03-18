
This is a simple server for tracking todo items.
The system contains two entity types User and Todo.
 - User has an id, a username, an email address and a list of todo items.
 - Todo has an id, a subject, a due date and if the item is done or not.


# Start server

The server requires Java SE 8 to run.

## Linux/OSX:

Run the startscript:
   go to setup/ and run a command: sh server_start.sh

The server startup on port 8080 and you can access it at localhost:8080/swagger-ui.html#/

The server provides to following REST API:

---------------
-- Backup accounts --
---------------
This API will initiate a complete backup of all todo items in the TodoItemServer. The backup is asynchronous and the API will return the the id for the initiated backup.

Request: 
POST /backups
Request body: N/A 
Response body: 
{
“backupId” : <backupId>
}

--------------
-- List backups  --
--------------
This API will list all backups that have initiated. Backup status is one of the following:
	◦	In progress
	◦	Done
	◦	Failed

Request: GET /backups
Request body: N/A
Response body:
[
    {
        "date": <date>,
        "status": <backup status>,
        "backupId": <backup id>
    }
]


-----------------
-- Export backup --
-----------------
This API will return the content of a specified backup id 
the CSV format specified below. 

Request: GET /exports/{backup id}
Request body: N/A
Response body:
Username;TodoItemId;Subject;DueDate;Done
{username};{todoitemid};{subject};{duedate};{done}
