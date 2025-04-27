package io.quarkus.deployment.i18n;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@MessageBundle
public interface BravoMessages {

    @Message("Hello bravo!")
    String hello();

}
