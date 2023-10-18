package uam.volontario.handler;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uam.volontario.crud.service.FileService;
import uam.volontario.crud.service.UserService;
import uam.volontario.crud.service.impl.file.FileServiceQualifiers;
import uam.volontario.model.common.impl.User;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class ProfilePictureHandler
{
    private final FileService< MultipartFile, byte[] > profilePictureService;
    private final UserService userService;

    private static final Logger LOGGER = LogManager.getLogger( ProfilePictureHandler.class );

    public ProfilePictureHandler( final @Qualifier( FileServiceQualifiers.PROFILE_PICTURE_FILES_SERVICE )
                                  FileService< MultipartFile, byte[] > aMultipartFileFileService,
                                  final UserService aUserService )
    {
        this.profilePictureService = aMultipartFileFileService;
        this.userService = aUserService;
    }

    /**
     * Transactional method that stores profile picture for specified user.
     * If given user already has profile picture assigned,
     * the old one is deleted from the system and replaced by the new one.
     *
     * @param aUserId id of user
     *
     * @param aPhoto content of a user profile picture
     *
     * @return object with new file name
     */
    @Transactional
    public Map<String, String> saveUserProfilePicture( final Long aUserId, final MultipartFile aPhoto )
    {
        final User user = this.getUser( aUserId );
        if( user.hasAssignedPicture() )
        {
            this.profilePictureService.deleteFile( user.getPathToImage() );
        }
        final String photoPath;
        try
        {
            photoPath = this.profilePictureService.saveFile( aPhoto );
        } catch ( IOException aE ) {
            LOGGER.error( aE.getMessage(), aE );
            throw new IllegalStateException( String.format( "Could not save file for user: %s, exception message: %s",
                    user.getUsername(), aE.getMessage() ) );
        }
        user.setPathToImage( photoPath );
        this.userService.saveOrUpdate( user );
        return Map.of( "fileName", photoPath );
    }

    /**
     * Retrieves image for selected user.
     *
     * @param aUserId id of user that image needs to be retrieved
     *
     * @return response entity with empty body if user does not have any photo assigned, or
     *          response entity with photo content
     */
    public ResponseEntity< ? > getUserProfilePicture( final Long aUserId )
    {
        final User user = this.getUser( aUserId );
        if( !user.hasAssignedPicture() )
        {
            return ResponseEntity.ok().build();
        }
        final byte[] loadedPicture;
        try {
         loadedPicture = this.profilePictureService.loadFile( user.getPathToImage() );
        } catch ( IOException aE )
        {
            LOGGER.error( aE.getMessage(), aE );
            throw new IllegalStateException( String.format( "Could not load file for user: %s, exception message: %s",
                    user.getUsername(), aE.getMessage() ) );
        }
        return ResponseEntity.ok().contentType( getPictureMediaType( user.getPathToImage() ) )
                .body( loadedPicture );
    }

    private User getUser( final Long aUserId )
    {
        return this.userService.tryToFindById( aUserId )
                .orElseThrow(() -> new EntityNotFoundException( "User with given id does not exist" ) );
    }

    private MediaType getPictureMediaType( final String aImagePath )
    {
        if( aImagePath.toLowerCase().endsWith( ".jpg" ) || aImagePath.toLowerCase().endsWith( ".jpeg" ) )
        {
            return MediaType.IMAGE_JPEG;
        }
        if( aImagePath.toLowerCase().endsWith( ".png" ) )
        {
            return MediaType.IMAGE_PNG;
        }
        throw new IllegalArgumentException( "Saved image path does not contain any of known image extensions. " +
                "Supported extensions are .jpg and .png");
    }
}
