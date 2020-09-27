package no.hvl.dat250.restapi;

import com.google.gson.Gson;

import java.util.List;

import static spark.Spark.*;

public class App {

    static TodoDAO tDao = new TodoDAO();
    static Todo todo = new Todo();
    static Gson gson = new Gson();


    public static void main(String[] args) {

        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        after((req, res) -> {
            res.type("application/json");
        });

        post("/todos", (request, response) -> {
            todo = gson.fromJson(request.body(), Todo.class);
            tDao.addTodo(todo);
            return todo.toJson();
        });
        get("/todos", (request, response) -> {
            List <Todo> todo = tDao.getTodo();
            return gson.toJson(todo.toArray());
        });
        get("/todos/:id", (request, response) -> {
            todo = tDao.getTodo(Long.parseLong(request.params(":id")));
            return todo.toJson();
        });
        put("/todos", (request, response) -> {
            todo = gson.fromJson(request.body(), Todo.class);
            tDao.updateTodo(todo);
            return todo.toJson();
        });
        delete("/todos/:id", (request, response) -> {
            tDao.deleteTodo(Long.parseLong(request.params(":id")));
            return "Deleted: " + (Long.parseLong((request.params(":id"))));
        });


    }


}
