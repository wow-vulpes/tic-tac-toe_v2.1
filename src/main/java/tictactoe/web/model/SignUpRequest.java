package tictactoe.web.model;

public class SignUpRequest {
    private String login;
    private String password;

    public SignUpRequest() {}

    public SignUpRequest(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}