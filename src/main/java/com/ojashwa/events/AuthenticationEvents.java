package com.ojashwa.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
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
public class AuthenticationEvents {


    /**
     * Logs a message when the user logs in successfully.
     *
     * @param successEventEvent the event that holds the information about the
     *                          successful authentication
     */

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent successEventEvent) {
      log.info("Login successful for the user: {}", successEventEvent.getAuthentication().getName());
    }


    /**
     * Logs a message when the user fails to log in.
     *
     * @param failureEvent the event that holds the information about the
     *                     failed authentication
     */

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
        log.error("Login failed for the user: {} due to: ", failureEvent.getAuthentication().getName(),
                failureEvent.getException().getMessage());
    }

}
