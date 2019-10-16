package pl.wszeborowski.mateusz.resource.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * This class is responsible for configuring CORS in order for the frontend to be able to get
 * responses from a server
 *
 * @author wszeborowskimateusz
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {
        responseContext.getHeaders()
                       .add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders()
                       .add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders()
                       .add("Access-Control-Allow-Headers",
                               "origin, content-type, accept, authorization");
        responseContext.getHeaders()
                       .add("Access-Control-Allow-Methods",
                               "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
