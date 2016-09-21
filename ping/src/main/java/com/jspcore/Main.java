package com.jspcore;

import com.hazelcast.config.*;
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


        Config hazelcastConfig = new Config();
        AwsConfig awsConfig = new AwsConfig();
        awsConfig.setAccessKey("ACCESS_KEY");
        awsConfig.setSecretKey("ACCESS_KEY");
        awsConfig.setRegion("us-west-1");

        hazelcastConfig
                .setNetworkConfig(new NetworkConfig()
                .setJoin(new JoinConfig().setMulticastConfig(new MulticastConfig().setEnabled(Boolean.FALSE))
                        .setAwsConfig(awsConfig)));
// Now set some stuff on the config (omitted)

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);

        VertxOptions vertxOptions = new VertxOptions().setClustered(true).setClusterManager(mgr);
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
