package ameba.websocket;

import ameba.websocket.internal.DefaultServerEndpointConfig;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author icode
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WebSocket {

    /**
     * Enable SockJS support
     * @return SockJS enabled
     */
    boolean withSockJS() default false;

    /**
     * The ordered array of web socket protocols this endpoint supports.
     * For example, {"superchat", "chat"}.
     *
     * @return the subprotocols.
     */
    public String[] subprotocols() default {};

    /**
     * The ordered array of decoder classes this endpoint will use. For example,
     * if the developer has provided a MysteryObject decoder, this endpoint will be able to
     * receive MysteryObjects as web socket messages. The websocket runtime will use the first
     * decoder in the list able to decode a message, ignoring the remaining decoders.
     *
     * @return the decoders.
     */
    public Class<? extends Decoder>[] decoders() default {};

    /**
     * The ordered array of encoder classes this endpoint will use. For example,
     * if the developer has provided a MysteryObject encoder, this class will be able to
     * send web socket messages in the form of MysteryObjects. The websocket runtime will use the first
     * encoder in the list able to encode a message, ignoring the remaining encoders.
     *
     * @return the encoders.
     */
    public Class<? extends Encoder>[] encoders() default {};


    /**
     * The optional custom configurator class that the developer would like to use
     * to further configure new instances of this endpoint. If no configurator
     * class is provided, the implementation uses its own.  The implementation
     * creates a new instance of the configurator per logical endpoint.
     *
     * @return the custom configuration class, or ServerEndpointConfig.Configurator.class
     * if none was set in the annotation.
     */
    public Class<? extends DefaultServerEndpointConfig.Configurator> configurator() default DefaultServerEndpointConfig.Configurator.class;

    public Class<? extends Extension>[] extensions() default {};
}