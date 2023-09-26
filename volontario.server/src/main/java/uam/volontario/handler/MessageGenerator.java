package uam.volontario.handler;

import lombok.experimental.UtilityClass;
import uam.volontario.dto.Application.ApplicationDto;
import uam.volontario.model.institution.impl.Institution;

@UtilityClass
public class MessageGenerator
{
    public static String getInstitutionNotVerifiedMessage( Institution institution )
    {
        return "Institution " + institution.getName()
                + ( institution.getKrsNumber() != null ? "(KRS: " + institution.getKrsNumber() + ")" : "" )
                + " is not yet accepted by system administrator.";
    }

    public static String getUserNotFoundMessage( ApplicationDto aDto )
    {
        return getUserNotFoundMessage( aDto.getVolunteerId() );
    }
    public static String getUserNotFoundMessage( Long aVolunteerId )
    {
        return "User with id " + aVolunteerId + " not found.";
    }


    public static String getOfferNotFoundMessage( ApplicationDto aDto )
    {
        return getOfferNotFoundMessage( aDto.getOfferId() );
    }

    public static String getOfferNotFoundMessage( Long aOfferId )
    {
        return "Offer with id " + aOfferId + " not found.";
    }

    public static String getApplicationNotFoundMessage( Long aApplicationId )
    {
        return "Application with id " + aApplicationId + " not found.";
    }

    public static String getVolunteerNotFoundMessage( Long aVolunteerId )
    {
        return "Volunteer with id " + aVolunteerId + " not found.";
    }

    public static String getInstutionNotFoundMessage( Long aInstitutionId )
    {
        return "Institution with id " + aInstitutionId + " not found.";
    }

    public static String getModeratorNotFoundMessage( Long aModeratorId )
    {
        return "Moderator with id " + aModeratorId + " not found.";
    }

    public static String getEmployeeWithMailNotFound( String decodedContactEmail )
    {
        return "Employee with email " + decodedContactEmail + " not found.";
    }

    public static String getInstitutionNotActiveMessage( Long aInstitutionId )
    {
        return "Institution with id " + aInstitutionId + " is not active.";
    }

    public static String getInstitutionWorkerNotInInstitutionMessage(Long aInstitutionWorkerId, Long aInstitutionId )
    {
        return String.format( "Institution worker of id %o does not belong to Institution of id %o", aInstitutionWorkerId,
                aInstitutionId );
    }

    public static String getInstitutionWorkerNotFoundMessage( Long aInstitutionWorkerId )
    {
        return "Institution worker with id " + aInstitutionWorkerId + " not found.";
    }

    public static String getInstitutionNotFoundMessage( Long aInstitutionId )
    {
        return "Institution with id " + aInstitutionId + " not found.";
    }

    public static String getLoginNotInSystemMessage( String aLogin )
    {
       return  "No user with phone number/contact email " + aLogin
                + " is registered in the system.";
    }

    public static String getWrongPasswordMessage( String aLogin )
    {
        return "Wrong password for user registered with phone number/contact email "
                + aLogin + ".";
    }
}
