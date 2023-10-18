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
     * Deletes single file.
     *
     * @param aFileName name of the file to be deleted
     */
    void deleteFile( final String aFileName );
}
