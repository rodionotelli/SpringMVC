package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;

    private final String GET_METHOD = "GET";
    private final String POST_METHOD = "POST";
    private final String DELETE_METHOD = "DELETE";
    private final String API_POSTS = "/api/posts";
    private final String POSTS_ID = "/api/posts/\\d+";
    private final String PATH = "/";


    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET_METHOD) && path.equals(API_POSTS)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET_METHOD) && path.matches(POSTS_ID)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(PATH) + 1));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST_METHOD) && path.equals(API_POSTS)) {
                controller.save(req.getReader(), resp);
            }
            if (method.equals(DELETE_METHOD) && path.matches(POSTS_ID)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(PATH) + 1));
                controller.removeById(id, resp);
            }
        } catch (NotFoundException e) {
            e.getMessage();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            e.getMessage();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

