package com.clientApiVoiture.ClientApiVoiture.client;

import com.clientApiVoiture.ClientApiVoiture.form.VoitureForm;
import com.clientApiVoiture.ClientApiVoiture.model.Voiture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class mainController {
    private static List<Voiture> voitures = new ArrayList<Voiture>();

    // Injectez (inject) via application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    /**
     * @param model
     * @return
     */
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("message", message);

        return "index";
    }

    /**
     * @param model
     * @return
     */
    @RequestMapping(value = { "/voitureListe" }, method = RequestMethod.GET)
    public String carList(Model model) {
        String url = "http://localhost:8080/Voitures";
        RestTemplate restTemplate = new RestTemplate();
        List<Voiture> voitures = restTemplate.getForObject(url, List.class);
        model.addAttribute("voitures", voitures);

        return "carList";
    }

    @RequestMapping(value = { "/voitureListe/{id}" }, method = RequestMethod.GET)
    public String carDetail(@PathVariable int id, Model model) {
        String url = "http://localhost:8080/Voitures/"+id;
        RestTemplate restTemplate = new RestTemplate();
        Voiture voiture = restTemplate.getForObject(url, Voiture.class);
        model.addAttribute("voiture", voiture);

        return "detailCar";
    }

    @RequestMapping(value = { "/modifVoiture/{id}" }, method = RequestMethod.GET)
    public String carModif(@PathVariable int id, Model model) {
        String url = "http://localhost:8080/Voitures/"+id;
        RestTemplate restTemplate = new RestTemplate();
        VoitureForm voitureForm = restTemplate.getForObject(url, VoitureForm.class);
        model.addAttribute("voitureForm", voitureForm);


        return "updateCar";
    }

    @PostMapping(value = { "/modifVoiture/{id}" })
    public String modifCar(Model model, //
                          @ModelAttribute("voitureForm") VoitureForm voitureForm, @PathVariable int id) {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Voiture voiture = new Voiture();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (voitureForm != null) {
            voiture.setNom(voitureForm.getNom());
            voiture.setPrix(voitureForm.getPrix());
            voiture.setMarque(voitureForm.getMarque());
            voiture.setAnnee(voitureForm.getAnnee());
            HttpEntity<Voiture> request = new HttpEntity<Voiture>(voiture, headers);
            String url = "http://localhost:8080/Voitures/"+id;
            rt.put(url, request, Voiture.class);

            return "redirect:/voitureListe";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCar";
    }




    @DeleteMapping(value = "/voiture/{id}")
    public String remove(@PathVariable Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/Voitures/" + id;
        restTemplate.delete(url);
        return "carList";
    }



    @RequestMapping(value = { "/ajoutVoiture" }, method = RequestMethod.GET)
    public String showAddCarPage(Model model) {

        VoitureForm voitureForm = new VoitureForm();
        model.addAttribute("voitureForm", voitureForm);

        return "addCar";
    }


    @RequestMapping(value = { "/ajoutVoiture" }, method = RequestMethod.POST)
    public String saveCar(Model model, //
                          @ModelAttribute("voitureForm") VoitureForm voitureForm) {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Voiture voiture = new Voiture();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (voitureForm != null) {
            voiture.setNom(voitureForm.getNom());
            voiture.setPrix(voitureForm.getPrix());
            voiture.setMarque(voitureForm.getMarque());
            voiture.setAnnee(voitureForm.getAnnee());
            HttpEntity<Voiture> request = new HttpEntity<Voiture>(voiture, headers);
            String url = "http://localhost:8080/Voitures/";
            rt.postForObject(url, request, Voiture.class);

            return "redirect:/voitureListe";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCar";
    }
}
