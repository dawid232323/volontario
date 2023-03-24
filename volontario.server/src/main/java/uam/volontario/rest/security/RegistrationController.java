package uam.volontario.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uam.volontario.crud.service.UserService;
import uam.volontario.dto.VolunteerDto;
import uam.volontario.dto.convert.DtoConverter;
import uam.volontario.model.common.impl.User;
import uam.volontario.validation.ValidationResult;
import uam.volontario.validation.service.UserValidationService;

@RestController
@RequestMapping("/")
public class RegistrationController
{
    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private UserService userService;

    @PostMapping( value = "/register", consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > registerVolunteer( @RequestBody VolunteerDto aDto )
    {
        try
        {
            final User user = DtoConverter.createVolunteerFromDto( aDto );
            final ValidationResult validationResult = userValidationService.validateVolunteerUser( user );

            if( validationResult.isValidated() )
            {
                userService.saveOrUpdate( user );
                return ResponseEntity.status( HttpStatusCode.valueOf( 201 ) )
                        .body( validationResult.getValidatedEntity() );
            }

            return ResponseEntity.badRequest()
                    .body( validationResult.getValidationViolations() );
        }
        catch ( Exception aE )
        {
            return ResponseEntity.status( HttpStatusCode.valueOf( 500 ) )
                    .build();
        }
    }
}
