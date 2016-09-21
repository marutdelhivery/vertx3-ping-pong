package com.jspcore;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by gsagrawal on 20/09/16.
 */
public class Main {

    public static void main(final String... args) {


        VertxOptions vertxOptions = new VertxOptions().setClustered(true);
        System.out.println(args.length);

        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                DeploymentOptions options = new DeploymentOptions().setInstances(1);
                JsonObject config = new JsonObject();
                options.setConfig(config);
                System.out.println("**event bus is **8"+vertx.eventBus());
                vertx.deployVerticle("com.jspcore.Ping", options);

            } else {
                System.out.println("failed" + res);
            }
        });
        System.out.println("Done..Stopped");
    }
}
