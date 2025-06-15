package cathaybk.project.controller;

import cathaybk.project.dto.CoindeskResponseDTO;
import cathaybk.project.service.CoindeskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coindesk")
public class CoindeskController {

    private final CoindeskService service;

    public CoindeskController(CoindeskService service) {
        this.service = service;
    }

    @GetMapping("/original")
    public String getOriginalJson() {
        return service.getOriginalCoindeskJson();
    }

    @GetMapping("/converted")
    public CoindeskResponseDTO getConvertedData() {
        return service.getConvertedData();
    }

}
