package com.zar.demo.zar.demo.controller;

import com.zar.demo.Profile.ProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Controller public class TweetController {
    @Autowired
    private Twitter twitter;

    @RequestMapping("/")
    public String home() {
        return "searchPage";
    }


    @RequestMapping("/result")
    public String hello(@RequestParam(defaultValue = "csgo") String search, Model model) {

        SearchResults searchResults = twitter.searchOperations().search(search);
        List<Tweet> tweets = searchResults.getTweets();
        model.addAttribute("tweets", tweets);
        model.addAttribute("search", search);
        return "resultPage";
    }

    @RequestMapping(value = "/postSearch", method = RequestMethod.POST)
    public String postSearch(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String search = request.getParameter("search");
        if (search.toLowerCase().contains("śmieci")) {
            redirectAttributes.addFlashAttribute("error", "Spróbuj wpisac coś innego!");
            return "redirect:/";
        }
        redirectAttributes.addAttribute("search", search);
        return "redirect:result";

    }

    @Controller
    public class ProfileController {
        @RequestMapping("/profile")
        public String displayProfile(ProfileForm profileForm) {
            return "profile/profilePage";
        }

        @RequestMapping(value = "/profile", method = RequestMethod.POST)
        public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult){
            if(bindingResult.hasErrors()){
                return "profile/profilePage";
            }
            System.out.println("pomyślnie zapisany profil " + profileForm);
            return "redirect:/profile";
        }
    }

}

