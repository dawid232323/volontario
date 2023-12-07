package uam.volontario.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uam.volontario.dto.templates.LandingPageDto;
import uam.volontario.handler.ConfigurationHandler;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/configuration",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigurationController
{
    private final ConfigurationHandler configurationHandler;

    @GetMapping("/landingPage")
    @ResponseStatus(HttpStatus.OK)
    public LandingPageDto resolveLandingPageData() throws IOException
    {
        return configurationHandler.resolveLandingPageData();
    }

    @PostMapping("/landingPage")
    @PreAuthorize("@permissionEvaluator.allowForAdministrators( authentication.principal )")
    @ResponseStatus(HttpStatus.CREATED)
    public LandingPageDto storeNewConfiguration( @RequestBody final LandingPageDto newConfiguration ) throws IOException
    {
        return configurationHandler.soreNewLandingPageConfiguration( newConfiguration );
    }
}
