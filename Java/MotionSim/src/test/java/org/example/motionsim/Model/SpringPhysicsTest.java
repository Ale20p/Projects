package org.example.motionsim.Model;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpringPhysicsTest {

    private SpringPhysics physics;

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
        }
    }

    @BeforeEach
    void setUp() {
        physics = SpringPhysics.getInstance();
        physics.reset();
    }

    @Test
    void testCalculateSpringPotentialEnergy() {
        double k = 10.0, x = 2.0;
        double expected = 0.5 * k * x * x;
        assertEquals(expected,
                SpringPhysics.calculateSpringPotentialEnergy(k, x),
                1e-6,
                "½·k·x² should match expected potential energy");
    }

    @Test
    void testCalculateAngleRad() {
        assertEquals(Math.PI / 2,
                SpringPhysics.calculateAngleRad(90.0),
                1e-6,
                "90° → π/2");
        assertEquals(Math.PI / 6,
                SpringPhysics.calculateAngleRad(30.0),
                1e-6,
                "30° → π/6");
    }

    @Test
    void testCalculateLaunchVelocity() {
        double k = 10.0, m = 5.0, x = 2.0;
        double expectedV = x * Math.sqrt(k / m);
        assertEquals(expectedV,
                physics.calculateLaunchVelocity(k, m, x),
                1e-6,
                "v = x·√(k/m)");
    }

    @Test
    void testProjectileMotionX() {
        double vx = 8.0, t = 2.5;
        physics.setHorizontalVelocity(vx);
        physics.setInitialPosition(0, 0);
        assertEquals(vx * t,
                physics.calculateX(t),
                1e-6,
                "x(t) = v_x · t");
    }

    @Test
    void testProjectileMotionY() {
        double vy = 4.0, g = 9.8, t = 3.0;
        physics.setVerticalVelocity(vy);
        physics.setGravity(g);
        physics.setInitialPosition(0, 0);
        double expectedY = vy * t + 0.5 * g * t * t;
        assertEquals(expectedY,
                physics.calculateY(t),
                1e-6,
                "y(t) = v_y t + ½ g t²");
    }
}
