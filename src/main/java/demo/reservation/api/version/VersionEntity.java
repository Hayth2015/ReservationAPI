package demo.reservation.api.version;

public class VersionEntity {
    private final String name;
    public VersionEntity(String versionName) {
        name = versionName;
    }

    public final String getName() {
        return name;
    }
}
