package Model;

import java.util.Arrays;

public class WorkPreference {

  public enum PreferenceType implements AppEnum {
    EARLY("Start early"),
    LATE("Start late"),
    NO_CHANGE("Regular hours"),
    HOME("Work from home");

    private final String text;
  
    
    PreferenceType(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }
  private PreferenceType preferenceType;
  private int hourChange;

  public WorkPreference() {
    this.preferenceType = PreferenceType.NO_CHANGE;
    this.hourChange = 0;
  }

  public WorkPreference(PreferenceType type, int hourChange) {
    this.preferenceType = type;
    this.hourChange = hourChange;
  }

  public int getHourChange() {
    return hourChange;
  }

  public PreferenceType getPreferenceType() {
    return preferenceType;
  }

  public static boolean isHourChangePreference(PreferenceType type) {
    return Arrays.asList(PreferenceType.EARLY, PreferenceType.LATE).contains(type);
  }

  public void setHourChange(int hourChange) {
    this.hourChange = hourChange;
  }

  public void setPreferenceType(PreferenceType preferenceType) {
    this.preferenceType = preferenceType;
  }
}
