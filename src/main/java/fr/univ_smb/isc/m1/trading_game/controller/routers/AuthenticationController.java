package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = URLMap.LOGIN_PAGE)
    public String logIn(HttpServletRequest request, Model model) {
        model.addAttribute("performLogin", URLMap.PERFORM_LOGIN);
        model.addAttribute("signupPage", URLMap.SIGNUP_PAGE);

        HttpSession session = request.getSession(false);
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                model.addAttribute("errorMessage", "Identifiants incorrects");
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }

        return "logIn";
    }

    @GetMapping(value = URLMap.SIGNUP_PAGE)
    public String register(Model model){
        model.addAttribute("performRegister",URLMap.PERFORM_SIGNUP);
        return "register";
    }

    @PostMapping(value =URLMap.PERFORM_SIGNUP)
    public String performRegister(@RequestParam(name = "username")String username,
                                  @RequestParam(name = "password")String password,
                                  RedirectAttributes redirectAttrs){
        if(userService.register(username, password, false)){
            return "redirect:"+URLMap.LOGIN_PAGE;
        } else {
            redirectAttrs.addFlashAttribute("error", "L'utilisateur existe déjà");
            return "redirect:"+URLMap.SIGNUP_PAGE;
        }

    }
}
