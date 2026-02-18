package com.ticket.gestione_ticket.loggers;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component("retryLogger")
public class RetryLogger implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
                                                 RetryCallback<T, E> callback) {
        int attempt = context.getRetryCount() + 1;
        System.out.println("Avvio tentativo n." + attempt);
        return true;
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context,
                                                 RetryCallback<T, E> callback,
                                                 Throwable throwable) {
        System.out.println("Tentativo n." + context.getRetryCount()
                + " fallito: " + throwable.getMessage());
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback,
                                               Throwable throwable) {
        if (throwable != null) System.out.println("Esauriti tutti i tentativi.");
    }
}