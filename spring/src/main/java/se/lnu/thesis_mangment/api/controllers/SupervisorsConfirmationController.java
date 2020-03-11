package se.lnu.thesis_mangment.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.lnu.thesis_mangment.configurations.responses.ResourceNotFoundException;
import se.lnu.thesis_mangment.model.SupervisorsConfirmation;
import se.lnu.thesis_mangment.model.SupervisorsConfirmationDTO;
import se.lnu.thesis_mangment.services.SupervisorsConfirmationServices;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * The type Supervisors confirmation controller.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/api/supervisor-confirmation")
public class SupervisorsConfirmationController extends Controller
{
    @Autowired
    private SupervisorsConfirmationServices services;
    private static final String CONFIRMATION = "confirmation";

    /**
     * Get map.
     *
     * @param input the input
     * @return the map
     */
    @GetMapping(value = "/get")
    public Map<String, Object> get(@Valid SupervisorsConfirmationDTO input)
    {
        var tt = services.get(input);
        return response(new ResponseArgument<>(CONFIRMATION, tt));
    }

    /**
     * Add map.
     *
     * @param input the input
     * @return the map
     */
    @PostMapping(value = "/add")
    @Transactional
    public Map<String, Object> add(@Valid SupervisorsConfirmationDTO input)
    {

        SupervisorsConfirmation supervisorsConfirmation = getSupervisorsConfirmationFromInput(input);
        services.add(supervisorsConfirmation);
        return response(new ResponseArgument<>(CONFIRMATION, supervisorsConfirmation));
    }

    /**
     * Update confirmation map.
     *
     * @param input the input
     * @return the map
     */
    @PostMapping(value = "/update/{id}")
    public Map<String, Object> updateConfirmation(@Valid SupervisorsConfirmationDTO input)
    {
        SupervisorsConfirmationDTO dInput = new SupervisorsConfirmationDTO();
        dInput.setId(input.getId());
        SupervisorsConfirmation supervisorsConfirmation = getById(dInput);


        supervisorsConfirmation.setConfirmed(input.getConfirmed());
        services.update(supervisorsConfirmation);
        return response(new ResponseArgument<>(CONFIRMATION, supervisorsConfirmation));
    }

    /**
     * Gets by id.
     *
     * @param input the input
     * @return the by id
     */
    public SupervisorsConfirmation getById(@Valid SupervisorsConfirmationDTO input)
    {
        List<SupervisorsConfirmation> list = services.get(input);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("confirmation not found");
        }
        return list.get(0);
    }


    // need improve
    private SupervisorsConfirmation getSupervisorsConfirmationFromInput(SupervisorsConfirmationDTO input)
    {
        SupervisorsConfirmation supervisorsConfirmation = new SupervisorsConfirmation();
        supervisorsConfirmation.setConfirmed(input.getConfirmed());
        supervisorsConfirmation.setProjectPlanId(input.getProjectPlanId());
        supervisorsConfirmation.setUser(input.getUser());
        supervisorsConfirmation.setSupervisorId(input.getSupervisorId());
        return supervisorsConfirmation;
    }



}
