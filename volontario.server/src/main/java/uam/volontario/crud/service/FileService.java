package uam.volontario.crud.service;

import java.io.IOException;

/**
 * Interface that should be implemented by all services that handle file transfer
 * between backend server and s3 storage system
 *
 * @param <T> type of input objects
 *
 * @param <K> type of output objects
 */
public interface FileService<T, K>
{
    /**
     * Loads file from file storage system, based on given file name.
     *
     * @param aFileName name of the file to be loaded
     *
     * @return retrieved file content or null if file with given name does not exist
     *
     * @throws IOException when load operation fails
     */
    K loadFile( final String aFileName ) throws IOException;


    /**
     * Saves file in the file system storage. File name depends on the service implementation.
     *
     * @param aFileContent file content to be stored
     *
     * @return name of the stored file to be persisted in the corresponding entity
     *
     * @throws IOException when save operation fails
     */
    String saveFile( final T aFileContent ) throws IOException;

    /**
     * Saves file in the file system storage with custom file name.
     *
     * @param aFileContent file content to be stored
     *
     * @param aFileName name of the file to be saved
     *
     * @return name of the stored file to be persisted in the corresponding entity
     *
     * @throws IOException when save operation fails
     */
    String saveFile( final String aFileName, final T aFileContent ) throws IOException;

    /**
     * Deletes single file.
     *
     * @param aFileName name of the file to be deleted
     */
    void deleteFile( final String aFileName );

    /**
     * Checks if file with given name exists in system bucket.
     * S3 storage directory prefixes should be added on the implementation level
     *
     * @param aFileName name of the file to be checked
     *
     * @return boolean value that depends on file existence
     */
    boolean doesFileExist( final String aFileName );
}
