package me.badstagram.vortex.core;

import me.badstagram.vortex.util.FormatUtil;
import org.joda.time.Duration;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private final ScheduledExecutorService executorService;

    public Scheduler(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public void createScheduledTask(long delay, ChronoUnit unit, Runnable action) {
        Vortex.getLogger().debug("Scheduled task created. Duration {} {}. Executing at {}", delay, unit.toString(), FormatUtil.formatDate(OffsetDateTime.now().plus(delay, unit)));


        this.executorService.schedule(action, delay, TimeUnit.of(unit));
    }

    public void createScheduledTask(long delay, Runnable action) {
        this.createScheduledTask(delay, ChronoUnit.MINUTES, action);
    }

    public void createScheduledTask(Duration duration, Runnable action) {
        if (duration.getStandardSeconds() != 0) {
            this.createScheduledTask(duration.getStandardSeconds(), ChronoUnit.SECONDS, action);
        } else if (duration.getStandardMinutes() != 0) {
            this.createScheduledTask(duration.getStandardSeconds(), ChronoUnit.MINUTES, action);

        } else if (duration.getStandardHours() != 0) {
            this.createScheduledTask(duration.getStandardSeconds(), ChronoUnit.HOURS, action);

        } else if (duration.getStandardDays() != 0) {
            this.createScheduledTask(duration.getStandardSeconds(), ChronoUnit.DAYS, action);
        }
    }
}
