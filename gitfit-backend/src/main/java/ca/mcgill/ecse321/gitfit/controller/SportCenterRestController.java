package ca.mcgill.ecse321.gitfit.controller;

import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gitfit.dto.HoursDto;
import ca.mcgill.ecse321.gitfit.dto.SportCenterDto;
import ca.mcgill.ecse321.gitfit.model.SportCenter;
import ca.mcgill.ecse321.gitfit.service.SportCenterService;

/**
 * @author William Wang (wangwiza)
 * 
 * 
 */
@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class SportCenterRestController {

    @Autowired
    private SportCenterService sportCenterService;

    /**
     * Get the sport center
     * 
     * @author William Wang (wangwiza)
     * @return
     */
    @GetMapping(value = { "/sportcenter", "/sportcenter/" })
    public SportCenterDto getSportCenter() {
        return convertToDto(sportCenterService.getSportCenter());
    }

    /**
     * Update sport center name
     * 
     * @author William Wang (wangwiza)
     * @param sportCenter
     * @return
     */
    @PutMapping(value = { "/sportcenter/name", "/sportcenter/name/" })
    public SportCenterDto updateSportCenterName(@RequestBody String name) {
        return convertToDto(sportCenterService.setSportCenterName(name));
    }

    /**
     * Update sport center max capacity
     * 
     * @author William Wang (wangwiza)
     * @param sportCenter
     * @return
     */
    @PutMapping(value = { "/sportcenter/capacity", "/sportcenter/capacity/" })
    public SportCenterDto updateSportCenterMaxCapacity(@RequestBody int maxCapacity) {
        return convertToDto(sportCenterService.setSportCenterMaxCapacity(maxCapacity));
    }

    /**
     * Update sport center opening hours
     * 
     * @author William Wang (wangwiza)
     * @param sportCenter
     * @return
     */
    @PutMapping(value = { "/sportcenter/hours", "/sportcenter/hours/" })
    public SportCenterDto updateSportCenterHours(@RequestBody HoursDto hours) {
        return convertToDto(
                sportCenterService.setOpenHours(Time.valueOf(hours.getOpeningTime()),
                        Time.valueOf(hours.getClosingTime())));
    }

    /**
     * Convert model instance to DTO instance
     * 
     * @author William Wang (wangwiza)
     * @param sportCenter
     * @return
     */
    private SportCenterDto convertToDto(SportCenter sportCenter) {
        if (sportCenter == null) {
            throw new IllegalArgumentException("There is no such sport center!");
        }
        SportCenterDto sportCenterDto = new SportCenterDto(sportCenter.getName(), sportCenter.getMaxCapacity(),
                sportCenter.getOpeningTime(),
                sportCenter.getClosingTime());
        return sportCenterDto;
    }
}
