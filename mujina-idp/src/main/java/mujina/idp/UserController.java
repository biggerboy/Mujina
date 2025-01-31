package mujina.idp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final List<Map<String, String>> samlAttributes;

    @Autowired
    @SuppressWarnings("unchecked")
    public UserController(ObjectMapper objectMapper,
                          @Value("${idp.saml_attributes_config_file}") String samlAttributesConfigFile) throws IOException {

        DefaultResourceLoader loader = new DefaultResourceLoader();
        this.samlAttributes = objectMapper.readValue(
                loader.getResource(samlAttributesConfigFile).getInputStream(), List.class);
    }

    @GetMapping("/")
    public String index(Authentication authentication) {
        return authentication == null ? "index" : "redirect:/user.html";
    }

    @GetMapping("/user.html")
    public String user(Authentication authentication, ModelMap modelMap) {
        modelMap.addAttribute("user", authentication);
        return "user";
    }

    @GetMapping("/login")
    public String login(ModelMap modelMap) {
        modelMap.addAttribute("samlAttributes", samlAttributes);
        return "login";
    }
}
