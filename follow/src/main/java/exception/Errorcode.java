package exception;

public enum Errorcode {
    SELF_FOLLOW("자기 자신을 팔로우할 수 없습니다."),
    ALREADY_FOLLOW("이미 팔로우하였습니다."),
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다.");

    private final String message;

    Errorcode(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
