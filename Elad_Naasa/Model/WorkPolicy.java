package Model;

public class WorkPolicy {

  private boolean allowHourChange;
  private boolean requireSync;
  private WorkPreference currentPolicy;

  public WorkPolicy(boolean allowHourChange, boolean requireSync) {
    this.allowHourChange = allowHourChange;
    this.requireSync = requireSync;
    this.currentPolicy = new WorkPreference();
  }

  public WorkPolicy(boolean allowHourChange, boolean requireSync, WorkPreference workPolicy) {
    this.allowHourChange = allowHourChange;
    this.requireSync = requireSync;
    this.currentPolicy = workPolicy;
  }

  public boolean getAllowHourChange() {
    return allowHourChange;
  }

  public boolean getRequireSync() {
    return requireSync;
  }

  public void setAllowHourChange(boolean allowHourChange) {
    this.allowHourChange = allowHourChange;
  }

  public void setRequireSync(boolean requireSync) {
    this.requireSync = requireSync;
  }

  public WorkPreference getCurrentPolicy() {
    return currentPolicy;
  }

  public void setCurrentPolicy(WorkPreference currentPolicy) {
    this.currentPolicy = currentPolicy;
  }
}
