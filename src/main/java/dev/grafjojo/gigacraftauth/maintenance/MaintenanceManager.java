package dev.grafjojo.gigacraftauth.maintenance;

import dev.grafjojo.gigacraftauth.GigaAuth;

public class MaintenanceManager {

    public boolean isMaintenanceActive() {
        return MaintenanceConfig.maintenance;
    }

    public void setMaintenance(boolean active) {
        MaintenanceConfig.maintenance = active;
        GigaAuth.get().getConfigFactory().update(MaintenanceConfig.class);
    }

    public String getMaintenanceReason() {
        return MaintenanceConfig.maintenanceReason;
    }

    public void setMaintenanceReason(String reason) {
        MaintenanceConfig.maintenanceReason = reason;
        GigaAuth.get().getConfigFactory().update(MaintenanceConfig.class);
    }
}
