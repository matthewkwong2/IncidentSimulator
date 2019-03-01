package main;

import javafx.concurrent.Worker;
import javafx.util.Duration;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class IncidentSimulation {
    private static final IncidentSimulation ourInstance = new IncidentSimulation();
    final BlockingQueue<String> incidentIDs = new LinkedBlockingQueue<>();

    private final IncidentAppearanceSimulationService incidentAppearanceSimulationService
            = new IncidentAppearanceSimulationService();
    private final IncidentClearanceSimulationService incidentClearanceSimulationService
            = new IncidentClearanceSimulationService();

    private final Duration simulationPeriod = Duration.seconds(3);

    private IncidentSimulation() {
        initServices();
    }

    @Contract(pure = true)
    static IncidentSimulation getInstance() {
        return ourInstance;
    }

    private void initServices() {
        incidentAppearanceSimulationService.setPeriod(simulationPeriod);
        incidentClearanceSimulationService.setPeriod(simulationPeriod);

        incidentAppearanceSimulationService.setOnSucceeded(event -> {
            incidentAppearanceSimulationService.setPeriod(
                    simulationPeriod.multiply(ThreadLocalRandom.current().nextDouble())
            );
        });

        incidentClearanceSimulationService.setOnSucceeded(event -> {
            incidentClearanceSimulationService.setPeriod(
                    simulationPeriod.multiply(ThreadLocalRandom.current().nextDouble())
            );
        });
    }

    public void start() {
        incidentAppearanceSimulationService.start();
        incidentClearanceSimulationService.start();
    }

    public void cancel() {
        incidentAppearanceSimulationService.cancel();
        incidentClearanceSimulationService.cancel();
    }

    public void restart() {
        incidentAppearanceSimulationService.restart();
        incidentClearanceSimulationService.restart();
    }

    public void runIncidentAppearanceSimulationServiceNow() {
        incidentAppearanceSimulationService.restart();
    }

    public void runIncidentClearanceSimulationServiceNow() {
        incidentClearanceSimulationService.restart();
    }
}
