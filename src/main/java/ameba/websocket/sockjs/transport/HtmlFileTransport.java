package ameba.websocket.sockjs.transport;

import org.glassfish.jersey.server.ChunkedOutput;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * @author icode
 */
public class HtmlFileTransport implements Transport {

    @Override
    public ChunkedOutput apply(ContainerRequestContext containerRequestContext) {
        return null;
    }
}