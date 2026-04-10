package tictactoe.web.model.jwt;

public record JwtResponse(String type, String accessToken, String refreshToken) {
}
