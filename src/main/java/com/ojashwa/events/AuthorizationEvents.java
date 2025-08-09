package com.ojashwa.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.stereotype.Component;

/**
 * This component is an example of how to hook into Spring Security's events.
 * Spring Security emits events at different stages of the authentication process,
 * and by registering an event listener, you can tap into these events. This
 * component is a simple example of how to log events for when a user logs in
 * successfully or fails to do so.
 * <p>
 * The methods annotated with {@link EventListener} are the ones that will be
 * invoked when the corresponding event is published.
 */

@Component
@Slf4j
public class AuthorizationEvents {

    /**
     * Logs a message when the user fails to get authorized.
     *
     * @param deniedEvent the event that holds the information about the
     *                     failed authorization.
     */

    @EventListener
    public void onFailure(AuthorizationDeniedEvent deniedEvent) {
        log.error("Authorization failed for the user: {} due to: {}", deniedEvent.getAuthentication().get().getName(),
                deniedEvent.getAuthorizationResult().toString());
    }

    /**
     * Logs a message when the user is authorized successfully.
     *
     * @param successEvent the event that holds the information about the
     *                     successful authorization.
     */

    @EventListener
    public void onSuccess(AuthorizationGrantedEvent successEvent) {
        log.info("Authorization successful for the user: {} with: {}", successEvent.getAuthentication().get().getName(),
                successEvent.getAuthorizationResult().toString());
    }

}
