package next.web.qna;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import next.model.qna.Question;
import next.service.qna.ExistedAnotherUserException;
import next.service.qna.QnaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value={"", "/questions"})
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Resource(name = "qnaService")
	private QnaService qnaService;

	@RequestMapping("")
	public String list(Model model) {
		model.addAttribute("questions", qnaService.findAll());
		return "qna/list";
	}
	
	@RequestMapping("/{id}")
	public String show(@PathVariable long id, Model model) {
		model.addAttribute("question", qnaService.findById(id));
		return "qna/show";
	}
	
	@RequestMapping("/form")
	public String form(Model model) {
		model.addAttribute("question", new Question());
		model.addAttribute("url", "/questions");
		return "qna/form";
	}
	
	@RequestMapping("/{id}/form")
	public String editQuestion(@PathVariable long id, Model model) {
		model.addAttribute("question",  qnaService.findById(id));
		model.addAttribute("url", "/questions/edit/" + id);
		model.addAttribute("isEdit", "true");
		return "qna/form";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String save(@Valid Question question, BindingResult bindingResult) {
		logger.debug("Question : {}", question);
		if (bindingResult.hasFieldErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError error : errors) {
				logger.debug("field : {}, error code : {}", error.getField(), error.getCode());
			}
			return "qna/form";
		}
		qnaService.save(question);
		return "redirect:/";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST)
	public String edit(@PathVariable long id, @Valid Question question, BindingResult bindingResult) {
		logger.debug("Question : {}", question);
		if (bindingResult.hasFieldErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError error : errors) {
				logger.debug("field : {}, error code : {}", error.getField(), error.getCode());
			}
			return "qna/form";
		}
		qnaService.update(question, id);
		return "redirect:/";
	}
	
	@RequestMapping(value="/{id}/delete", method=RequestMethod.GET)
	public String edit(@PathVariable long id) {
		try {
			qnaService.delete(id);
		} catch (ExistedAnotherUserException e) {
			return "redirect:/" + id;
		}
		return "redirect:/";
	}
}
