package ameba.lib;

import akka.actor.ActorSystem;
import ameba.container.Container;
import ameba.core.Application;
import ameba.event.Listener;
import ameba.event.SystemEventBus;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Akka class.</p>
 *
 * @author icode
 * @since 0.1.6e
 */
public class Akka {
    private static ActorSystem system;

    private Akka() {
    }

    /**
     * <p>system.</p>
     *
     * @return a {@link akka.actor.ActorSystem} object.
     */
    public static ActorSystem system() {
        return system;
    }

    public static class AddOn extends ameba.core.AddOn {
        @Override
        public void setup(Application application) {
            String name = StringUtils.defaultString(application.getApplicationName(), "ameba");
            system = ActorSystem.create(name);
            SystemEventBus.subscribe(Container.ShutdownEvent.class, new Listener<Container.ShutdownEvent>() {
                @Override
                public void onReceive(Container.ShutdownEvent event) {
                    system.shutdown();
                }
            });
        }
    }
}
