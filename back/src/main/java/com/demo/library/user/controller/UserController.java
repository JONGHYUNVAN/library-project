package com.demo.library.user.controller;


import com.demo.library.library.dto.LibraryDto;
import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.security.service.JWTAuthService;
import com.demo.library.user.dto.UserDto;
import com.demo.library.user.entity.User;
import com.demo.library.user.mapper.UserMapper;
import com.demo.library.user.service.UserService;
import com.demo.library.utils.UriCreator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final static String USER_DEFAULT_URL = "/users";
    private final UserService service;
    private final UserMapper mapper;
    private final JWTAuthService jwtAuthService;

    @PostMapping
    public ResponseEntity<Void> postUser(@Valid @RequestBody UserDto.Post post) {
        User user = mapper.postToUser(post);
        service.create(user);

        URI location = UriCreator.createUri(USER_DEFAULT_URL, user.getId());
        return ResponseCreator.created(location);
    }
    @PatchMapping
    public ResponseEntity<SingleResponseDto<UserDto.Response>>
                                patchUser(@Valid @RequestBody UserDto.Patch patch) throws IllegalAccessException, InstantiationException {
       service.isValidRequest(patch.getId());

        User userRequest = mapper.patchToUser(patch);
        User updatedUser = service.update(userRequest);

        UserDto.Response responseDto = mapper.userToResponse(updatedUser);
        return ResponseCreator.single(responseDto);
    }

    @GetMapping
    public ResponseEntity<SingleResponseDto<UserDto.Response>>
                                getUser(@Valid @RequestBody UserDto.Request request) throws IllegalAccessException, InstantiationException {
        User user = service.find(request);

        UserDto.Response responseDto = mapper.userToResponse(user);
        return ResponseCreator.single(responseDto);
    }
    @GetMapping("/my")
    public ResponseEntity<SingleResponseDto<UserDto.My>> getMy() {
        User user = service.findByEmail(jwtAuthService.getEmail());
        UserDto.My responseDto = mapper.userToMy(user);
        return ResponseCreator.single(responseDto);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable long userId) {
        service.delete(userId);

        return ResponseCreator.deleted();
    }


}
