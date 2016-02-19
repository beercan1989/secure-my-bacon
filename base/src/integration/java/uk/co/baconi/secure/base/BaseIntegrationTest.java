package uk.co.baconi.secure.base;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore("abstract integration test class")
@Profile("integration")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseConfiguration.class, loader = SpringApplicationContextLoader.class)
public abstract class BaseIntegrationTest {
}
