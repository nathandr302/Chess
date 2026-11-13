package facade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GamesListData;
import model.JoinRequestData;
import model.LoginData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void deleteDataBase() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public UserData postRegisterUser(String username, String password, String email) throws ResponseException {
        var request = buildRequest("POST", "/user", null);
        var response = sendRequest(request);
        return handleResponse(response, UserData.class);
    }

    public LoginData postLoginUser(String username, String password) throws ResponseException {
        var request = buildRequest("POST", "/session", null);
        var response = sendRequest(request);
        return handleResponse(response, LoginData.class);
    }

    public void deleteLogoutUser(String auth) throws ResponseException {
        var request = buildRequest("DELETE", "/session", null);
    }

    public GamesListData getListGame(String auth) throws ResponseException {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, GamesListData.class);
    }

    public int postCreateGame(String auth, String gameName) throws ResponseException {
        var request = buildRequest("POST", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, null);
    }

    public JoinRequestData putJoinGame(String auth, String color, int gameID) throws ResponseException {
        var request = buildRequest("PUT", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, JoinRequestData.class);

    }


    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
