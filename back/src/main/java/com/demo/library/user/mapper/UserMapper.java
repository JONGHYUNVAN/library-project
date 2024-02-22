package com.demo.library.user.mapper;

import com.demo.library.genre.entity.UserGenre;
import com.demo.library.loanNban.dto.LoanDto;
import com.demo.library.loanNban.mapper.LoanMapper;
import com.demo.library.mapper.Mapper;
import com.demo.library.user.dto.UserDto;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final LoanMapper loanMapper;
    public final Mapper mapper;

    public User postToUser(UserDto.Post post) {
        return mapper.map(post,User.class);
    }

    public User patchToUser(UserDto.Patch patch) {
        return mapper.map(patch,User.class);
    }

    public UserDto.Response userToResponse(User user)  {
        UserDto.Response response = mapper.map(user, UserDto.Response.class);
        List<LoanDto.Response> loanResponses = loanMapper.loansToLoanResponses(user.getLoans());
        response.setLoans(loanResponses);
        return response;
    }

    public UserDto.My userToMy(User user){
        UserDto.My my = mapper.map(user, UserDto.My.class);
        List<LoanDto.Response> loanResponses = loanMapper.loansToLoanResponses(user.getLoans());
        my.setLoans(loanResponses);

        List<UserDto.MyUserGenre> myUserGenres = user.getUserGenres().stream()
                .map(this::userGenreToMyUserGenre)
                .collect(Collectors.toList());

        my.setGenres(myUserGenres);
        return my;
    }

    private UserDto.MyUserGenre userGenreToMyUserGenre(UserGenre userGenre) {
        UserDto.MyUserGenre myUserGenre = mapper.map(userGenre, UserDto.MyUserGenre.class);
        myUserGenre.setName(userGenre.getGenre().getName());
        return myUserGenre;
    }


}