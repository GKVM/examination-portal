package dto.response;

public class Authenticated {
    private Boolean isVerified;

    public Authenticated(
            Boolean isVerified
    ) {
        this.isVerified = isVerified;
    }

    public Boolean getVerified() {
        return isVerified;
    }
}
