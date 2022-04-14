package mx.com.adoptame.entities.character;

import mx.com.adoptame.entities.user.User;
import mx.com.adoptame.entities.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/character")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @Secured("ROLE_ADMINISTRATOR")
    public String type(Model model) {
        model.addAttribute("list", characterService.findAll());
        return "views/resources/character/characterList";
    }

    @GetMapping("/form")
    @Secured("ROLE_ADMINISTRATOR")
    public String form(Model model, Character character) {
        return "views/resources/character/characterForm";
    }

    @PostMapping("/save")
    @Secured("ROLE_ADMINISTRATOR")
    public String save(Authentication authentication, @Valid Character character, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                return "views/resources/character/characterForm";
            } else {
                String username = authentication.getName();
                Optional<User> user = userService.findByEmail(username);
                if (user.isPresent()) {
                    character.setStatus(true);
                    characterService.save(character, user.get());
                    redirectAttributes.addFlashAttribute("msg_success", "Carácter guardado exitosamente");
                }
            }
        } catch (Exception e) {
        }
        return "redirect:/character/";
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_ADMINISTRATOR")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Character character = characterService.findOne(id).orElse(null);
        if (character == null) {
            redirectAttributes.addFlashAttribute("msg_error", "Carácter no encontrado");
            return "redirect:/character/";
        }
        model.addAttribute("character", character);
        return "views/resources/character/characterForm";
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_ADMINISTRATOR")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            if (Boolean.TRUE.equals(characterService.delete(id, user.get()))) {
                redirectAttributes.addFlashAttribute("msg_success", "Carácter eliminado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Carácter no eliminado");
            }
        }
        return "redirect:/character/";
    }

}
