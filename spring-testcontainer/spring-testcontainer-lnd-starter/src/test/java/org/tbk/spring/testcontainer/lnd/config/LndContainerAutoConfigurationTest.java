package org.tbk.spring.testcontainer.lnd.config;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.tbk.spring.testcontainer.bitcoind.config.BitcoindContainerAutoConfiguration;
import org.tbk.spring.testcontainer.lnd.LndContainer;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class LndContainerAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void noBeansAreCreated() {
        this.contextRunner.withUserConfiguration(
                BitcoindContainerAutoConfiguration.class,
                LndContainerAutoConfiguration.class
        ).withPropertyValues(
                "org.tbk.spring.testcontainer.lnd.enabled=false"
        ).run(context -> {
            assertThat(context.containsBean("lndContainer"), is(false));
            try {
                context.getBean(LndContainer.class);
                Assert.fail("Should have thrown exception");
            } catch (NoSuchBeanDefinitionException e) {
                // continue
            }
        });
    }

    @Test
    public void beansAreCreated() {
        this.contextRunner.withUserConfiguration(
                BitcoindContainerAutoConfiguration.class,
                LndContainerAutoConfiguration.class
        ).withPropertyValues(
                "org.tbk.spring.testcontainer.bitcoind.enabled=true",
                "org.tbk.spring.testcontainer.bitcoind.rpcuser=myrpcuser",
                "org.tbk.spring.testcontainer.bitcoind.rpcpassword=correcthorsebatterystaple",
                "org.tbk.spring.testcontainer.bitcoind.exposed-ports=28332, 28333",
                "org.tbk.spring.testcontainer.bitcoind.commands=" +
                        "-zmqpubrawblock=tcp://0.0.0.0:28332, " +
                        "-zmqpubrawtx=tcp://0.0.0.0:28333"
                ,
                // ----------------------------------------------
                "org.tbk.spring.testcontainer.lnd.enabled=true",
                "org.tbk.spring.testcontainer.lnd.commands=" +
                        "--bitcoind.rpcuser=myrpcuser, " +
                        "--bitcoind.rpcpass=correcthorsebatterystaple"
        ).run(context -> {
            assertThat(context.containsBean("lndContainer"), is(true));
            assertThat(context.getBean(LndContainer.class), is(notNullValue()));
        });
    }

}
