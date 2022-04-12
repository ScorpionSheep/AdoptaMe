package mx.com.adoptame.entities.size;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/size")
@Slf4j
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping("/")
    @Secured("ROLE_ADMINISTRATOR")
    public String type(Model model) {
        model.addAttribute("list", sizeService.findAll());
        return "views/resources/size/sizeList";
    }

    @GetMapping("/form")
    @Secured("ROLE_ADMINISTRATOR")
    public String form(Model model, Size size) {
        return "views/resources/size/sizeForm";
    }

    @PostMapping("/save")
    @Secured("ROLE_ADMINISTRATOR")
    public String save(Model model, @Valid Size size, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                return "views/resources/size/sizeForm";
            } else {
                size.setStatus(true);
                sizeService.save(size);
                redirectAttributes.addFlashAttribute("msg_success", "Tamaño guardado exitosamente");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "redirect:/size/";
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_ADMINISTRATOR")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Size size = sizeService.findOne(id).orElse(null);
        if (size == null) {
            redirectAttributes.addFlashAttribute("msg_error", "Tamaño no encontrado");
            return "redirect:/size/";
        }
        model.addAttribute("size", size);
        return "views/resources/size/sizeForm";
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_ADMINISTRATOR")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        if (Boolean.TRUE.equals(sizeService.delete(id))) {
            redirectAttributes.addFlashAttribute("msg_success", "Tamaño eliminado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Tamaño no eliminado");
        }
        return "redirect:/size/";
    }
}
