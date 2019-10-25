package pl.wszeborowski.mateusz.resource;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Service class for easy creation of JAX-RS uris.
 *
 * @author wszeborowskimateusz
 */
public class UriHelper {

    /**
     * Creates parametrized local (without hostname) URI to the resource. When passed to the
     * Response.created is converted
     * to the global address with current hostname and context path.
     *
     * @param clazz  jAX-RS resource class
     * @param method text name of the JAX-RS resource class method to which uri should point
     * @param vals   values used to fill method params
     * @return parametrized URI to the method in the JAX-RS resource
     */
    public static URI uri(Class<?> clazz, String method, Object... vals) {
        return UriBuilder.fromResource(clazz)
                         .path(clazz, method)
                         .build(vals);
    }


    /**
     * Creates parametrized global (with hostname) URI to the resource.
     *
     * @param info   allows to create URI with current hostname and application context path
     * @param clazz  jAX-RS resource class
     * @param method text name of the JAX-RS resource class method to which uri should point
     * @param params values used to fill method path params
     * @return parametrized URI to the method in the JAX-RS resource
     */
    public static URI uri(UriInfo info, Class<?> clazz, String method, Object... params) {
        return info.getBaseUriBuilder()
                   .path(clazz)
                   .path(clazz, method)
                   .build(params);
    }

    /**
     * Creates parametrized global (with hostname)  with page number URI to the resource.
     *
     * @param info   allows to create URI with current hostname and application context path
     * @param clazz  jAX-RS resource class
     * @param method text name of the JAX-RS resource class method to which uri should point
     * @param page   page number fo paginations
     * @param params values used to fill method path params
     * @return parametrized URI to the method in the JAX-RS resource
     */
    public static URI pagedUri(UriInfo info, Class<?> clazz, String method, int page,
                               Object... params) {
        return info.getBaseUriBuilder()
                   .path(clazz)
                   .path(clazz, method)
                   .queryParam("page", page)
                   .build(params);
    }

}
