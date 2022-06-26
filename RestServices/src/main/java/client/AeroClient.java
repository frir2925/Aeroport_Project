/*
package client;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import repository.RepositoryException;

import java.util.concurrent.Callable;

public class ShowsClient {
    public static final String URL = "http://localhost:8080/";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) throws RepositoryException {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new RepositoryException(e);
        } catch (Exception e) {
            throw new RepositoryException(String.valueOf(e));
        }
    }

    public Zbor[] getAll() throws AeroportException {
        return execute(() -> restTemplate.getForObject(URL, Zbor[].class));
    }



 */

//    public void update(User user) {
//        execute(() -> {
//            restTemplate.put(String.format("%s/%s", URL, user.getId()), user);
//            return null;
//        });
//    }
//
//    public void delete(String id) {
//        execute(() -> {
//            restTemplate.delete(String.format("%s/%s", URL, id));
//            return null;
//        });
//    }


