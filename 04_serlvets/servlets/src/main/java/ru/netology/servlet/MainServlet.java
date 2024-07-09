package ru.netology.servlet;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import ru.netology.SpringConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String GET = "GET";
  private static final String POST = "POST";
  private static final String DELETE = "DELETE";
  private static final String API_POSTS = "/api/posts";
  private static final String API_POSTS_ID_REGEX = "/api/posts/\\d+";

  @Override
  public void init(ServletConfig config) throws ServletException {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.register(SpringConfig.class);
    context.refresh();
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      switch (method) {
        case GET:
          if (path.equals(API_POSTS)) {
            controller.all(resp);
          } else if (path.matches(API_POSTS_ID_REGEX)) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.getById(id, resp);
          } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
          break;
        case POST:
          if (path.equals(API_POSTS)) {
            controller.save(req.getReader(), resp);
          } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
          break;
        case DELETE:
          if (path.matches(API_POSTS_ID_REGEX)) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.removeById(id, resp);
          } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
          break;
        default:
          resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

