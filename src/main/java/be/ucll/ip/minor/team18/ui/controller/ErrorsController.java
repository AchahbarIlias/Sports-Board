package be.ucll.ip.minor.team18.ui.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorMessage", "not.found.error");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorMessage", "internal.server.error");
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorMessage", "forbidden.error");
            } else {
                model.addAttribute("errorMessage", "error.message");
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
