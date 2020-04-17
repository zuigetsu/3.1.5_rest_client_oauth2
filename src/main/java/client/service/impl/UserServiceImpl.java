package client.service.impl;

import client.model.User;
import client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private RestOperations restTemplate;
    private final String url;



    @Autowired
    public UserServiceImpl(RestOperations restTemplate,
                           @Value("${db.server.port}") String port,
                           @Value("${db.server.domain}") String domain,
                           @Value("${db.server.user.api.url}") String serverUrl) {
        url = domain + port + serverUrl;
        this.restTemplate = restTemplate;
    }


    @Override
    public User save(User user) {
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(user), User.class);
        return response.getBody();
    }

    @Override
    public User getById(Integer id) {
        ResponseEntity<User> response = restTemplate.getForEntity(url + id, User.class);
        return response.getBody();
    }

    @Override
    public List<User> getAll() {
        ResponseEntity<List<User>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
        return response.getBody();
    }

    @Override
    public void deleteById(Integer id) {
        restTemplate.delete(url + id);
    }

    @Override
    public boolean isExist(Integer id) {
        ResponseEntity<User> response = restTemplate.exchange(url + id, HttpMethod.GET, null, User.class);
        return response.hasBody();
    }

    @Override
    public void setRolesByRoleIds(User user, Integer[] roleIds) {

    }

    @Override
    public User update(User user) {
        ResponseEntity<User> response = restTemplate.exchange(url + user.getId(), HttpMethod.PUT, new HttpEntity<>(user), User.class);
        return response.getBody();
    }

    @Override
    public User getByName(String name) {
        ResponseEntity<User> response = restTemplate.getForEntity(url +"/name/" + name, User.class);
        return response.getBody();
    }

    @Override
    public void oauthLogin(User user) {
        User loggedUser = null;
        if (!isExistByName(user.getUsername())) {
            save(user);
        }
        loggedUser = getByName(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), loggedUser.getAuthorities()));
    }

    @Override
    public boolean isExistByName(String username) {
        ResponseEntity<User> response = restTemplate.getForEntity(url +"/name/" + username, User.class);
        return response.hasBody();
    }
}
