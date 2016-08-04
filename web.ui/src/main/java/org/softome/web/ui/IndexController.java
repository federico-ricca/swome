package org.softome.web.ui;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String index(Model model) throws IOException {
		return "index";
	}
}
